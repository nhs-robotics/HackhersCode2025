package codebase.gamepad.input.types;

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



public class Button implements GInput, GAnalog<Button>, GOnToggle<Button>, GIsToggled,GIsPressed,GOnPress<Button>,GOnRelease<Button>{
    private final Supplier<Boolean> isPressed;
    private final Controller controller;

    private Runnable onPress, onRelease, whileDown,onToggleOn, onToggleOff;

    private Consumer<Boolean> onToggle;

    private boolean wasDownLast;

    private boolean toggleState;

    public Button(Controller controller, Supplier<Boolean> isDown){
        isPressed = isDown;
        this.controller = controller;
    }

    @Override
    public void loop(){
        boolean isPressed = this.isPressed.get();
        if(this.onPress != null && isPressed && !this.wasDownLast){
            onPress.run();
        }
        if(this.onRelease != null && isPressed && !this.wasDownLast){
            onRelease.run();
        }
        if(this.onPress != null && isPressed && !this.wasDownLast){
            onPress.run();
        }

        //Toggle

        if(isPressed && !wasDownLast){
            if(onToggle != null ){
                this.toggleState = !this.toggleState;

            }
        }


    }

}
