package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    public void smoothSpline(){
        points = new ArrayList<Point>();
        points.add(new Point(0,120,1.0));
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
    public void moveToTargetZone(int numRings){
        points= new ArrayList<Point>();

        if(numRings==0){
            //A
            points.add(new Point(75,270,power));
        }
        else if(numRings==1){
            //B
            points.add(new Point(120,330,power));
        }
        else{
            //C
            points.add(new Point(75,390,power));
        }
        finished = new boolean[points.size()];
    }

    public void moveToPowerShot(){
        points = new ArrayList<Point>();
        points.add(new Point(180,60,power));
        points.add(new Point(125,210,power));
        //op.telemetry.update();
        finished = new boolean[points.size()];
    }
    public void moveToGoal(){
        points = new ArrayList<Point>();
        points.add(new Point(90,210,power));
        op.telemetry.addData("power",power);
        //op.telemetry.update();
        finished = new boolean[points.size()];
    }

    public void moveToLaunchLine(){
        //ToDO: add points for launch line
    }

    public State getState(){
        return state;
    }
    public void shiftPowershot(int ps_ix){
        points = new ArrayList<Point>();
        //coordinates are not right, tweak pls - rohan
        if(ps_ix==0){
            points.add(new Point(100,210,power)); //150-(19.5-4.5)
        }
        if(ps_ix==1){
            points.add(new Point(70,210,power)); //150-(19.5-4.5)
        }
        if(ps_ix==2){
            //doesnt matter
            points.add(new Point(70,210,power));
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
                finished[i]=true;
                if(finished[points.size()-1]) {
                    chassisComp.stop();
                }
                op.telemetry.addData("finished: ",i);

                //op.telemetry.update();
                odometry.last_X = odometry.global_X;
                odometry.last_Y = odometry.global_Y;

                continue;
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
                break;
        }
    }

}