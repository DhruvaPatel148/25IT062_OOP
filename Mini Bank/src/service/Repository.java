package service;

import model.annotation.Id;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Generic repository: stores any type, finds the key through the @Id field by reflection. */
public class Repository<T> {
    // ConcurrentHashMap is a Map just like HashMap, but safe for many threads
    private final Map<String, T> store = new ConcurrentHashMap<>();

    public T save(T entity) {
        store.put(idOf(entity), entity);
        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) save(item);
    }

    public T findById(String id)       { return store.get(id); }
    public ArrayList<T> findAll()      { return new ArrayList<>(store.values()); }
    public boolean delete(String id)   { return store.remove(id) != null; }
    public int count()                 { return store.size(); }

    private String idOf(T entity) {
        try {
            for (Class<?> c = entity.getClass(); c != null; c = c.getSuperclass()) {
                for (Field f : c.getDeclaredFields()) {
                    if (f.isAnnotationPresent(Id.class)) {
                        f.setAccessible(true);
                        return String.valueOf(f.get(entity));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalArgumentException(entity.getClass().getSimpleName() + " has no @Id field");
    }

    /** Bounded type parameter: only Integer, Double, Long ... allowed. */
    public static <N extends Number> double average(N[] values) {
        double sum = 0;
        for (N v : values) sum += v.doubleValue();
        return values.length == 0 ? 0 : sum / values.length;
    }
}
