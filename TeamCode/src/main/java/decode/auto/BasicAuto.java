package decode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import codebase.hardware.Motor;

@Autonomous(name="BasicAuto", group="Autonomous")
public class BasicAuto extends LinearOpMode {

    private Motor fl, fr, bl, br;

    @Override
    public void runOpMode() {

        // Map motors (names must match teleop)
        fl = new Motor(hardwareMap.get(com.qualcomm.robotcore.hardware.DcMotorEx.class, "fl"));
        fr = new Motor(hardwareMap.get(com.qualcomm.robotcore.hardware.DcMotorEx.class, "fr"));
        bl = new Motor(hardwareMap.get(com.qualcomm.robotcore.hardware.DcMotorEx.class, "bl"));
        br = new Motor(hardwareMap.get(com.qualcomm.robotcore.hardware.DcMotorEx.class, "br"));

        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        waitForStart();

        // Move forward at 50% power
        double power = 1;
        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);

        // Move forward for 0.5 seconds
        sleep(500);

        // Stop all drive motors
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

        telemetry.addData("Status", "Done moving forward");
        telemetry.update();
    }
}
