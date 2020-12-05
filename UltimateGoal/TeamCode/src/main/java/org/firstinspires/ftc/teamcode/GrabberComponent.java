package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class GrabberComponent {
    private static final double ARM_DOWN = 1;
    private static final double ARM_STRAIGHT = 0.5;
    private static final double ARM_UP = 0;
    private static final double CLAW_OPEN = 0;
    private static final double CLAW_CLOSE = 1;

    private Servo arm;
    private Servo clawl;
    private Servo clawr;
    private double armPosition;
    private double clawPosition;
    private OpMode opmode;

    GrabberComponent (OpMode op) {
        opmode = op;
    }
    public void init(){
        arm = opmode.hardwareMap.get(Servo.class, "Arm");
        clawl = opmode.hardwareMap.get(Servo.class, "Clawl");
        clawr = opmode.hardwareMap.get(Servo.class, "Clawr");
        arm.setPosition(ARM_UP);
        armPosition = ARM_UP;
        clawr.setPosition(CLAW_CLOSE);
        clawl.setPosition(1 - CLAW_CLOSE);
        clawPosition = CLAW_CLOSE;
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
        clawr.setPosition(clawPosition);
        clawl.setPosition(1 - clawPosition);
    }
    public void clawClose(){
        clawPosition = CLAW_CLOSE;
        clawr.setPosition(clawPosition);
        clawl.setPosition(1 - clawPosition);
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
        armUp();
    }
    public void safeWobbleDownAndRelease(){
        armDown();
        sleep(750);
        clawOpen();
        armUp();
    }

    //likely will not work - servo doesn't give any position between the ones that were set
    public boolean isClawBusy(){
        if (clawr.getPosition() != clawPosition || clawr.getPosition() > CLAW_OPEN){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isArmBusy(){
        if (arm.getPosition() < armPosition || arm.getPosition() > ARM_DOWN){
            return true;
        }
        else{
            return false;
        }
    }

}
