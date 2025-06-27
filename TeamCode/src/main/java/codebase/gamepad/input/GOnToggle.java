package codebase.gamepad.input;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptAprilTag;

import java.util.function.Consumer;

public interface GOnToggle<T> {
    T onToggle(Runnable runnable);
    T onToggleOff(Runnable runnable);

    T toggleInitalState(boolean toggled);

    T onToggle(Consumer<Boolean> stateNew);


}
