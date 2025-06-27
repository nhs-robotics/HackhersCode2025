package codebase.hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Motor {
    private DcMotorEx motor;
    private double ticksPerRotation;
    /**
     * Diameter of the wheel measured in inches.
     */
    private double wheelDiameter;

    public Motor(DcMotorEx motor, double ticksPerRotation, double wheelDiameter) {
        this.motor = motor;
        this.ticksPerRotation = ticksPerRotation;
        this.wheelDiameter = wheelDiameter;
    }

    /**
     * Sets the velocity in relation to the diameter of the wheels and the ticks per second of the motor.
     * @param velocity The desired velocity measured in inches per second.
     */
    public void setVelocity(double velocity) {
        double ticksPerSecond = velocity * (ticksPerRotation / (wheelDiameter * Math.PI));

        motor.setVelocity(ticksPerSecond);
    }

    /**
     * Gets the velocity in relation to the diameter of the wheels and the ticks per second of the motor.
     * @return The desired velocity measured in inches per second.
     */
    public double getVelocity() {
        return motor.getVelocity() / (ticksPerRotation / (wheelDiameter * Math.PI));
    }

    public void setPower(double power) {
        motor.setPower(power);
    }

    public double getPower() {
        return motor.getPower();
    }
}