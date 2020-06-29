package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class BuildingZoneFullAuto {
    private LinearOpMode opMode;
    private RoboNavigator robotNavigator;
    private Hook hook;
    private Grabber grab;
    private Elevator elevator;
    private Alliance alliance;
    private Parking parking;
    private double NAVIGATOR_POWER = 0.5;

    public enum Alliance {
        BLUE,
        RED
    }

    public enum Parking {
        BRIDGE,
        WALL
    }

    BuildingZoneFullAuto(LinearOpMode op, Alliance al, Parking pa) {
        this.opMode = op;
        this.alliance = al;
        this.parking = pa;
    }
    public void run() {

        robotNavigator = new RoboNavigator(opMode);
        robotNavigator.init();
        robotNavigator.setNavigatorPower(0.8);

        hook = new Hook(opMode);
        hook.init();
        hook.detach();

        grab = new Grabber(opMode);
        grab.init();

        elevator = new Elevator(opMode);
        elevator.init();
        elevator.goUpByInches(1,6);
        opMode.sleep(14000);
        // Set hook in attach position
        hook.halfattach(0.4);

        // Go to Foundation and grab it
        robotNavigator.moveForward(29, 10000);
        hook.attach();
        robotNavigator.setNavigatorPower(0.7);
        robotNavigator.moveForward(1.5,10000);
        robotNavigator.setNavigatorPower(1.0);
        opMode.sleep(100);


        /*
         * Move the foundation to the triangle build ZONE
         */
        // Move foundation away from opposing alliance foundation
        //pull foundation towards the bridges, away from north wall
        // then turning the foundation
        robotNavigator.moveBackward(24,5000);
        if(alliance == Alliance.RED){
            robotNavigator.shiftLeft(24,5000);
            robotNavigator.shiftRight(6,5000);

        }
        else {
            robotNavigator.shiftRight(24, 5000);
            robotNavigator.shiftLeft(6, 5000);

        }
        int turnAngle=115;
        //have to test turn angle
        if(alliance == Alliance.RED){
            robotNavigator.turnRight(turnAngle,10000);
        }
        else{
            robotNavigator.turnLeft(turnAngle,10000);
        }
        hook.detach();
        robotNavigator.moveForward(30,10000);
        if (parking==Parking.WALL){
            if(alliance == Alliance.RED){
                robotNavigator.shiftRight(25,10000);
                robotNavigator.shiftLeft(5,10000);
                robotNavigator.moveBackward(5,10000);
            }
            else{
                robotNavigator.shiftLeft(25,10000);
                robotNavigator.shiftRight(5,10000);
                robotNavigator.moveBackward(5,10000);
            }

        }
        elevator.goDownByInches(1,4);
        robotNavigator.moveBackward(40,1000);


    }
        // ROBOT IS PARKED UNDER THE ALLIANCE SKYBRIDGE*/


    public void stage2(){

    }
    public void HomePosition(){
        grab.rotateToDegrees_180();
        grab.grabIn();
        elevator.upDownEncoderDrive(NAVIGATOR_POWER, elevator.height-5,10000);
        elevator.goInByRotations(0.4,4);
        elevator.upDownEncoderDrive(NAVIGATOR_POWER, elevator.height,10000);


    }
}
                                           
