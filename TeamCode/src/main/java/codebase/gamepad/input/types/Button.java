package codebase.gamepad.input.types;

import java.util.function.Consumer;
import java.util.function.Supplier;

import codebase.gamepad.Gamepad;
import codebase.gamepad.input.GInput;
import codebase.gamepad.input.GIsPressed;
import codebase.gamepad.input.GIsToggled;
import codebase.gamepad.input.GOnPress;
import codebase.gamepad.input.GOnRelease;
import codebase.gamepad.input.GOnToggle;
import codebase.gamepad.input.GWhileDown;


public class Button implements GInput, GOnToggle<Button>, GIsToggled, GIsPressed, GWhileDown<Button>, GOnPress<Button>, GOnRelease<Button> {
    private final Supplier<Boolean> isPressed;
    private final Gamepad gamepad;

    private Runnable onPress, onRelease, whileDown, onToggleOn, onToggleOff;

    private Consumer<Boolean> onToggle;

    private boolean wasDownLast;

    private boolean toggleState;

    public Button(Gamepad gamepad, Supplier<Boolean> isDown) {
        isPressed = isDown;
        this.gamepad = gamepad;
    }

    @Override
    public void loop() {
        boolean isPressed = this.isPressed.get();
        if (isPressed && !this.wasDownLast) {
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
    public Gamepad getGamepad() {
        return this.gamepad;
    }

    @Override
    public boolean isPressed() {
        return this.isPressed.get();
    }

    @Override
    public boolean isToggled() {
        return this.toggleState;
    }

    @Override
    public Button whileDown(Runnable whileDown) {
        this.whileDown = whileDown;
        return this;
    }

    @Override
    public Button onPress(Runnable onPress) {
        this.onPress = onPress;
        return this;

    }

    @Override
    public Button onRelease(Runnable onRelease) {
        this.onRelease = onRelease;
        return this;
    }

    @Override
    public Button onToggleOn(Runnable onToggleOn) {
        this.onToggleOn = onToggleOn;
        return this;
    }

    @Override
    public Button onToggleOff(Runnable onToggleOff) {
        this.onToggleOff = onToggleOff;
        return this;
    }

    @Override
    public Button initialToggleState(boolean toggled) {
        this.toggleState = toggled;
        return this;
    }

    @Override
    public Button onToggle(Consumer<Boolean> onToggle) {
        this.onToggle = onToggle;
        return this;
    }
}
