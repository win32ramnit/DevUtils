package org.manish.utilix.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionUtilsTest {

    @Test
    void testNullToEmpty() {
        assertTrue(CollectionUtils.nullToEmpty((List<String>) null).isEmpty());
        assertTrue(CollectionUtils.nullToEmpty((Set<String>) null).isEmpty());
        assertTrue(CollectionUtils.nullToEmpty((Map<String, String>) null).isEmpty());
    }

    @Test
    void testCopyOf() {
        List<String> list = new ArrayList<String>();
        list.add("a");
        List<String> listCopy = CollectionUtils.copyOf(list);
        assertEquals(list, listCopy);
        assertNotSame(list, listCopy);

        Set<String> set = new HashSet<String>();
        set.add("b");
        Set<String> setCopy = CollectionUtils.copyOf(set);
        assertEquals(set, setCopy);
        assertNotSame(set, setCopy);

        Map<String, String> map = new HashMap<String, String>();
        map.put("k", "v");
        Map<String, String> mapCopy = CollectionUtils.copyOf(map);
        assertEquals(map, mapCopy);
        assertNotSame(map, mapCopy);
    }
}
