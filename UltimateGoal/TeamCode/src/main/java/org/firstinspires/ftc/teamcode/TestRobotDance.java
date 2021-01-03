package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;

@Autonomous(name = "TestRobotDance", group = "UltimateGoalAutonomous")
public class TestRobotDance extends OpMode {

    private OpMode op;
    private Odometry odometry;
    private ChassisComponent chassisComponent;
    private ChassisSerialMotion chassisSerialMotion;

    @Override
    public void init() {
        chassisComponent = new ChassisComponent(op);
        chassisComponent.init();
        chassisComponent.initIMU();
        odometry = new Odometry(op, chassisComponent,98,47);

        chassisSerialMotion = new ChassisSerialMotion(odometry, chassisComponent,op);

    }


    @Override
    public void loop(){

    }

}

