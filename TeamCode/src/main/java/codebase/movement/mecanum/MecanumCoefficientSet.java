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

    public MecanumCoefficientSet normalize(double maxValue) {
        double maxAbs = Math.max(Math.max(Math.abs(this.fl), Math.abs(this.fr)),
                Math.max(Math.abs(this.bl), Math.abs(this.br)));
        double scale = (maxAbs > maxValue) ? (maxValue / maxAbs) : 1.0;

        return new MecanumCoefficientSet(
                this.fl * scale,
                this.fr * scale,
                this.bl * scale,
                this.br * scale
        );
    }
}
