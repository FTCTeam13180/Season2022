package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.lang.reflect.Array;
import java.util.ArrayList;

class Point{
    private double X;
    private double Y;
    private double power;
    Point(double x, double y, double p){
        X = x;
        Y = y;
        power = p;
    }
    public double getX(){
        return X;
    }
    public double getY(){
        return Y;
    }
    public double getPower(){
        return power;
    }

}

public class ChassisStateMachine implements BasicCommand {

    enum State {
        INIT,
        EXECUTE,
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
    private ArrayList <Point> points;
    private boolean finished[];
    private int check = 0;

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
        points = new ArrayList<Point>();
        points.add(new Point(60,60,power));
        finished = new boolean[points.size()];
    }
    public void moveToRings(){
        points = new ArrayList<Point>();
        points.add(new Point(90,120,power));
        finished = new boolean[points.size()];
    }
    public void pickUpRingsMovement(){
        points = new ArrayList<Point>();
        points.add(new Point(90,180,power));
        finished = new boolean[points.size()];
    }
    public void smoothSpline(int n){
        points = new ArrayList<Point>();
        points.add(new Point(60*n,60*n,power));
        /*
        points.add(new Point(120,120,power));
        points.add(new Point(125,125,power));
        points.add(new Point(130,135,power));
        points.add(new Point(135,150,power));
        points.add(new Point(140,170,power));
        points.add(new Point(145,195,power));
        points.add(new Point(150,225,power));
        points.add(new Point(155,260,power));
*/
        finished = new boolean[points.size()];
    }
    public void moveToTargetZone(int numRings,boolean first){
        points= new ArrayList<Point>();


        if(first){

            if(numRings==0){
                //A
                points.add(new Point(80,240,power));
            }
            else if(numRings==1){
                //B
                points.add(new Point(125,300,power));
            }
            else{
                //C
                points.add(new Point(55,340,power));
            }
        }
        else{
            points.add(new Point(150,81,power));
            points.add(new Point(150,230,power));
            if(numRings==0){
                //A
                points.add(new Point(70,235,power));
            }
            else if(numRings==1){
                //B
                points.add(new Point(125,305,power));
            }
            else{
                //C
                points.add(new Point(70,340,power));
            }
        }

        finished = new boolean[points.size()];
    }
    public void moveToSecondWobble(){
        points = new ArrayList<Point>();
        points.add(new Point(150,240,power));
        points.add(new Point(150,81,power));
        points.add(new Point(93,83,power));
        finished = new boolean[points.size()];
    }

    public void moveToPowerShot(){
        points = new ArrayList<Point>();
        points.add(new Point(97, 60, power));
        points.add(new Point(174,60,power));
        points.add(new Point(174,210,power));
        //op.telemetry.update();
        finished = new boolean[points.size()];
    }
    public void moveToGoal(){
        points = new ArrayList<Point>();
        points.add(new Point(30,120,power));
        op.telemetry.addData("power",power);
        //op.telemetry.update();
        finished = new boolean[points.size()];
    }

    public void moveToLaunchLine(){
        points = new ArrayList<Point>();
        points.add(new Point(120,240,power));
        finished = new boolean[points.size()];
    }

    public State getState(){
        return state;
    }

    public void shiftPowershot(int ps_ix){
        points = new ArrayList<Point>();
        //coordinates are not right, tweak pls - rohan
        if(ps_ix==0){
            points.add(new Point(155,210,power)); //150-(19.5-4.5)
        }
        if(ps_ix==1){
            points.add(new Point(136,210,power)); //150-(19.5-4.5)
        }
        if(ps_ix==2){
            //doesnt matter
            points.add(new Point(75,210,power));
        }
        finished=new boolean[points.size()];

    }
    public void init() {
        // reset the timeout time and start motion.
        runtime = new ElapsedTime();
        runtime.reset();

    }
// 1 0 0 0
    public void execute(){
         for(int i = 0;i<points.size();i++){
            if(finished[i]) continue;
            Point current = points.get(i);
            odometry.nextPoint(current.getX(),current.getY(),current.getPower());
            if( odometry.isFinished(current.getX(),current.getY()) ){
                check++;
                chassisComp.spinToXDegree(0);
                if(check > 0) {
                    check = 0;
                    finished[i] = true;
                    if (finished[points.size() - 1]) {
                        odometry.updateLog("Before chassis.stop()");
                        chassisComp.stop();
                        odometry.updateLog("After chassis.stop()");
                    }
                    //op.telemetry.addData("finished: ",i);

                    //op.telemetry.update();
                    odometry.last_X = odometry.global_X;
                    odometry.last_Y = odometry.global_Y;

                    continue;
                }
            }
            break;
         }
    }
    public void stop(){
        chassisComp.stop();
    }

    public ChassisStateMachine(ParallelStateMachineOpMode opMode, ParallelStateMachineOpMode.DIRECTION direction,  double timeoutMs){
        this.timeoutMs = timeoutMs;
        op = opMode;
    }


    public void run() {
        switch(state){

            case INIT:
                init();
                state = State.EXECUTE;
                break;

            case EXECUTE:
                execute();
                if (finished[points.size()-1]) {
                    state = State.STOP;
                }
                break;

            case STOP:
                odometry.updateLog("STOP");
                break;
        }
    }

}