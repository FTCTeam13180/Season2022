package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class StackerComponent {
    private static final double STACKER_DOWN = 0;
    private static final double STACKER_UP = 1;
    private static final double WHACKER_OUT = 1;
    private static final double WHACKER_IN = 0;
    private Servo stacker;
    private Servo whacker;
    private double stackerPosition;
    private double whackerPosition;
    private OpMode opmode;

    StackerComponent (OpMode op) {
        opmode = op;
    }

    public void init(){
        stacker = opmode.hardwareMap.get(Servo.class, "Stacker");
        whacker = opmode.hardwareMap.get(Servo.class, "Whacker");
        stacker.setPosition(STACKER_DOWN);
        whacker.setPosition(WHACKER_IN);
    }

    public void stackerUp(){
        stackerPosition = STACKER_UP;
        stacker.setPosition(stackerPosition);
    }
    public void stackerDown(){
        stackerPosition = STACKER_DOWN;
        stacker.setPosition(stackerPosition);
    }
    public void safeWhack(){
        if(stackerPosition == STACKER_UP){
            unsafeWhackerOut();
            //TODO: Add a sleep function to allow whacker to fully move
            unsafeWhackerIn();
        }
    }
    public void unsafeWhackerOut() {
        whackerPosition = WHACKER_OUT;
        whacker.setPosition(WHACKER_OUT);
    }
    public void unsafeWhackerIn(){
        whackerPosition = WHACKER_IN;
        whacker.setPosition(WHACKER_IN);
    }

    //likely will not work - servo doesn't give any position between the ones that were set
    public boolean isStackerBusy(){
        if (stacker.getPosition() != stackerPosition || stacker.getPosition() > STACKER_DOWN){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isWhackerBusy(){
        if (whacker.getPosition() < whackerPosition || whacker.getPosition() > STACKER_DOWN){
            return true;
        }
        else{
            return false;
        }
    }

}
