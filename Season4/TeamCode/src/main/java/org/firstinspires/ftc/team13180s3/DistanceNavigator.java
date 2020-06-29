package org.firstinspires.ftc.team13180s3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class DistanceNavigator extends LinearOpMode {
    private RoboNavigator robonav;
    private SensorDistance distance;

    public void runOpMode (){
        robonav = new RoboNavigator(this);
        distance = new SensorDistance(this);
        robonav.init();
        distance.init();
        waitForStart();
        while (opModeIsActive()){

                if(distance.getDistanceL() > distance.getDistanceR()) {
                    while (distance.getDistanceL() > distance.getDistanceR()) {
                        robonav.encoderDrive(RoboNavigator.DIRECTION.TURN_LEFT, 0.6, 10, 10000);
                        sleep(500);
                        double diff = Math.abs(distance.getDistanceR()- distance.getDistanceL());
                        if (diff > 0.2 && diff < 0.5) {
                            break;
                        }
                    }
                }
                else if(distance.getDistanceR()> distance.getDistanceL()){
                    while(distance.getDistanceR()> distance.getDistanceL()){
                        robonav.encoderDrive(RoboNavigator.DIRECTION.TURN_RIGHT,0.6,10,10000);
                        sleep(500);
                        double diff = Math.abs(distance.getDistanceR()- distance.getDistanceL());
                        if (diff > 0.2 && diff < 0.5) {
                            break;
                        }
                }
            }

        }

    }
}


