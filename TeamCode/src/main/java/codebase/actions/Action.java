package codebase.actions;

import codebase.Loop;

public interface Action extends Loop {
    void init();

    boolean isComplete();
}
