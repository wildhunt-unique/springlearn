package cn.mine.learning.spring.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 丁星（镜月）
 * @since 2021-03-29
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MultipleMap<K, V> {
    Map<K, List<V>> val;

    private MultipleMap(int n) {
        this.val = new HashMap<>(n);
    }

    public static <K, V> MultipleMap<K, V> create() {
        return new MultipleMap<>(64);
    }

    public static <K, V> MultipleMap<K, V> create(int n) {
        return new MultipleMap<>(n);
    }

    public List<V> find(K key) {
        List<V> list = val.get(key);
        if (null == list) {
            return new ArrayList<>(4);
        } else {
            return list;
        }
    }

    public void put(K key, V v) {
        List<V> vs = val.get(key);
        if (vs != null) {
            vs.add(v);
        } else {
            vs = new ArrayList<>(8);
            vs.add(v);
            val.put(key, vs);
        }
    }
}
