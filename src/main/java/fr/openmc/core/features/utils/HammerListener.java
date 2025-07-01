package fr.openmc.core.features.utils;

import fr.openmc.core.utils.customitems.CustomItemRegistry;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import java.util.Objects;

public class HammerListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        Player player = event.getPlayer();
        BlockFace face = getDestroyedBlockFace(player).getOppositeFace();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir())
            return;

        if (!player.getGameMode().equals(GameMode.SURVIVAL))
            return;

        int radius = 0;
        int x, y, z;
        int depth = 0;

        if (Objects.equals(CustomItemRegistry.getByName("omc_items:iron_hammer").getBest().getType(), item.getType())) {
            radius = 1;
            depth = 0;
        } else if (Objects.equals(CustomItemRegistry.getByName("omc_items:diamond_hammer").getBest().getType(), item.getType())) {
            radius = 1;
            depth = 1;
        } else if (Objects.equals(CustomItemRegistry.getByName("omc_items:netherite_hammer").getBest().getType(), item.getType())) {
            radius = 1;
            depth = 2;
        } else {
            return;
        }

        Block blockToBrake;
        Material brokenBlockType = brokenBlock.getType();

        switch (face) {
            case NORTH:
            case SOUTH:
                for (x = -radius; x <= radius; x++) {
                    for (y = -radius; y <= radius; y++) {
                        for (z = -depth; z <= depth; z++) {
                            if (brokenBlock.getType().isAir())
                                brokenBlock.setType(brokenBlockType);

                            blockToBrake = brokenBlock.getRelative(x, y, z);

                            if (Objects.equals(blockToBrake, brokenBlock))
                                continue;
                            if (blockToBrake.getType().getHardness() > 41)
                                continue;
                            if (!blockToBrake.getType().equals(brokenBlock.getType()))
                                continue;

                            blockToBrake.breakNaturally(item);
                        }
                    }
                }
                break;

            case EAST:
            case WEST:
                for (x = -depth; x <= depth; x++) {
                    for (y = -radius; y <= radius; y++) {
                        for (z = -radius; z <= radius; z++) {
                            if (brokenBlock.getType().isAir())
                                brokenBlock.setType(brokenBlockType);

                            blockToBrake = brokenBlock.getRelative(z, y, x);

                            if (Objects.equals(blockToBrake, brokenBlock))
                                continue;
                            if (blockToBrake.getType().getHardness() > 41)
                                continue;
                            if (!blockToBrake.getType().equals(brokenBlock.getType()))
                                continue;

                            blockToBrake.breakNaturally(item);
                        }
                    }
                }
                break;
            case UP:
            case DOWN:
                for (x = -radius; x <= radius; x++) {
                    for (y = -depth; y <= depth; y++) {
                        for (z = -radius; z <= radius; z++) {
                            if (brokenBlock.getType().isAir())
                                brokenBlock.setType(brokenBlockType);

                            blockToBrake = brokenBlock.getRelative(x, y, z);

                            if (Objects.equals(blockToBrake, brokenBlock))
                                continue;
                            if (blockToBrake.getType().getHardness() > 41)
                                continue;
                            if (!blockToBrake.getType().equals(brokenBlock.getType()))
                                continue;

                            blockToBrake.breakNaturally(item);
                        }
                    }
                }
                break;
        }
    }

    public static BlockFace getDestroyedBlockFace(Player player) {
        Location eyeLoc = player.getEyeLocation();
        RayTraceResult result = player.getLocation().getWorld().rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 10,
                FluidCollisionMode.NEVER);

        return result.getHitBlockFace() != null ? result.getHitBlockFace() : BlockFace.SELF;
    }

}
