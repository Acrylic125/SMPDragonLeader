package com.acrylic.smpdl.captures;

@FunctionalInterface
public interface ScoreSource<T> {

    double getScore(T action);

}
