package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.LauncherComponent;
import org.firstinspires.ftc.teamcode.component.StackerComponent;

@TeleOp(name = "TestLauncher", group ="teleop")
public class TestLauncher extends LinearOpMode {

    LauncherComponent launcherComp;
    StackerComponent stackerComponent;
    ChassisComponent chassisComponent;

    double pastTime = 0;
    double time = 0;
    boolean run = false;
    int count = 0;
    boolean gamepad2_left_bumper_being_pressed = false;
    boolean gamepad2_a_being_pressed = false;
    double rpm = 1000;

    public void runOpMode() {
        launcherComp = new LauncherComponent(this);
        launcherComp.init();
        stackerComponent = new StackerComponent(this);
        stackerComponent.init();
        chassisComponent = new ChassisComponent(this);
        chassisComponent.init();
        chassisComponent.initIMU();
        SmartWhack smartWhack = new SmartWhack(this, launcherComp, stackerComponent);

        telemetry.setAutoClear(false);
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {

            if (Math.abs(gamepad2.left_stick_y) > 0.1 || Math.abs(gamepad2.left_stick_x) > 0.1
                    || (Math.abs(gamepad2.right_stick_x) > 0.1)) {
                double x = gamepad2.left_stick_x;
                double y = -gamepad2.left_stick_y; // note: joystick y is reversed.
                double turn = -gamepad2.right_stick_x; //for driver specifically arnav who has practiced the other way
                double power = Math.sqrt(x * x + y * y);
                power = (power > 0) ? power : Math.abs(turn);
                chassisComponent.fieldCentricDrive(x, y, power * 0.5, false, turn);
            } else {
                chassisComponent.stop();
            }

            if (gamepad2.left_bumper) {
                if (!gamepad2_left_bumper_being_pressed) {
                    gamepad2_left_bumper_being_pressed = true;
                    rpm += 50;
                    launcherComp.setTargetRPM(rpm);
                }
            }
            else {
                gamepad2_left_bumper_being_pressed = false;
            }

            if (gamepad2.a){
                if (!gamepad2_a_being_pressed) {
                    gamepad2_a_being_pressed = true;
                    rpm -= 50;
                    launcherComp.setTargetRPM(rpm);
                }
            } else {
                gamepad2_a_being_pressed = false;
            }

            if (gamepad2.right_bumper){
                this.telemetry.addData("", "RPM: %.0f ANGLE: %f",  launcherComp.getRPM(), chassisComponent.getAngle());
                smartWhack.whack(1);
            }
            if(gamepad2.dpad_up){
                stackerComponent.stackerUp();
            }
            if (gamepad2.dpad_down){
                stackerComponent.stackerDown();
            }


            if (gamepad2.x && !run) {
//                launcherComp.shoot();
                runtime.reset();
                run = true;
                count = 0;
            }
/*            if (run){
                time = runtime.milliseconds();
                if (time - pastTime >= 20) {
                    count++;
//                    this.telemetry.addLine("RPM:" + launcherComp.getRPM() + " Time: " + time);
                    this.telemetry.addData("Count:", "%d Time: %.0f  RPM: %.0f ANGLE: %.0f", count, time, launcherComp.getRPM(), chassisComponent.getAngle()/Math.PI/2);
                    pastTime = runtime.milliseconds();
                }
             }

 */
            if (gamepad2.y)
                launcherComp.stop();
            telemetry.update();
        }
    }
}
