package org.firstinspires.ftc.teamcode.component;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class StackerComponent implements Component {
    private static final double STACKER_DOWN = 1;
    private static final double STACKER_DUMP = 0;
    private static final double STACKER_UP = 0.56;
    private static final double WHACKER_OUT = 0;
    private static final double WHACKER_IN = 1;
    private Servo stacker;
    private Servo whacker;
    private double stackerPosition;
    private double whackerPosition;
    private OpMode opmode;

    // Telemetry Items
    Telemetry.Item log_stacker;
    Telemetry.Item log_whacker;

    public StackerComponent (OpMode op) {
        opmode = op;
    }

    public void init(){
        stacker = opmode.hardwareMap.get(Servo.class, "Stacker");
        whacker = opmode.hardwareMap.get(Servo.class, "Whacker");
        stacker.setPosition(STACKER_DOWN);
        whacker.setPosition(WHACKER_IN);
        log_stacker = opmode.telemetry.addData("Stacker:", "DOWN");
        log_whacker = opmode.telemetry.addData("Whacker:", "IN");

    }

    public void logStackerPosition() {
        opmode.telemetry.addData("StackerPositon:", stacker.getPosition());
        opmode.telemetry.addData("WhackerPosition:", whacker.getPosition());
    }
    public void stackerDump(){
        stackerPosition = STACKER_DUMP;
        stacker.setPosition(stackerPosition);
        log_stacker.setValue("DUMP");
    }
    public void stackerUp(){
        stackerPosition = STACKER_UP;
        stacker.setPosition(stackerPosition);
        log_stacker.setValue("UP");
    }
    public void stackerDown(){
        stackerPosition = STACKER_DOWN;
        stacker.setPosition(stackerPosition);
        log_stacker.setValue("DOWN");
    }

    public boolean isStackerUp(){
        return stackerPosition == STACKER_UP;
    }

    public boolean isStackerDown(){
        return stackerPosition == STACKER_DOWN;
    }

    public void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void safeWhack(){
        if((stackerPosition == STACKER_UP) || (stackerPosition == STACKER_DUMP)) {
            unsafeWhackerOut();
            sleep(100);
            unsafeWhackerIn();
        }
    }
    public void safeWhackThree()
    {
        if(stackerPosition == STACKER_UP){
            unsafeWhackerOut();
            sleep(100);
            unsafeWhackerIn();
            sleep(175);
            unsafeWhackerOut();
            sleep(100);
            unsafeWhackerIn();
            sleep(175);
            unsafeWhackerOut();
            sleep(100);
            unsafeWhackerIn();
            sleep(175);
            unsafeWhackerOut();
            sleep(100);
            unsafeWhackerIn();
        }
    }
    public void unsafeWhackerOut() {
        whackerPosition = WHACKER_OUT;
        whacker.setPosition(WHACKER_OUT);
        log_whacker.setValue("OUT");
    }
    public void unsafeWhackerIn(){
        whackerPosition = WHACKER_IN;
        whacker.setPosition(WHACKER_IN);
        log_whacker.setValue("IN");
    }

    public void toggleWhacker() {
        if (whackerPosition == WHACKER_IN)
            unsafeWhackerOut();
        else
            unsafeWhackerIn();
    }

    public void stackerIncremetalUp()
    {
        stacker.setPosition(stacker.getPosition() + 0.02);
        sleep(500);
    }

    public void stackerIncremetalDown()
    {
        stacker.setPosition(stacker.getPosition() - 0.02);
        sleep(500);
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

    public boolean isBusy(){
        if (isWhackerBusy() || isStackerBusy()){
            return true;
        }
        else{
            return false;
        }
    }

    public void stop(){
        stacker.setPosition(STACKER_DOWN);
        whacker.setPosition(WHACKER_IN);
    }

}
