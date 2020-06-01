package minicraft.backend.map;

import static org.junit.Assert.*;

import org.junit.Test;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.utils.BlockCoord;

public class DimensionMapTest {
    @Test
    public void generateTest(){
        DimensionMap map = new DimensionMap("world");
        map.generateFromGenerator(true);

        assertEquals("grass", map.getBlockByCoord(new BlockCoord(0, 4, 0)).getName());
        assertEquals("bedrock", map.getBlockByCoord(new BlockCoord(0, 0, 0)).getName());
        assertEquals("air", map.getBlockByCoord(new BlockCoord(-15, 5, 8)).getName());
        assertEquals("dirt", map.getBlockByCoord(new BlockCoord(-110, 1, 82)).getName());
    }

    @Test
    public void placeBlockTest(){
        DimensionMap map = new DimensionMap("world");
        map.generateFromGenerator(true);

        BlockBackend block = BlockBackend.getBlockInstanceByID(2); //草方块
        block.placeAt(new BlockCoord(-17, 8, 66), map);

        assertEquals("grass", map.getBlockByCoord(new BlockCoord(-17, 8, 66)).getName());
        assertEquals("air", map.getBlockByCoord(new BlockCoord(-17, 7, 66)).getName());
        assertEquals("air", map.getBlockByCoord(new BlockCoord(-16, 8, 66)).getName());
        assertEquals("grass", map.getBlockByCoord(new BlockCoord(-17, 4, 66)).getName());
        assertEquals("dirt", map.getBlockByCoord(new BlockCoord(-17, 2, 66)).getName());

        
        assertFalse(block.placeAt(new BlockCoord(-17, 9, 66), map));
        //不会放置，因为这个对象已经放在了(-17, 8, 66)
        assertEquals("air", map.getBlockByCoord(new BlockCoord(-17, 9, 66)).getName());
    }

    @Test
    public void destroyBlockTest(){
        DimensionMap map = new DimensionMap("world");
        map.generateFromGenerator(true);

        BlockBackend block = map.getBlockByCoord(new BlockCoord(-110, 4, 82));
        assertEquals("grass", block.getName());
        block.destoryBlock();  //此后这个对象无意义，将在GC后自动丢弃
        assertEquals("air", map.getBlockByCoord(new BlockCoord(-110, 4, 82)).getName());

        block = map.getBlockByCoord(new BlockCoord(-110, 3, 82));
        assertEquals("dirt", block.getName());
        block.destoryBlock();  //此后这个对象无意义，将在GC后自动丢弃
        assertEquals("air", map.getBlockByCoord(new BlockCoord(-110, 3, 82)).getName());

        block = BlockBackend.getBlockInstanceByID(1); //石头
        block.placeAt(new BlockCoord(-110, 4, 82), map);
        assertEquals("air", map.getBlockByCoord(new BlockCoord(-110, 3, 82)).getName());
        assertEquals("stone", map.getBlockByCoord(new BlockCoord(-110, 4, 82)).getName());

    }

    @Test
    public void foreachTest(){
        DimensionMap overworld = new DimensionMap("world");
        overworld.generateFromGenerator(true);
        for(int x=Constant.minX;x<=Constant.maxX;x++) {
            for(int y=0;y<Constant.maxY;y++) {
                for(int z=Constant.minZ;z<=Constant.maxZ;z++) {
                    overworld.getBlockByCoord(new BlockCoord(x,y,z));
                }
            }
        }
    }

    // @Test
    // public void initUpdateTest(){
    //     DimensionMap overworld = new DimensionMap("world");
    //     overworld.generateFromGenerator(true);

    //     HashSet<BlockBackend> blockset = overworld.refreshWholeUpdateBlockSet();

    //     assertEquals(6400, blockset.size());

    //     for(BlockBackend b: blockset){
    //         assertEquals(4, b.getBlockCoord().getY());
    //     }

    // }
}