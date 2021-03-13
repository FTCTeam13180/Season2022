package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

    private Telemetry.Item log_target_X_Y;

    public ChassisStateMachine(Odometry odometry,ChassisComponent chassisComponent ,OpMode op) {
        this.odometry=odometry;
        this.chassisComp=chassisComponent;
        this.op = op;
        log_target_X_Y = op.telemetry.addData("target_X_Y:", "(0, 0)");
    }

    public void setTimeoutMs(double ms){
        timeoutMs = ms;
    }
    public State getState(){
        return state;
    }
    public void setState(State st){ state = st; }
    public void smoothSpline(int n){
        int distance = 60;

        //square
        spline = new Spline(new Waypoint(distance*0,distance*n,power));
        //  spline.add(new Waypoint(distance*n, distance*0, power ));
        //spline.add(new Waypoint(distance*0, -distance*n, power ));
        //spline.add(new Waypoint(-distance*n, distance*0, power ));
    }



/*autonomous sequence*/
    public void moveToGoal(int instance){
        if (instance == 1) {
            //op.telemetry.addData("power", power);
            //op.telemetry.update();
            spline = new Spline(new Waypoint(120, 70, power));
            spline.add(new Waypoint(130, 90, power));
            spline.add(new Waypoint(135, 120, power));
            spline.add(new Waypoint(135, 160, power));

            //hack to avoid rampdown and save time
            spline.add(new Waypoint(130, 197, power));
        }
        else{
            spline = new Spline(new Waypoint(125, 195, power));
            spline.add(new Waypoint(130, 197, power)); //shooting point (130, 105)
        }
    }

    public void moveToTargetZone(int numRings,boolean first){
        rings = numRings;
        if(first){
            if(rings==0){
                //A
                spline = new Spline(new Waypoint(115,185,power));
                spline.add(new Waypoint(94, 183, power));
                spline.add(new Waypoint(72, 194, power));
                spline.add(new Waypoint(56, 214, power));
            }
            else if(rings==1){
                //B
               // spline = new Spline(new Waypoint(120,180,power));
                spline = new Spline(new Waypoint(130,300,power));
            }
            else if(rings == 4){
                //C
                spline = new Spline(new Waypoint(60,355,power));
                spline.add(new Waypoint(60,355,power));
            }
        }
        else{

            if(rings==0){
                //A
              spline = new Spline(new Waypoint(75, 235, power));
                spline.add(new Waypoint(80,235,power));
            }
            else if(rings==1){
                //B
                spline = new Spline(new Waypoint(126,200,power));
                spline.add(new Waypoint(126, 260, power));
                spline.add(new Waypoint(126, 270, power));
                spline.add(new Waypoint(126,273,power));
            }
            else if (rings == 4){
                //C
                spline = new Spline(new Waypoint(70,345,power));
                spline.add(new Waypoint(80,345,power));
            }
        }
    }

    public void moveToSecondWobble(){
        if(rings == 0){
            //starts from 60, 240
            spline = new Spline(new Waypoint(77,184,power));
            spline.add(new Waypoint(96,157,power));
            spline.add(new Waypoint(102, 129, power));
            spline.add(new Waypoint(102, 106, power));
            spline.add(new Waypoint(100, 89, power));
            spline.add(new Waypoint(95.5,64,power));
            //spline.add(new Waypoint(88,68,power));
        }
        if(rings == 1){
            //starts from 130, 300
          //  spline = new Spline(new Waypoint(147,274,0.7));
           // spline.add(new Waypoint(158,237,0.7));
           // spline.add(new Waypoint(160,197,0.7));
           // spline.add(new Waypoint(157,165,0.7));
           // spline.add(new Waypoint(149,135,0.7));
           // spline.add(new Waypoint(138,110,0.7));
           // spline.add(new Waypoint(120,88,0.7));
           // spline.add(new Waypoint( 95.5,64,0.7));
            /*
            spline = new Spline(new Waypoint(157,275,0.4));
            spline.add(new Waypoint(180,244,0.4));
            spline.add(new Waypoint(191,208,0.4));
            spline.add(new Waypoint(194,179,0.4));
            spline.add(new Waypoint(189,147,0.4));
            spline.add(new Waypoint(177,124,0.4));
            spline.add(new Waypoint(156,100,0.4));
            spline.add(new Waypoint(136,83,0.4));
            spline.add(new Waypoint(114,72,0.4));
            spline.add(new Waypoint( 95.5,64,0.4));
             */
            spline = new Spline(new Waypoint(130, 290, .5));

            spline.add(new Waypoint(130, 70, power ));
            spline.add(new Waypoint(130, 64, power ));



            spline.add(new Waypoint(85, 64, power));

        }
        else if(rings == 4){
            //starts from 60, 340
            spline = new Spline(new Waypoint(100,290,power));
            spline.add(new Waypoint(130,230,power));
            spline.add(new Waypoint(150,170,power));
            spline.add(new Waypoint(145,150,power));
            spline.add(new Waypoint(135,130,power));
            spline.add(new Waypoint(115,100,power));
            spline.add(new Waypoint(105,90,power));
            spline.add(new Waypoint(95.5,64,power));

        }
    }

    public void pickUpRingsMovement(){
        if (rings == 1 || rings == 0) {
            spline = new Spline(new Waypoint(95,84,power));
            spline.add(new Waypoint(80, 155, power));
            spline.add(new Waypoint(80, 180, power));
        }
        if (rings == 4) {
            spline = new Spline(new Waypoint(95, 84, power));
            spline.add(new Waypoint(80, 155, power));
            spline.add(new Waypoint(80, 180, power));
            spline.add(new Waypoint(80, 150, power));
        }
    }

    public void ParkAtLaunchLine(){
        if (rings == 4){
            spline = new Spline(new Waypoint(56, 214, power));
            spline.add(new Waypoint(72, 194, power));
            spline.add(new Waypoint(94, 183, power));
            spline.add(new Waypoint(115,185,power));
            spline.add(new Waypoint(130, 197, power));


        }
        else if(rings == 0){
            //drop at 80, 235
            spline = new Spline(new Waypoint(80, 224, power));
            spline.add(new Waypoint(87, 211, power));
            spline.add(new Waypoint(99, 208, power));
            spline.add(new Waypoint(113,210,power));
            spline.add(new Waypoint(126, 214, power));
            spline.add(new Waypoint(134, 224, power));
        }
        else{
            spline = new Spline(new Waypoint(126, 240, power));
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
        log_target_X_Y.setValue("(%.1f, %.1f)", targetPoint.getX(), targetPoint.getY());

        // Fix Robot Angle
//        chassisComp.spinToXDegree (0, 0.03);

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
            chassisComp.spinToXDegree(0, 0.3);
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
            chassisComp.spinToXDegree(0, 0.3);
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
                    chassisComp.spinToXDegree(0, 0.3);
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
    public void moveToWobble(){
        spline = new Spline(new Waypoint(60,60,power));
    }
    public void moveToRings(){
        spline = new Spline(new Waypoint(90,120,power));
    }

}