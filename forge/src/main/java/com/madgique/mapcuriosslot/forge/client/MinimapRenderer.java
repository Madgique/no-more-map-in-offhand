package com.madgique.mapcuriosslot.forge.client;

import com.madgique.mapcuriosslot.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import top.theillusivec4.curios.api.CuriosApi;

public class MinimapRenderer {

        public static void renderMinimap(GuiGraphics guiGraphics, float partialTick) {
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer player = minecraft.player;
                ModConfig config = ModConfig.getInstance();

                if (player == null || minecraft.level == null) {
                        return;
                }

                // Check if minimap is enabled
                if (!config.isEnabled()) {
                        return;
                }

                // Get map from Curios slot - check EVERY frame
                ItemStack mapStack = getMapFromCuriosSlot(player);
                if (mapStack.isEmpty() || !(mapStack.getItem() instanceof MapItem)) {
                        return;
                }

                MapItem mapItem = (MapItem) mapStack.getItem();
                Integer mapId = MapItem.getMapId(mapStack);
                if (mapId == null) {
                        return;
                }

                MapItemSavedData mapData = MapItem.getSavedData(mapId, minecraft.level);
                if (mapData == null) {
                        return;
                }

                // Note: Map updates are handled by ClientMapTickHandler

                // Use config values
                int minimapX = config.getMinimapX();
                int minimapY = config.getMinimapY();
                int minimapSize = config.getMinimapSize();

                // Render borders only if enabled
                if (config.shouldShowBorder()) {
                        // Décorations papyrus AVANT le rendu de la carte
                        // Bordure externe - seulement les côtés
                        // Côté gauche
                        guiGraphics.fill(minimapX - 4, minimapY - 4, minimapX, minimapY + minimapSize + 4, 0xFF8B4513);
                        // Côté droit
                        guiGraphics.fill(minimapX + minimapSize, minimapY - 4, minimapX + minimapSize + 4,
                                        minimapY + minimapSize + 4, 0xFF8B4513);
                        // Côté haut
                        guiGraphics.fill(minimapX - 4, minimapY - 4, minimapX + minimapSize + 4, minimapY, 0xFF8B4513);
                        // Côté bas
                        guiGraphics.fill(minimapX - 4, minimapY + minimapSize, minimapX + minimapSize + 4,
                                        minimapY + minimapSize + 4, 0xFF8B4513);

                        // Bordure moyenne - seulement les côtés
                        // Côté gauche
                        guiGraphics.fill(minimapX - 2, minimapY - 2, minimapX, minimapY + minimapSize + 2, 0xFFD2B48C);
                        // Côté droit
                        guiGraphics.fill(minimapX + minimapSize, minimapY - 2, minimapX + minimapSize + 2,
                                        minimapY + minimapSize + 2, 0xFFD2B48C);
                        // Côté haut
                        guiGraphics.fill(minimapX - 2, minimapY - 2, minimapX + minimapSize + 2, minimapY, 0xFFD2B48C);
                        // Côté bas
                        guiGraphics.fill(minimapX - 2, minimapY + minimapSize, minimapX + minimapSize + 2,
                                        minimapY + minimapSize + 2, 0xFFD2B48C);
                }

                // Rendu de la carte avec position et échelle correctes
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(minimapX, minimapY, 0);

                // Scale pour remplir tout l'espace disponible
                float renderScale = (float) minimapSize / 128.0f;
                guiGraphics.pose().scale(renderScale, renderScale, 1.0f);

                // Maintenant render la carte avec les transformations appliquées
                minecraft.gameRenderer.getMapRenderer().render(
                                guiGraphics.pose(),
                                guiGraphics.bufferSource(),
                                mapId,
                                mapData,
                                false, // Paramètre pour le rendu des décorations
                                15728880);

                guiGraphics.pose().popPose();
        }

        private static ItemStack getMapFromCuriosSlot(LocalPlayer player) {
                return CuriosApi.getCuriosInventory(player)
                                .map(curiosInventory -> curiosInventory.getStacksHandler("map")
                                                .map(slotInventory -> {
                                                        // Check all slots in the "map" slot type for a map item
                                                        for (int i = 0; i < slotInventory.getSlots(); i++) {
                                                                ItemStack stack = slotInventory.getStacks()
                                                                                .getStackInSlot(i);
                                                                if (!stack.isEmpty()
                                                                                && stack.getItem() instanceof MapItem) {
                                                                        return stack;
                                                                }
                                                        }
                                                        return ItemStack.EMPTY;
                                                })
                                                .orElse(ItemStack.EMPTY))
                                .orElse(ItemStack.EMPTY);
        }
}