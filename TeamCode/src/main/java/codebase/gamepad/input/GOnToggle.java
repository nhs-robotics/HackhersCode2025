package codebase.gamepad.input;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptAprilTag;

import java.util.function.Consumer;

public interface GOnToggle<T> {
    T onToggleOn(Runnable runnable);
    T onToggleOff(Runnable runnable);

    T initalToggleState(boolean toggled);

    T onToggle(Consumer<Boolean> stateNew);


}
