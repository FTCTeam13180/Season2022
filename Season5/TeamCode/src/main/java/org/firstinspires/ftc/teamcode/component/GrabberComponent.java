package org.firstinspires.ftc.teamcode.component;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class GrabberComponent implements Component {
    private static final double ARM_DOWN = 1;
    private static final double ARM_STRAIGHT = 0;
    private static final double ARM_UP = 0;
    private static final double CLAW_OPEN = 0.8;
    private static final double CLAW_CLOSE = 0;

    private Servo arm;
    private Servo claw;
    private double armPosition;
    private double clawPosition;
    private OpMode opmode;

    public GrabberComponent (OpMode op) {
        opmode = op;
    }
    public void init(){
        arm = opmode.hardwareMap.get(Servo.class, "Arm");
        claw = opmode.hardwareMap.get(Servo.class, "Claw");
        clawClose();
        armStraight();
//        opmode.telemetry.addData("GrabberComponent", "Initialized");
    }

    public void armUp(){
        armPosition = ARM_UP;
        arm.setPosition(armPosition);
    }
    public void armStraight(){
        armPosition = ARM_STRAIGHT;
        arm.setPosition(armPosition);
    }
    public void armDown(){
        armPosition = ARM_DOWN;
        arm.setPosition(armPosition);
    }
    public void clawOpen(){
        if(armPosition != ARM_UP) {
            clawPosition = CLAW_OPEN;
            claw.setPosition(clawPosition);
        }
    }

    public void unsafeClawOpen(){
            clawPosition = CLAW_OPEN;
            claw.setPosition(clawPosition);
    }

    public void clawClose(){
        clawPosition = CLAW_CLOSE;
        claw.setPosition(clawPosition);
    }
    public void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void safeWobbleGrabAndUp(){
        if (armPosition != ARM_DOWN) {
            armDown();
            sleep(500);
        }
        if (clawPosition != CLAW_CLOSE) {
            clawClose();
            sleep(500);
        }
        armStraight();
    }
    public void safeWobbleDownAndRelease(){
        if (armPosition != ARM_DOWN) {
            armDown();
            sleep(500);
        }
        if (clawPosition != CLAW_OPEN) {
            clawOpen();
            sleep(350);
        }
//        armStraight();
    }

    public boolean isArmDown() {
        return armPosition == ARM_DOWN;
    }
    //likely will not work - servo doesn't give any position between the ones that were set
    /*public boolean isClawBusy(){
        if (clawr.getPosition() != clawPosition || clawr.getPosition() > CLAW_OPEN){
            return true;
        }
        else{
            return false;
        }
    }*/
    public boolean isBusy(){
        if (arm.getPosition() < armPosition || arm.getPosition() > ARM_DOWN){
            return true;
        }
        else{
            return false;
        }
    }

    public void stop(){
        clawPosition = CLAW_CLOSE;
        arm.setPosition(ARM_STRAIGHT);
    }

}
