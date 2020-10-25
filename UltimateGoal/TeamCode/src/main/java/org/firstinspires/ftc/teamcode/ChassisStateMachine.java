package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

class Point{
    private double X;
    private double Y;
    private double power;
    Point(double x, double y, double power){
        X = x;
        Y = y;
        power = this.power;
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
    double power = 0.9;
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

    public void moveToTargetZone(int numRings){
        points= new ArrayList<Point>();
        points.add(new Point(150,180,power));
        if(numRings==0){
            //A
            points.add(new Point(60,240,power));
        }
        else if(numRings==1){
            //B
            points.add(new Point(90,270,power));
        }
        else{
            //C
            points.add(new Point(60,330,power));
        }
        finished = new boolean[points.size()];
    }

    public void moveToLaunch(){
        //owo

        points = new ArrayList<Point>();
        points.add(new Point(90,120,power));
        finished = new boolean[points.size()];
    }

    public State getState(){
        return state;
    }
    public void init() {

        op.telemetry.addData("Drive: ", "Resetting Encoders");
        odometry.setChassisMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odometry.setChassisMode(DcMotor.RunMode.RUN_USING_ENCODER);
        op.telemetry.addData("Drive:", "Encoders reset");

        // reset the timeout time and start motion.
        runtime = new ElapsedTime();
        runtime.reset();
    }

    public void execute(){
        for(int i = 0;i<points.size();i++){
            Point current = points.get(i);
            odometry.nextPoint(current.getX(),current.getY(),current.getPower());
            if(finished[i]) continue;
            else{
                if(( (odometry.frontR.getCurrentPosition()>odometry.y_cnts) || (odometry.rearL.getCurrentPosition()>odometry.y_cnts) ) && (odometry.rearR.getCurrentPosition()>odometry.x_cnts)){
                    odometry.stopChassisMotor();
                    finished[i]=true;
                    odometry.setChassisMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    continue;
                }
                break;
            }
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
