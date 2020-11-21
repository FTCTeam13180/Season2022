package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class StackerSerialTask {
    private StackerComponent stackerComponent;
    private StackerStateMachine stackerStateMachine;

    public StackerSerialTask (StackerComponent sc, OpMode op) {
        this.stackerComponent = sc;
        stackerStateMachine = new StackerStateMachine(sc, op);
    }
    //must set target to either launching or receiving
    public void setWhacks (int t){
        stackerStateMachine.setWhacks(t);
    }
    public StackerStateMachine.State getState() {
        return stackerStateMachine.getState();
    }
    public void run() {stackerStateMachine.run();}
}
