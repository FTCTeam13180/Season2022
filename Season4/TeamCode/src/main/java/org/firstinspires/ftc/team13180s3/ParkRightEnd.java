package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="ParkRightEnd", group="autonomousGroup1")
public class ParkRightEnd extends LinearOpMode {
    private Elevator elevator;
    private Grabber grab;
    private RoboNavigator robo;
    @Override
    public void runOpMode(){
        robo=new RoboNavigator(this);
        robo.init();
        waitForStart();
        while (opModeIsActive()){
            sleep(25000);
            robo.shiftRight(36,5000);
            break;
        }
    }
}
