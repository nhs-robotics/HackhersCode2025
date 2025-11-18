package decode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import codebase.gamepad.Gamepad;
import codebase.movement.mecanum.MecanumCoefficientMatrix;

import codebase.movement.mecanum.MecanumCoefficientSet;
import codebase.movement.mecanum.MecanumDriver;
 import codebase.hardware.Motor;

@TeleOp(name="Basic Mecanum TeleOp")
public class BasicTeleop extends OpMode {

    private Motor fl, fr, bl, br;
    private Motor intake;
    private Motor sabine;

    private boolean intakeForward = false;
    private boolean intakeReverse = false;
    private boolean sabineForward = false;
    private boolean sabineReverse = false;

    private boolean prevAState = false;
    private boolean prevYState = false;
    private boolean prevBState = false;
    private boolean prevXState = false;

    private MecanumDriver driver;
    private Gamepad gamepad;

    @Override
    public void init() {
        fl = new Motor(hardwareMap.get(DcMotorEx.class, "fl"));
        fr = new Motor(hardwareMap.get(DcMotorEx.class, "fr"));
        bl = new Motor(hardwareMap.get(DcMotorEx.class, "bl"));

        br = new Motor(hardwareMap.get(DcMotorEx.class, "br"));
        intake = new Motor(hardwareMap.get(DcMotorEx.class, "intake")); // port 1?
        sabine = new Motor(hardwareMap.get(DcMotorEx.class, "sabine")); // port 3
        driver = new MecanumDriver(
                fl, fr, bl, br,
                new MecanumCoefficientMatrix(new MecanumCoefficientSet(1, 1, 1, 1), 1),
                1
        );

        gamepad = new Gamepad(gamepad1);
    }

    @Override
    public void loop() {
        gamepad.loop();

        // Mecanum drive (the wheels)
        double forward = gamepad.leftJoystick.getY();
        double strafe = gamepad.leftJoystick.getX();
        double rotate = gamepad.rightJoystick.getX();

        double flPower = forward + strafe + rotate;
        double frPower = forward - strafe - rotate;

        double blPower = forward - strafe + rotate;
        double brPower = forward + strafe - rotate;

        double max = Math.max(1.0, Math.max(Math.abs(flPower),

                Math.max(Math.abs(frPower),
                        Math.max(Math.abs(blPower), Math.abs(brPower)))));

        fl.setPower(flPower / max);

        fr.setPower(frPower / max);

        bl.setPower(blPower / max);

        br.setPower(brPower / max);

        // SABINE (motor for the outtake/spinny wheels)
        if (gamepad1.a && !prevAState) {
            sabineForward = !sabineForward;
            sabineReverse = false;
        }
        if (gamepad1.y && !prevYState) {
            sabineReverse = !sabineReverse;
            sabineForward = false;
        }

        if (sabineForward) sabine.setPower(1.0);
        else if (sabineReverse) sabine.setPower(-1.0);
        else sabine.setPower(0.0);

        // INTAKE (Motor/green spinny things)
        if (gamepad1.b && !prevBState) {
            intakeForward = !intakeForward;
            intakeReverse = false;
        }
        if (gamepad1.x && !prevXState) {
            intakeReverse = !intakeReverse;
            intakeForward = false;
        }

        if (intakeForward) intake.setPower(1.0);
        else if (intakeReverse) intake.setPower(-1.0);
        else intake.setPower(0.0);

        // Update prev statess yay
        prevAState = gamepad1.a;
        prevYState = gamepad1.y;
        prevBState = gamepad1.b;
        prevXState = gamepad1.x;
    }
}
