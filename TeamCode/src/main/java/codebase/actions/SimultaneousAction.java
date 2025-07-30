package codebase.actions;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class SimultaneousAction implements Action {

    private final ArrayList<Action> actions;

    public SimultaneousAction(Action... actions) {
        if (actions.length == 0) {
            throw new IllegalArgumentException("You must pass at least one action");
        }

        this.actions = new ArrayList<Action>();
        for (Action action : actions) {
            this.add(action);
        }
    }

    @Override
    public void init() {
        for (Action action : actions) {
            action.init();
        }
    }

    @Override
    public boolean isComplete() {
        for (Action action : actions) {
            if (!action.isComplete()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void loop() {
        for (Action action : actions) {
            if (!action.isComplete()) {
                action.loop();
            }
        }
    }

    public void add(@NonNull Action action) {
        String actionName = action.getClass().getName();

        for (Action a : actions) {
            if (a.getClass().getName().equals(actionName)) {
                throw new IllegalArgumentException("You can't add multiple of the same Action to SimultaneousAction.");
            }
        }
        actions.add(action);
    }
}