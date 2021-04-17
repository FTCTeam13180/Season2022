package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.component.LauncherComponent;
import org.firstinspires.ftc.teamcode.component.StackerComponent;

public class SmartWhack {

    private double RPM_TOLERANCE = 50;
    private double TIMEOUT_PER_RING = 500;

    private OpMode opmode;
    private LauncherComponent launcher;
    private StackerComponent stacker;

    public SmartWhack (OpMode op, LauncherComponent l, StackerComponent s)
    {
        opmode = op;
        launcher = l;
        stacker = s;
    }
    public void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /*
    public void whack(int count)
    {
        double rpm = 0;
        double prev_rpm = 0;
        double MIN_SAFE_RPM = launcher.getTargetRPM() - RPM_TOLERANCE;
        double MAX_SAFE_RPM = launcher.getTargetRPM() + RPM_TOLERANCE;
        double TIMEOUT = count * TIMEOUT_PER_RING;

        ElapsedTime accel_time = new ElapsedTime();
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();

        while (count > 0 && launcher.isBusy() && runtime.milliseconds() < TIMEOUT) {
            prev_rpm = launcher.getRPM();
            accel_time.reset();
            sleep(10);
            rpm = launcher.getRPM();

            double acceleration = (rpm - prev_rpm)/accel_time.milliseconds();
            double rpm_predicted = rpm + acceleration * 10;

            boolean go_for_whack = rpm >= MIN_SAFE_RPM && rpm <= MAX_SAFE_RPM && rpm_predicted >= MIN_SAFE_RPM && rpm_predicted <= MAX_SAFE_RPM;
            opmode.telemetry.addData("", "Time: %.0f  RPM: %.0f PRED_RPM: %.0f PID: %s",
                    runtime.milliseconds(), rpm, rpm_predicted, launcher.getPIDFCoefficients().toString());

            if (go_for_whack) {
                opmode.telemetry.addData("", "Time: %.0f  RPM: %.0f", runtime.milliseconds(), launcher.getRPM());
                stacker.unsafeWhackerOut();
                sleep(100);
                stacker.unsafeWhackerIn();
                sleep(100);
                count--;
            }
        }

        return;
    }
    */

    public void whack (int count, double target_rpm, double rpm_tolerance, double timeout) {
        for (int i=0; i<count; i++)
        {
            double rpm = ensureTargetRPM(target_rpm, rpm_tolerance, timeout);
            opmode.telemetry.addData("", "WHACK: %d TGT_RPM: %.0f, RPM: %.0f", i+1, target_rpm, rpm);
            stacker.unsafeWhackerOut();
            sleep(100);
            stacker.unsafeWhackerIn();
            sleep(100);
        }
    }

    public double ensureTargetRPM(double target_rpm, double rpm_tolerance, double timeout)
    {
        launcher.setTargetRPM(target_rpm);

        double rpm = 0;
        double prev_rpm = 0;
        double MIN_SAFE_RPM = target_rpm - rpm_tolerance;
        double MAX_SAFE_RPM = target_rpm + rpm_tolerance;

        ElapsedTime accel_time = new ElapsedTime();
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();

        while (runtime.milliseconds() < timeout) {
            prev_rpm = launcher.getRPM();
            accel_time.reset();
            sleep(10);
            rpm = launcher.getRPM();

            double acceleration = (rpm - prev_rpm)/accel_time.milliseconds();
            double rpm_predicted = rpm + acceleration * 10;

            if (rpm >= MIN_SAFE_RPM && rpm <= MAX_SAFE_RPM && rpm_predicted >= MIN_SAFE_RPM && rpm_predicted <= MAX_SAFE_RPM)
                break;
        }
        return rpm;
    }

}