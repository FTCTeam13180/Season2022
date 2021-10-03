package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class WhackerSerialTask {
    private WhackerComponent whackerComponent;
    private WhackerStateMachine whackerStateMachine;

    public WhackerSerialTask (WhackerComponent wc, OpMode op) {
        this.whackerComponent = wc;
        whackerStateMachine = new WhackerStateMachine(wc, op);
    }
    //must set target to either launching or receiving
    public void setTarget (WhackerStateMachine.Target t){
        whackerStateMachine.setTarget(t);
    }
    public WhackerStateMachine.State getState() {
        return whackerStateMachine.getState();
    }
    public void run() {whackerStateMachine.run();}
}
