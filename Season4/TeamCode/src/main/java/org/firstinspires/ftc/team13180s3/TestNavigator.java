package org.firstinspires.ftc.team13180s3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;



@TeleOp(name="TestNavigator", group="manualmode")

public class TestNavigator extends LinearOpMode {

    private RoboNavigator robotnav;

    static final double SPEED = 1.0;

    @Override
    public void runOpMode() {

        robotnav = new RoboNavigator(this);
        robotnav.init();
        robotnav.setNavigatorPower(1.0);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_up) {
                robotnav.moveForward(SPEED);
            }
            else if (gamepad1.dpad_down) {
                robotnav.moveBackward(SPEED);
            }
            else if (gamepad1.dpad_left) {
                robotnav.shiftLeft(SPEED);
            }
            else if (gamepad1.dpad_right) {
                robotnav.shiftRight(SPEED);
            }
            else if (gamepad1.right_bumper) {
                robotnav.turnRight(90,10000);
            }
            else if (gamepad1.left_bumper) {
                robotnav.turnLeft(90,10000);
            }
            else if(Math.abs(gamepad1.left_stick_x)>0.1 || Math.abs(gamepad1.left_stick_y)>0.1){   //can go any direction 360 degrees based on controller input
                if(Math.abs(gamepad1.left_stick_x)> 0.1 || Math.abs(gamepad1.left_stick_y) > 0.1) {
                    // VERY IMPORTANT: Gamepad joystick y-axis positive points downward, x-axis points right
                    // Always remember to reverse joystick stick_y value.

                    // OmniImu still has a bug where left and right are reversed, until that is fixed we use AnyMecanum drive.
                    //roboNav.OmniImu(nav_omni_x, -nav_omni_y, multiplier);
                    robotnav.AnyMecanumSwerve(gamepad1.left_stick_x, -gamepad1.left_stick_y, SPEED);
                }
            }
            // EncoderMode
            else if (gamepad1.y) {
                //robotnav.moveForward(96, 10000);
                // Testing encode with Rampup. Start slow in first two seconds and then full speed.
                //robotnav.moveForwardWithRampup(96,10000, 100);
                robotnav.moveForward(96,10000);
            }
            else if (gamepad1.a) {
                robotnav.moveBackward(96, 10000);
            }
            else if (gamepad1.b) {
                robotnav.shiftRight(96, 10000);
            }
            else if (gamepad1.x) {
                robotnav.shiftLeft(96, 10000);
            }
            else {
                robotnav.stopMotor();
            }

        }
    }
}