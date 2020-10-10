package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AutoRoute extends LinearOpMode {
    Odometry odometry;
    @Override
    public void runOpMode() throws InterruptedException {
        odometry = new Odometry(this,121.92,100);
        odometry.initDriveHardwareMap();
        waitForStart();
        if(opModeIsActive()){
            odometry.nextPoint(150,120,0.5);
        }
    }
}
