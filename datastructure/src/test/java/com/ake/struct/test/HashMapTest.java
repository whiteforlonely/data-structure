package com.ake.struct.test;

import java.util.HashMap;
import java.util.Map;

public class HashMapTest {
    public static void main(String[] args) {

        int result = tableSizeFor(16);
        System.out.println(result);

        Map<String, Integer> map = new HashMap<String, Integer>(3);
        map.put("1", 12);
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= 5000) ? 5000 : n + 1;
    }
}
