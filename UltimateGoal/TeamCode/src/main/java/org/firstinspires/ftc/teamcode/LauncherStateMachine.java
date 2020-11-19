package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class LauncherStateMachine {

    enum State {
        INIT,
        POWERING_UP,
        REACHED_MAX,
        STOP
    }

    private LauncherComponent launcherComponent;
    private double timeInterval;
    private static final double poweringUpTimeIntervel = 2000;
    private OpMode op;
    private static final double timeoutMs = 30000;
    private double power;
    ElapsedTime runtime;
    ElapsedTime executingTime;
    ElapsedTime powerUpTimer;
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

    /** How long you want the launcher to keep spinning
     * Set in milliseconds - so 5 sec would be 5000
     */
    public void setRunningTime (double t) { timeInterval = t;}

    public State getState() {
        return state;
    }
    public void init(){
        launcherComponent.init();
        op.telemetry.addData("LauncherStateMachine: ", "initialized");

        runtime = new ElapsedTime();
        runtime.reset();
    }

    public void powerUp() {
        launcherComponent.givePower(power);
        powerUpTimer = new ElapsedTime();
        executingTime = new ElapsedTime();
    }

    public void reachedMax(){
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
                state = State.POWERING_UP;
                break;

            case POWERING_UP:
                powerUp();
                if (powerUpTimer.milliseconds() > poweringUpTimeIntervel || runtime.milliseconds() > timeoutMs){
                    state = State.REACHED_MAX;
                }
                break;
            case REACHED_MAX:
                reachedMax();
                if(executingTime.milliseconds() > timeInterval || runtime.milliseconds() > timeoutMs){
                    state = State.STOP;
                }

            case STOP:
                stop();
                break;
        }
    }
}