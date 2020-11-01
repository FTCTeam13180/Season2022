package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class LauncherComponent {
    public OpMode opmode;
    LauncherComponent (OpMode op) {
        opmode = op;
    }
    private DcMotor launcher;

    public void init(){
        launcher = opmode.hardwareMap.get(DcMotor.class, "Launcher");
        launcher.setDirection(DcMotor.Direction.FORWARD);
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        opmode.telemetry.addData("Launcher:", "Initialized");
    }
    // method to get the launcher motor moving
    public void givePower(double p){
        launcher.setPower(p);
        opmode.telemetry.addData("Launcher", "Running");
    }
    //completely stops the launcher from moving
    public void stop(){
        launcher.setPower(0);
        opmode.telemetry.addData("Launcher", "Stopped");
    }
    public boolean isbusy(){
        return launcher.isBusy();
    }
}
