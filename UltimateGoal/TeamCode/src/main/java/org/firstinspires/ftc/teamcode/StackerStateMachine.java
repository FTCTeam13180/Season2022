package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class StackerStateMachine {
    enum State {
        INIT,
        STACKER_UP,
        WHACKING,
        STACKER_DOWN,
        STOP
    }

    private StackerComponent stackerComponent;
    private int whacks;
    private OpMode op;
    private static final double timeoutMs = 30000;

    private ElapsedTime runtime;
    private State state = State.INIT;

    public StackerStateMachine(StackerComponent stackerComponent, OpMode op){
        this.stackerComponent = stackerComponent;
        this.op = op;
    }

    /*
    must pass a "whacks" for the number of times we want to whack; ex if there are 3 rings in the
    stacker box, enter 3 for setWhacks
     */
    public void setWhacks (int w){
        whacks = w;
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
    public void stackerUp(){
        stackerComponent.stackerUp();
        op.telemetry.addData("StackerStateMachine: ", "stacker moved up");
    }
    public void whacking(){
        int i = 0;
        while (i < whacks){
            i++;
            stackerComponent.safeWhack();
            op.telemetry.addData("StackerStateMachine: ", "whacked " + i + " times");
        }
    }
    public void stackerDown(){
        stackerComponent.stackerDown();
        op.telemetry.addData("StackerStateMachine: ", "stacker moved down");
    }

    public void stop(){
        op.telemetry.addData("StackerStateMachine", "Finished");
    }
    public boolean isStackerbusy(){
        return stackerComponent.isStackerBusy();
    }
    public boolean isWhackerbusy(){
        return stackerComponent.isWhackerBusy();
    }
    public void run(){
        switch(state){

            case INIT:
                init();
                state = State.STACKER_UP;
                break;

            case STACKER_UP:
                stackerUp();
                if (!stackerComponent.isStackerBusy() || (runtime.milliseconds() >= timeoutMs)) {
                    state = State.WHACKING;
                }
                break;

            case WHACKING:
                whacking();
                if(!stackerComponent.isWhackerBusy() || (runtime.milliseconds() >= timeoutMs)){
                    state = State.STACKER_DOWN;
                }
               break;

            case STACKER_DOWN:
                stackerDown();
                if (!stackerComponent.isStackerBusy() || (runtime.milliseconds() >= timeoutMs)) {
                state = State.STOP;
            }
                break;
            case STOP:
                stop();
                break;
        }
    }
}
