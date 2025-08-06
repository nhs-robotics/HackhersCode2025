package codebase.geometry;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class FieldPosition extends Point {

    /**
     * The direction of the field position defined as radians counterclockwise from directly to the right of the field
     */
    public double direction;

    public FieldPosition(double x, double y, double direction) {
        super(x, y);
        this.direction = direction;
    }

    public Pose2D toPose2D() {
        return new Pose2D(DistanceUnit.INCH, this.x, this.y, AngleUnit.RADIANS, this.direction);
    }
}