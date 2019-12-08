package com.drazisil.messy.event;

import com.drazisil.messy.BlockSave;
import com.drazisil.messy.Messy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;


public class LuckyEventDisco implements LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        Location standingLocation = location;

        ArrayList<ArrayList<BlockSave>> oldStateMatrix = new ArrayList<>();

        ArrayList<ArrayList<Block>> newBlockMatrix = new ArrayList<>();

        // Drop it by one
        standingLocation.setY(standingLocation.getY() - 1);

        Location startLocation = standingLocation;
        startLocation.setX(startLocation.getX() - 3);
        startLocation.setZ(startLocation.getZ() - 3);

        Location cursorLocation = startLocation;

        double startX = startLocation.getX();
        double startZ = startLocation.getZ();

        for (int z = 0; z <= 6; z++) {
            cursorLocation.setZ(startZ + z);
            ArrayList<BlockSave> blockRow = new ArrayList<>();
            for (int x = 0; x <= 6; x++) {
                cursorLocation.setX(startX + x);

                Block block = cursorLocation.getBlock();
                Material type = block.getType();
                BlockState state = block.getState();

                blockRow.add(new BlockSave(block, type, cursorLocation, state));
            }
            oldStateMatrix.add(blockRow);

        }

        for (int z = 0; z <= 6; z++) {
            cursorLocation.setZ(startZ + z);
            ArrayList<Block> blockRow = new ArrayList<>();
            for (int x = 0; x <= 6; x++) {
                cursorLocation.setX(startX + x);
                blockRow.add(cursorLocation.getBlock());
            }
            newBlockMatrix.add(blockRow);

        }

        // ============================================


        for (int z = 0; z <= 6; z++) {
            cursorLocation.setX(startZ + z);
            for (int x = 0; x <= 6; x++) {
                cursorLocation.setX(startX + x);
                Block block = newBlockMatrix.get(z).get(x);

                // Attempt to clear drops if furnace
                if (block.getType() == Material.FURNACE) {
                    ((Furnace)block.getState()).getInventory().setFuel(null);
                    ((Furnace)block.getState()).getInventory().setResult(null);
                    ((Furnace)block.getState()).getInventory().setSmelting(null);
                }

                // Attempt to clear drops if chest
                if (block.getType() == Material.CHEST) {
                    ((Chest) block.getState()).getBlockInventory().clear();
//                    Chest state = ((Chest) block.getState());
//                    ItemStack[] newContents = new ItemStack[] {new ItemStack(Material.AIR)};
//                    state.getBlockInventory().setContents(newContents);
//                    state.update();
                }


                block.setType(Material.REDSTONE_LAMP);
            }

        }



        Messy plugin = Messy.getInstance();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (int z = 0; z <= 6; z++) {
                for (int x = 0; x <= 6; x++) {
                    newBlockMatrix.get(z).get(x).setType(oldStateMatrix.get(z).get(x).getType());
                    oldStateMatrix.get(z).get(x).getState().update();
                }

            }
        }, 400L);

    }
}