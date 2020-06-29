package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

@TeleOp(name="TestDistanceSensor", group="manualmode")
public class TestDistanceSensor extends LinearOpMode {
    public SensorDistance distance;
    @Override
    public void runOpMode() throws InterruptedException {
        distance = new SensorDistance(this);
        distance.init();
        double mydistance = 0;
        waitForStart();
        while (opModeIsActive()){
            if(gamepad1.a){
                mydistance = distance.getDistanceR();
                telemetry.addData("Distance: ", String.format(Locale.US, "%.02f", mydistance));
                telemetry.update();
            }
        }
    }
}
