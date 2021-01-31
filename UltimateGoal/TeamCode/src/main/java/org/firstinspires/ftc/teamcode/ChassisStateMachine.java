package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;

public class ChassisStateMachine implements BasicCommand {

    enum State {
        INIT,
        EXECUTE,
        AUTO_CORRECT,
        STOP
    }

    Odometry odometry;
    ChassisComponent chassisComp;
    private double speed;
    private double cms;
    private double timeoutMs;
    private OpMode op;
    ElapsedTime runtime;
    double power = 1.0;
    private State state = State.INIT;
    private Spline spline;
    private int rings;
    private int check = 0;
    private AutoPositionCorrector autoPositionCorrector;
    private boolean isFinished;

    public ChassisStateMachine(Odometry odometry,ChassisComponent chassisComponent ,OpMode op) {
        this.odometry=odometry;
        this.chassisComp=chassisComponent;
        this.op = op;
    }

    public void setTimeoutMs(double ms){
        timeoutMs = ms;
    }
    public State getState(){
        return state;
    }
    public void setState(State st){ state = st; }

    public void moveToPowerShot(){
        spline = new Spline(new Waypoint(98, 60, power));
        spline.add(new Waypoint(150,120,power));
        spline.add(new Waypoint(174,200,power));
    }

    public void shiftPowershot(int ps_ix){
        if(ps_ix==0){
            spline = new Spline(new Waypoint(155,200,power)); //150-(19.5-4.5)
        }
        if(ps_ix==1){
            spline = new Spline(new Waypoint(136,200,power)); //150-(19.5-4.5)
        }
        if(ps_ix==2){
            //same point in order to avoid rampdown while moving between powershots
            spline = new Spline(new Waypoint(136,200,power));
        }

    }

    public void moveToTargetZone(int numRings,boolean first){
        rings = numRings;
        if(first){
            if(rings==0){
                //A
                spline = new Spline(new Waypoint(60,240,power));
                spline.add(new Waypoint(60,240,power));
            }
            else if(rings==1){
                //B
                spline = new Spline(new Waypoint(120,300,power));
                spline.add(new Waypoint(120,300,power));
            }
            else if(rings == 4){
                //C
                spline = new Spline(new Waypoint(60,345,power));
                spline.add(new Waypoint(60,345,power));
            }
        }
        else{

            if(rings==0){
                //A
                spline = new Spline(new Waypoint(70,245,power));
                spline.add(new Waypoint(70,245,power));
            }
            else if(rings==1){
                //B
                spline = new Spline(new Waypoint(126,278,power));
                spline.add(new Waypoint(126,278,power));
            }
            else if (rings == 4){
                //C
                spline = new Spline(new Waypoint(70,355,power));
                spline.add(new Waypoint(70,355,power));
            }
        }
    }

    public void moveToSecondWobble(){
        if(rings == 0){
            //starts from 60, 250
            spline = new Spline(new Waypoint(120,220,power));
            spline.add(new Waypoint(150,200,power));
            spline.add(new Waypoint(160,170,power));
            spline.add(new Waypoint(160,160,power));
            spline.add(new Waypoint(150,130,power));
            spline.add(new Waypoint(125,100,power));
            spline.add(new Waypoint(95,84,power));
        }
        if(rings == 1){
            //starts from 120, 300
            spline = new Spline(new Waypoint(140,260,power));
            spline.add(new Waypoint(150,220,power));
            spline.add(new Waypoint(160,170,power));
            spline.add(new Waypoint(160,160,power));
            spline.add(new Waypoint(150,130,power));
            spline.add(new Waypoint(125,100,power));
            spline.add(new Waypoint(95,84,power));
        }
        else if(rings == 4){
            //starts from 60, 340
            spline = new Spline(new Waypoint(100,290,power));
            spline.add(new Waypoint(140,230,power));
            spline.add(new Waypoint(160,170,power));
            spline.add(new Waypoint(160,160,power));
            spline.add(new Waypoint(150,130,power));
            spline.add(new Waypoint(125,100,power));
            spline.add(new Waypoint(95,84,power));
        }
    }

    public void moveToWobble(){
        spline = new Spline(new Waypoint(60,60,power));
    }
    public void moveToRings(){
        spline = new Spline(new Waypoint(90,120,power));
    }
    public void pickUpRingsMovement(){
        spline = new Spline(new Waypoint(90,84,power));
        spline.add(new Waypoint(90, 180, power));
    }
    public void smoothSpline(int n){
       int distance = 60;

        //square
        spline = new Spline(new Waypoint(distance*0,distance*n,power));
      //  spline.add(new Waypoint(distance*n, distance*0, power ));
        //spline.add(new Waypoint(distance*0, -distance*n, power ));
        //spline.add(new Waypoint(-distance*n, distance*0, power ));
    }

    public void moveToGoal(int instance){
        if (instance == 1) {
            op.telemetry.addData("power", power);
            //op.telemetry.update();
            spline = new Spline(new Waypoint(110, 55, power));
            spline.add(new Waypoint(130, 75, power));
            spline.add(new Waypoint(140, 90, power));
            spline.add(new Waypoint(150, 120, power));
            spline.add(new Waypoint(140, 150, power));
            spline.add(new Waypoint(130, 170, power));
            spline.add(new Waypoint(100, 183, power));

            //destination
            spline.add(new Waypoint(85, 198, power));
        }
        else{
            spline = new Spline(new Waypoint(85, 198, power));
        }
    }

    public void moveToLaunchLine(){
        spline = new Spline(new Waypoint(120,240,power));
    }

    public void init() {
        // reset the timeout time and start motion.
        runtime = new ElapsedTime();
        runtime.reset();
        autoPositionCorrector = new AutoPositionCorrector(spline.getDestination());
    }

    public void execute() {
        // Log robot's current position
        autoPositionCorrector.logPosition(odometry.global_X, odometry.global_Y);

        // Get target waypoint and move towards it
        Waypoint targetPoint = spline.getTargetWaypoint();
        if (spline.movingToDestination())
        {
            odometry.nextPointRampdown(targetPoint.getX(),targetPoint.getY(),targetPoint.getPower());
            isFinished = odometry.isFinishedRampdown(targetPoint.getX(),targetPoint.getY());
        }
        else
        {
            odometry.nextPoint(targetPoint.getX(),targetPoint.getY(),targetPoint.getPower());
            isFinished = odometry.isFinished(targetPoint.getX(),targetPoint.getY());
        }
        if(isFinished) {
            targetPoint.setReached();
            chassisComp.spinToXDegree(0);
            //op.telemetry.addData("finished: ",i);

            //op.telemetry.update();
            //odometry.last_X = odometry.global_X;
            //odometry.last_Y = odometry.global_Y;
        }
    }

    public void autoCorrectPosition() {
        // Log robot's current position
        autoPositionCorrector.logPosition(odometry.global_X, odometry.global_Y);
        if (autoPositionCorrector.correctionDone()) {
            // Correction is done. Stop the robot.
            chassisComp.stop();
            chassisComp.spinToXDegree(0);
            spline.setCorrected();
        } else {
            // Correction is not done yet. Continue correction
            Waypoint destination = spline.getDestination();
            odometry.nextPointRampdown(destination.getX(),destination.getY(),destination.getPower());
        }
    }

    public void stop(){
        chassisComp.stop();
    }

    public void run() {
        switch(state){

            case INIT:
                init();
                state = State.EXECUTE;
                break;

            case EXECUTE:
                execute();
                if (spline.isCompleted()) {
                    chassisComp.spinToXDegree(0);
                    state = State.AUTO_CORRECT;
                }
                break;

            case AUTO_CORRECT:
                autoCorrectPosition();
                if (spline.isCorrected()) {
                    state = State.STOP;
                }
                break;

            case STOP:
                odometry.updateLog("STOP");
                break;
        }
    }

}