package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

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

    private AutonomousTasks.State state = AutonomousTasks.State.INIT;
    private OpMode op;

    private Odometry odometry;
    private ChassisComponent chassisComponent;
    private ChassisSerialMotion chassisSerialMotion;

    private LauncherComponent launcherComponent;
    private LauncherSerialTask launcherSerialTask;

    private StackerComponent stackerComponent;
    private StackerSerialTask stackerSerialTask;

    private GrabberComponent grabberComponent;
    private IntakeComponent intakeComponent;
    private Detector detect;

    ElapsedTime time;
    double TimeoutMs;

    int numOfRings = 0;
    int whackedRingCount;
    boolean psFinished[]= new boolean[3];

    public AutonomousTasks(OpMode opmode){
        op = opmode;
    }

    //ToDo: add init for other components
    public void init(){
        op.telemetry.addData("AutonomousTask", "Initializing");

        odometry = new Odometry(op, 120,45);
        odometry.init();
        chassisComponent = new ChassisComponent(op);
        chassisComponent.init();
        chassisSerialMotion = new ChassisSerialMotion(odometry, op);

        launcherComponent = new LauncherComponent(op);
        launcherComponent.init();
        launcherSerialTask = new LauncherSerialTask(launcherComponent, op);

        stackerComponent = new StackerComponent(op);
        stackerComponent.init();
        stackerSerialTask = new StackerSerialTask(stackerComponent, op);

        grabberComponent = new GrabberComponent(op);
        grabberComponent.init();
        intakeComponent = new IntakeComponent(op);
        intakeComponent.init();

        detect = new Detector(op);
        detect.init();
        time = new ElapsedTime();
        time.reset();
        op.telemetry.addData("AutonomousTasks", "Initialized");
    }

    /**
     *  Before Auto Loop
     */


    public void detectNumberOfRings() {
        List<Recognition> updatedRecognitions = detect.scan();
        int i = 0;
        if(updatedRecognitions != null) {
            for (Recognition recognition : updatedRecognitions) {
                op.telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                op.telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                        recognition.getLeft(), recognition.getTop());
                op.telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                        recognition.getRight(), recognition.getBottom());
                if(recognition.getLabel() == Detector.LABEL_SECOND_ELEMENT){
                    numOfRings = 1;
                }
                else if(recognition.getLabel() == Detector.LABEL_FIRST_ELEMENT){
                    numOfRings = 4;
                }
                else {
                    numOfRings = 0;
                }
            }
        }
        else {
            numOfRings = 0;
        }
        op.telemetry.addData("Number of rings = ", numOfRings);
    }


    /**
     * Auto Loop
     **/

    public void grabWobble(){
        //move to wobble and pick it up
    }

    public void moveToTargetZone() {
        chassisSerialMotion.moveToTarget(numOfRings);
    }

    public void moveToPowerShotLaunchPosition() {
        chassisSerialMotion.moveToPowerShot();

    }

    public void launchRingAtPowerShots() {
        stackerSerialTask.setWhacks(3);
        while(stackerSerialTask.getState() != StackerStateMachine.State.STOP){
            stackerSerialTask.run();
            launcherSerialTask.setRunningTime(8000);

            while(launcherSerialTask.getState() != LauncherStateMachine.State.STOP){
                launcherSerialTask.run();

            }

           /*
           Every time a ring is launched at a power shot, the robot must slightly turn towards the next power shot
           (possibly parallel - can retract whacker while turning to next power shot)
           if(whackerSerialTask.getTarget() == WhackerStateMachine.Target.RETRACT){ <-- if getTarget is Retract, switch to Extend
               whackerSerialTask.setTarget(WhackerStateMachine.Target.EXTEND;
               whackedRingCount++;
           }
           else if(whackerSerialTask.getTarget() == WhackerStateMachine.Target.EXTEND){    <-- if getTarget is Extend, switch to Retract
               whackerSerialTask.setTarget(WhackerStateMachine.Target.RETRACT;


*/
        }

//        launcherSerialTask.setRunningTime(0);

    }
/*
    public void pickUpRings() {

    }

    public void moveToHighGoalLaunchPosition() {
        chassisSerialMotion.moveToGoal();
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

    public void run(){

        switch(state){

            case INIT:

                state = State.MOVE_TO_POWER_SHOT_LAUNCH_POSITION;
//                state = State.LAUNCH_RINGS_AT_POWER_SHOTS;
                break;

            case MOVE_TO_POWER_SHOT_LAUNCH_POSITION:

                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.MOVE_TO_TARGET_ZONE;
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);
                    break;
                }

                //  if(chassisSerialMotion.getState()==ChassisStateMachine.State.INIT){
                //    moveToPowerShotLaunchPosition(); ToDo: set points at initialization; points cannot be set multiple times
                //   }
                chassisSerialMotion.run();
                break;

            case LAUNCH_RINGS_AT_POWER_SHOTS:
                if(launcherSerialTask.getState() == LauncherStateMachine.State.POWERING_UP){
                    launchRingsAtPowerShots();
                }
                for(int ps=0;ps<3;ps++){
                    if(psFinished[ps]) continue;

                }





                if(launcherSerialTask.getState() == LauncherStateMachine.State.STOP){
                    state = State.MOVE_TO_TARGET_ZONE;
                    state = State.GRAB_WOBBLE;
                    break;
                }

                launcherSerialTask.run();
                break;

            case MOVE_TO_TARGET_ZONE:
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state=State.STOP;
                    /*
                    if(numOfRings == 0 || numOfRings == 1){
                        state = State.GRAB_WOBBLE;
                    }
                    else if(numOfRings == 4){
                        state = State.PICK_UP_RINGS;
                    }
      */
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.INIT){
                    moveToTargetZone();
                }
                chassisSerialMotion.run();
                break;

            case GRAB_WOBBLE:
                grabWobble();
                state = State.MOVE_SECOND_WOBBLE_TO_TARGET_ZONE;
                //           state = State.PICK_UP_RINGS;
                break;

            case MOVE_SECOND_WOBBLE_TO_TARGET_ZONE:

                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.PARK_AT_LAUNCH_LINE;
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.INIT){
                    moveToTargetZone();
                }
                chassisSerialMotion.run();
                break;

            case PICK_UP_RINGS:

//                pickUpRings();
                state = State.MOVE_TO_HIGH_GOAL_LAUNCH_POSITION;
                //state=State.LAUNCH_RINGS_AT_HIGH_GOAL;
                break;

            case MOVE_TO_HIGH_GOAL_LAUNCH_POSITION:
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);
                    state = State.PARK_AT_LAUNCH_LINE;
                    break;
                }
                if(chassisSerialMotion.getState()==ChassisStateMachine.State.INIT){
                    //                  moveToHighGoalLaunchPosition();
                }
                chassisSerialMotion.run();
                break;

            case LAUNCH_RINGS_AT_HIGH_GOAL:
                //               launchRingsAtHighGoal();
                state = State.PARK_AT_LAUNCH_LINE;
                break;

            case PARK_AT_LAUNCH_LINE:
                //             parkAtLaunchLine();
                state = State.STOP;
                break;

            case STOP:
                //           stop();
                break;


        }
    }
}