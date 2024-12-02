package me.BlockDynasty.Economy.domain.repository;

public interface Callback<T> {
    void call(T t);
}
