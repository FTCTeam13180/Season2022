package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;

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
    State state = State.INIT;

    public ChassisStateMachine(ChassisComponent chassisComponent, double speed, double cms, double timeoutMs, OpMode op) {
        this.chassisComponent = chassisComponent;
        this.speed = speed;
        this.cms = cms;
        this.timeoutMs = timeoutMs;
        this.op = op;
    }

    public void init(){

        chassisComponent.init();
        op.telemetry.addData("Drive: ", "Resetting Encoders");
        chassisComponent.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        chassisComponent.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        op.telemetry.addData("Drive:", "Encoders reset");

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
                if (!chassisComponent.isBusy() && (runtime.milliseconds() >= timeoutMs)) {
                    state = State.STOP;
                }
                break;

            case STOP:
                stop();
                break;
        }
    }

}
