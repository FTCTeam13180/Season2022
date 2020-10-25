package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class IntakeStateMachine implements BasicCommand {

    enum State {
        INIT,
        EXECUTE,
        STOP
    }

    IntakeComponent intakeComponent;
    private OpMode op;
    private static final double timeoutMs = 30000;
    private double power;
    ElapsedTime runtime;
    private State state = State.INIT;

    public IntakeStateMachine(IntakeComponent intakeComponent, OpMode op){
        this.intakeComponent = intakeComponent;
        this.op = op;
    }

    public void setPower(double p) {
        power = p;
    }

    public State getState() {
        return state;
    }

    public void init() {
        intakeComponent.init();
        op.telemetry.addData("IntakeStateMachine: ", "initialized");

        runtime = new ElapsedTime();
        runtime.reset();

    }

    public void execute(){
        intakeComponent.setPower(power);
    }

    public void stop(){
        intakeComponent.stop();
    }
    private boolean isBusy() {
        return  intakeComponent.isbusy();
    }
    public void run() {

        switch(state){

            case INIT:
                init();
                state = IntakeStateMachine.State.EXECUTE;
                break;

            case EXECUTE:
                execute();
                if (!intakeComponent.isbusy() || (runtime.milliseconds() >= timeoutMs)) {
                    state = IntakeStateMachine.State.STOP;
                }
                break;

            case STOP:
                stop();
                break;
        }
    }
}
