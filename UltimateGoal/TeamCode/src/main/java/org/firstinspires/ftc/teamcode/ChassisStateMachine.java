package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    public void setState(State st){
        state = st;
    }
    public void moveToWobble(){
        spline = new Spline(new Waypoint(60,60,power));
    }
    public void moveToRings(){
        spline = new Spline(new Waypoint(90,120,power));
    }
    public void pickUpRingsMovement(){
        spline = new Spline(new Waypoint(90,180,power));
    }
    public void smoothSpline(int n){
        /*
        points = new ArrayList<Point>();
        points.add(new Point(60*n,60*n,power));
         */
        /* old comment
        points.add(new Point(120,120,power));
        points.add(new Point(125,125,power));
        points.add(new Point(130,135,power));
        points.add(new Point(135,150,power));
        points.add(new Point(140,170,power));
        points.add(new Point(145,195,power));
        points.add(new Point(150,225,power));
        points.add(new Point(155,260,power));
        */
        spline = new Spline(new Waypoint(0,60*n,power));
    }

    public void moveToTargetZone(int numRings,boolean first){

        if(first){

            if(numRings==0){
                //A
                spline = new Spline(new Waypoint(80,240,power));
                spline.add(new Waypoint(80,240,power));
            }
            else if(numRings==1){
                //B
                spline = new Spline(new Waypoint(125,300,power));
                spline.add(new Waypoint(125,300,power));
            }
            else{
                //C
                spline = new Spline(new Waypoint(60,340,power));
                spline.add(new Waypoint(60,340,power));
            }
        }
        else{
            spline = new Spline(new Waypoint(150,81,power));
            spline.add(new Waypoint(150,230,power));
            if(numRings==0){
                //A
                spline.add(new Waypoint(70,245,power));
                spline.add(new Waypoint(70,245,power));
            }
            else if(numRings==1){
                //B
                spline.add(new Waypoint(125,310,power));
                spline.add(new Waypoint(125,310,power));
            }
            else{
                //C
                spline.add(new Waypoint(60,350,power));
                spline.add(new Waypoint(60,350,power));
            }
        }
    }

    public void moveToSecondWobble(){
        spline = new Spline(new Waypoint(150,240,power));
        spline.add(new Waypoint(150,83,power));
        spline.add(new Waypoint(93,83,power));
    }

    public void moveToPowerShot(){
        spline = new Spline(new Waypoint(97, 60, power));
        spline.add(new Waypoint(174,60,power));
        spline.add(new Waypoint(174,200,power));
    }
    public void moveToGoal(){
        op.telemetry.addData("power",power);
        //op.telemetry.update();
        spline = new Spline(new Waypoint(30,120,power));
    }

    public void moveToLaunchLine(){
        spline = new Spline(new Waypoint(120,240,power));
    }

    public State getState(){
        return state;
    }

    public void shiftPowershot(int ps_ix){
        //coordinates are not right, tweak pls - rohan
        if(ps_ix==0){
            spline = new Spline(new Waypoint(155,200,power)); //150-(19.5-4.5)
        }
        if(ps_ix==1){
            spline = new Spline(new Waypoint(136,200,power)); //150-(19.5-4.5)
        }
      if(ps_ix==2){
            //doesnt matter
            spline = new Spline(new Waypoint(136,200,power));
        }

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