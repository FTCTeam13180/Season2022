package org.firstinspires.ftc.teamcode;

import android.sax.StartElementListener;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
    private double speed;
    private double cms;
    private double timeoutMs;
    private OpMode op;
    ElapsedTime runtime;
    double power = 1.0;
    private State state = State.INIT;
    private ArrayList <Point> points;
    private boolean finished[];

    public ChassisStateMachine(Odometry odometry, OpMode op) {
        this.odometry=odometry;
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
    public void moveToTargetZone(int numRings){
        points= new ArrayList<Point>();
        if(numRings==0){
            //A
            points.add(new Point(45,210,power));
        }
        else if(numRings==1){
            //B
            points.add(new Point(90,270,power));
        }
        else{
            //C
            points.add(new Point(45,330,power));
        }
        finished = new boolean[points.size()];
    }

    public void moveToPowerShot(){


        points = new ArrayList<Point>();
        points.add(new Point(180,60,power));
        points.add(new Point(150,210,power));
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

    public State getState(){
        return state;
    }

    public void init() {
        // reset the timeout time and start motion.
        runtime = new ElapsedTime();
        runtime.reset();

        //move to power shot
        points = new ArrayList<Point>();
        points.add(new Point(180,60,power));
        points.add(new Point(150,210,power));
        //op.telemetry.update();
        finished = new boolean[points.size()];

    }
// 1 0 0 0
    public void execute(){
        op.telemetry.addData("ChassisStateMachine:", "In Execute");
        for(int i = 0;i<points.size();i++){
            if(finished[i]) continue;
            Point current = points.get(i);
            // current.getPower() -> 0
            odometry.nextPoint(current.getX(),current.getY(),current.getPower());
            if( odometry.isFinished(current.getX(),current.getY()) ){
                //odometry.stopChassisMotor();
                op.telemetry.addData("finished: ",i);

                //op.telemetry.update();
                odometry.last_X = odometry.global_X;
                odometry.last_Y = odometry.global_Y;
                finished[i]=true;

                continue;
            }
            break;
        }
    }
    public void stop(){
        odometry.stopChassisMotor();
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
                stop();
                break;
        }
    }

}