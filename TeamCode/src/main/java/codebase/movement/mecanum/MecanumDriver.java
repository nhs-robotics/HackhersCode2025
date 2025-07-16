package codebase.movement.mecanum;

import codebase.geometry.MovementVector;
import codebase.hardware.Motor;

public class MecanumDriver {
    public final Motor fl;
    public final Motor fr;
    public final Motor bl;
    public final Motor br;
    public final MecanumCoefficientMatrix omniDriveCoefficients;

    public MecanumDriver(
            Motor fl,
            Motor fr,
            Motor bl,
            Motor br,
            MecanumCoefficientMatrix omniDriveCoefficients
    ) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
        this.omniDriveCoefficients = omniDriveCoefficients;
    }

    public void setMotorVelocities(double fl, double fr, double bl, double br) {
        this.fl.setVelocity(fl);
        this.fr.setVelocity(fr);
        this.bl.setVelocity(bl);
        this.br.setVelocity(br);
    }

    public void setRelativeVelocity(MovementVector velocity) {
        MecanumCoefficientSet coefficientSet = this.omniDriveCoefficients.calculateCoefficientsWithPower(
                velocity.getVertical(),
                velocity.getHorizontal(),
                velocity.getRotation()
        );

        this.setMotorVelocities(coefficientSet.fl, coefficientSet.fr, coefficientSet.bl, coefficientSet.br);

    }

//.    public void setAbsoluteVelocity()

}
