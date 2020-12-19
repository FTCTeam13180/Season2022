package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class GrabberComponent implements Component {
    private static final double ARM_DOWN = .8;
    private static final double ARM_STRAIGHT = 0.3;
    private static final double ARM_UP = 0;
    private static final double CLAW_OPEN = 1;
    private static final double CLAW_CLOSE = 0;

    private Servo arm;
    private Servo claw;
    private double armPosition;
    private double clawPosition;
    private OpMode opmode;

    GrabberComponent (OpMode op) {
        opmode = op;
    }
    public void init(){
        arm = opmode.hardwareMap.get(Servo.class, "Arm");
        claw = opmode.hardwareMap.get(Servo.class, "Claw");
        clawPosition = CLAW_CLOSE;
        arm.setPosition(ARM_STRAIGHT);
        armPosition = ARM_STRAIGHT;
        opmode.telemetry.addData("GrabberComponent", "Initialized");
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
        armDown();
        sleep(500);
        clawClose();
        sleep(500);
        armStraight();
    }
    public void safeWobbleDownAndRelease(){
        armDown();
        sleep(500);
        clawOpen();
        sleep(500);
        armStraight();
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
    public boolean isArmBusy(){
        if (arm.getPosition() < armPosition || arm.getPosition() > ARM_DOWN){
            return true;
        }
        else{
            return false;
        }
    }

}
