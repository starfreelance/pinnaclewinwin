package com.pinnacle.winwin.listener;

public interface AddFragmentListener<T> {
    void addFragmentWithType(int fragmentType, T data);
}
