package codebase.gamepad.input;


import codebase.Loop;
import codebase.gamepad.Gamepad;

public interface GInput extends Loop {

    Gamepad getGamepad();
}
