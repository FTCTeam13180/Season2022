package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WhackerStateMachine {
    public enum State{
        INIT,
        EXECUTE,
        STOP
    }
    public enum Target{
        EXTEND,
        RETRACT
    }
    public WhackerComponent whackerComponent;
    public Target target;
    public OpMode op;
    private static final double timeoutMs = 30000;

    private ElapsedTime runtime;
    private State state = State.INIT;
    WhackerStateMachine(WhackerComponent wc, OpMode op){
        this.whackerComponent = wc;
        this.op = op;
    }
    public void setTarget(Target t){
        target = t;
    }
    public State getState(){
        return state;
    }
    public void init(){
        whackerComponent.init();
        op.telemetry.addData("WhackerStateMachine: ", "initialized");

        runtime = new ElapsedTime();
        runtime.reset();
    }
    public void execute(){
        switch (target){
            case EXTEND:
                whackerComponent.extendPosition();
                break;
            case RETRACT:
                whackerComponent.retractPosition();
                break;

        }

    }
    public void stop(){
        op.telemetry.addData("WhackerStateMachine", "Finished");
    }
    public boolean isbusy(){
        return whackerComponent.isBusy();
    }
    public void run(){
        switch (state){
            case INIT:
                init();
                state = State.EXECUTE;
                break;

            case EXECUTE:
                execute();
                if (!whackerComponent.isBusy() || (runtime.milliseconds() >= timeoutMs)) {
                    state = State.STOP;
                }
                break;

            case STOP:
                stop();
                break;
        }
    }

}
