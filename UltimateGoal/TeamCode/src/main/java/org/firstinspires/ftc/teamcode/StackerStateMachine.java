package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class StackerStateMachine {
    enum State {
        INIT,
        EXECUTE,
        STOP
    }
    enum Target{
        LAUNCHING,
        RECEIVING
    }
    private StackerComponent stackerComponent;
    private Target target;
    private OpMode op;
    private static final double timeoutMs = 30000;

    private ElapsedTime runtime;
    private State state = State.INIT;

    public StackerStateMachine(StackerComponent stackerComponent, OpMode op){
        this.stackerComponent = stackerComponent;
        this.op = op;
    }
    //in order for this program to run, a target, either launching or receiving, needs to be passed
    public void setTarget(Target t){
        target = t;
    }
    public State getState(){
        return state;
    }
    public void init(){
        stackerComponent.init();
        op.telemetry.addData("StackerStateMachine: ", "initialized");

        runtime = new ElapsedTime();
        runtime.reset();
    }

    public void execute(){
        switch (target){
            case LAUNCHING:
                stackerComponent.launchPosition();
                break;
            case RECEIVING:
                stackerComponent.receivingPosition();
                break;
        }
    }
    public void stop(){
        op.telemetry.addData("StackerStateMachine", "Finished");
    }
    public boolean isbusy(){
        return stackerComponent.isBusy();
    }
    public void run(){
        switch(state){

            case INIT:
                init();
                state = StackerStateMachine.State.EXECUTE;
                break;

            case EXECUTE:
                execute();
                if (!stackerComponent.isBusy() || (runtime.milliseconds() >= timeoutMs)) {
                    state = StackerStateMachine.State.STOP;
                }
                break;

            case STOP:
                stop();
                break;
        }
    }
}
