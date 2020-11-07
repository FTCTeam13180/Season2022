package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


public class AutonomousTasks{

    enum State {
        INIT,
        MOVE_TO_TARGET_ZONE,
        GRAB_WOBBLE,
        MOVE_TO_LAUNCH_POSITION,
        LAUNCH_RINGS,
        PICK_UP_RINGS,
        PARK_AT_LAUNCH_LINE,
        STOP
    }

    AutonomousTasks.State state = AutonomousTasks.State.INIT;
    ElapsedTime time;
    double TimeoutMs;
    OpMode op;
    int numberOfRings;
    Odometry odometry;
    ChassisSerialMotion chassisSerialMotion;
    public static final double DISTANCE_TO_TARGET_ZONE_A = 127.64; // calculated using field specifications (80.25 - (11.5 + 18)) * 2.54
    public static final double DISTANCE_TO_TARGET_ZONE_B = 186.06; // <-- need to be converted to coordinates
    public static final double DISTANCE_TO_TARGET_ZONE_C = 244.48;

    public AutonomousTasks(OpMode opmode){
        op = opmode;
    }

    public void setRingNumber(int numOfRings){
        numberOfRings = numOfRings;
    }
    public void init(){
        time = new ElapsedTime();
        time.reset();
        odometry = new Odometry(op,120,45);
        odometry.init();
        chassisSerialMotion = new ChassisSerialMotion(odometry, op);
    }

    public void grabWobble(){
        //close servo
    }

    public void moveToTargetZone() {
        chassisSerialMotion.moveToTarget(numberOfRings);
    }

    public void moveToLaunchPosition() {
        chassisSerialMotion.moveToLaunch();

    }

    public void launchRings() {

    }

    public void pickUpRings() {
    }

    public void parkAtLaunchLine() {
    }

    public void stop (){

    }


    public void run() {

        switch(state){

            case INIT:
                init();
                state = State.GRAB_WOBBLE;
                break;

            case GRAB_WOBBLE:
                grabWobble();
                state = State.LAUNCH_RINGS;
                //           state = State.MOVE_TO_TARGET_ZONE;    'still finishing odom' - rohan
                break;

            case MOVE_TO_TARGET_ZONE:
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.MOVE_TO_LAUNCH_POSITION;
                    break;
                }
                if(chassisSerialMotion.getState()!=ChassisStateMachine.State.EXECUTE){
                    moveToTargetZone();
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_LAUNCH_POSITION:
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.LAUNCH_RINGS;
                    break;
                }
                if(chassisSerialMotion.getState()!=ChassisStateMachine.State.EXECUTE){
                    moveToLaunchPosition();
                }
                chassisSerialMotion.run();
                break;

            case LAUNCH_RINGS:
                launchRings();
                state = State.PICK_UP_RINGS;
                break;

            case PICK_UP_RINGS:
                pickUpRings();
                state = State.PARK_AT_LAUNCH_LINE;
                break;

            case PARK_AT_LAUNCH_LINE:
                parkAtLaunchLine();
                state = State.STOP;
                break;

            case STOP:
                stop();
                break;


        }
    }
}


