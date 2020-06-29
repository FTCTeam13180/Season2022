package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class FullAuto {
    private LinearOpMode opMode;
    private RoboNavigator robotNavigator;
    private Hook hook;
    private Grabber grab;
    private Elevator elevator;
    private Alliance alliance;
    private Parking parking;
    private Side side;
    private double NAVIGATOR_POWER=0.5;
    public enum Alliance {
        BLUE,
        RED
    }

    public enum Parking {
        BRIDGE,
        WALL
    }

    public enum Side{
        Build,
        Load
    }

    FullAuto (LinearOpMode op, Alliance al, Parking pa) {
        this.opMode = op;
        this.alliance = al;
        this.parking = pa;
    }

    public void run() {
        robotNavigator = new RoboNavigator(opMode);
        robotNavigator.init();
        robotNavigator.setNavigatorPower(1);
        hook = new Hook(opMode);
        hook.init();
        hook.detach();

        grab = new Grabber(opMode);
        grab.init();
        grab.release();

        elevator = new Elevator(opMode);
        elevator.init();


//        hook.detach();


        opMode.telemetry.addLine("Front of Robot Should be forward");
        opMode.telemetry.update();
        if(side == Side.Build){
            robotNavigator.moveForward(24,1000);
            if (alliance == Alliance.BLUE) {
                robotNavigator.turnLeft(90,1000);
            } else {
                robotNavigator.turnRight(90,10000);
            }
            robotNavigator.moveForward(70,1000);
            if (alliance == Alliance.BLUE) {
                robotNavigator.turnRight(90,1000);
            } else {
                robotNavigator.turnLeft(90,1000);
            }
            //robotNavigator.moveBackward(24,1000);


        }

        elevator.playposition();
        grab.rotateToDegrees_0();
        //       grab.release()

        if(side== Side.Load){
            robotNavigator.moveForward(24,4000);
        }
/*  *** SKYSTONE DETECTION- ADD LATER ***
            detect.activate();
            if(detect.scan()){
                robotNavigator.encoderDrive(RoboNavigator.DIRECTION.FORWARD, NAVIGATOR_POWER, 25*2.54, 4000);
                grab.grabIn();
            }
            else{
                robotNavigator.encoderDrive(RoboNavigator.DIRECTION.FORWARD, NAVIGATOR_POWER, 25*2.54, 4000);
                //shutupshutupshutupshutup
            }
*/

        grab.grabIn();
        opMode.sleep(100);
        robotNavigator.moveBackward(3,10000);
        if(alliance==Alliance.BLUE)
            robotNavigator.turnLeft(90,5000);
        else
            robotNavigator.turnRight(90,5000);
        robotNavigator.moveForward(69,10000);
        elevator.goUpByRotations(1,1.6);
        if(alliance==Alliance.BLUE)
            robotNavigator.turnRight(90,5000);
        else
            robotNavigator.turnLeft(90,5000);
        robotNavigator.moveForward(4,5000);
        elevator.goDownByRotations(1,0.8);
        grab.release();
        opMode.sleep(100);

        robotNavigator.moveBackward(4,5000);
        if(alliance==Alliance.BLUE)
            robotNavigator.turnLeft(180,5000);
        else
            robotNavigator.turnRight(180,5000);
        hook.halfattach(0.5);
        robotNavigator.moveBackward(9,5000);
        hook.attach();
        opMode.sleep(250);
        robotNavigator.setNavigatorPower(0.4);
        robotNavigator.moveBackward(1.5,5000);
        robotNavigator.setNavigatorPower(1.0);
       /* robotNavigator.turnLeft(5,1000);
        robotNavigator.turnRight(10,1000);
        robotNavigator.turnLeft(5,1000);*/
        robotNavigator.moveForward(17,5000);
        double turn_degrees = 210;
        if (alliance == Alliance.BLUE) {
            robotNavigator.shiftLeft(12, 10000);
            robotNavigator.turnLeft(turn_degrees, 10000);
            robotNavigator.shiftRight(28,1000);
        } else {
            robotNavigator.shiftRight(12, 10000);
            robotNavigator.turnRight(turn_degrees, 10000);
            robotNavigator.shiftLeft(28,1000);
        }

        // Pushing foundation into depot

        robotNavigator.moveBackward(15, 10000);
        /*
         * Detach from the foundation
         */

        opMode.sleep(200);
        hook.detach();
        opMode.sleep(200);

        if(alliance == Alliance.BLUE){
            if (parking == parking.WALL) {
                robotNavigator.shiftRight(20, 10000);
            }
        }
        else {
            if (parking == parking.WALL) {

                robotNavigator.shiftLeft(20, 10000);
                robotNavigator.shiftRight(3, 10000);
            }
        }
        elevator.goDownByRotations(1,0.5);
        robotNavigator.moveForward(36,5000);
//        elevator.homeposition();
//        opMode.sleep(200);


/*--------------------------------------------
        elevator.goDownByRotations(1,0.8);
        robotNavigator.moveBackward(65+7.75,10000);

        if(alliance==Alliance.BLUE)
            robotNavigator.turnRight(90,5000);
        else
            robotNavigator.turnLeft(90,5000);


        robotNavigator.moveForward(3,5000);
        grab.grabIn();
        opMode.sleep(200);

        robotNavigator.moveBackward(5,5000);
        //
        if(alliance==Alliance.BLUE)
            robotNavigator.turnLeft(90,5000);
        else
            robotNavigator.turnRight(90,5000);
        robotNavigator.moveForward(65+7.75,10000);

        elevator.goUpByRotations(1,1.8);
        if(alliance==Alliance.BLUE)
            robotNavigator.turnRight(90,5000);
        else
            robotNavigator.turnLeft(90,5000);
        robotNavigator.moveForward(12,5000);
        grab.release();
        opMode.sleep(200);

        robotNavigator.moveBackward(12,5000);
        if(alliance==Alliance.BLUE)
            robotNavigator.turnLeft(90,5000);
        else
            robotNavigator.turnRight(90,5000);
        elevator.goDownByRotations(1,1.4);
        robotNavigator.moveBackward(110,10000);
*/
        //-----------------------------

    }

}