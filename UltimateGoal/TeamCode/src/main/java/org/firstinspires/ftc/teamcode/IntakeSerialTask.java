package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class IntakeSerialTask {
    public static final double POWER = 1.0;

    private IntakeComponent intakeComponent;
    private IntakeStateMachine intakeStateMachine;

    public IntakeSerialTask(IntakeComponent intakeComponent, OpMode opMode) {
        this.intakeComponent = intakeComponent;
        intakeStateMachine = new IntakeStateMachine(intakeComponent, opMode);


    }
    public void setPower (double POWER) {intakeStateMachine.setPower(POWER);}

    public IntakeStateMachine.State getState(){
        return intakeStateMachine.getState();
    }

    public void run(){
        intakeStateMachine.run();
    }
}
