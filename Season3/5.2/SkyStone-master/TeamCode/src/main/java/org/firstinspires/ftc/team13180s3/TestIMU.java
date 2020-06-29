package org.firstinspires.ftc.team13180s3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="TestIMU", group="manualmode")

public class TestIMU extends LinearOpMode {

    private RoboNavigator robotnav;

    static final double SPEED = 0.8;

    @Override
    public void runOpMode() {

        robotnav = new RoboNavigator(this);
        robotnav.init();
        robotnav.initIMU();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                robotnav.PrintImuRadians();
            }
        }
    }
}