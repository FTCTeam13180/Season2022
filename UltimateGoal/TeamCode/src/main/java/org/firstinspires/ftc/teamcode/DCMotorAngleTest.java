package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "DCMotorAngleTest", group = "POC")
public class DCMotorAngleTest extends LinearOpMode {
    int count = 0;
    ElapsedTime runtime;

    DcMotor motor;
    double gamepad_y;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "launcher");
        runtime = new ElapsedTime();

        telemetry.setAutoClear(false);
        telemetry.addData("Status", "In init, Elapsed Time = " + runtime.milliseconds());

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            gamepad_y = gamepad1.left_stick_y;

            if (Math.abs(gamepad_y) > 0.1) {
                motor.setPower(gamepad_y);
            }
            else {
                motor.setPower(0);
            }
        }
    }

}
