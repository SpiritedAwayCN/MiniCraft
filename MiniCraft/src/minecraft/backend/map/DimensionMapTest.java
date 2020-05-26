package minecraft.backend.map;

import static org.junit.Assert.*;

import org.junit.Test;

import minecraft.backend.map.block.Block;
import minecraft.backend.utils.BlockCoordinate;

public class DimensionMapTest {
    @Test
    public void generateTest(){
        DimensionMap map = new DimensionMap("world");
        map.generateFromGenerator(true);

        assertEquals("grass", map.getBlockByCoordinate(new BlockCoordinate(0, 4, 0)).getName());
        assertEquals("bedrock", map.getBlockByCoordinate(new BlockCoordinate(0, 0, 0)).getName());
        assertEquals("air", map.getBlockByCoordinate(new BlockCoordinate(-15, 5, 8)).getName());
        assertEquals("dirt", map.getBlockByCoordinate(new BlockCoordinate(-110, 1, 82)).getName());
    }

    @Test
    public void placeBlockTest(){
        DimensionMap map = new DimensionMap("world");
        map.generateFromGenerator(true);

        Block block = Block.getBlockInstanceByID(2); //草方块
        block.placeAt(new BlockCoordinate(-17, 8, 66), map);

        assertEquals("grass", map.getBlockByCoordinate(new BlockCoordinate(-17, 8, 66)).getName());
        assertEquals("air", map.getBlockByCoordinate(new BlockCoordinate(-17, 7, 66)).getName());
        assertEquals("air", map.getBlockByCoordinate(new BlockCoordinate(-16, 8, 66)).getName());
        assertEquals("grass", map.getBlockByCoordinate(new BlockCoordinate(-17, 4, 66)).getName());
        assertEquals("dirt", map.getBlockByCoordinate(new BlockCoordinate(-17, 2, 66)).getName());

        
        assertFalse(block.placeAt(new BlockCoordinate(-17, 9, 66), map));
        //不会放置，因为这个对象已经放在了(-17, 8, 66)
        assertEquals("air", map.getBlockByCoordinate(new BlockCoordinate(-17, 9, 66)).getName());
    }

    @Test
    public void destroyBlockTest(){
        DimensionMap map = new DimensionMap("world");
        map.generateFromGenerator(true);

        Block block = map.getBlockByCoordinate(new BlockCoordinate(-110, 4, 82));
        assertEquals("grass", block.getName());
        block.destoryBlock();  //此后这个对象无意义，将在GC后自动丢弃
        assertEquals("air", map.getBlockByCoordinate(new BlockCoordinate(-110, 4, 82)).getName());

        block = map.getBlockByCoordinate(new BlockCoordinate(-110, 3, 82));
        assertEquals("dirt", block.getName());
        block.destoryBlock();  //此后这个对象无意义，将在GC后自动丢弃
        assertEquals("air", map.getBlockByCoordinate(new BlockCoordinate(-110, 3, 82)).getName());

        block = Block.getBlockInstanceByID(1); //石头
        block.placeAt(new BlockCoordinate(-110, 4, 82), map);
        assertEquals("air", map.getBlockByCoordinate(new BlockCoordinate(-110, 3, 82)).getName());
        assertEquals("stone", map.getBlockByCoordinate(new BlockCoordinate(-110, 4, 82)).getName());

    }
}