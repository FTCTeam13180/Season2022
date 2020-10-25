package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


public class ChassisStateMachine implements BasicCommand {

    enum State {
        INIT,
        EXECUTE,
        STOP
    }

    ChassisComponent chassisComponent;
    private double speed;
    private double cms;
    private double timeoutMs;
    private OpMode op;
    ElapsedTime runtime;
    private State state = State.INIT;

    public ChassisStateMachine(ChassisComponent chassisComponent, OpMode op) {
        this.chassisComponent = chassisComponent;
        this.op = op;
    }

    public void setSpeed(double s){ speed = s; }

    public void setDistance(double c){ cms = c; }

    public void setTimeoutMs(double ms){
        timeoutMs = ms;
    }


    public State getState(){
        return state;
    }
    public void init() {

        chassisComponent.init();
        op.telemetry.addData("ChassisStateMachine: ", "initialized");

        // Set Target Position
        chassisComponent.setTargetPosition(cms);

        // Turn On RUN_TO_POSITION
        chassisComponent.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime = new ElapsedTime();
        runtime.reset();
    }

    public void execute(){
        // Based on direction call corresponding Move function
        chassisComponent.move(speed, speed, speed, speed);
        chassisComponent.logCurrentPosition();
    }

    public void stop(){
        // Stop all motion;
        chassisComponent.stop();
        // Turn off RUN_TO_POSITION
        chassisComponent.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public ChassisStateMachine(ParallelStateMachineOpMode opMode, ParallelStateMachineOpMode.DIRECTION direction, double speed, double cms, double timeoutMs){
         this.speed = speed;
         this.cms = cms;
         this.timeoutMs = timeoutMs;
         op = opMode;
    }

    private boolean isBusy() {
        return  chassisComponent.isBusy();
    }

    public void run() {

        switch(state){

            case INIT:
                init();
                state = State.EXECUTE;
                break;

            case EXECUTE:
                execute();
                if (!chassisComponent.isBusy() || (runtime.milliseconds() >= timeoutMs)) {
                    state = State.STOP;
                }
                break;

            case STOP:
                stop();
                break;
        }
    }

}
