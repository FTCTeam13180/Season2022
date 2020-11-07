package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class StackerComponent {
    private static final double receivePosition = 0;
    private static final double launchPosition = 1;
    public OpMode opmode;
    StackerComponent (OpMode op) {
        opmode = op;
    }
    private Servo stacker;
    public void init(){
        stacker = opmode.hardwareMap.get(Servo.class, "Stacker");
    }
    public void receivingPosition(){
        stacker.setPosition(receivePosition);
    }
    public void launchPosition(){
        stacker.setPosition(launchPosition);
    }
    //if the servo position is not 1 or 0, then motor is busy
    public boolean isBusy(){
        if (stacker.getPosition() < launchPosition || stacker.getPosition() > receivePosition){
            return true;
        }
        else{
            return false;
        }
    }

}
