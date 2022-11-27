package cn.mine.learning.spring.core.util;

import java.util.*;

/**
 * @author 丁星（镜月）
 * @since 2021/12/11
 */
@SuppressWarnings("unused")
public class Collections {
    public static  <K, V> Map<K, V> emptyMap(){
        return java.util.Collections.emptyMap();
    }

    public static <K, V> Map<K, V> newMap() {
        return newMap(16);
    }

    public static <K, V> Map<K, V> newMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = 0;
        }
        return new HashMap<>(initialCapacity);
    }

    public static <K, V> Map<K, V> newMap(K k1, V v1) {
        Map<K, V> map = newMap(16);
        map.put(k1, v1);
        return map;
    }

    public static <K, V> Map<K, V> newMap(K k1, V v1, K k2, V v2) {
        Map<K, V> kvMap = newMap(k1, v1);
        kvMap.put(k2, v2);
        return kvMap;
    }

    public static <K, V> Map<K, V> newMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> kvMap = newMap(k1, v1, k2, v2);
        kvMap.put(k3, v3);
        return kvMap;
    }

    public static <K, V> Map<K, V> newMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> kvMap = newMap(k1, v1, k2, v2, k3, v3);
        kvMap.put(k4, v4);
        return kvMap;
    }


    public static <E> List<E> newArrayListWithLength(int length) {
        if (length <= 0) {
            length = 0;
        }
        return new ArrayList<>(length);
    }

    public static <E> List<E> newArrayList() {
        return new ArrayList<>();
    }

    @SafeVarargs
    public static <E> List<E> newArrayList(E... element) {
        if (null == element) {
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(element));
    }

    public boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    public boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    @SuppressWarnings("unchecked")
    public static <E> List<E> emptyList() {
        return (List<E>) EMPTY_LIST;
    }

    private final static List<?> EMPTY_LIST = new ArrayList<Object>() {
        @Override
        public boolean add(Object o) {
            throw new UnsupportedOperationException("not.support");
        }

        @Override
        public Object remove(int index) {
            throw new UnsupportedOperationException("not.support");
        }
    };
}
