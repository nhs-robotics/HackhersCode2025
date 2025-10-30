package decode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import codebase.Constants;
import codebase.gamepad.Gamepad;
import codebase.geometry.MovementVector;
import codebase.hardware.Motor;
import codebase.movement.mecanum.MecanumCoefficientMatrix;
import codebase.movement.mecanum.MecanumCoefficientSet;
import codebase.movement.mecanum.MecanumDriver;

@TeleOp(name="Coefficient Configuration")
public class CoefficientConfigurationTeleop extends OpMode {
    private Motor fl;
    private Motor fr;
    private Motor bl;
    private Motor br;

    private MecanumDriver driver;
    private Gamepad gamepad;

    private MecanumCoefficientSet currentCoefficients;

    private Telemetry.Item coefficientsDisplay;

    @Override
    public void init() {
        this.fl = new Motor(hardwareMap.get(DcMotorEx.class, "fl"), 1200, 2.5);
        this.fr = new Motor(hardwareMap.get(DcMotorEx.class, "fr"), 1200, 2.5);
        this.bl = new Motor(hardwareMap.get(DcMotorEx.class, "bl"), 1200, 2.5);
        this.br = new Motor(hardwareMap.get(DcMotorEx.class, "br"), 1200, 2.5);

        currentCoefficients = new MecanumCoefficientSet(1, 1, 1, 1);

        coefficientsDisplay = telemetry.addData("fl, fr, bl, fr", currentCoefficients.toString());

        driver = new MecanumDriver(fl, fr, bl, br, new MecanumCoefficientMatrix(currentCoefficients, 1), 1);

        gamepad = new Gamepad(gamepad1);

        gamepad.aButton.onPress(() -> {
            invertCoefficients(true, false, false, false);
            updateCoefficientsDisplay();
        }).getGamepad().bButton.onPress(() -> {
            invertCoefficients(false, true, false, false);
            updateCoefficientsDisplay();
        }).getGamepad().xButton.onPress(() -> {
            invertCoefficients(false, false, true, false);
            updateCoefficientsDisplay();
        }).getGamepad().yButton.onPress(() -> {
            invertCoefficients(false, false, false, true);
            updateCoefficientsDisplay();
        });
    }

    private void updateCoefficientsDisplay() {
        coefficientsDisplay.setValue(currentCoefficients.toString());
        telemetry.update();
    }

    private void invertCoefficients(boolean fl, boolean fr, boolean bl, boolean br) {
        MecanumCoefficientSet newCoefficients = new MecanumCoefficientSet(
                currentCoefficients.fl * (fl ? -1 : 1),
                currentCoefficients.fr * (fr ? -1 : 1),
                currentCoefficients.bl * (bl ? -1 : 1),
                currentCoefficients.br * (br ? -1 : 1)
        );

        driver = new MecanumDriver(
                this.fl, this.fr, this.bl, this.br,
                new MecanumCoefficientMatrix(
                        newCoefficients,
                        1
                ),
                1);

        currentCoefficients = newCoefficients;
    }

    @Override
    public void loop() {
        driver.setRelativePower(new MovementVector(gamepad.leftJoystick.getY(), gamepad.leftJoystick.getX(), gamepad.rightJoystick.getX()));

        gamepad.loop();
    }
}