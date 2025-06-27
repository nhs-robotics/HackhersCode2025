package codebase.gamepad.input;
import java.net.InterfaceAddress;
import java.util.function.BiConsumer;

public interface GAnalog<T> {

    T onMove(BiConsumer<Float, Float> onMove);

}