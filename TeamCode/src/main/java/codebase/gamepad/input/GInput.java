package codebase.gamepad.input;


import codebase.Loop;
import codebase.gamepad.Controller;

public interface GInput extends Loop {
    // Has all Loop methods and returns a controller for the method ok
    Controller ok();
}
