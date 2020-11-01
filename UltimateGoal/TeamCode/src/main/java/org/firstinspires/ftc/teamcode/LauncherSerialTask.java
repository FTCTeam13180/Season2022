package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class LauncherSerialTask {
    private LauncherComponent launcherComponent;
    private LauncherStateMachine launcherStateMachine;

    public LauncherSerialTask (LauncherComponent lc, OpMode op) {
        this.launcherComponent = lc;
        launcherStateMachine = new LauncherStateMachine(lc, op);
    }
    public void setPower (double power){
        launcherStateMachine.setPower(power);
    }
    public LauncherStateMachine.State getState() {
        return launcherStateMachine.getState();
    }
    public void run() {launcherStateMachine.run();}
}
