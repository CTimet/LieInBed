package io.github.ctimet.lieinbedapp.properties;

public interface ValueChecker<T> {
    boolean check(T value);
}
