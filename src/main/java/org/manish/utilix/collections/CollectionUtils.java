package org.manish.utilix.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides collection helpers for null-safe access and shallow defensive copying.
 */
public final class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * Determines whether a collection reference is {@code null} or empty.
     *
     * @param collection the collection to inspect
     * @return {@code true} when the collection is {@code null} or has no elements
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Determines whether a map reference is {@code null} or empty.
     *
     * @param map the map to inspect
     * @return {@code true} when the map is {@code null} or has no entries
     */
    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Returns the supplied list or an immutable empty list when the reference is {@code null}.
     *
     * @param list the list to normalize
     * @param <T> the element type
     * @return {@code list} when non-null, otherwise {@link Collections#emptyList()}
     */
    public static <T> List<T> nullToEmpty(List<T> list) {
        return list == null ? Collections.<T>emptyList() : list;
    }

    /**
     * Returns the supplied set or an immutable empty set when the reference is {@code null}.
     *
     * @param set the set to normalize
     * @param <T> the element type
     * @return {@code set} when non-null, otherwise {@link Collections#emptySet()}
     */
    public static <T> Set<T> nullToEmpty(Set<T> set) {
        return set == null ? Collections.<T>emptySet() : set;
    }

    /**
     * Returns the supplied map or an immutable empty map when the reference is {@code null}.
     *
     * @param map the map to normalize
     * @param <K> the key type
     * @param <V> the value type
     * @return {@code map} when non-null, otherwise {@link Collections#emptyMap()}
     */
    public static <K, V> Map<K, V> nullToEmpty(Map<K, V> map) {
        return map == null ? Collections.<K, V>emptyMap() : map;
    }

    /**
     * Creates a shallow copy of a list.
     *
     * @param list the source list
     * @param <T> the element type
     * @return a new list containing the same elements, or an immutable empty list when the source
     *     is {@code null}
     */
    public static <T> List<T> copyOf(List<T> list) {
        return list == null ? Collections.<T>emptyList() : new ArrayList<T>(list);
    }

    /**
     * Creates a shallow copy of a set while preserving insertion order.
     *
     * @param set the source set
     * @param <T> the element type
     * @return a new set containing the same elements, or an immutable empty set when the source is
     *     {@code null}
     */
    public static <T> Set<T> copyOf(Set<T> set) {
        return set == null ? Collections.<T>emptySet() : new LinkedHashSet<T>(set);
    }

    /**
     * Creates a shallow copy of a map while preserving insertion order.
     *
     * @param map the source map
     * @param <K> the key type
     * @param <V> the value type
     * @return a new map containing the same entries, or an immutable empty map when the source is
     *     {@code null}
     */
    public static <K, V> Map<K, V> copyOf(Map<K, V> map) {
        return map == null ? Collections.<K, V>emptyMap() : new LinkedHashMap<K, V>(map);
    }
}
