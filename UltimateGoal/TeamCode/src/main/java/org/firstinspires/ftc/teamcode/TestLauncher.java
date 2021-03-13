package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.component.LauncherComponent;
import org.firstinspires.ftc.teamcode.component.StackerComponent;

@TeleOp(name = "TestLauncher", group ="teleop")
public class TestLauncher extends LinearOpMode {

    LauncherComponent launcherComp;
    StackerComponent stackerComponent;

    double pastTime = 0;
    double time = 0;
    boolean run = false;
    int count = 0;
    boolean gamepad2_left_bumper_being_pressed = false;
    double rpm = 1000;

    public void runOpMode() {
        launcherComp = new LauncherComponent(this);
        launcherComp.init();
        stackerComponent = new StackerComponent(this);
        stackerComponent.init();

        telemetry.setAutoClear(false);
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {

            if (gamepad2.left_bumper) {
                if (!gamepad2_left_bumper_being_pressed) {
                    gamepad2_left_bumper_being_pressed = true;
                    rpm += 100;
                    launcherComp.setTargetRPM(rpm);
                }
            }
            else {
                gamepad2_left_bumper_being_pressed = false;
            }

            if (gamepad2.right_bumper){
                stackerComponent.safeWhack();
            }
            if(gamepad2.dpad_up){
                stackerComponent.stackerUp();
            }
            if (gamepad2.dpad_down){
                stackerComponent.stackerDown();
            }


            if (gamepad2.x && run == false) {
//                launcherComp.shoot();
                runtime.reset();
                run = true;
                count = 0;
            }
            if (run == true){
                time = runtime.milliseconds();
                if (time - pastTime >= 20) {
                    count++;
//                    this.telemetry.addLine("RPM:" + launcherComp.getRPM() + " Time: " + time);
                    this.telemetry.addData("Count:", "%d Time: %.0f  RPM: %.0f", count, time, launcherComp.getRPM());
                    pastTime = runtime.milliseconds();
                }
             }
            if (gamepad2.y)
                launcherComp.stop();
            telemetry.update();
        }
    }
}
