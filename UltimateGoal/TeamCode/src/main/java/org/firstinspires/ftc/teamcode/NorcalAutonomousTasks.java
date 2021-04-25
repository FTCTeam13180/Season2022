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
        PICK_UP_1_RING,
        PICK_UP_3_RINGS,
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
        intakeComponent = new IntakeComponent(op);
        intakeComponent.init();

        odometry = new Odometry(op, chassisComponent, intakeComponent, 97,45);
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
        chassisSerialMotion.setNumOfRings(numOfRings);
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
        chassisSerialMotion.moveToTarget(first);
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
    public void moveToPickUpRings(int instance){chassisSerialMotion.moveToPickUpRings(instance);}
    public void pickUpRings() {
        //TODO: robot turn to orientation to face intake to pile of rings
        chassisSerialMotion.moveToRings();
        intakeComponent.in();

    }

    public void moveToHighGoalLaunchPosition(int instance) {
        chassisSerialMotion.moveToGoal(instance);
    }

    public void launchRingsAtHighGoal(int num, double min_rpm, double max_rpm, double timeout){
        smartWhack.whack(num, min_rpm, max_rpm, timeout);
    }

    public void launchRingsAtHighGoal(int num){
      //  launcherComponent.setTargetRPM(1670);
        chassisComponent.spinToXDegree(-0.18, .005, 0.1);
        while (num > 0) {
            smartWhack.whack(1, 1650, 1690, 1000);
            num--;
        }
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
    int powershot_count = 0;
    int numRingFromPile = 1;

    public void run(){
        double wobbleGoalTime = 0;

        if (first_time_called)
        {
            first_time_called = false;
            time.reset();
        }

        log_autonomouTasks_state.setValue(state);
 //       if (numOfRings == 4 && time.milliseconds() > 29500){
 //           grabberComponent.unsafeClawOpen();
 //       }
        switch(state){

            case INIT:
                if(numOfRings == 0){
                    launcherComponent.setTargetRPM(1650);
                    state = State.MOVE_TO_POWER_SHOT_LAUNCH_POSITION;
                }
                else{
                    launcherComponent.setTargetRPM(1670);
                    state = State.MOVE_TO_HIGH_GOAL_LAUNCH_POSITION;
                }
                grabberComponent.armStraight();
                stackerComponent.stackerUp();
                break;

            case MOVE_TO_HIGH_GOAL_LAUNCH_POSITION:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
//                    chassisComponent.spinToXDegree(-0.15, .005, 0.2);
                    if(numOfRings == 1){
                        launchRingsAtHighGoal(1);
                    }
                    else if(numOfRings == 4){
                        stackerComponent.sleep(1000);
                        launchRingsAtHighGoal(3);
/*
                        smartWhack.whack(1, 1700, 1750, 350);
                        smartWhack.whack(1, 1700, 1750, 350);
                        smartWhack.whack(1, 1700, 1750, 350);

 */
                    }
                    state = State.PICK_UP_1_RING;
                    break;
                }
                if(chassisSerialMotion.getState()==NorcalChassisStateMachine.State.INIT){
                    moveToHighGoalLaunchPosition(1);
                }
                chassisSerialMotion.run();
                break;

            case PICK_UP_1_RING:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){

                    if(numOfRings == 1) {
                        intakeComponent.stop();
                        state = State.MOVE_TO_POWER_SHOT_LAUNCH_POSITION;
                    }
//                    else if(numOfRings == 4 && numRingFromPile == 1){
                    else if(numOfRings == 4){
 //                       numRingFromPile++;
//                        intakeComponent.expel();
//                        intakeComponent.stop();
                        state = State.MOVE_TO_HIGH_GOAL_LAUNCH_POSITION_2;
                    }
//                    else {
//                      state = State.MOVE_TO_POWER_SHOT_LAUNCH_POSITION;
//                    }

                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.INIT){
                    stackerComponent.stackerDown();
                    intakeComponent.in();
                    moveToPickUpRings(1);
                }
                chassisSerialMotion.run();
                break;

            case PICK_UP_3_RINGS:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP) {
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    state = State.MOVE_TO_POWER_SHOT_LAUNCH_POSITION;

                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState()==NorcalChassisStateMachine.State.INIT){
//                    stackerComponent.stackerUp();
//                    if (numOfRings < 4) { // already done in MOVE_TO_HIGH_GOAL_LAUNCH_POSITION_2
//                        chassisComponent.spinToXDegree(-0.15, .005, 0.2);
//                        launchRingsAtHighGoal(1, 1850, 1900, 1000);
//                    }
//                    stackerComponent.stackerDown();
//                    intakeComponent.in();
                    moveToPickUpRings(2);
                }
                chassisSerialMotion.run();
                break;


            case MOVE_TO_TARGET_ZONE:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    grabberComponent.safeWobbleDownAndRelease();
                        state = State.MOVE_TO_SECOND_WOBBLE;
//                  grabberComponent.safeWobbleGrabAndUp();

                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.INIT){
                    stackerComponent.stackerDown();
                    moveToTargetZone(true);
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_SECOND_WOBBLE:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    state = State.MOVE_SECOND_WOBBLE_TO_TARGET_ZONE;

                    grabWobble();
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.INIT){
  //                  wobbleGoalTime = time.milliseconds();;
                    moveToSecondWobble();
                }
                chassisSerialMotion.run();

               // if ((time.milliseconds() - wobbleGoalTime) > 1000 && grabberComponent.isArmDown())
                //{
                  //  grabberComponent.safeWobbleGrabAndUp();
                //}

                break;

            case MOVE_TO_HIGH_GOAL_LAUNCH_POSITION_2:
                stackerComponent.stackerUp();;
//                chassisComponent.spinToXDegree(-0.15, .005, 0.2);
                launchRingsAtHighGoal(1);
                stackerComponent.stackerDown();
                intakeComponent.in();
                state = State.PICK_UP_3_RINGS;

                chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                break;

            case MOVE_SECOND_WOBBLE_TO_TARGET_ZONE:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    grabberComponent.safeWobbleDownAndRelease();
                    // grabberComponent.armStraight();
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
                    state = State.STOP;
                    stackerComponent.stackerDown();
                    grabberComponent.safeWobbleGrabAndUp();
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

            case MOVE_TO_POWER_SHOT_LAUNCH_POSITION:
                if(chassisSerialMotion.getState() == NorcalChassisStateMachine.State.STOP){
                    state = State.LAUNCH_RINGS_AT_POWER_SHOTS;
                    intakeComponent.stop();
                    chassisSerialMotion.setState(NorcalChassisStateMachine.State.INIT);
                    break;
                }

                if(chassisSerialMotion.getState()==NorcalChassisStateMachine.State.INIT){
                    launcherComponent.setTargetRPM(1650);
                    moveToPowerShotLaunchPosition();
                }
                chassisSerialMotion.run();
                break;

            case LAUNCH_RINGS_AT_POWER_SHOTS:
                powershot_count++;
                if (powershot_count == 1) {
                    launcherComponent.setTargetRPM(1570);
                    stackerComponent.stackerUp();
                    chassisComponent.spinToXDegree(-0.39, .005, 0.1);
                    smartWhack.whack(1, 1650, 1680, 350);
//                    stackerComponent.safeWhack();
                }
                else if (powershot_count == 2) {
                    chassisComponent.spinToXDegree(-0.48, .005, 0.1);
                    smartWhack.whack(1, 1600, 1640, 350);
//                    stackerComponent.safeWhack();
                }
                else {
                    chassisComponent.spinToXDegree(-0.59, .005, 0.1);
                    smartWhack.whack(1, 1600, 1640, 350);
//                    stackerComponent.safeWhack();
                    state = State.MOVE_TO_TARGET_ZONE;
                    launcherComponent.stop();
                }
                break;

        }
    }
}