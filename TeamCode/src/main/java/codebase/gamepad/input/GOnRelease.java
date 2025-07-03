package codebase.gamepad.input;

// Same as Onpress except when the button is released
public interface GOnRelease<T> {
    T onRelease(Runnable runnable);
}
