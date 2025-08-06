package codebase.controllers;

import java.util.function.Supplier;

public class PIDController {

    private final double Kp;
    private final double Ki;
    private final double Kd;

    private final Supplier<Double> currentPositionSupplier;

    private double targetPosition;

    private double integralSum = 0;
    private double lastError = 0;
    private double lastTime = 0;


    public PIDController(double Kp, double Ki, double Kd, Supplier<Double> currentPositionSupplier, double targetPosition) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;

        this.currentPositionSupplier = currentPositionSupplier;
        this.targetPosition = targetPosition;
    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    public double getPower() {
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis();
            return 0;
        }

        double deltaTime = System.currentTimeMillis() - lastTime;
        double deltaTimeSeconds = deltaTime / 1000;

        double currentPosition = currentPositionSupplier.get();

        double error = targetPosition - currentPosition;

        double derivative = (error - lastError) / deltaTimeSeconds;

        integralSum += (error * deltaTimeSeconds);

        double result = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

        lastError = error;
        lastTime = System.currentTimeMillis();

        return result;
    }
}
