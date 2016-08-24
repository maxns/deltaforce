package org.namstorm.deltaforce.core;

import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by maxnamstorm on 6/8/2016.
 */
public class DeltaUtilTest extends TestCase {

    public static final String KEY = "key";
    public static final String MAGIC_KEY = "magic_key";
    public static final String MAGIC_MAP_KEY = "magic_map_key";

    public static final int MAGIC_ORIG_VALUE = 10_10123;
    DeltaMap deltaMap;

    Map<String,Object> refSourceMap;
    Map<String,Object> refTargetMap;
    Map<String,Object> testMap;
    Map<String,Object> testParentMap;


    @org.junit.Before
    public void setUp() throws Exception {
        refSourceMap = createMultiMap(3);

        refTargetMap = new HashMap<>(refSourceMap);

        testMap = new HashMap<>();
        testParentMap = new HashMap<>();

        deltaMap = new DeltaMap(Delta.OP.UPDATE, getName(), refSourceMap);
    }
    Random rnd = new Random(System.currentTimeMillis());

    public Map createMultiMap(int depth) {
        Map res = createMap();

        if (depth > 0) {
            res.put(MAGIC_MAP_KEY, createMultiMap(depth-1));
        }
        return res;
    }

    public Map createMap() {
        HashMap<String,Object> res = new HashMap<>();

        res.put(MAGIC_KEY, MAGIC_ORIG_VALUE);
        res.put(KEY + rnd.nextInt(), new BigDecimal(rnd.nextDouble()));
        res.put(KEY + rnd.nextInt(), new BigDecimal(rnd.nextDouble()));
        res.put(KEY + rnd.nextInt(), new BigDecimal(rnd.nextDouble()));

        return res;
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @Test
    public void testApplyMapDeltas() throws Exception {
        String TEST_UPDATE_VALUE = "testValue123";

        deltaMap.addDelta(MAGIC_KEY, new Delta<>(Delta.OP.UPDATE, MAGIC_KEY,refSourceMap.get(MAGIC_KEY),TEST_UPDATE_VALUE));


        DeltaUtil.applyMapDeltas(deltaMap, testMap);

        assertEquals("Updated map value", TEST_UPDATE_VALUE, testMap.get(MAGIC_KEY));



        deltaMap.addDelta(MAGIC_KEY+"Add", new Delta<>(Delta.OP.ADD, MAGIC_KEY+"Add",refSourceMap.get(MAGIC_KEY),TEST_UPDATE_VALUE+"Add"));

        DeltaUtil.applyMapDeltas(deltaMap, testMap);

        assertEquals("Added map value", TEST_UPDATE_VALUE+"Add", testMap.get(MAGIC_KEY+"Add"));



        deltaMap.addDelta(MAGIC_KEY, new Delta<>(Delta.OP.ADD, MAGIC_KEY,refSourceMap.get(MAGIC_KEY),null));

        DeltaUtil.applyMapDeltas(deltaMap, testMap);

        assertEquals("Remove map value", null, testMap.get(MAGIC_KEY));


    }
    @Test
    public void testApplyNestedMapDeltas() throws Exception {
        String TEST_UPDATE_VALUE = "testMapValue123";

        DeltaMap deepDeltas;

        deepDeltas = new DeltaMap(Delta.OP.UPDATE, MAGIC_MAP_KEY, (Map) refSourceMap.get(MAGIC_MAP_KEY));
        deepDeltas.addDelta(MAGIC_KEY, new Delta<>(Delta.OP.UPDATE, MAGIC_KEY,refSourceMap.get(MAGIC_KEY),TEST_UPDATE_VALUE));

        deltaMap.addDelta(MAGIC_MAP_KEY, deepDeltas);
        DeltaUtil.applyMapDeltas(deltaMap, testMap);

        assertEquals("Updated value", TEST_UPDATE_VALUE, ((Map)testMap.get(MAGIC_MAP_KEY)).get(MAGIC_KEY));

        deepDeltas = new DeltaMap(Delta.OP.UPDATE,MAGIC_MAP_KEY, (Map) refSourceMap.get(MAGIC_MAP_KEY));
        deepDeltas.addDelta(MAGIC_KEY, new Delta<>(Delta.OP.REMOVE, MAGIC_KEY,refSourceMap.get(MAGIC_KEY),TEST_UPDATE_VALUE));

        deltaMap.addDelta(MAGIC_MAP_KEY, deepDeltas);
        DeltaUtil.applyMapDeltas(deltaMap, testMap);

        assertNull("Removed value", ((Map)testMap.get(MAGIC_MAP_KEY)).get(MAGIC_KEY));

    }

    @org.junit.Test
    public void visitDeltas() throws Exception {

    }

}