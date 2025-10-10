package codebase.controllers;

import com.qualcomm.robotcore.hardware.PIDCoefficients;

import java.util.function.Supplier;

public class PIDController {

    private final PIDCoefficients coefficients;
    private final Supplier<Double> errorSupplier;

    private double integralSum = 0;
    private double lastError = 0;
    private double lastTime = 0;
    private double lastDerivative = 0;

    public PIDController(PIDCoefficients coefficients, Supplier<Double> currentPositionSupplier, Supplier<Double> targetPositionSupplier) {
        this(coefficients, () -> targetPositionSupplier.get() - currentPositionSupplier.get());
    }

    public PIDController(PIDCoefficients coefficients, Supplier<Double> errorSupplier) {
        this.coefficients = coefficients;
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

        double rawDerivative = (error - lastError) / deltaTimeSeconds;

        double alpha = 0.01;
        double derivative = (alpha * rawDerivative) + ((1 - alpha) * lastDerivative);

        integralSum += (error * deltaTimeSeconds);

        double result = (coefficients.p * error) + (coefficients.i * integralSum) + (coefficients.d * derivative);

        lastError = error;
        lastTime = System.currentTimeMillis();
        lastDerivative = derivative;

        return result;
    }
}