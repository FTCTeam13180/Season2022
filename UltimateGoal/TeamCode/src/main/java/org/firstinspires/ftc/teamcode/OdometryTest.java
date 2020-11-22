package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Odometry Test",group="UltimateGoalAutonomous")
public class OdometryTest extends OpMode {

    enum State {
        INIT,
        MOVE_TO_TARGET_ZONE_1,
        MOVE_TO_TARGET_ZONE_2,
        MOVE_TO_POWERSHOT,
        MOVE_TO_WOBBLE,
        MOVE_TO_GOAL,
        MOVE_TO_RINGS,
        STOP
    }


    OdometryTest.State state = OdometryTest.State.INIT;
    ElapsedTime time;
    double TimeoutMs;
    Odometry odometry;
    int numberOfRings=0; //just cuz
    ChassisSerialMotion chassisSerialMotion;


    public void init(){
        telemetry.addLine("Initializing");
        telemetry.update();
        time = new ElapsedTime();
        time.reset();
        odometry = new Odometry(this,120,45);
        odometry.init();
        chassisSerialMotion = new ChassisSerialMotion(odometry, this);
    }

    public void moveToTargetZone() {
        chassisSerialMotion.moveToTarget(numberOfRings);
    }

    public void moveToPowerShot() {
        chassisSerialMotion.moveToPowerShot();
    }
    public void moveToWobble(){
        chassisSerialMotion.moveToWobble();
    }
    public void moveToRings(){
        chassisSerialMotion.moveToRings();
    }
    public void moveToGoal(){
        chassisSerialMotion.moveToGoal();
    }

    public void stop (){
        telemetry.addData("auto katham",time.milliseconds());
        
    }


    public void loop() {

        switch(state){

            case INIT:
                telemetry.addLine("Finished INIT state");
                //telemetry.update();
                state = State.MOVE_TO_POWERSHOT;

                break;

            case MOVE_TO_POWERSHOT:

                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.MOVE_TO_TARGET_ZONE_1;
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);

                    //telemetry.update();
                    break;
                }

                if(chassisSerialMotion.getState()==ChassisStateMachine.State.INIT){
                    moveToPowerShot();
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_TARGET_ZONE_1:

                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.MOVE_TO_WOBBLE;
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState()==ChassisStateMachine.State.INIT){
                    moveToTargetZone();
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_WOBBLE:

                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.MOVE_TO_TARGET_ZONE_2;
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState()==ChassisStateMachine.State.INIT){
                    moveToWobble();
                }
                chassisSerialMotion.run();
                break;
            case MOVE_TO_TARGET_ZONE_2:

                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.MOVE_TO_RINGS;
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState()==ChassisStateMachine.State.INIT){
                    moveToTargetZone();
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_RINGS:

                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.MOVE_TO_GOAL;
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState()==ChassisStateMachine.State.INIT){
                    moveToRings();
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_GOAL:

                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.STOP;
                    chassisSerialMotion.setState(ChassisStateMachine.State.INIT);
                    break;
                }
                if(chassisSerialMotion.getState()==ChassisStateMachine.State.INIT){
                    moveToGoal();
                }
                chassisSerialMotion.run();
                break;

            case STOP:
                stop();
                break;


        }
    }
}