package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class LauncherStateMachine implements BasicCommand {

    enum State {
        INIT,
        EXECUTE,
        STOP
    }

    LauncherComponent launcherComponent;
    private OpMode op;
    private static final double timeoutMs = 30000;
    private double power;
    ElapsedTime runtime;
    private State state = State.INIT;

    public LauncherStateMachine(LauncherComponent launcherComponent, OpMode op){
        this.launcherComponent = launcherComponent;
        this.op = op;
    }

    /** Different powers will be needed to hit the power shots or reach the top goal.
     *  For now, power is not a constant, and can be set for testing what speed is needed
     *  to get the ring to each target. It will later be turned into a constant once
     *  we figure out what the exact optimal speeds are for each.
     */
    public void setPower(double p) {
        power = p;
    }

    public State getState() {
        return state;
    }
    public void init(){
        launcherComponent.init();
        op.telemetry.addData("LauncherStateMachine: ", "initialized");

        runtime = new ElapsedTime();
        runtime.reset();
    }

    public void execute() {
        launcherComponent.givePower(power);
    }
    //will set power of the launcher to 0, thus stopping it
    public void stop(){
        launcherComponent.stop();
    }
    private boolean isBusy() {
        return  launcherComponent.isbusy();
    }
    public void run() {

        switch(state){

            case INIT:
                init();
                state = LauncherStateMachine.State.EXECUTE;
                break;

            case EXECUTE:
                execute();
                if (!launcherComponent.isbusy() || (runtime.milliseconds() >= timeoutMs)) {
                    state = LauncherStateMachine.State.STOP;
                }
                break;

            case STOP:
                stop();
                break;
        }
    }
}
