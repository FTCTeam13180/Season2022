package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;


public class ChassisSerialMotion {

    public static final double POWER = 1.0;
    public static final double CMS = 60 * 2.54;
    public static final double TIMOUTMS = 20000;

    private Odometry odometry;
    private ChassisStateMachine chassisStateMachine;

    public ChassisSerialMotion(Odometry odom, OpMode opMode) {
        this.odometry = odom;
        chassisStateMachine = new ChassisStateMachine(odometry, opMode);

    }
    public void moveToTarget(int numRings){ chassisStateMachine.moveToTargetZone(numRings); }
    public void moveToLaunch(){chassisStateMachine.moveToLaunch();}
    public void setTimeoutMs(double ms){
        chassisStateMachine.setTimeoutMs(ms);
    }

    public ChassisStateMachine.State getState(){
        return chassisStateMachine.getState();
    }

    public void run(){
        chassisStateMachine.run();
    }
}
