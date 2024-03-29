package eu.cosup.cores.managers;

import org.bukkit.block.Block;

import java.util.ArrayList;

public class BlockManager {

    private final ArrayList<Block> playerPlacedBlocks = new ArrayList<>();

    public BlockManager() {
    }

    public void addBlock(Block block) {
        playerPlacedBlocks.add(block);
    }

    public void removeBlock(Block block) {
        playerPlacedBlocks.remove(block);
    }

    public boolean isBlockPlaced(Block block) {
        return playerPlacedBlocks.contains(block);
    }
}
