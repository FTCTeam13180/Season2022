package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="ParkLeftBeg", group="autonomousGroup1")
public class ParkLeftBeg extends LinearOpMode {
    private Elevator elevator;
    private Grabber grab;
    private RoboNavigator robo;
    @Override
    public void runOpMode(){
        robo=new RoboNavigator(this);
        robo.init();
        waitForStart();
        while (opModeIsActive()){
            robo.shiftLeft(36,5000);
            break;
        }
    }
}
