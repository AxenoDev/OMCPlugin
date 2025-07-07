package fr.openmc.core.features.utils;

import com.sk89q.worldedit.world.block.BlockType;
import fr.openmc.core.OMCPlugin;
import fr.openmc.core.features.city.CityManager;
import fr.openmc.core.features.city.ProtectionsManager;
import fr.openmc.core.utils.customitems.CustomItemRegistry;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class BuilderWand implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null && event.getItem() == null)
            return;

        if (event.getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (!CustomItemRegistry.getByName("omc_items:builder_wand").getBest().getType().equals(event.getItem().getType()))
            return;

        Block interactedBlock = event.getClickedBlock();

        if (interactedBlock == null) {
            return;
        }

        Material blockType = interactedBlock.getType();

        if (blockType.isAir()) {
            return;
        }

        Player player = event.getPlayer();
        BlockFace blockFace = event.getBlockFace();

        if (!player.getGameMode().equals(GameMode.CREATIVE) && !player.getInventory().contains(blockType)) {
            return;
        }
        event.setCancelled(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                placeBlocks(player, interactedBlock, blockType, blockFace);
            }
        }.runTaskLater(OMCPlugin.getInstance(), 1L);
    }

    private void placeBlocks(Player player, Block interactedBlock, Material blockType, BlockFace blockFace) {

        ItemStack builderWand = player.getInventory().getItemInMainHand();
        World world = player.getWorld();
        int radius = 10;
        int x;
        int y;
        int z;

        switch (blockFace) {
            case UP:
            case DOWN:
                for (x = -radius; x <= radius; x++) {
                    for (z = -radius; z <= radius; z++) {

                        Location newLoc = interactedBlock.getLocation().add(x, 0, z);
                        boolean blockPlaced = tryPlaceBlock(world, newLoc, blockType, blockFace, player);

                        if (!blockPlaced) {
                            continue;
                        }

                        if (player.getGameMode().equals(GameMode.CREATIVE)) {
                            continue;
                        }

                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item != null && item.getType() == blockType) {
                                item.setAmount(item.getAmount() - 1);
                                break;
                            }
                        }
                    }
                }
                break;
            case NORTH:
            case SOUTH:
                for (x = -radius; x <= radius; x++) {
                    for (y = -radius; y <= radius; y++) {

                        Location newLoc = interactedBlock.getLocation().add(x, y, 0);
                        boolean blockPlaced = tryPlaceBlock(world, newLoc, blockType, blockFace, player);

                        if (!blockPlaced) {
                            continue;
                        }

                        if (player.getGameMode().equals(GameMode.CREATIVE)) {
                            continue;
                        }

                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item != null && item.getType() == blockType) {
                                item.setAmount(item.getAmount() - 1);
                                break;
                            }
                        }

                    }
                }
                break;
            case EAST:
            case WEST:
                for (z = -radius; z <= radius; z++) {
                    for (y = -radius; y <= radius; y++) {

                        Location newLoc = interactedBlock.getLocation().add(0, y, z);
                        boolean blockPlaced = tryPlaceBlock(world, newLoc, blockType, blockFace, player);

                        if (!blockPlaced) {
                            continue;
                        }

                        if (player.getGameMode().equals(GameMode.CREATIVE)) {
                            continue;
                        }

                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item != null && item.getType() == blockType) {
                                item.setAmount(item.getAmount() - 1);
                                break;
                            }
                        }
                    }
                }
                break;
        }

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

//        CustomItemsUtils.damageItem(builderWand, 1);
    }

    private boolean tryPlaceBlock(World world, Location newLoc, Material blockType, BlockFace blockFace, Player player) {

        Block adjacentBlock = world.getBlockAt(newLoc);
        Material adjacentBlockType = adjacentBlock.getType();

        if (adjacentBlock.getType().isAir() || adjacentBlockType != blockType) {
            return false;
        }

        ProtectionsManager.verify(player, null, adjacentBlock.getLocation());

        Block blockToPlace = adjacentBlock.getRelative(blockFace);

        if (!blockToPlace.getType().isAir()) {
            return false;
        }

        Collection<Entity> entities = blockToPlace.getWorld().getNearbyEntities(blockToPlace.getLocation(), 1, 1, 1);

        if (!entities.isEmpty()) {
            return false;
        }

        if (!player.getGameMode().equals(GameMode.CREATIVE) && !player.getInventory().contains(blockType)) {
            return false;
        }

        blockToPlace.setType(blockType);
        return true;
    }

}