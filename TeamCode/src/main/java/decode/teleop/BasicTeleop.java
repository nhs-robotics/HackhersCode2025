package decode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import codebase.gamepad.Gamepad;
import codebase.geometry.MovementVector;
import codebase.hardware.Motor;
import codebase.movement.mecanum.MecanumCoefficientMatrix;
import codebase.movement.mecanum.MecanumCoefficientSet;
import codebase.movement.mecanum.MecanumDriver;

@TeleOp(name="Basic Mecanum TeleOp")
public class BasicTeleop extends OpMode {

    private Motor fl, fr, bl, br;
    private Motor intake;

    private boolean intakeForward = false;
    private boolean intakeReverse = false;
    private boolean prevAState = false;
    private boolean prevYState = false;

    private MecanumDriver driver;
    private Gamepad gamepad;

    @Override
    public void init() {
        fl = new Motor(hardwareMap.get(DcMotorEx.class, "fl"));
        fr = new Motor(hardwareMap.get(DcMotorEx.class, "fr"));
        bl = new Motor(hardwareMap.get(DcMotorEx.class, "bl"));
        br = new Motor(hardwareMap.get(DcMotorEx.class, "br"));
        intake = new Motor(hardwareMap.get(DcMotorEx.class, "intake"));

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

        // Drive controls
        double forward = gamepad.leftJoystick.getY();  // fixed: forward stick = forward
        double strafe = gamepad.leftJoystick.getX();    // right stick = right
        double rotate = gamepad.rightJoystick.getX();   // right stick = rotate clockwise

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

        // Intake logic
        if (gamepad1.a && !prevAState) {
            intakeForward = !intakeForward;
            intakeReverse = false; // cancel reverse if active
        }

        if (gamepad1.y && !prevYState) {
            intakeReverse = !intakeReverse;
            intakeForward = false; // cancel forward if active
        }

        if (intakeForward) {
            intake.setPower(1.0);
        } else if (intakeReverse) {
            intake.setPower(-1.0);
        } else {
            intake.setPower(0.0);
        }

        prevAState = gamepad1.a;
        prevYState = gamepad1.y;
    }
}
