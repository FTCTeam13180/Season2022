package org.firstinspires.ftc.teamcode.component;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LauncherComponent implements Component {

    private OpMode opmode;
    private DcMotorEx launcher;
    private final double LAUNCHER_POWER = 0.85;
    private final double Auto_Launcher_Power = 0.85;
    private final double POWERSHOT_LAUNCHER_POWER = 0.75;
    private boolean finishedLaunching = false;
    private boolean is_running = false;

    private double target_rpm = 0;

    // Telemetry Items
    private Telemetry.Item log_launcher;

    public LauncherComponent (OpMode op) {
        opmode = op;
    }

    public void init(){
        launcher = (DcMotorEx) opmode.hardwareMap.dcMotor.get("Launcher");
        launcher.setDirection(DcMotor.Direction.FORWARD);
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        log_launcher = opmode.telemetry.addData("Launcher:", "Initialized");
//        PIDFCoefficients p = new PIDFCoefficients(100, 20, 0, 0);
//        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, p);
    }

    public void shoot(){
        is_running = true;
        launcher.setPower(LAUNCHER_POWER);
        log_launcher.setValue("SHOOTING");
    }
    public void powershotShoot() {
        is_running = true;
        launcher.setPower(POWERSHOT_LAUNCHER_POWER); //0.75
        log_launcher.setValue("SHOOTING");
    }

    public void autoShoot(){
        is_running = true;
        launcher.setPower(Auto_Launcher_Power);
    }
    public void reverse() {
        is_running = true;
        launcher.setPower(-LAUNCHER_POWER);
        log_launcher.setValue("REVERSING");
    }
    public void stop(){
        is_running = false;
        launcher.setPower(0);
        log_launcher.setValue("STOPPED");
    }

    public double getRPM()
    {
        double rpm = launcher.getVelocity();
        log_launcher.setValue("RPM Set: " + target_rpm + " Get: " + launcher.getVelocity());
        return rpm;
    }

    public void setTargetRPM(double rpm)
    {
        if (rpm == 0)
            is_running = false;
        else
            is_running = true;
        target_rpm = rpm;
        log_launcher.setValue("RPM Set: " + target_rpm + " Get:" + launcher.getVelocity());
        launcher.setVelocity(target_rpm);
    }

    public double getTargetRPM() {
        return target_rpm;
    }

    public PIDFCoefficients getPIDFCoefficients(){
        return launcher.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public boolean isBusy(){
        return is_running;
    }

    public boolean isFinishedLaunching() { return finishedLaunching; }

    public void setFinishedLaunching(boolean hasFinishedLaunching) { finishedLaunching = hasFinishedLaunching; }
}
