package com.uniquindio.edu.strucst.structure;

public interface SimpleList<T> extends Iterable<T>{

    void add(T element);

    boolean remove(T element);

    T removeAt(int index);

    T get(int index);

    boolean contains(T element);

    int size();

    boolean isEmpty();

    
}
