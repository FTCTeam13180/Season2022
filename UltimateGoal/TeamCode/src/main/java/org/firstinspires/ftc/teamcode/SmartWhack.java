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


    public void whack(int count, double min_rpm, double max_rpm, double timeout)
    {
        double rpm = 0;
        double prev_rpm = 0;

        double target_rpm = (min_rpm + max_rpm) / 2;
        launcher.setTargetRPM(target_rpm);

        ElapsedTime accel_time = new ElapsedTime();
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();

        while (count > 0) {
            accel_time.reset();
            prev_rpm = launcher.getRPM();

            boolean timed_out = runtime.milliseconds() >= timeout;

            if ((prev_rpm < min_rpm || prev_rpm > max_rpm) && !timed_out) {
                continue;
            }

            sleep(10);
            rpm = launcher.getRPM();

            double acceleration = (rpm - prev_rpm) / accel_time.milliseconds();
            double rpm_predicted = rpm + acceleration * 10;

            boolean go_for_whack = (rpm >= min_rpm && rpm <= max_rpm
                                    && rpm_predicted >= min_rpm && rpm_predicted <= max_rpm)
                                    || timed_out;

            if (go_for_whack) {
                if (timed_out)
                    opmode.telemetry.addData("Timedout", "Count: %d  TGT: %.0f, PRED: %.0f, RPM: %.0f", count, target_rpm, rpm_predicted, launcher.getRPM());
                else
                    opmode.telemetry.addData("Safe", "Count: %d  TGT: %.0f, PRED: %.0f, RPM: %.0f", count, target_rpm, rpm_predicted, launcher.getRPM());

                stacker.unsafeWhackerOut();
                sleep(100);
                stacker.unsafeWhackerIn();
                sleep(100);
                count--;
            }
        }

        return;
    }


    /*
    public void whack (int count, double target_rpm, double rpm_tolerance, double timeout) {
        launcher.setTargetRPM(target_rpm);

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
            sleep(50);
            rpm = launcher.getRPM();

            double acceleration = (rpm - prev_rpm)/accel_time.milliseconds();
            double rpm_predicted = rpm + acceleration * 10;

            if (rpm >= MIN_SAFE_RPM && rpm <= MAX_SAFE_RPM && rpm_predicted >= MIN_SAFE_RPM && rpm_predicted <= MAX_SAFE_RPM)
                break;
        }
        return rpm;
    }

     */

}