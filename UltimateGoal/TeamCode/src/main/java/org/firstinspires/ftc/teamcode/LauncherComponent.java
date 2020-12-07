package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class LauncherComponent {

    private OpMode opmode;
    private DcMotor launcher;
    private final double LAUNCHER_POWER = 1;
    private final double Auto_Launcher_Power = 0.750;
    boolean finishedLaunching = false;

    LauncherComponent (OpMode op) {
        opmode = op;
    }

    public void autoInit(){
        launcher = opmode.hardwareMap.dcMotor.get("Launcher");
        launcher.setDirection(DcMotor.Direction.REVERSE);
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        opmode.telemetry.addData("Launcher:", "Initialized");
    }

    public void teleInit(){
        launcher = opmode.hardwareMap.dcMotor.get("Launcher");
        launcher.setDirection(DcMotor.Direction.REVERSE);
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opmode.telemetry.addData("Launcher:", "Initialized");
    }
    public void shoot(){
        launcher.setPower(LAUNCHER_POWER);
        opmode.telemetry.addData("Launcher", "SHOOTING");
    }
    public void autoShoot(){
        launcher.setPower(Auto_Launcher_Power);
    }
    public void reverse() {
        launcher.setPower(-LAUNCHER_POWER);
        opmode.telemetry.addData("Launcher", "REVERSING");
    }
    public void stop(){
        launcher.setPower(0);
        opmode.telemetry.addData("Launcher", "Stopped");
    }
    public boolean isbusy(){
        return launcher.isBusy();
    }
}
