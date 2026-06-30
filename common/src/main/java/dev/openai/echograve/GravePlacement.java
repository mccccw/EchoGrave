package dev.openai.echograve;

import java.util.ArrayList;
import java.util.List;

/** Utility for selecting the death block first, then nearby offsets when the death block cannot be replaced. */
public final class GravePlacement {
    private GravePlacement() { }

    public static List<BlockOffset> candidateOffsets() {
        List<BlockOffset> offsets = new ArrayList<>();
        offsets.add(new BlockOffset(0, 0, 0));
        offsets.add(new BlockOffset(0, 1, 0));
        for (int radius = 1; radius <= 2; radius++) {
            offsets.add(new BlockOffset(radius, 0, 0));
            offsets.add(new BlockOffset(-radius, 0, 0));
            offsets.add(new BlockOffset(0, 0, radius));
            offsets.add(new BlockOffset(0, 0, -radius));
            offsets.add(new BlockOffset(radius, 1, 0));
            offsets.add(new BlockOffset(-radius, 1, 0));
            offsets.add(new BlockOffset(0, 1, radius));
            offsets.add(new BlockOffset(0, 1, -radius));
        }
        return List.copyOf(offsets);
    }

    public record BlockOffset(int x, int y, int z) { }
}
