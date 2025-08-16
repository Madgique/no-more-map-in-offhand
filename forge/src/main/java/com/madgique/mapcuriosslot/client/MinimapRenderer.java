package com.madgique.mapcuriosslot.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import top.theillusivec4.curios.api.CuriosApi;
import net.minecraftforge.common.util.LazyOptional;

public class MinimapRenderer {
    
    private static final int MINIMAP_SIZE = 150;
    private static final int MINIMAP_X = 10;
    private static final int MINIMAP_Y = 10;
    
    public static void renderMinimap(GuiGraphics guiGraphics, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        
        if (player == null || minecraft.level == null) {
            return;
        }
        
        // Get map from Curios slot - check EVERY frame
        ItemStack mapStack = getMapFromCuriosSlot(player);
        if (mapStack.isEmpty() || !(mapStack.getItem() instanceof MapItem)) {
            // Debug: toujours afficher si pas de carte pour voir ce qui se passe
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
        
        
        // Style papyrus/parchemin ancien
        RenderSystem.enableBlend();
        
        // Ombre portée pour la profondeur
        guiGraphics.fill(MINIMAP_X + 2, MINIMAP_Y + 2, 
                        MINIMAP_X + MINIMAP_SIZE + 8, MINIMAP_Y + MINIMAP_SIZE + 8, 
                        0x88000000); // Ombre noire semi-transparente
        
        // Bordure externe en brun foncé (comme du vieux papier)
        guiGraphics.fill(MINIMAP_X - 4, MINIMAP_Y - 4, 
                        MINIMAP_X + MINIMAP_SIZE + 4, MINIMAP_Y + MINIMAP_SIZE + 4, 
                        0xFF8B4513); // Brun foncé
        
        // Bordure moyenne en brun plus clair
        guiGraphics.fill(MINIMAP_X - 2, MINIMAP_Y - 2, 
                        MINIMAP_X + MINIMAP_SIZE + 2, MINIMAP_Y + MINIMAP_SIZE + 2, 
                        0xFFD2B48C); // Beige/tan
        
        // Fond du parchemin (couleur papyrus)
        guiGraphics.fill(MINIMAP_X, MINIMAP_Y, 
                        MINIMAP_X + MINIMAP_SIZE, MINIMAP_Y + MINIMAP_SIZE, 
                        0xFFF5DEB3); // Couleur blé/parchemin
        
        // Effet de vieillissement avec des lignes subtiles
        for (int i = 0; i < 3; i++) {
            int yPos = MINIMAP_Y + (MINIMAP_SIZE / 4) * (i + 1);
            guiGraphics.fill(MINIMAP_X + 5, yPos, MINIMAP_X + MINIMAP_SIZE - 5, yPos + 1, 
                            0x44CD853F); // Lignes de vieillissement
        }
        
        // Coins arrondis effet papyrus (simulés avec de petits carrés)
        // Coin haut-gauche
        guiGraphics.fill(MINIMAP_X - 4, MINIMAP_Y - 4, MINIMAP_X - 2, MINIMAP_Y - 2, 0x00000000);
        guiGraphics.fill(MINIMAP_X - 2, MINIMAP_Y - 4, MINIMAP_X - 1, MINIMAP_Y - 3, 0x00000000);
        
        // Coin haut-droit  
        guiGraphics.fill(MINIMAP_X + MINIMAP_SIZE + 2, MINIMAP_Y - 4, MINIMAP_X + MINIMAP_SIZE + 4, MINIMAP_Y - 2, 0x00000000);
        guiGraphics.fill(MINIMAP_X + MINIMAP_SIZE + 1, MINIMAP_Y - 4, MINIMAP_X + MINIMAP_SIZE + 2, MINIMAP_Y - 3, 0x00000000);
        
        // Coin bas-gauche
        guiGraphics.fill(MINIMAP_X - 4, MINIMAP_Y + MINIMAP_SIZE + 2, MINIMAP_X - 2, MINIMAP_Y + MINIMAP_SIZE + 4, 0x00000000);
        
        // Coin bas-droit
        guiGraphics.fill(MINIMAP_X + MINIMAP_SIZE + 2, MINIMAP_Y + MINIMAP_SIZE + 2, MINIMAP_X + MINIMAP_SIZE + 4, MINIMAP_Y + MINIMAP_SIZE + 4, 0x00000000);
        
        // Titre "Carte" en style ancien (petit texte au-dessus)
        guiGraphics.drawString(minecraft.font, "Carte", MINIMAP_X + MINIMAP_SIZE/2 - 10, MINIMAP_Y - 15, 0xFF8B4513);
        
        // Forcer la mise à jour de la position du joueur côté client aussi
        
        // Rendu de la carte GRANDE
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(MINIMAP_X, MINIMAP_Y, 0);
        
        // Scale pour remplir tout l'espace disponible 
        float scale = (float) MINIMAP_SIZE / 128.0f;
        guiGraphics.pose().scale(scale, scale, 1.0f);
        
        // Technique différente : copier temporairement l'item dans la main pendant le rendu
        ItemStack handItem = player.getMainHandItem();
        boolean wasHoldingMap = handItem.getItem() instanceof MapItem && MapItem.getMapId(handItem) != null && MapItem.getMapId(handItem).equals(mapId);
        
        if (!wasHoldingMap) {
            // Temporairement faire croire au système que le joueur tient la carte
            // Cela permet l'affichage du joueur sur la carte
            player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, mapStack);
        }
        
        // Rendu normal de la carte avec position du joueur
        minecraft.gameRenderer.getMapRenderer().render(
            guiGraphics.pose(), 
            guiGraphics.bufferSource(), 
            mapId, 
            mapData, 
            true, // Afficher les décorations (joueur inclus)
            15728880
        );
        
        if (!wasHoldingMap) {
            // Remettre l'objet original
            player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, handItem);
        }
        
        guiGraphics.pose().popPose();
        
        RenderSystem.disableBlend();
    }
    
    private static ItemStack getMapFromCuriosSlot(LocalPlayer player) {
        return CuriosApi.getCuriosInventory(player)
                .map(curiosInventory -> curiosInventory.getStacksHandler("map")
                        .map(slotInventory -> {
                            // Check all slots in the "map" slot type for a map item
                            for (int i = 0; i < slotInventory.getSlots(); i++) {
                                ItemStack stack = slotInventory.getStacks().getStackInSlot(i);
                                if (!stack.isEmpty() && stack.getItem() instanceof MapItem) {
                                    return stack;
                                }
                            }
                            return ItemStack.EMPTY;
                        })
                        .orElse(ItemStack.EMPTY))
                .orElse(ItemStack.EMPTY);
    }
}