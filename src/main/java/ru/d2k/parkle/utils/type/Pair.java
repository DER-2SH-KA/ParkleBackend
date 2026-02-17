package ru.d2k.parkle.utils.type;

public class Pair<K, V> {
    private final K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public final K getKey() {
        return this.key;
    }

    public final V getValue() {
        return this.value;
    }
}
