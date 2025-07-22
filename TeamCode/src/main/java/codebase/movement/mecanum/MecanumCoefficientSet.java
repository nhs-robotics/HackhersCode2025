package codebase.movement.mecanum;

public class MecanumCoefficientSet {
    public double fl;
    public double fr;
    public double bl;
    public double br;

    public MecanumCoefficientSet(
            double fl,
            double fr,
            double bl,
            double br
    ) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
    }

    public MecanumCoefficientSet(double[] coefficients) {
        this(coefficients[0], coefficients[1], coefficients[2], coefficients[3]);
    }
}
