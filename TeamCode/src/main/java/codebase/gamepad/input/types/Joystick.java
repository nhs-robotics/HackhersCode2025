package codebase.gamepad.input.types;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import codebase.gamepad.Controller;
import codebase.gamepad.input.GAnalog;
import codebase.gamepad.input.GInput;

public class Joystick implements GInput, GAnalog<Joystick> {

    private final Controller controller;

    private BiConsumer<Float, Float> onMove;

    private final Supplier<Float> xSupplier;
    private final Supplier<Float> ySupplier;

    private float lastX;
    private float lastY;

    public Joystick(Controller controller, Supplier<Float> xSupplier, Supplier<Float> ySupplier) {
        this.controller = controller;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
    }

    @Override
    public void loop() {
        float currentX = this.xSupplier.get();
        float currentY = this.ySupplier.get();

        if (currentX != lastX || currentY != lastY) {
            this.lastX = currentX;
            this.lastY = currentY;

            if (this.onMove != null) {
                this.onMove.accept(currentX, currentY);
            }
        }
    }

    @Override
    public Joystick onMove(BiConsumer<Float, Float> onMove) {
        this.onMove = onMove;
        return this;
    }

    @Override
    public Controller getController() {
        return this.controller;
    }
}
