package com.portingdeadmods.indrec.datagen.assets;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.IndustrialRecrafted;
import com.portingdeadmods.indrec.api.energy.EnergyTier;
import com.portingdeadmods.indrec.registries.*;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import com.portingdeadmods.portingdeadlibs.api.translations.DeferredTranslation;
import com.portingdeadmods.portingdeadlibs.api.translations.TranslatableConstant;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public class IRRuRuLangProvider extends LanguageProvider {
    public IRRuRuLangProvider(PackOutput output) {
        super(output, IndustrialRecrafted.MODID, "ru_ru");
    }

    @Override
    protected void addTranslations() {
        addItem(IRItems.RAW_TIN, "Необработанное олово");
        addItem(IRItems.RAW_URANIUM, "Необработанный уран");
        addItem(IRItems.RAW_IRIDIUM, "Необработанный иридий");

        addItem(IRItems.TIN_INGOT, "Оловянный слиток");
        addItem(IRItems.REFINED_IRON_INGOT, "Очищенное железо");
        addItem(IRItems.URANIUM_INGOT, "Очищенный уран");
        addItem(IRItems.BRONZE_INGOT, "Бронзовый слиток");
        addItem(IRItems.IRIDIUM_INGOT, "Иридиевый слиток");
        addItem(IRItems.MIXED_METAL_INGOT, "Композитный слиток");
        addItem(IRItems.IRIDIUM_ALLOY_INGOT, "Слиток иридиевого сплава");

        addItem(IRItems.TIN_PLATE, "Оловянная пластина");
        addItem(IRItems.COPPER_PLATE, "Медная пластина");
        addItem(IRItems.DENSE_COPPER_PLATE, "Плотная медная пластина");
        addItem(IRItems.ADVANCED_ALLOY_PLATE, "Пластина из продвинутого сплава");

        addItem(IRItems.TIN_DUST, "Оловянная пыль");
        addItem(IRItems.BRONZE_DUST, "Бронзовая пыль");
        addItem(IRItems.COPPER_DUST, "Медная пыль");
        addItem(IRItems.IRON_DUST, "Железная пыль");
        addItem(IRItems.GOLD_DUST, "Золотая пыль");
        addItem(IRItems.COAL_DUST, "Угольная пыль");

        addItem(IRItems.BASIC_CIRCUIT, "Электросхема");
        addItem(IRItems.ADVANCED_CIRCUIT, "Улучшенная электросхема");

        addItem(IRItems.WRENCH, "Гаечный ключ");
        addItem(IRItems.TREETAP, "Краник");
        addItem(IRItems.CUTTER, "Кусачки");
        addItem(IRItems.REMOTE_DETONATOR, "Радиопульт подрыва");
        addItem(IRItems.DYNAMITE, "Динамит");

        addItem(IRItems.ELECTRIC_TREETAP, "Электрокраник");
        //addItem(IRItems.ELECTRIC_WRENCH, "Electric Wrench");
        addItem(IRItems.ELECTRIC_HOE, "Электромотыга");
        addItem(IRItems.MINING_LASER, "Шахтёрский лазер");
        addItem(IRItems.NANO_SABER, "Наносабля");
        addItem(IRItems.BASIC_DRILL, "Шахтёрский бур");
        addItem(IRItems.ADVANCED_DRILL, "Алмазный бур");
        addItem(IRItems.BASIC_CHAINSAW, "Электропила");
        addItem(IRItems.ADVANCED_CHAINSAW, "Электропила");

        addItem(IRItems.NANO_HELMET, "Нановолоконный шлем");
        addItem(IRItems.NANO_CHESTPLATE, "Нановолоконный жилет");
        addItem(IRItems.NANO_LEGGINGS, "Нановолоконные поножи");
        addItem(IRItems.NANO_BOOTS, "Нановолоконные ботинки");

        addItem(IRItems.QUANTUM_HELMET, "Квантовый шлем");
        addItem(IRItems.QUANTUM_CHESTPLATE, "Квантовый жилет");
        addItem(IRItems.QUANTUM_LEGGINGS, "Квантовые поножи");
        addItem(IRItems.QUANTUM_BOOTS, "Квантовые ботинки");

        addItem(IRItems.JETPACK, "Реактивный ранец");
        addItem(IRItems.ELECTRIC_JETPACK, "Электрический реактивный ранец");

        addItem(IRItems.REDSTONE_BATTERY, "Аккумулятор");
        addItem(IRItems.ENERGY_CRYSTAL, "Энергетический кристалл");
        addItem(IRItems.LAPOTRON_CRYSTAL, "Лазуротроновый кристалл");

        addItem(IRItems.FLUID_CELL, "Универсальная жидкостная капсула");
        addItem(IRItems.FUSE, "Предохранитель");
        addItem(IRItems.TIN_CAN, "Консервная банка");
        addItem(IRItems.TIN_CAN_FOOD, "Заполненная консервная банка");

        addItem(IRItems.STICKY_RESIN, "Латекс");
        addItem(IRItems.RUBBER, "Резина");

        addItem(IRItems.COAL_BALL, "Угольный шарик");
        addItem(IRItems.COMPRESSED_COAL_BALL, "Сжатый угольный шарик");
        addItem(IRItems.GRAPHENE, "Графен");
        addItem(IRItems.CARBON_FIBER, "Углеволокно");
        addItem(IRItems.CARBON_MESH, "Углеткань");
        addItem(IRItems.CARBON_PLATE, "Углепластик");

        addItem(IRItems.SCRAP, "Утильсырьё");
        addItem(IRItems.SCRAP_BOX, "Коробка утильсырья");
        addItem(IRItems.UU_MATTER, "Материя");

        addItem(IRFluids.BIO_FUEL.getDeferredBucket(), "Ведро биотоплива");

        addItem(IRItems.PLANT_BALL, "Биомасса");

        addItem(IRItems.SINGLE_URANIUM_FUEL_ROD, "Топливный стержень (Уран)");
        addItem(IRItems.DOUBLE_URANIUM_FUEL_ROD, "Сдвоенный топливный стержень (Уран)");
        addItem(IRItems.QUAD_URANIUM_FUEL_ROD, "Счетверённый топливный стержень (Уран)");

        addBlock(IRBlocks.INDUSTRIAL_TNT, "Промышленный динамит");
        addBlock(IRBlocks.NUKE, "Ядерная бомба");

        addBlock(IRBlocks.MACHINE_FRAME, "Базовый корпус машины");
        addBlock(IRBlocks.ADVANCED_MACHINE_FRAME, "Улучшенный корпус машины");

        addBlock(IRMachines.BATTERY_BOX.getBlockSupplier(), "Энергохранилище");
        addBlock(IRMachines.BASIC_ENERGY_STORAGE_UNIT.getBlockSupplier(), "МЭСН");
        addBlock(IRMachines.ADVANCED_ENERGY_STORAGE_UNIT.getBlockSupplier(), "МФЭ");

        addBlock(IRMachines.NUCLEAR_REACTOR.getBlockSupplier(), "Ядерный реактор");
        addBlock(IRMachines.MATTER_FABRICATOR.getBlockSupplier(), "Генератор материи");
        addBlock(IRBlocks.NUCLEAR_REACTOR_CHAMBER, "Реакторная камера");

        addBlock(IRBlocks.TIN_CABLE, "Оловянный провод");
        addBlock(IRBlocks.COPPER_CABLE, "Медный провод");
        addBlock(IRBlocks.GOLD_CABLE, "Золотой провод");
        addBlock(IRBlocks.HV_CABLE, "Высоковольтный провод");
        addBlock(IRBlocks.GLASS_FIBRE_CABLE, "Стекловолоконный провод");
        addBlock(IRBlocks.BURNT_CABLE, "Перегоревший провод");

        addBlock(IRBlocks.REINFORCED_DOOR, "Укреплённая дверь");
        addBlock(IRBlocks.REINFORCED_GLASS, "Укреплённое стекло");
        addBlock(IRBlocks.REINFORCED_STONE, "Укреплённый камень");

        addBlock(IRBlocks.TIN_ORE, "Оловянная руда");
        addBlock(IRBlocks.URANIUM_ORE, "Урановая руда");
        addBlock(IRBlocks.IRIDIUM_ORE, "Иридиевая руда");
        addBlock(IRBlocks.DEEPSLATE_TIN_ORE, "Оловянная руда");
        addBlock(IRBlocks.DEEPSLATE_URANIUM_ORE, "Урановая руда");
        addBlock(IRBlocks.DEEPSLATE_IRIDIUM_ORE, "Иридиевая руда");

        addBlock(IRBlocks.RUBBER_SHEET, "Резиновый лист");
        addBlock(IRBlocks.STICKY_RESIN_SHEET, "Латексный лист");

        add(IRMachines.BASIC_GENERATOR.getBlock(), "Генератор");
        add(IRMachines.GEOTHERMAL_GENERATOR.getBlock(), "Геотермальный генератор");
        add(IRMachines.WIND_MILL.getBlock(), "Ветряная мельница");
        add(IRMachines.WATER_MILL.getBlock(), "Водяная мельница");
        add(IRMachines.ELECTRIC_FURNACE.getBlock(), "Электрическая печь");
        add(IRMachines.CHARGE_PAD.getBlock(), "Заряжающая плита");
        add(IRMachines.COMPRESSOR.getBlock(), "Сжиматель");
        add(IRMachines.RECYCLER.getBlock(), "Утилизатор");
        add(IRMachines.MACERATOR.getBlock(), "Дробитель");
        add(IRMachines.EXTRACTOR.getBlock(), "Экстрактор");
        add(IRMachines.CANNING_MACHINE.getBlock(), "Наполнитель");
        add(IRMachines.BASIC_SOLAR_PANEL.getBlock(), "Солнечная панель");

        addBlock(IRBlocks.TIN_BLOCK, "Оловянный блок");
        addBlock(IRBlocks.URANIUM_BLOCK, "Урановый блок");
        addBlock(IRBlocks.BRONZE_BLOCK, "Бронзовый блок");

        addBlock(IRBlocks.RUBBER_TREE_LOG, "Древесина гевеи");
        addBlock(IRBlocks.RUBBER_TREE_WOOD, "Древесина гевеи");
        addBlock(IRBlocks.STRIPPED_RUBBER_TREE_LOG, "Древесина гевеи");
        addBlock(IRBlocks.STRIPPED_RUBBER_TREE_WOOD, "Древесина гевеи");
        addBlock(IRBlocks.RUBBER_TREE_LEAVES, "Листва гевеи");
        addBlock(IRBlocks.RUBBER_TREE_SAPLING, "Саженец гевеи");
        addBlock(IRBlocks.RUBBER_TREE_PLANKS, "Доски");
        addBlock(IRBlocks.RUBBER_TREE_DOOR, "Дверь");
        addBlock(IRBlocks.RUBBER_TREE_TRAPDOOR, "Люк");
        addBlock(IRBlocks.RUBBER_TREE_FENCE, "Забор");
        addBlock(IRBlocks.RUBBER_TREE_FENCE_GATE, "Калитка");
        addBlock(IRBlocks.RUBBER_TREE_PRESSURE_PLATE, "Нажимная плита");
        addBlock(IRBlocks.RUBBER_TREE_BUTTON, "Кнопка");
        addBlock(IRBlocks.RUBBER_TREE_SLAB, "Плита");
        addBlock(IRBlocks.RUBBER_TREE_STAIRS, "Ступеньки");

        addEnergyTier(IREnergyTiers.NONE, "Нет");
        addEnergyTier(IREnergyTiers.LOW, "Низкий");
        addEnergyTier(IREnergyTiers.MEDIUM, "Средний");
        addEnergyTier(IREnergyTiers.HIGH, "Высокий");
        addEnergyTier(IREnergyTiers.EXTREME, "Экстремальный");
        addEnergyTier(IREnergyTiers.INSANE, "Безумный");
        addEnergyTier(IREnergyTiers.CREATIVE, "Творческий");

        addFluid(IRFluids.BIO_FUEL, "Биотопливо");

        translate(IRTranslations.ON, "Вкл");
        translate(IRTranslations.OFF, "Выкл");

        translate(IRTranslations.ENERGY_NAME, "Энергия");
        translate(IRTranslations.HEAT_NAME, "Тепло");
        translate(IRTranslations.FLUID_NAME, "Жидкость");

        translate(IRTranslations.FILLABLE, "Может быть заполнена с помощью контейнера для жидкостей");
        translate(IRTranslations.AOE_STATUS, "3x3 Добыча: %s");
        translate(IRTranslations.TREE_CUTTER_STATUS, "Валка деревьев: %s");
        translate(IRTranslations.ACTIVE, "Активен");
        translate(IRTranslations.INACTIVE, "Неактивен");
        translate(IRTranslations.SCRAP_BOX_TOOLTIP, "ПКМ, чтобы открыть коробку");
        translate(IRTranslations.SCRAP_TOOLTIP, "Может быть произведено в утилизаторе из любого предмета");

        translate(IRTranslations.ENERGY_STORED, "Накоплено: ");
        translate(IRTranslations.ENERGY_TIER, "Энергоуровень: ");
        translate(IRTranslations.ENERGY_AMOUNT, "%d");
        translate(IRTranslations.ENERGY_AMOUNT_WITH_CAPACITY, "%s/%s");

        translate(IRTranslations.FLUID_TYPE, "Жидкость: ");
        translate(IRTranslations.FLUID_STORED, "Накоплено: ");
        translate(IRTranslations.FLUID_AMOUNT, "%d");
        translate(IRTranslations.FLUID_AMOUNT_WITH_CAPACITY, "%d/%d");
        translate(IRTranslations.EMPTY_FLUID, "Alt + Shift + ПКМ, чтобы опустошить");

        translate(IRTranslations.HEAT_STORED, "Накоплено: ");
        translate(IRTranslations.HEAT_AMOUNT, "%.1f");
        translate(IRTranslations.HEAT_AMOUNT_WITH_CAPACITY, "%.1f/%.1f");

        translate(IRTranslations.MELTING_NOT_POSSIBLE, "Плавление невозможно");
        translate(IRTranslations.MELTING_PROGRESS, "%.1f/%.1f");

        translate(IRTranslations.MULTIBLOCK_HINT, "Это многоблочная структура, для инструкций постройки посмотрите на чертёж");
        translate(IRTranslations.CLAY_CASTING_MOLD, "Зажмите SHIFT и прокрутите + ПКМ, чтобы выбрать литейную форму");

        translate(IRTranslations.CRUCIBLE_SMELTING, "Плавка в тигле");
        translate(IRTranslations.CASTING, "Литьё");
        translate(IRTranslations.MOLD_CASTING, "Литьё в форму");
        translate(IRTranslations.ENERGY_USAGE, "%s/т: %d");
        translate(IRTranslations.ITEM_CONSUMED, "Предмет расходуется");

        translate(IRTranslations.BASIC_GENERATOR, "Генератор");
        translate(IRTranslations.GEOTHERMAL_GENERATOR, "Геотермальный генератор");
        translate(IRTranslations.ELECTRIC_FURNACE, "Электрическая печь");
        translate(IRTranslations.BASIC_SOLAR_PANEL, "Солнечная панель");
        translate(IRTranslations.COMPRESSOR, "Сжиматель");
        translate(IRTranslations.MACERATOR, "Дробитель");
        translate(IRTranslations.EXTRACTOR, "Экстрактор");
        translate(IRTranslations.RECYCLER, "Утилизатор");
        translate(IRTranslations.BATTERY_BOX, "Энергохранилище");
        translate(IRTranslations.CANNING_MACHINE, "Наполнитель");
        translate(IRTranslations.MATTER_FABRICATOR, "Генератор материи");

        translate(IRTranslations.GUIDE_ME_MISSING, "Для доступа к книге-руководству необходимо установить GuideME!");
        translate(IRTranslations.TOGGLED_AOE, "Режим добычи 3x3: %s");
        translate(IRTranslations.TOGGLED_TREE_CUTTER, "Режим валки деревьев: %s");
    }

    private void addFluid(PDLFluid key, String val) {
        add("fluid." + IndustrialRecrafted.MODID + "." + key.getName(), val);
        add("fluid_type." + IndustrialRecrafted.MODID + "." + key.getName(), val);
    }

    private void addEnergyTier(Supplier<? extends EnergyTier> key, String val) {
        add("energy_tier." + IndustrialRecrafted.MODID + "." + IRRegistries.ENERGY_TIER.getKey(key.get()).getPath(), val);
    }

    private void translate(DeferredTranslation<TranslatableConstant> key, String val) {
        add(key.key(), val);
    }
}
