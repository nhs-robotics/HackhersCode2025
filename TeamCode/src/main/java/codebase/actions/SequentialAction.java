package codebase.actions;

import java.util.Arrays;

public class SequentialAction implements Action {

    private ActionNode currentActionNode;

    public SequentialAction(Action... actions) {
        if (actions.length == 0) {
            throw new IllegalArgumentException("You must pass at least one action");
        }

        this.currentActionNode = new ActionNode(actions);
    }

    @Override
    public void init() {
        currentActionNode.action.init();
    }

    @Override
    public boolean isComplete() {
        return this.currentActionNode == null;
    }

    @Override
    public void loop() {
        if (this.currentActionNode.action.isComplete()) {
            this.currentActionNode = this.currentActionNode.next;
        }

        if (this.currentActionNode == null) {
            return;
        }

        this.currentActionNode.action.loop();
    }



}

class ActionNode {
    public Action action;
    public ActionNode next;
    public ActionNode(Action[] actions) {
        this.action = actions[0];
        if (actions.length == 1) {
            this.next = null;
            return;
        }
        this.next = new ActionNode(Arrays.copyOfRange(actions, 1, actions.length));
    }


}