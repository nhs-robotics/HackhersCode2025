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

@TeleOp(name="Coefficient Configuration")
public class BasicTeleop extends OpMode {

    private Motor fl, fr, bl, br;
    private Motor intake;
    private boolean spinning = false;
    private boolean prevAState = false;

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
        driver.setRelativePower(new MovementVector(
                gamepad.leftJoystick.getY(),
                gamepad.leftJoystick.getX(),
                gamepad.rightJoystick.getX()
        ));

        gamepad.loop();

        // Toggle intake on button A press
        if (gamepad1.a && !prevAState) {
            spinning = !spinning;
            intake.setPower(spinning ? -1.0 : 0.0); // negative = reverse spin
        }

        prevAState = gamepad1.a;
    }
}
