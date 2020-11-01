package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


public class AutonomousTasks{

    enum State {
        INIT,
        MOVE_TO_TARGET_ZONE,
        GRAB_WOBBLE,
        MOVE_TO_POWER_SHOT_LAUNCH_POSITION,
        LAUNCH_RINGS_AT_POWER_SHOTS,
        PICK_UP_RINGS,
        MOVE_SECOND_WOBBLE_TO_TARGET_ZONE,
        MOVE_TO_HIGH_GOAL_LAUNCH_POSITION,
        LAUNCH_RINGS_AT_HIGH_GOAL,
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

    public AutonomousTasks(OpMode opmode){
        op = opmode;
    }

    public void setRingNumber(int numOfRings){
        numberOfRings = numOfRings;
    }
    public void init(){
        op.telemetry.addData("Status", "Initializing");
        time = new ElapsedTime();
        time.reset();
        odometry = new Odometry(op,120,45);
        odometry.init();
        chassisSerialMotion = new ChassisSerialMotion(odometry, op);
    }

    public void grabWobble(){
        //move to wobble and pick it up
    }

    public void moveToTargetZone() {
        chassisSerialMotion.moveToTarget(numberOfRings);
    }

    public void moveToPowerShotLaunchPosition() {
        chassisSerialMotion.moveToLaunch();

    }

    public void launchRingsAtPowerShots() {

    }

    public void pickUpRings() {
    }

    public void moveToHighGoalLaunchPosition() {
        //move to the position to launch rings into the high goal
    }

    public void launchRingsAtHighGoal(){

    }

    public void parkAtLaunchLine() {
    }

    public void stop (){

    }

    /*
    (The robot starts already holding a wobble and three rings, and the number of rings will be detected before start is pressed)
    1) Move to the position for shooting at the power shots
    2) Launch all three rings at the power shots
    3) Move to the correct target zone (based on number of rings) and drop the wobble there
    4) If there is 0 or 1 ring, pick up the other wobble and drop it in the correct target zone
       If there are 4 rings, pick up three and shoot them into the high goal
    5) Park on the white launch line
     */

    public void run() {

        switch(state){

            case INIT:
                init();
                state = State.MOVE_TO_POWER_SHOT_LAUNCH_POSITION;
                break;

            case MOVE_TO_POWER_SHOT_LAUNCH_POSITION:
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.LAUNCH_RINGS_AT_POWER_SHOTS;
                    chassisSerialMotion.run(); //so that the STOP state in ChassisStateMachine can run
                    break;
                }
                if(chassisSerialMotion.getState()!=ChassisStateMachine.State.EXECUTE){
                    moveToPowerShotLaunchPosition(); //this cannot be called multiple times
                }
                chassisSerialMotion.run();
                break;

            case LAUNCH_RINGS_AT_POWER_SHOTS:
                launchRingsAtPowerShots();
                state = State.MOVE_TO_TARGET_ZONE;
                break;

            case MOVE_TO_TARGET_ZONE:
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    if(numberOfRings == 0 || numberOfRings == 1){
                        state = State.GRAB_WOBBLE;
                    }
                    else if(numberOfRings == 4){
                        state = State.PICK_UP_RINGS;
                    }
                    break;
                }
                if(chassisSerialMotion.getState() != ChassisStateMachine.State.EXECUTE){
                    moveToTargetZone();
                }
                chassisSerialMotion.run();
                break;

            case GRAB_WOBBLE:
                grabWobble();
                state = State.MOVE_SECOND_WOBBLE_TO_TARGET_ZONE;
                break;

            case MOVE_SECOND_WOBBLE_TO_TARGET_ZONE:
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.PARK_AT_LAUNCH_LINE;
                    break;
                }
                if(chassisSerialMotion.getState() != ChassisStateMachine.State.EXECUTE){
                    moveToTargetZone();
                }
                chassisSerialMotion.run();
                break;

            case PICK_UP_RINGS:
                pickUpRings();
                state = State.MOVE_TO_HIGH_GOAL_LAUNCH_POSITION;
                break;

            case MOVE_TO_HIGH_GOAL_LAUNCH_POSITION:
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.LAUNCH_RINGS_AT_HIGH_GOAL;
                    break;
                }
                if(chassisSerialMotion.getState()!=ChassisStateMachine.State.EXECUTE){
                    moveToHighGoalLaunchPosition();
                }
                chassisSerialMotion.run();
                break;

            case LAUNCH_RINGS_AT_HIGH_GOAL:
                launchRingsAtHighGoal();
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

