package com.portingdeadmods.indrec.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Statically analyzes a loot table to find out which items it can drop, how likely they are and in which amounts,
 * without rolling it.
 * <p>
 * The table is evaluated as if a fully grown crop was harvested by hand (no tool, no enchantments, no luck, no explosion):
 * <ul>
 *     <li>{@code block_state_property} conditions are tested against the given block state, they pass if no state is given</li>
 *     <li>{@code match_tool} conditions fail</li>
 *     <li>random chance conditions use their unenchanted chance</li>
 *     <li>fortune bonuses ({@code apply_bonus}) use enchantment level 0</li>
 *     <li>other conditions are assumed to pass</li>
 * </ul>
 * Supported entries are {@code item}, {@code empty}, {@code alternatives}, {@code group} and {@code sequence}.
 * Other entries (tags, nested tables, dynamic) take part in the weighting but contribute no items.
 * Supported count functions are {@code set_count}, {@code apply_bonus}, {@code limit_count} and {@code sequence},
 * other functions don't change the count.
 */
public final class LootTableParser {
    private static final int MAX_COUNT = 4096;

    /**
     * The state of the harvested block, null if unknown
     */
    private final @Nullable BlockState state;

    private LootTableParser(@Nullable BlockState state) {
        this.state = state;
    }

    public record LootDrop(Item item, double chance, int minCount, int maxCount, double averageCount, double[] countChances) {
        @Override
        public @NotNull String toString() {
            return String.format("%s: %.2f%% x%s (avg %.3f)", this.item.getDescription(), this.chance * 100,
                    this.minCount == this.maxCount ? this.minCount : this.minCount + "-" + this.maxCount, this.averageCount);
        }
    }

    public static List<LootDrop> parse(LootTable table, HolderLookup.Provider registries) {
        return parse(table, registries, null);
    }

    /**
     * @param state the state of the harvested block, tested by {@code block_state_property} conditions, if null these conditions pass
     */
    public static List<LootDrop> parse(LootTable table, HolderLookup.Provider registries, @Nullable BlockState state) {
        JsonElement json = LootTable.DIRECT_CODEC.encodeStart(RegistryOps.create(JsonOps.INSTANCE, registries), table).getOrThrow();
        return parse(json, state);
    }

    public static List<LootDrop> parse(JsonElement json) {
        return parse(json, null);
    }

    public static List<LootDrop> parse(JsonElement json, @Nullable BlockState state) {
        return new LootTableParser(state).parseTable(json);
    }

    private List<LootDrop> parseTable(JsonElement json) {
        JsonObject table = json.getAsJsonObject();
        JsonArray tableFunctions = array(table, "functions");

        Map<Item, double[]> totals = new LinkedHashMap<>();
        for (JsonElement pool : array(table, "pools")) {
            parsePool(pool.getAsJsonObject(), tableFunctions).forEach((item, dist) ->
                    totals.merge(item, dist, Dist::convolve));
        }

        List<LootDrop> drops = new ArrayList<>();
        totals.forEach((item, dist) -> {
            double chance = 1 - dist[0];
            if (chance <= 0) return;
            int min = 1;
            while (min < dist.length && dist[min] <= 0) min++;
            int max = dist.length - 1;
            while (max > 0 && dist[max] <= 0) max--;
            drops.add(new LootDrop(item, chance, min, max, Dist.mean(dist), dist));
        });
        return drops;
    }

    private Map<Item, double[]> parsePool(JsonObject pool, JsonArray tableFunctions) {
        Map<List<Singleton>, Double> candidates = new LinkedHashMap<>();
        candidates.put(List.of(), 1d);
        for (JsonElement entry : array(pool, "entries")) {
            List<Branch> branches = expand(entry.getAsJsonObject());
            Map<List<Singleton>, Double> next = new LinkedHashMap<>();
            candidates.forEach((list, p) -> {
                for (Branch branch : branches) {
                    next.merge(concat(list, branch.entries()), p * branch.chance(), Double::sum);
                }
            });
            candidates = next;
        }

        JsonArray poolFunctions = array(pool, "functions");
        Map<Item, double[]> perRoll = new LinkedHashMap<>();
        candidates.forEach((list, p) -> {
            List<Singleton> weighted = list.stream().filter(s -> s.weight() > 0).toList();
            int totalWeight = weighted.stream().mapToInt(Singleton::weight).sum();
            for (Singleton singleton : weighted) {
                if (singleton.item() == null) continue;

                double pick = weighted.size() == 1 ? 1 : (double) singleton.weight() / totalWeight;
                double[] count = applyFunctions(applyFunctions(applyFunctions(Dist.delta(1), singleton.functions()), poolFunctions), tableFunctions);
                perRoll.merge(singleton.item(), Dist.scale(count, p * pick), Dist::add);
            }
        });

        double[] rolls = numberDist(pool.get("rolls"));
        double poolChance = conditionsChance(pool);
        Map<Item, double[]> result = new LinkedHashMap<>();
        perRoll.forEach((item, dist) -> {
            double[] single = Dist.withNothing(dist);
            double[] total = new double[1];
            double[] power = Dist.delta(0);
            for (int r = 0; r < rolls.length; r++) {
                if (r > 0) power = Dist.convolve(power, single);
                if (rolls[r] > 0) total = Dist.add(total, Dist.scale(power, rolls[r]));
            }
            result.put(item, Dist.withNothing(Dist.scale(total, poolChance)));
        });
        return result;
    }

    private record Branch(double chance, List<Singleton> entries, boolean success) {
    }

    private record Singleton(@Nullable Item item, int weight, JsonArray functions) {
    }

    private List<Branch> expand(JsonObject entry) {
        double chance = conditionsChance(entry);
        String type = stripNamespace(string(entry, "type", "item"));
        List<Branch> branches = new ArrayList<>();
        if (chance < 1) branches.add(new Branch(1 - chance, List.of(), false));
        if (chance <= 0) return branches;

        switch (type) {
            case "alternatives", "sequence" -> {
                boolean stopOnSuccess = type.equals("alternatives");
                List<Branch> pending = List.of(new Branch(chance, List.of(), true));
                for (JsonElement child : array(entry, "children")) {
                    List<Branch> childBranches = expand(child.getAsJsonObject());
                    List<Branch> next = new ArrayList<>();
                    for (Branch state : pending) {
                        for (Branch childBranch : childBranches) {
                            Branch combined = new Branch(state.chance() * childBranch.chance(), concat(state.entries(), childBranch.entries()), childBranch.success());
                            if (childBranch.success() == stopOnSuccess) branches.add(combined);
                            else next.add(combined);
                        }
                    }
                    pending = next;
                }
                for (Branch state : pending) {
                    branches.add(new Branch(state.chance(), state.entries(), !stopOnSuccess));
                }
            }
            case "group" -> {
                List<Branch> states = List.of(new Branch(chance, List.of(), true));
                for (JsonElement child : array(entry, "children")) {
                    List<Branch> childBranches = expand(child.getAsJsonObject());
                    List<Branch> next = new ArrayList<>();
                    for (Branch state : states) {
                        for (Branch childBranch : childBranches) {
                            next.add(new Branch(state.chance() * childBranch.chance(), concat(state.entries(), childBranch.entries()), true));
                        }
                    }
                    states = next;
                }
                branches.addAll(states);
            }
            default -> {
                Item item = type.equals("item") ? BuiltInRegistries.ITEM.get(ResourceLocation.parse(string(entry, "name", "minecraft:air"))) : null;
                int weight = entry.has("weight") ? entry.get("weight").getAsInt() : 1;
                branches.add(new Branch(chance, List.of(new Singleton(item, weight, array(entry, "functions"))), true));
            }
        }
        return branches;
    }

    private double[] applyFunctions(double[] count, JsonArray functions) {
        for (JsonElement element : functions) {
            count = applyFunction(count, element);
        }
        return count;
    }

    private double[] applyFunction(double[] count, JsonElement element) {
        if (element.isJsonArray()) return applyFunctions(count, element.getAsJsonArray());

        JsonObject function = element.getAsJsonObject();
        double[] applied = switch (stripNamespace(string(function, "function", ""))) {
            case "set_count" -> {
                double[] value = numberDist(function.get("count"));
                yield function.has("add") && function.get("add").getAsBoolean() ? Dist.convolve(count, value) : value;
            }
            case "apply_bonus" -> {
                if (stripNamespace(string(function, "formula", "")).equals("binomial_with_bonus_count")) {
                    JsonObject params = function.getAsJsonObject("parameters");
                    yield Dist.convolve(count, Dist.binomial(params.get("extra").getAsInt(), params.get("probability").getAsDouble()));
                }
                yield count;
            }
            case "limit_count" -> {
                JsonElement limit = function.get("limit");
                if (limit.isJsonPrimitive()) {
                    yield Dist.clamp(count, Integer.MIN_VALUE, limit.getAsInt());
                }
                JsonObject range = limit.getAsJsonObject();
                int min = range.has("min") ? (int) Math.round(numberMean(range.get("min"))) : Integer.MIN_VALUE;
                int max = range.has("max") ? (int) Math.round(numberMean(range.get("max"))) : Integer.MAX_VALUE;
                yield Dist.clamp(count, min, max);
            }
            case "sequence" -> applyFunctions(count, array(function, "functions"));
            default -> count;
        };

        double chance = conditionsChance(function);
        if (chance >= 1) return applied;
        return Dist.add(Dist.scale(applied, chance), Dist.scale(count, 1 - chance));
    }

    private double conditionsChance(JsonObject element) {
        double chance = 1;
        for (JsonElement condition : array(element, "conditions")) {
            chance *= conditionChance(condition);
        }
        return chance;
    }

    private double conditionChance(JsonElement element) {
        if (element.isJsonArray()) {
            double chance = 1;
            for (JsonElement term : element.getAsJsonArray()) chance *= conditionChance(term);
            return chance;
        }

        JsonObject condition = element.getAsJsonObject();
        return switch (stripNamespace(string(condition, "condition", ""))) {
            case "inverted" -> 1 - conditionChance(condition.get("term"));
            case "all_of" -> conditionChance(array(condition, "terms"));
            case "any_of" -> {
                double none = 1;
                for (JsonElement term : array(condition, "terms")) none *= 1 - conditionChance(term);
                yield 1 - none;
            }
            case "random_chance" -> clamp01(numberMean(condition.get("chance")));
            case "random_chance_with_enchanted_bonus" -> clamp01(condition.get("unenchanted_chance").getAsDouble());
            case "table_bonus" -> clamp01(array(condition, "chances").get(0).getAsDouble());
            case "match_tool" -> 0;
            case "block_state_property" -> blockStatePropertyChance(condition);
            default -> 1;
        };
    }

    private double blockStatePropertyChance(JsonObject condition) {
        if (this.state == null) return 1;
        Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(string(condition, "block", "minecraft:air")));
        if (!this.state.is(block)) return 0;
        JsonElement properties = condition.get("properties");
        if (properties == null) return 1;
        return StatePropertiesPredicate.CODEC.parse(JsonOps.INSTANCE, properties)
                .result()
                .map(predicate -> predicate.matches(this.state) ? 1d : 0d)
                // Unknown properties format, assume the condition passes like before
                .orElse(1d);
    }

    private static double[] numberDist(@Nullable JsonElement element) {
        if (element == null) return Dist.delta(1);
        if (element.isJsonPrimitive()) return Dist.delta((int) Math.round(element.getAsDouble()));

        JsonObject provider = element.getAsJsonObject();
        String type = stripNamespace(string(provider, "type", provider.has("min") ? "uniform" : provider.has("n") ? "binomial" : "constant"));
        return switch (type) {
            case "constant" -> Dist.delta((int) Math.round(provider.get("value").getAsDouble()));
            case "uniform" -> {
                double[] min = numberDist(provider.get("min"));
                double[] max = numberDist(provider.get("max"));
                double[] result = new double[1];
                for (int a = 0; a < min.length; a++) {
                    for (int b = 0; b < max.length; b++) {
                        if (min[a] > 0 && max[b] > 0) {
                            result = Dist.add(result, Dist.scale(Dist.uniform(a, b), min[a] * max[b]));
                        }
                    }
                }
                yield result;
            }
            case "binomial" -> {
                double[] n = numberDist(provider.get("n"));
                double p = clamp01(numberMean(provider.get("p")));
                double[] result = new double[1];
                for (int i = 0; i < n.length; i++) {
                    if (n[i] > 0) result = Dist.add(result, Dist.scale(Dist.binomial(i, p), n[i]));
                }
                yield result;
            }
            default -> Dist.delta(1);
        };
    }

    private static double numberMean(@Nullable JsonElement element) {
        if (element == null) return 1;
        if (element.isJsonPrimitive()) return element.getAsDouble();

        JsonObject provider = element.getAsJsonObject();
        String type = stripNamespace(string(provider, "type", provider.has("min") ? "uniform" : provider.has("n") ? "binomial" : "constant"));
        return switch (type) {
            case "constant" -> provider.get("value").getAsDouble();
            case "uniform" -> (numberMean(provider.get("min")) + numberMean(provider.get("max"))) / 2;
            case "binomial" -> Dist.mean(numberDist(provider.get("n"))) * numberMean(provider.get("p"));
            default -> 1;
        };
    }

    private static JsonArray array(JsonObject object, String key) {
        JsonElement element = object.get(key);
        return element != null && element.isJsonArray() ? element.getAsJsonArray() : new JsonArray();
    }

    private static String string(JsonObject object, String key, String fallback) {
        JsonElement element = object.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsString() : fallback;
    }

    private static String stripNamespace(String id) {
        return id.startsWith("minecraft:") ? id.substring("minecraft:".length()) : id;
    }

    private static double clamp01(double value) {
        return Math.clamp(value, 0, 1);
    }

    private static <T> List<T> concat(List<T> a, List<T> b) {
        if (a.isEmpty()) return b;
        if (b.isEmpty()) return a;
        List<T> list = new ArrayList<>(a);
        list.addAll(b);
        return List.copyOf(list);
    }

    private static final class Dist {
        static double[] delta(int count) {
            double[] dist = new double[cap(count) + 1];
            dist[cap(count)] = 1;
            return dist;
        }

        static double[] uniform(int min, int max) {
            if (min >= max) return delta(min);
            double[] dist = new double[cap(max) + 1];
            double p = 1d / (max - min + 1);
            for (int i = min; i <= max; i++) dist[cap(i)] += p;
            return dist;
        }

        static double[] binomial(int n, double p) {
            n = Math.max(0, n);
            double[] dist = new double[cap(n) + 1];
            double coefficient = 1;
            for (int k = 0; k <= n; k++) {
                dist[cap(k)] += coefficient * Math.pow(p, k) * Math.pow(1 - p, n - k);
                coefficient = coefficient * (n - k) / (k + 1);
            }
            return dist;
        }

        static double[] add(double[] a, double[] b) {
            double[] result = Arrays.copyOf(a, Math.max(a.length, b.length));
            for (int i = 0; i < b.length; i++) result[i] += b[i];
            return result;
        }

        static double[] scale(double[] dist, double factor) {
            double[] result = new double[dist.length];
            for (int i = 0; i < dist.length; i++) result[i] = dist[i] * factor;
            return result;
        }

        static double[] convolve(double[] a, double[] b) {
            double[] result = new double[Math.min(a.length + b.length - 1, MAX_COUNT + 1)];
            for (int i = 0; i < a.length; i++) {
                if (a[i] == 0) continue;
                for (int j = 0; j < b.length; j++) {
                    result[Math.min(i + j, MAX_COUNT)] += a[i] * b[j];
                }
            }
            return result;
        }

        static double[] clamp(double[] dist, int min, int max) {
            max = Math.min(max, MAX_COUNT);
            double[] result = new double[Math.max(1, Math.min(dist.length, max + 1))];
            for (int i = 0; i < dist.length; i++) {
                result[Math.max(0, Math.min(Math.max(i, min), max))] += dist[i];
            }
            return result;
        }

        static double[] withNothing(double[] dist) {
            double[] result = dist.length == 0 ? new double[1] : dist.clone();
            result[0] += Math.max(0, 1 - Arrays.stream(dist).sum());
            return result;
        }

        static double mean(double[] dist) {
            double mean = 0;
            for (int i = 0; i < dist.length; i++) mean += i * dist[i];
            return mean;
        }

        private static int cap(int count) {
            return Math.clamp(count, 0, MAX_COUNT);
        }
    }
}
