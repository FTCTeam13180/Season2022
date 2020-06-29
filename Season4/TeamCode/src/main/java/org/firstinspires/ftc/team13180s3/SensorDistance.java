package org.firstinspires.ftc.team13180s3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class SensorDistance {
    public LinearOpMode opMode;
    private DistanceSensor sensorDistanceRight;
    private DistanceSensor sensorDistanceLeft;

    SensorDistance(LinearOpMode op) {
        opMode = op;
    }

    public void init() {
        sensorDistanceRight = opMode.hardwareMap.get(DistanceSensor.class, "sensor_color_distance");
        sensorDistanceLeft = opMode.hardwareMap.get(DistanceSensor.class, "sensor_color_distance");

    }
    public double getDistanceR(){
         return sensorDistanceRight.getDistance(DistanceUnit.CM);
    }
    public double getDistanceL(){return sensorDistanceLeft.getDistance(DistanceUnit.CM);}

}

