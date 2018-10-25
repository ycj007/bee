package com.ycj.bee.refactor.flux;

import java.util.List;

interface MyEventListener<T> {
    void onDataChunk(List<T> chunk);
    void processComplete();
}