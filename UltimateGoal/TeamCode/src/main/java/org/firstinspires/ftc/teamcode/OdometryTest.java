package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Odometry Test",group="UlimateGoalAutonomous")
public class OdometryTest extends OpMode {

    enum State {
        INIT,
        MOVE_TO_TARGET_ZONE,
        MOVE_TO_LAUNCH_POSITION,
        STOP
    }

    public void init(){
        telemetry.addLine("Not Really Initializing");
    }

    OdometryTest.State state = OdometryTest.State.INIT;
    ElapsedTime time;
    double TimeoutMs;
    Odometry odometry;
    int numberOfRings=0; //just cuz
    ChassisSerialMotion chassisSerialMotion;


    public void initSM(){

        time = new ElapsedTime();
        time.reset();
        odometry = new Odometry(this,120,45);
        odometry.init();
        chassisSerialMotion = new ChassisSerialMotion(odometry, this);

    }


    public void moveToTargetZone() {
        chassisSerialMotion.moveToTarget(numberOfRings);
    }

    public void moveToLaunchPosition() {
        chassisSerialMotion.moveToLaunch();

    }


    public void stop (){
        telemetry.addData("auto katham",time.milliseconds());
        telemetry.update();

    }


    public void loop() {

        switch(state){

            case INIT:
                telemetry.addLine("Initializing");
                telemetry.update();
                initSM();
                state = State.MOVE_TO_TARGET_ZONE;
                telemetry.addLine("Initialized");
                telemetry.update();
                break;


            case MOVE_TO_TARGET_ZONE:
                telemetry.addData("Going to Target Zone",chassisSerialMotion.getState());
                telemetry.update();
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.MOVE_TO_LAUNCH_POSITION;
                    telemetry.addData("At Target Zone",chassisSerialMotion.getState());
                    telemetry.update();
                    break;
                }
                if(chassisSerialMotion.getState()!=ChassisStateMachine.State.EXECUTE){
                    moveToTargetZone();
                }
                chassisSerialMotion.run();
                break;

            case MOVE_TO_LAUNCH_POSITION:
                telemetry.addData("Going to Launch Zone",chassisSerialMotion.getState());
                telemetry.update();
                if(chassisSerialMotion.getState() == ChassisStateMachine.State.STOP){
                    state = State.STOP;
                    telemetry.addData("At Launch Zone",chassisSerialMotion.getState());
                    telemetry.update();
                    break;
                }
                if(chassisSerialMotion.getState()!=ChassisStateMachine.State.EXECUTE){
                    moveToLaunchPosition();
                }
                chassisSerialMotion.run();
                break;
          case STOP:
                stop();
                break;


        }
    }
}


