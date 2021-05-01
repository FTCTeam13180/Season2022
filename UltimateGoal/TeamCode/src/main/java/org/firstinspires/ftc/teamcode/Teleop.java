package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.GrabberComponent;
import org.firstinspires.ftc.teamcode.component.IntakeComponent;
import org.firstinspires.ftc.teamcode.component.LauncherComponent;
import org.firstinspires.ftc.teamcode.component.StackerComponent;
import org.firstinspires.ftc.teamcode.component.WingsComponent;

@TeleOp(name = "Teleop", group = "POC")
public class Teleop extends LinearOpMode {
    private OpMode op;
    private ChassisComponent chassisComponent;
    private IntakeComponent intakeComponent;
    private GrabberComponent grabberComponent;
    private LauncherComponent launcherComponent;
    private WingsComponent wingsComponent;
    private Telemetry.Item log_angle;

    private ElapsedTime stackerTime;
    private StackerComponent stackerComponent;

    double power = 1.0;
    private static double POWERSHOT_RPM = 1650;
    private static double HIGHGOAL_RPM = 1850;
    private static double RPM_TOLERANCE = 20;
    private static double ONE_WHACK_TIMEOUT_MS = 350;

    boolean powershot_mode = false;
    boolean gamepad2_y_being_pressed = false;
    boolean gamepad1_x_being_pressed = false;

    public void runOpMode() {

        this.telemetry.setAutoClear(false);

        grabberComponent = new GrabberComponent(this);
        grabberComponent.init();

        chassisComponent = new ChassisComponent(this);
        chassisComponent.init();
        chassisComponent.initIMU();

        launcherComponent = new LauncherComponent(this);
        launcherComponent.init();

        intakeComponent = new IntakeComponent(this);
        intakeComponent.init();

        wingsComponent = new WingsComponent(this);
        wingsComponent.init();

        stackerTime = new ElapsedTime();
        stackerTime.reset();

        stackerComponent = new StackerComponent(this);
        stackerComponent.init();
        log_angle = this.telemetry.addData("log_angle:", chassisComponent.getAngle() * 180 / Math.PI - 90);

        SmartWhack smartWhack = new SmartWhack(this, launcherComponent, stackerComponent);

        waitForStart();


        /*
        GamePad 1: Navigator
            a - reset the imu at current robot orientation
            left stick - field centric controls
            right stick - turning (only push to left or right)
            dpad up - move completely forward
            dpad down - move completely backward
            right bumper - intake IN
            left bumper - intake OUT

        GamePad 2: Internal motors
            right joystick - launcher:
                up = shoot, down = reverse
            right bumper - toggle whacker
            left bumper - full safeWhacker out and then in
            dpad up - stacker box up
            dpad down - stacker box down
            x - dump

            dpad right - claw open
            dpad left - claw close
            left stick - arm up/down based on stick up and down

         */
        while (opModeIsActive()) {

            //
            // gamepad 1
            //

            if (gamepad1.a) {
                chassisComponent.initIMU();
            }
            if (gamepad1.x) {
                log_angle.setValue(chassisComponent.getAngle() * 180 / Math.PI - 90);
            }

            if (Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1
                    || (Math.abs(gamepad1.right_stick_x) > 0.1)) {
                double x = gamepad1.left_stick_x;
                double y = -gamepad1.left_stick_y; // note: joystick y is reversed.
                double turn = -gamepad1.right_stick_x; //for driver specifically arnav who has practiced the other way
                double power = Math.sqrt(x * x + y * y);
                power = (power > 0) ? power : Math.abs(turn);
                chassisComponent.fieldCentricDrive(x, y, power, false, turn);
            } else if (gamepad1.dpad_up) {
                chassisComponent.moveForward(power * 0.5);
            } else if (gamepad1.dpad_down) {
                chassisComponent.moveBackward(power * 0.5);
            } else {
                chassisComponent.stop();
            }

            if (gamepad1.right_bumper) {
                intakeComponent.expel();
            } else if (gamepad1.left_bumper) {
                intakeComponent.in();
            } else {
                intakeComponent.stop();
            }

            // Wings Controls
            if (gamepad1.x) {
                if (!gamepad1_x_being_pressed) {
                    gamepad1_x_being_pressed = true;
                    wingsComponent.toggleWings();
                }
            } else
                gamepad1_x_being_pressed = false;


            //
            //gamepad 2
            //

            if (gamepad2.a) {
                stackerComponent.stackerDump();
            }
            if (gamepad2.right_bumper) {
                if(stackerTime.milliseconds() > 200) {
                    if (powershot_mode)
                        smartWhack.whack(1, POWERSHOT_RPM, POWERSHOT_RPM + RPM_TOLERANCE, ONE_WHACK_TIMEOUT_MS);
                    else
                        stackerComponent.safeWhack();
                }
            } else if (gamepad2.dpad_up) {
                stackerTime.reset();
                stackerComponent.stackerUp();
                wingsComponent.wingsUp();
            } else if (gamepad2.dpad_down) {
                stackerComponent.stackerDown();
                wingsComponent.wingsDown();
            }

            /*
            if (gamepad2.left_bumper) {
                // Note: safeWhackThree sleeps for a while which introduces race condition between Driver 1 stopping
                // the robot right before driver 2 presses safeWhackThree. Robot continues to move during safeWhackThree
                // if driver 1 stop does not get registered in time.
                // Fix: Force robot to stop moving before we go in this long (sleep) thread.
                // Do this only if launcher is also running to ensure the intent is to shoot rather than unjam.
                if (gamepad2.right_stick_y < 0)
                    chassisComponent.stop();

                if (powershot_mode)
                    smartWhack.whack(1, POWERSHOT_RPM, POWERSHOT_RPM + RPM_TOLERANCE, ONE_WHACK_TIMEOUT_MS);
                else {
                    smartWhack.whack(3, HIGHGOAL_RPM, HIGHGOAL_RPM + RPM_TOLERANCE, 1500);
                    stackerComponent.stackerDown();
                }
            }

             */


            if (gamepad2.y) {
                if (!gamepad2_y_being_pressed) {
                    gamepad2_y_being_pressed = true;
                    powershot_mode = !powershot_mode;
                }
            } else
                gamepad2_y_being_pressed = false;

            if (gamepad2.right_stick_y < 0) {
                if (powershot_mode)
                    launcherComponent.setTargetRPM(POWERSHOT_RPM);
                else {
                    launcherComponent.setTargetRPM(HIGHGOAL_RPM);
                }
            } else if (gamepad2.right_stick_y > 0) {
                launcherComponent.reverse();
            } else {
                launcherComponent.stop();
            }

            //wobble arm controls
            if (gamepad2.dpad_left) {
                grabberComponent.clawOpen();
            } else if (gamepad2.dpad_right) {
                grabberComponent.clawClose();
            }
            if (Math.abs(gamepad2.left_stick_y) > 0.1) {
                if (gamepad2.left_stick_y < 0) {
                    grabberComponent.clawClose();
                    grabberComponent.armStraight();
                }
                if (gamepad2.left_stick_y > 0) {
                    grabberComponent.armDown();
                }
            }


            this.telemetry.update();
        }


    }

}
