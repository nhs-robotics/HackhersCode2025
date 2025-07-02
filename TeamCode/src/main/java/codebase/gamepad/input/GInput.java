package codebase.gamepad.input;


import codebase.Loop;
import codebase.gamepad.Controller;

public interface GInput extends Loop {

    Controller getController();
}
