package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class StackerComponent {
    private static final double STACKER_DOWN = 1;
    private static final double STACKER_DUMP = 0;
    private static final double STACKER_UP = 0.635;
    private static final double WHACKER_OUT = 0;
    private static final double WHACKER_IN = 1;
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

    public void stackerDump(){
        stackerPosition = STACKER_DUMP;
        stacker.setPosition(stackerPosition);
    }
    public void stackerUp(){
        stackerPosition = STACKER_UP;
        stacker.setPosition(stackerPosition);
    }
    public void stackerDown(){
        stackerPosition = STACKER_DOWN;
        stacker.setPosition(stackerPosition);
    }
    public void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void safeWhack(){
        if(stackerPosition == STACKER_UP){
            unsafeWhackerOut();
            sleep(500);
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

    public void toggleWhacker() {
        if (whackerPosition == WHACKER_IN)
            unsafeWhackerOut();
        else
            unsafeWhackerIn();
    }


    public double getStackerPosition(){
        return stackerPosition;
    }
    public double getWhackerPosition(){
        return whackerPosition;
    }

    //likely will not work - servo doesn't give any position between the ones that were set
    public boolean isStackerBusy(){
        if (stacker.getPosition() != stackerPosition){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isWhackerBusy(){
        if (whacker.getPosition() < whackerPosition){
            return true;
        }
        else{
            return false;
        }
    }

}
