package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.IntakeComponent;

@Autonomous(name = "TestRobotDance", group = "UltimateGoalAutonomous")
public class TestRobotDance extends OpMode {

    private OpMode op;
    private Odometry odometry;
    private ChassisComponent chassisComponent;
    private IntakeComponent intakeComponent;
    private ChassisSerialMotion chassisSerialMotion;

    @Override
    public void init() {
        chassisComponent = new ChassisComponent(op);
        chassisComponent.init();
        chassisComponent.initIMU();
        intakeComponent = new IntakeComponent(op);
        odometry = new Odometry(op, chassisComponent, intakeComponent, 98,47);
        odometry.init();

        chassisSerialMotion = new ChassisSerialMotion(odometry, chassisComponent,op);

    }


    @Override
    public void loop(){

    }

}

