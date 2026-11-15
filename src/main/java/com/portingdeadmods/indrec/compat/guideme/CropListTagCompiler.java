package com.portingdeadmods.indrec.compat.guideme;

import com.portingdeadmods.indrec.IRRegistries;
import com.portingdeadmods.indrec.api.crops.Crop;
import guideme.compiler.IndexingContext;
import guideme.compiler.IndexingSink;
import guideme.compiler.PageCompiler;
import guideme.compiler.tags.BlockTagCompiler;
import guideme.compiler.tags.MdxAttrs;
import guideme.document.block.LytBlockContainer;
import guideme.document.block.LytParagraph;
import guideme.document.block.LytSlot;
import guideme.document.block.table.LytTable;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Lists all industrial crops registered in the current world (crops are data-driven, so this depends on the loaded datapacks).
 * <p>
 * Usage: {@code <CropList />}, optionally filtered by namespace: {@code <CropList namespace="indrec" />}
 */
public class CropListTagCompiler extends BlockTagCompiler {
    public static final String TAG_NAME = "CropList";

    @Override
    public Set<String> getTagNames() {
        return Set.of(TAG_NAME);
    }

    @Override
    protected void compile(PageCompiler compiler, LytBlockContainer parent, MdxJsxElementFields el) {
        String namespace = MdxAttrs.getString(compiler, parent, el, "namespace", null);

        var level = Minecraft.getInstance().level;
        var registry = level == null ? null : level.registryAccess().registry(IRRegistries.CROP).orElse(null);
        if (registry == null) {
            parent.append(LytParagraph.of("Join a world to see the list of industrial crops."));
            return;
        }

        List<CropEntry> crops = registry.holders()
                .filter(holder -> namespace == null || holder.key().location().getNamespace().equals(namespace))
                .map(CropEntry::new)
                .sorted(Comparator.comparing(CropEntry::name, String.CASE_INSENSITIVE_ORDER))
                .toList();

        if (crops.isEmpty()) {
            parent.append(LytParagraph.of("No industrial crops found."));
            return;
        }

        var table = new LytTable();
        table.setMarginBottom(5);

        var header = table.appendRow();
        header.modifyStyle(style -> style.bold(true));
        header.appendCell().append(LytParagraph.of("Seed"));
        header.appendCell().append(LytParagraph.of("Crop"));
        header.appendCell().append(LytParagraph.of("Stages"));

        for (CropEntry crop : crops) {
            var row = table.appendRow();
            var seedCell = row.appendCell();
            crop.holder().value().seed().ifPresentOrElse(
                    seed -> seedCell.append(new LytSlot(new ItemStack(seed))),
                    () -> seedCell.append(LytParagraph.of("-"))
            );
            row.appendCell().append(LytParagraph.of(crop.name()));
            row.appendCell().append(LytParagraph.of(String.valueOf(crop.holder().value().stages())));
        }

        parent.append(table);
    }

    @Override
    public void index(IndexingContext indexer, MdxJsxElementFields el, IndexingSink sink) {
        // Content depends on the current world, nothing to index
    }

    private record CropEntry(Holder.Reference<Crop> holder, String name) {
        CropEntry(Holder.Reference<Crop> holder) {
            this(holder, nameOf(holder.key()));
        }

        private static String nameOf(ResourceKey<Crop> key) {
            return Component.translatableWithFallback(Crop.getTranslationKey(key), key.location().toString()).getString();
        }
    }
}
