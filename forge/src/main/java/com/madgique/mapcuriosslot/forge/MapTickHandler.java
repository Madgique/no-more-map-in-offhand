package com.madgique.mapcuriosslot.forge;

import com.madgique.mapcuriosslot.MapCuriosSlotMod;
import top.theillusivec4.curios.api.CuriosApi;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MapCuriosSlotMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MapTickHandler {

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Phase END uniquement
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        // Côté serveur uniquement
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }

        // Optimisation: ne vérifier que toutes les 5 ticks (0.25 secondes)
        tickCounter++;
        if (tickCounter % 5 != 0) {
            return;
        }

        // Vérifier si le joueur a une map dans un slot Curios
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("map").ifPresent(stacksHandler -> {
                // Pour chaque slot "map"
                for (int i = 0; i < stacksHandler.getSlots(); i++) {
                    ItemStack stack = stacksHandler.getStacks().getStackInSlot(i);
                    
                    // Vérifier si c'est une map
                    if (!stack.isEmpty() && stack.getItem() instanceof MapItem) {
                        processMapStack(player, stack);
                    }
                }
            });
        });
    }

    private static void processMapStack(ServerPlayer player, ItemStack stack) {
        // Récupérer le mapId
        Integer mapId = MapItem.getMapId(stack);
        if (mapId == null) return;

        // Récupérer le MapItemSavedData
        MapItemSavedData data = MapItem.getSavedData(mapId, player.level());
        if (data == null) return;

        // ÉTAPE 1: Vérifier si le joueur est dans la bonne dimension pour cette map
        if (data.dimension != player.level().dimension()) {
            return; // Le joueur n'est pas dans la dimension de cette map
        }

        // ÉTAPE 2: Utiliser l'instance de MapItem pour mettre à jour la carte
        MapItem mapItem = (MapItem) stack.getItem();
        mapItem.update(player.level(), player, data);
        
        // ÉTAPE 3: Appeler tickCarriedBy pour créer le curseur du joueur
        // Cette méthode crée automatiquement la MapDecoration PLAYER basée sur la position du joueur
        data.tickCarriedBy(player, stack);
    }
}