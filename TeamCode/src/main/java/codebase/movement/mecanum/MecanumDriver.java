package codebase.movement.mecanum;

import codebase.geometry.MovementVector;
import codebase.hardware.Motor;

public class MecanumDriver {
    public final Motor fl;
    public final Motor fr;
    public final Motor bl;
    public final Motor br;

    public MecanumDriver(
            Motor fl,
            Motor fr,
            Motor bl,
            Motor br
    ) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
    }


    public void setVelocity(MovementVector velocity){
        MecanumCoefficientSet
    }


}
