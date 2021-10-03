package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class WhackerComponent {
    private static final double extendPosition = 1;
    private static final double retractPosition = 0;
    public OpMode opmode;
    WhackerComponent (OpMode op) {
        opmode = op;
    }
    private Servo whacker;
    public void init(){
        whacker = opmode.hardwareMap.get(Servo.class, "Whacker");
    }
    public void extendPosition() {
        whacker.setPosition(extendPosition);
    }
    public void retractPosition(){
        whacker.setPosition(retractPosition);
    }
    public boolean isBusy(){
        if (whacker.getPosition() < extendPosition || whacker.getPosition() > retractPosition){
            return true;
        }
        else{
            return false;
        }
    }

}
