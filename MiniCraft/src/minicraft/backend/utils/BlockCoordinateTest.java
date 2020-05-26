package minicraft.backend.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class BlockCoordinateTest {
    @Test
    public void block2ChunkTest(){
        BlockCoordinate blockCoordinate = new BlockCoordinate(2, 23, 20);
        ChunkCoordinate chunkCoordinate = blockCoordinate.toChunkCoordnate();

        assertEquals(0, chunkCoordinate.getX());
        assertEquals(1, chunkCoordinate.getZ());

        blockCoordinate = new BlockCoordinate(0, 12, 0);
        chunkCoordinate = blockCoordinate.toChunkCoordnate();

        assertEquals(0, chunkCoordinate.getX());
        assertEquals(0, chunkCoordinate.getZ());

        blockCoordinate = new BlockCoordinate(-1, 12, 22);
        chunkCoordinate = blockCoordinate.toChunkCoordnate();

        assertEquals(-1, chunkCoordinate.getX());
        assertEquals(1, chunkCoordinate.getZ());

        blockCoordinate = new BlockCoordinate(-16, 12, -22);
        chunkCoordinate = blockCoordinate.toChunkCoordnate();

        assertEquals(-1, chunkCoordinate.getX());
        assertEquals(-2, chunkCoordinate.getZ());
    }
}