package org.firstinspires.ftc.teamcode;

import android.text.SpannableString;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.LauncherComponent;

@TeleOp(name = "TestLauncher", group ="teleop")
public class TestLauncher extends LinearOpMode {

    LauncherComponent launcherComp;
    double pastTime = 0;
    double time = 0;
    boolean run = false;
    public void runOpMode() {
        launcherComp = new LauncherComponent(this);
        launcherComp.init();
        telemetry.setAutoClear(false);
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {
            if (gamepad2.x && run == false) {
                launcherComp.shoot();
                runtime.reset();
                run = true;
            }
            if (run == true){
                time = runtime.milliseconds();
                if (time - pastTime >= 20) {
                    this.telemetry.addLine("RPM:" + launcherComp.getRPM() + " Time: " + time);
                    pastTime = runtime.milliseconds();
                }
             }
            if (gamepad2.y)
                launcherComp.stop();
            telemetry.update();
        }
    }
}
