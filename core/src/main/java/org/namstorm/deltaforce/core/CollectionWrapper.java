package org.namstorm.deltaforce.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by maxnamstorm on 14/8/2016.
 */
public abstract class CollectionWrapper <T> implements Collection<T> {

    protected abstract Collection<T> items();


    @Override
    public int size() {
        return items().size();
    }

    @Override
    public boolean isEmpty() {
        return items().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return items().contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return items().iterator();
    }

    @Override
    public Object[] toArray() {
        return items().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return items().toArray(a);
    }

    @Override
    public boolean add(T t) {
        return items().add(t);
    }

    @Override
    public boolean remove(Object o) {
        return items().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return items().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return items().addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return items().removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return items().removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return items().retainAll(c);
    }

    @Override
    public void clear() {
        items().clear();
    }

    @Override
    public boolean equals(Object o) {
        return items().equals(o);
    }

    @Override
    public int hashCode() {
        return items().hashCode();
    }

    @Override
    public Spliterator<T> spliterator() {
        return items().spliterator();
    }

    @Override
    public Stream<T> stream() {
        return items().stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return items().parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        items().forEach(action);
    }



}
