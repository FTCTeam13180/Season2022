package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.GrabberComponent;
import org.firstinspires.ftc.teamcode.component.IntakeComponent;
import org.firstinspires.ftc.teamcode.component.LauncherComponent;
import org.firstinspires.ftc.teamcode.component.StackerComponent;

import java.util.List;

public class NorcalAutonomousTasks {

    enum State {
        INIT,
        MOVE_TO_POWER_SHOT_LAUNCH_POSITION,
        LAUNCH_RINGS_AT_POWER_SHOTS,
        MOVE_TO_TARGET_ZONE,
        GRAB_WOBBLE,
        PICK_UP_RINGS,
        MOVE_TO_SECOND_WOBBLE,
        MOVE_SECOND_WOBBLE_TO_TARGET_ZONE,
        MOVE_TO_HIGH_GOAL_LAUNCH_POSITION,
        MOVE_TO_HIGH_GOAL_LAUNCH_POSITION_2,
        MOVE_THROUGH_RINGS_TO_HIGH_GOAL,
        LAUNCH_RINGS_AT_HIGH_GOAL,
        PARK_AT_LAUNCH_LINE,
        TURN,
        STOP
    }

    private NorcalAutonomousTasks.State state = NorcalAutonomousTasks.State.INIT;
    private OpMode op;

    private Odometry odometry;
    private ChassisComponent chassisComponent;
    private NorcalChassisSerialMotion chassisSerialMotion;

    private LauncherComponent launcherComponent;
    private LauncherSerialTask launcherSerialTask;

    private StackerComponent stackerComponent;
    private StackerSerialTask stackerSerialTask;
    private SmartWhack smartWhack;

    private GrabberComponent grabberComponent;
    private IntakeComponent intakeComponent;
    private Detector detect;

    ElapsedTime time;
    double TimeoutMs;

    int numOfRings = 0;
    int whackedRingCount;
    boolean psFinished[][]= new boolean[3][2];


    // Telemetry Items
    Telemetry.Item log_num_OfRings;
    Telemetry.Item log_autonomouTasks_state;

    public NorcalAutonomousTasks(OpMode opmode){
        op = opmode;
        log_num_OfRings = op.telemetry.addData("Rings", numOfRings);
        log_autonomouTasks_state = op.telemetry.addData("AutonomousState", state);
    }

    //ToDo: add init for other components
    public void init(){
//        op.telemetry.addData("AutonomousTask", "Initializing");

        chassisComponent = new ChassisComponent(op);
        chassisComponent.init();
        chassisComponent.initIMU();
        odometry = new Odometry(op, chassisComponent,97,45);
        odometry.init();

        chassisSerialMotion = new NorcalChassisSerialMotion(odometry, chassisComponent,op);

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

        smartWhack = new SmartWhack(op, launcherComponent, stackerComponent);

        detect = new Detector(op);
        detect.init();
        time = new ElapsedTime();
        time.reset();
//        op.telemetry.addData("AutonomousTasks", "Initialized");
    }

    /**
     *  Before Auto Loop
     */


    public void detectNumberOfRings() {
        List<Recognition> updatedRecognitions = detect.scan();
        int i = 0;
        if(updatedRecognitions != null) {
            for (Recognition recognition : updatedRecognitions) {
//                op.telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
//                op.telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
//                        recognition.getLeft(), recognition.getTop());
//                op.telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
//                        recognition.getRight(), recognition.getBottom());
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
//        op.telemetry.addData("Number of rings = ", numOfRings);
        log_num_OfRings.setValue(numOfRings);
    }


    /**
     * Auto Loop
     **/


    public void grabWobble(){
        //ToDo: odom code to reach second wobble goal
        //TODO: Change robot orientation as needed
       // stackerComponent.sleep(500);
        grabberComponent.safeWobbleGrabAndUp();
    }

    public void moveToTargetZone(boolean first) {
        chassisSerialMotion.moveToTarget(numOfRings,first);
    }
    public void shiftPowershot(int ps) {
        chassisSerialMotion.shiftPowershot(ps);
    }

    public void moveToPowerShotLaunchPosition() {
        chassisSerialMotion.moveToPowerShot();
//TODO: maybe start the launcher here so that it's max when position is reached
    }

    public void launchRingAtPowerShots() {
        stackerSerialTask.setWhacks(3);
        while(stackerSerialTask.getState() != StackerStateMachine.State.STOP){
            stackerSerialTask.run();
            launcherSerialTask.setRunningTime(8000);

            while(launcherSerialTask.getState() != LauncherStateMachine.State.STOP){
                launcherSerialTask.run();

            }
        }
    }

    public void moveToLaunchLine() {
        chassisSerialMotion.moveToLaunchLine();
    }
    public void moveToSecondWobble() {
        chassisSerialMotion.moveToSecondWobble();
    }
    public void moveToPickUpRings(){chassisSerialMotion.moveToPickUpRings();}
    public void pickUpRings() {
        //TODO: robot turn to orientation to face intake to pile of rings
        chassisSerialMotion.moveToRings();
        intakeComponent.in();

    }

    public void moveToHighGoalLaunchPosition(int instance) {
        chassisSerialMotion.moveToGoal(instance);
    }

    public void launchRingsAtHighGoal(){
        stackerComponent.sleep(100);
        smartWhack.whack(3);

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

    boolean first_time_called = true;

    public void run(){

        if (first_time_called)
        {
            first_time_called = false;
            time.reset();
        }

        log_autonomouTasks_state.setValue(state);
        if (numOfRings == 4 && time.milliseconds() > 29500){
            grabberComponent.unsafeClawOpen();
            state = State.TURN;
        }
        switch(state){

            case INIT:
                grabberComponent.armStraight();
//                launcherComponent.autoShoot();
                launcherComponent.setTargetRPM(2000);
                stackerComponent.stackerUp();
                state = State.MOVE_TO_HIGH_GOAL_LAUNCH_POSITION;
                break;

            case MOVE_TO_HIGH_GOAL_LAUNCH_POSITION:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    launchRingsAtHighGoal();
                    state = State.MOVE_TO_TARGET_ZONE;
                    break;
                }
                if(chassisSerialMotion.getState()==NorcalChassisStateMachine.State.INIT){
                    moveToHighGoalLaunchPosition(1);
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_TARGET_ZONE:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    grabberComponent.safeWobbleDownAndRelease();
                        state = State.MOVE_TO_SECOND_WOBBLE;

                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.INIT){
                    launcherComponent.stop();
                    stackerComponent.stackerDown();
                    moveToTargetZone(true);
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_SECOND_WOBBLE:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    if (numOfRings == 0 ){
                        state = State.MOVE_SECOND_WOBBLE_TO_TARGET_ZONE;
                    }
                    else {
                        state = State.PICK_UP_RINGS;
                    }
                    grabWobble();
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.INIT){
                    moveToSecondWobble();
                }
                chassisSerialMotion.run();
                break;

            case PICK_UP_RINGS:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    state = State.MOVE_TO_HIGH_GOAL_LAUNCH_POSITION_2;
                    intakeComponent.expel();
                    stackerComponent.stackerUp();

                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.INIT){
                    stackerComponent.stackerDown();
                    intakeComponent.in();
                    moveToPickUpRings();
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_HIGH_GOAL_LAUNCH_POSITION_2:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    state = State.MOVE_SECOND_WOBBLE_TO_TARGET_ZONE;
                    launchRingsAtHighGoal();
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.INIT){
                    moveToHighGoalLaunchPosition(2);
                    launcherComponent.shoot();
                }
                chassisSerialMotion.run();
                break;

            case MOVE_SECOND_WOBBLE_TO_TARGET_ZONE:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    grabberComponent.safeWobbleDownAndRelease();
                    grabberComponent.armStraight();
                    state = State.PARK_AT_LAUNCH_LINE;
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.INIT){
                    moveToTargetZone(false);
                }

                chassisSerialMotion.run();
                break;

            case PARK_AT_LAUNCH_LINE:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    state = State.TURN;
                    launcherComponent.stop();
                    stackerComponent.stackerDown();
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState()==NorcalChassisStateMachine.State.INIT){
                    moveToLaunchLine();
                    intakeComponent.stop();
                }
                chassisSerialMotion.run();
                intakeComponent.stop();
                break;

            case TURN:
//                stackerComponent.sleep(200);
                chassisComponent.spinToXDegree(-Math.PI/2, 0.03, 0.5);
//                stackerComponent.sleep(200);
                chassisComponent.spinToXDegree(-Math.PI/2, 0.02, 0.3);
//                stackerComponent.sleep(200);
//                chassisComponent.spinToXDegree(-Math.PI/2, 0.04, 0.5);
                state = State.STOP;
            case STOP:
                stop();
                break;

            case MOVE_TO_POWER_SHOT_LAUNCH_POSITION:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    state = State.LAUNCH_RINGS_AT_POWER_SHOTS;
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }

                if(chassisSerialMotion.getState()==NorcalChassisStateMachine.State.INIT){
                    moveToPowerShotLaunchPosition();
                }
                chassisSerialMotion.run();
                break;
            case LAUNCH_RINGS_AT_POWER_SHOTS:
                for(int ps=0;ps<3;ps++){
                    if(psFinished[ps][1]) continue;
                    if(!psFinished[ps][0]){
                        stackerComponent.sleep(100);
                        stackerComponent.safeWhack();
                        stackerComponent.sleep(500);
                        psFinished[ps][0]=true;
                        break;
                    }
                    else{
                        if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                            psFinished[ps][1]=true;
                            chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                            if(psFinished[2][1]){
                                launcherComponent.setFinishedLaunching(true);
                            }
                            break;
                        }
                        if(chassisSerialMotion.getState()==NorcalChassisStateMachine.State.INIT){
                            shiftPowershot(ps);
                        }
                        chassisSerialMotion.run();
                        break;
                    }
                }
                if(launcherComponent.isFinishedLaunching()){
                    state = State.MOVE_TO_TARGET_ZONE;
                    launcherComponent.stop();
                    launcherComponent.setFinishedLaunching(false);
                    break;
                }
                break;

        }
    }
}