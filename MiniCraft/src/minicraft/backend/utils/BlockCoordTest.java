package minicraft.backend.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class BlockCoordTest {
    @Test
    public void block2ChunkTest(){
        BlockCoord blockCoord = new BlockCoord(2, 23, 20);
        ChunkCoord chunkCoord = blockCoord.toChunkCoord();

        assertEquals(0, chunkCoord.getX());
        assertEquals(1, chunkCoord.getZ());

        blockCoord = new BlockCoord(0, 12, 0);
        chunkCoord = blockCoord.toChunkCoord();

        assertEquals(0, chunkCoord.getX());
        assertEquals(0, chunkCoord.getZ());

        blockCoord = new BlockCoord(-1, 12, 22);
        chunkCoord = blockCoord.toChunkCoord();

        assertEquals(-1, chunkCoord.getX());
        assertEquals(1, chunkCoord.getZ());

        blockCoord = new BlockCoord(-16, 12, -22);
        chunkCoord = blockCoord.toChunkCoord();

        assertEquals(-1, chunkCoord.getX());
        assertEquals(-2, chunkCoord.getZ());
    }
}