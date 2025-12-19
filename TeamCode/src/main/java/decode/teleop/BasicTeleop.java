package decode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import codebase.controllers.PIDController;
import codebase.geometry.Angles;
import codebase.movement.mecanum.MecanumCoefficientMatrix;
import codebase.movement.mecanum.MecanumCoefficientSet;
import codebase.movement.mecanum.MecanumDriver;
import codebase.hardware.Motor;
import com.qualcomm.robotcore.hardware.PIDCoefficients;


@TeleOp(name="BasicTeleOp")
public class BasicTeleop extends OpMode {

    // Drive
    private Motor fl, fr, bl, br;
    // ===== STORAGE PID =====
    private static final double STORAGE_P = 4;
    private static final double STORAGE_I = 0.0;
    private static final double STORAGE_D = 0.0;


    // Accessories
    private Motor intake, sabine, sabine2;
    //sabines are the flywheels
    private DcMotorEx storage;
    private Servo servo1;
    //pusher to push artifact into flywheel
    double countsPerRev = 260.0; //can be changed as per rotation
    double countsPerDegree = countsPerRev / 360.0;

    private int storageStepIndex = 0;


    // ===== STORAGE CONSTANTS =====
    private static final int TICKS_PER_REV = 286; //measured val
    private static final int STEP_DEGREES = 30;
    private static final int TICKS_PER_STEP =
            (int)(TICKS_PER_REV * (STEP_DEGREES / 360.0));

    private static final double STORAGE_POWER = 0.4;


    // Button edge detection
    private boolean prevLB = false;
    private boolean prevRB = false;
    private boolean prevA  = false;
    private boolean prevB  = false;
    private boolean prevX  = false;
    private boolean prevY  = false;

    // Toggles
    private boolean intakeForward = false;
    private boolean intakeReverse = false;
    private boolean sabineForward = false;
    private boolean sabineReverse = false;
    private boolean servosForward = false;

    private float storagePosition = 0;

    private PIDController storagePID;

    // Mecanum
    private MecanumDriver driver;

    @Override
    public void init() {

        // Drive motors
        fl = new Motor(hardwareMap.get(DcMotorEx.class, "fl"));
        fr = new Motor(hardwareMap.get(DcMotorEx.class, "fr"));
        bl = new Motor(hardwareMap.get(DcMotorEx.class, "bl"));
        br = new Motor(hardwareMap.get(DcMotorEx.class, "br"));

        // Accessories
        intake  = new Motor(hardwareMap.get(DcMotorEx.class, "intake"));
        sabine  = new Motor(hardwareMap.get(DcMotorEx.class, "sabine"));
        sabine2 = new Motor(hardwareMap.get(DcMotorEx.class, "sabine2"));

        //storage setup
        storage = hardwareMap.get(DcMotorEx.class, "storage");

        storage.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        storage.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        storage.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        storagePID =
                new PIDController(new PIDCoefficients(STORAGE_P, STORAGE_I, STORAGE_D), () -> Angles.angleDifference(storage.getCurrentPosition(), storagePosition));

        //servo setup
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo1.setPosition(0);

        //mecanum setup
        driver = new MecanumDriver(
                fl, fr, bl, br,
                new MecanumCoefficientMatrix(
                        new MecanumCoefficientSet(1, 1, 1, 1),
                        1
                ),
                1
        );





    }

    @Override
    public void loop() {
        telemetry.addData("Storage Encoder", storage.getCurrentPosition());
        telemetry.addData("Storage Step", storageStepIndex);
        telemetry.addData("Storage Target",
                storageStepIndex * TICKS_PER_STEP);


        // ================= DRIVE =================
        double forward = -gamepad1.left_stick_y;
        double strafe  =  gamepad1.left_stick_x;
        double rotate  =  gamepad1.right_stick_x;

        double flPower = forward + strafe + rotate;
        double frPower = forward - strafe - rotate;
        double blPower = forward - strafe + rotate;
        double brPower = forward + strafe - rotate;

        double max = Math.max(1.0,
                Math.max(Math.abs(flPower),
                        Math.max(Math.abs(frPower),
                                Math.max(Math.abs(blPower), Math.abs(brPower)))));

        double driveVelocity = 4500;

        fl.setVelocity((flPower / max) * driveVelocity);
        fr.setVelocity((frPower / max) * driveVelocity);
        bl.setVelocity((blPower / max) * driveVelocity);
        br.setVelocity((brPower / max) * driveVelocity);

        // ================= INTAKE =================
        double intakeVelocity = 900;

        if (gamepad1.a && !prevA) {
            intakeForward = !intakeForward;
            intakeReverse = false;
        }

        if (gamepad1.y && !prevY) {
            intakeReverse = !intakeReverse;
            intakeForward = false;
        }

        if (intakeForward) intake.setVelocity(intakeVelocity);
        else if (intakeReverse) intake.setVelocity(-intakeVelocity);
        else intake.setVelocity(0);

        // ================= SABINE =================
        double sabineVelocity = 500;

        if (gamepad1.b && !prevB) {
            sabineForward = !sabineForward;
            sabineReverse = false;
        }

        if (gamepad1.x && !prevX) {
            sabineReverse = !sabineReverse;
            sabineForward = false;
        }

        if (sabineForward) {
            sabine.setVelocity(sabineVelocity);
            sabine2.setVelocity(sabineVelocity);
        } else if (sabineReverse) {
            sabine.setVelocity(-sabineVelocity);
            sabine2.setVelocity(-sabineVelocity);
        } else {
            sabine.setVelocity(0);
            sabine2.setVelocity(0);
        }

        // ===== STORAGE CONTROL =====
        if (gamepad1.left_bumper && !prevLB) {

            storageStepIndex++; // advance to next preset

            storagePosition = storageStepIndex * TICKS_PER_STEP;

            storage.setPower(storagePosition);
        }

// Optional holding power after reaching target


        //if (!storage.isBusy() && storage.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
        //    storage.setPower(0);
        //    storage.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //}


        // ======= SERVO =============
        if (gamepad1.right_bumper && !prevRB) {
            servosForward = !servosForward;
        }

        servo1.setPosition(servosForward ? 0.0 : 1.0);

        // ============ BUTTON STATE UPDATES =================
        prevLB = gamepad1.left_bumper;
        prevRB = gamepad1.right_bumper;
        prevA  = gamepad1.a;
        prevB  = gamepad1.b;
        prevX  = gamepad1.x;
        prevY  = gamepad1.y;

    }
}
