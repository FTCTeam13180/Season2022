package org.firstinspires.ftc.teamcode.component;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LauncherComponent implements Component {

    private OpMode opmode;
    private DcMotor launcher;
    private final double LAUNCHER_POWER = 0.85;
    private final double Auto_Launcher_Power = 0.85;
    private final double POWERSHOT_LAUNCHER_POWER = 0.75;
    private boolean finishedLaunching = false;

    // Telemetry Items
    private Telemetry.Item log_launcher;

    public LauncherComponent (OpMode op) {
        opmode = op;
    }

    public void init(){
        launcher = opmode.hardwareMap.dcMotor.get("Launcher");
        launcher.setDirection(DcMotor.Direction.FORWARD);
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        log_launcher = opmode.telemetry.addData("Launcher:", "Initialized");
    }
    public void safeshoot(){
        launcher.setPower(LAUNCHER_POWER);
        log_launcher.setValue("SHOOTING");
    }
    public void shoot(){
        launcher.setPower(LAUNCHER_POWER);
        log_launcher.setValue("SHOOTING");
    }
    public void powershotSoot() {
        launcher.setPower(POWERSHOT_LAUNCHER_POWER);
    }

    public void autoShoot(){
        launcher.setPower(Auto_Launcher_Power);
    }
    public void reverse() {
        launcher.setPower(-LAUNCHER_POWER);
        log_launcher.setValue("REVERSING");
    }
    public void stop(){
        launcher.setPower(0);
        log_launcher.setValue("STOPPED");
    }
    public boolean isBusy(){
        return launcher.isBusy();
    }

    public boolean isFinishedLaunching() { return finishedLaunching; }

    public void setFinishedLaunching(boolean hasFinishedLaunching) { finishedLaunching = hasFinishedLaunching; }
}
