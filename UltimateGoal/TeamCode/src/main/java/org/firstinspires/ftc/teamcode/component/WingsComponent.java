package org.firstinspires.ftc.teamcode.component;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class WingsComponent implements Component {
    private static final double WINGS_DOWN = 1;
    private static final double WINGS_UP = 0;
    private Servo wingLeft;
    private Servo wingRight;
    private OpMode opmode;
    private double WING_STATE;

    public WingsComponent(OpMode op) {
        opmode = op;
    }

    public void init(){
        wingLeft = opmode.hardwareMap.get(Servo.class, "Wing_L");
        wingRight = opmode.hardwareMap.get(Servo.class, "Wing_R");
        wingsUp();
    }

    public void wingsUp(){
        wingRight.setPosition(WINGS_DOWN);
        wingLeft.setPosition(WINGS_UP);
        WING_STATE = WINGS_UP;
    }

    public void wingsDown(){
        wingRight.setPosition(WINGS_UP);
        wingLeft.setPosition(WINGS_DOWN);
        WING_STATE = WINGS_DOWN;
    }

    public void toggleWings() {
        if (WING_STATE == WINGS_DOWN)
            wingsUp();
        else
            wingsDown();
    }

    public boolean isBusy(){
        if (WING_STATE == WINGS_DOWN){
            return true;
        }
        else{
            return false;
        }
    }

    public void stop(){
        wingsUp();
    }

}
