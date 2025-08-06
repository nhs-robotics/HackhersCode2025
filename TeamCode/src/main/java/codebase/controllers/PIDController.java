package codebase.controllers;

import java.util.function.Supplier;

public class PIDController {

    private final double Kp;
    private final double Ki;
    private final double Kd;

    private final Supplier<Double> currentPositionSupplier;

    private final Supplier<Double> targetPositionSupplier;

    private Supplier<Double> errorSupplier;

    private double integralSum = 0;
    private double lastError = 0;
    private double lastTime = 0;

    public PIDController(double Kp, double Ki, double Kd, Supplier<Double> currentPositionSupplier, Supplier<Double> targetPositionSupplier) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;

        this.currentPositionSupplier = currentPositionSupplier;
        this.targetPositionSupplier = targetPositionSupplier;

        this.errorSupplier = () -> this.targetPositionSupplier.get() - this.currentPositionSupplier.get();
    }

    public PIDController(double Kp, double Ki, double Kd, Supplier<Double> currentPositionSupplier, Supplier<Double> targetPositionSupplier, Supplier<Double> errorSupplier) {
        this(Kp, Ki, Kd, currentPositionSupplier, targetPositionSupplier);

        this.errorSupplier = errorSupplier;
    }

    public double getPower() {
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis();
            return 0;
        }

        double deltaTime = System.currentTimeMillis() - lastTime;
        double deltaTimeSeconds = deltaTime / 1000;

        double error = errorSupplier.get();

        double derivative = (error - lastError) / deltaTimeSeconds;

        integralSum += (error * deltaTimeSeconds);

        double result = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

        lastError = error;
        lastTime = System.currentTimeMillis();

        return result;
    }
}