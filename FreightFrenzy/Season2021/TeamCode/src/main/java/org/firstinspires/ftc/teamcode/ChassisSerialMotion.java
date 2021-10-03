package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;


public class ChassisSerialMotion {

    public static final double POWER = 1.0;
    public static final double CMS = 60 * 2.54;
    public static final double TIMOUTMS = 20000;

    private Odometry odometry;
    private ChassisStateMachine chassisStateMachine;
    public ChassisSerialMotion(Odometry odom, ChassisComponent chassisComponent, OpMode opMode) {
        this.odometry = odom;
        chassisStateMachine = new ChassisStateMachine(odometry, chassisComponent,opMode);

    }
    public void moveToTarget(int numRings,boolean first){ chassisStateMachine.moveToTargetZone(numRings,first); }
    public void moveToPowerShot(){chassisStateMachine.moveToPowerShot();}
    public void moveToPickUpRings(){chassisStateMachine.pickUpRingsMovement();}
    public void moveToWobble(){ chassisStateMachine.moveToWobble(); }
    public void moveToRings(){chassisStateMachine.moveToRings();}
    public void moveToGoal(int instance){chassisStateMachine.moveToGoal(instance);}
    public void moveToSecondWobble(){chassisStateMachine.moveToSecondWobble();}
    public void moveToLaunchLine(){chassisStateMachine.ParkAtLaunchLine();}
    public void smoothSpline(int n){chassisStateMachine.smoothSpline(n);}
    public void shiftPowershot(int ps) { chassisStateMachine.shiftPowershot(ps); }
    public void setTimeoutMs(double ms){
        chassisStateMachine.setTimeoutMs(ms);
    }

    public ChassisStateMachine.State getState(){
        return chassisStateMachine.getState();
    }
    public void setState(ChassisStateMachine.State s){chassisStateMachine.setState(s);}

    public void run(){
        chassisStateMachine.run();
    }
}
