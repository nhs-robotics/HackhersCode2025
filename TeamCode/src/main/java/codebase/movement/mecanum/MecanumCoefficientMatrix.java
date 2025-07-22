package codebase.movement.mecanum;
import codebase.movement.mecanum.MecanumCoefficientSet;

/**
 * Coefficient matrix for the total coefficients, vertical driving coefficients, horizontal driving coefficients, and the rotational coefficients. Omni driving coefficients are used for corrections that are easier to make on the software side than hardware side:
 * <ul>
 *     <li>if hardware sets motors up backward</li>
 *     <li>to adjust motor power</li>
 *     <li>to correct drift and/or uneven motor power</li>
 *     <li>to allow for omni driving</li>
 * </ul>
 */
public class MecanumCoefficientMatrix {
    /**
     * The default coefficient matrix. Assumes rotational radius of 10 inches; adjust as needed for your robot.
     */
    private static final MecanumCoefficientMatrix DEFAULT = new MecanumCoefficientMatrix(new double[]{1, 1, 1, 1}, 10.0);

    /**
     * The default vertical coefficient set (FL, FR, BL, BR).
     */
    private static final MecanumCoefficientSet vertical = new MecanumCoefficientSet(1, 1, 1, 1);

    /**
     * The default horizontal coefficient set (FL, FR, BL, BR).
     */
    private static final MecanumCoefficientSet horizontal = new MecanumCoefficientSet(1, -1, -1, 1);

    /**
     * The default rotational coefficient set (FL, FR, BL, BR).
     */
    private static final MecanumCoefficientSet rotational = new MecanumCoefficientSet(1, -1, 1, -1);

    /**
     * The total coefficient set that is primarily used to adjust coefficients.
     */
    private final MecanumCoefficientSet totals;

    private final double rotationalRadius;

    /**
     * Creates a new coefficient matrix.
     *
     * @param configuredCoefficients Coefficients applied after the vertical, horizontal, or rotational coefficients are applied.
     * @param rotationalRadius The effective rotational radius in inches: (trackWidth + wheelBase) / 2. For a square robot, this is the wheel separation distance.
     */
    public MecanumCoefficientMatrix(double[] configuredCoefficients, double rotationalRadius) {
        this.totals = new MecanumCoefficientSet(configuredCoefficients);
        this.rotationalRadius = rotationalRadius;
    }

    /**
     * Calculates the motor coefficients that are then passed to the motors for power-based control.
     *
     * @param verticalPower -1 is backward full speed, 1 is forward full speed.
     * @param horizontalPower -1 is left full speed, 1 is right full speed.
     * @param rotationalPower -1 is counterclockwise full speed, 1 is clockwise full speed.
     * @return The coefficients that have been multiplied by the powers.
     */
    public MecanumCoefficientSet calculateCoefficientsWithPower(double verticalPower, double horizontalPower, double rotationalPower) {
        // Notice how this is just matrix multiplication.
        return new MecanumCoefficientSet(
                totals.fl * (verticalPower * vertical.fl + horizontalPower * horizontal.fl + rotationalPower * rotational.fl),
                totals.fr * (verticalPower * vertical.fr + horizontalPower * horizontal.fr + rotationalPower * rotational.fr),
                totals.bl * (verticalPower * vertical.bl + horizontalPower * horizontal.bl + rotationalPower * rotational.bl),
                totals.br * (verticalPower * vertical.br + horizontalPower * horizontal.br + rotationalPower * rotational.br)
        );
    }

    /**
     * Calculates the motor coefficients that are then passed to the motors for velocity-based control.
     *
     * @param verticalVelocity Backward/forward velocity in inches per second.
     * @param horizontalVelocity Left/right velocity in inches per second.
     * @param rotationalVelocity Counterclockwise/clockwise angular velocity in radians per second.
     * @return The coefficients that have been multiplied by the velocities (resulting in wheel velocities).
     */
    public MecanumCoefficientSet calculateCoefficientsWithVelocity(double verticalVelocity, double horizontalVelocity, double rotationalVelocity) {
        // Apply kinematics with rotational scaling.
        return new MecanumCoefficientSet(
                totals.fl * (verticalVelocity * vertical.fl + horizontalVelocity * horizontal.fl + rotationalVelocity * rotational.fl * rotationalRadius),
                totals.fr * (verticalVelocity * vertical.fr + horizontalVelocity * horizontal.fr + rotationalVelocity * rotational.fr * rotationalRadius),
                totals.bl * (verticalVelocity * vertical.bl + horizontalVelocity * horizontal.bl + rotationalVelocity * rotational.bl * rotationalRadius),
                totals.br * (verticalVelocity * vertical.br + horizontalVelocity * horizontal.br + rotationalVelocity * rotational.br * rotationalRadius)
        );
    }
}