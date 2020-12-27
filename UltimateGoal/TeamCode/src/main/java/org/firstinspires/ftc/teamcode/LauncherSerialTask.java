package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class LauncherSerialTask {
    private LauncherComponent launcherComponent;
    private LauncherStateMachine launcherStateMachine;
    OpMode op;

    public LauncherSerialTask (LauncherComponent lc, OpMode op) {
        this.launcherComponent = lc;
        launcherStateMachine = new LauncherStateMachine(lc, op);
    }
    //in milliseconds - so 8 sec = 8000 input
    public void setRunningTime (double milliseconds) {
        launcherStateMachine.setRunningTime(milliseconds);
    }
    //public void setPower (double power){launcherStateMachine.setPower(power);}

    public LauncherStateMachine.State getState() {
        return launcherStateMachine.getState();
    }
    public void init(){
        launcherComponent.init();
    }
    public void run() {
        launcherStateMachine.run();}
}
