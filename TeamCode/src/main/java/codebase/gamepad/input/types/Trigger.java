package codebase.gamepad.input.types;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


import codebase.gamepad.Controller;
import codebase.gamepad.input.GInput;
import codebase.gamepad.input.GAnalog;
import codebase.gamepad.input.GIsPressed;
import codebase.gamepad.input.GIsToggled;
import codebase.gamepad.input.GOnPress;
import codebase.gamepad.input.GOnRelease;
import codebase.gamepad.input.GOnToggle;
import codebase.gamepad.input.GWhileDown;


public class Trigger implements GInput, GIsPressed, GIsToggled, GWhileDown<Trigger>, GOnToggle<Trigger>, GOnPress<Trigger>, GOnRelease<Trigger> {
    private final Supplier<Float> valueSupplier;
    private final Controller controller;

    private Runnable onPress, onRelease, whileDown, onToggleOn, onToggleOff;

    private Consumer<Boolean> onToggle;

    private boolean wasDownLast;

    private boolean toggleState;

    /**
     * The minimum value for the trigger to be considered down
     */
    private static final float PRESS_THRESHOLD = 0;

    public Trigger(Controller controller, Supplier<Float> valueSupplier) {
        this.valueSupplier = valueSupplier;
        this.controller = controller;
    }

    @Override
    public void loop() {
        boolean isPressed = isPressed();

        if(isPressed && !this.wasDownLast) {
            if (this.onPress != null) {
                onPress.run();
            }

            // Toggle
            this.toggleState = !this.toggleState;

            if (this.onToggle != null) {
                this.onToggle.accept(this.toggleState);
            }

            if (this.onToggleOff != null && !this.toggleState) {
                this.onToggleOff.run();
            }

            if (this.onToggleOn != null && this.toggleState) {
                this.onToggleOn.run();
            }
        }
        if (this.onRelease != null && !isPressed && this.wasDownLast) {
            onRelease.run();
        }
        if (this.whileDown != null && isPressed) {
            whileDown.run();
        }

        this.wasDownLast = isPressed;
    }

    @Override
    public Controller getController() {
        return this.controller;
    }

    @Override
    public boolean isPressed() {
        return this.valueSupplier.get() >= PRESS_THRESHOLD;
    }

    @Override
    public boolean isToggled() {
        return this.toggleState;
    }

    @Override
    public Trigger onPress(Runnable onPress) {
        this.onPress = onPress;
        return this;
    }

    @Override
    public Trigger onRelease(Runnable onRelease) {
        this.onRelease = onRelease;
        return this;
    }

    @Override
    public Trigger whileDown(Runnable whileDown) {
        this.whileDown = whileDown;
        return this;
    }

    @Override
    public Trigger onToggleOn(Runnable onToggleOn) {
        this.onToggleOn = onToggleOn;
        return this;
    }

    @Override
    public Trigger onToggleOff(Runnable onToggleOff) {
        this.onToggleOff = onToggleOff;
        return this;
    }

    @Override
    public Trigger initalToggleState(boolean toggled) {
        this.toggleState = toggled;
        return this;
    }

    @Override
    public Trigger onToggle(Consumer<Boolean> onToggle) {
        this.onToggle = onToggle;
        return this;
    }
}
