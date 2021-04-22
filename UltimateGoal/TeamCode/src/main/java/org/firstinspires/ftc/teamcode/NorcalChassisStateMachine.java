package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.component.ChassisComponent;

public class NorcalChassisStateMachine implements BasicCommand {

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

    public NorcalChassisStateMachine(Odometry odometry, ChassisComponent chassisComponent , OpMode op) {
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
            //start 97, 45
//            spline = new Spline(new Waypoint(97, 60, 0.6 * power));
            spline = new Spline(new Waypoint(105,68,power));
            spline.add(new Waypoint(98, 90, power));
            spline.add(new Waypoint(89,113,power*0.7));

        }
        else{
            // starting position (80, 165)
            spline = new Spline(new Waypoint(90, 155, power));
            spline.add(new Waypoint(101, 157, power));
            spline.add(new Waypoint(115, 167, power));
            spline.add(new Waypoint(130, 197, power)); //shooting point
        }
    }

    public void moveToTargetZone(int numRings,boolean first){
        rings = numRings;
        if(first){
            if(rings==0){
                //A
                spline = new Spline(new Waypoint(73,202,power));
           //     spline.add(new Waypoint(94, 183, power));
             //   spline.add(new Waypoint(72, 194, power));
                spline.add(new Waypoint(56, 214, power));
            }
            else if(rings==1){
                //B
               // spline = new Spline(new Waypoint(120,180,power));

                spline = new Spline(new Waypoint(130,265,power));
                spline.add(new Waypoint(116,290,power));
            }
            else if(rings == 4){
                //C
                spline = new Spline(new Waypoint(74, 240, power));
//                spline.add(new Waypoint(85, 240, power));
//                spline.add(new Waypoint(75, 260, power));
                spline.add(new Waypoint(66, 285, power));
                spline.add(new Waypoint(60,348,power));

            }
        }
        else{

            if(rings==0){
                //A
              spline = new Spline(new Waypoint(80, 235, power));
                spline.add(new Waypoint(80,235,power));
            }
            else if(rings==1){
                //B
                spline = new Spline(new Waypoint(126,200,power));
                spline.add(new Waypoint(126, 240, power));
                spline.add(new Waypoint(126,273,power));
            }
            else if (rings == 4){
                //C
                spline = new Spline(new Waypoint(83,222,power));
//                spline.add(new Waypoint(93,263,power));
                spline.add(new Waypoint(74,335,0.7 * power));
            }
        }
    }

    public void moveToSecondWobble(){
        if(rings == 0){
            //starts from 60, 240
            spline = new Spline(new Waypoint(96,196,power));
            spline.add(new Waypoint(117,160,power));
            spline.add(new Waypoint(121, 124, power));
            spline.add(new Waypoint(118, 90, power));
            spline.add(new Waypoint(90,64,power));
        }
        if(rings == 1){
            //starts from 116, 290
            spline = new Spline(new Waypoint(130,232,power));
            spline.add(new Waypoint(136,185,power));
            spline.add(new Waypoint(131,128,power));
            spline.add(new Waypoint(118,90,power));
            spline.add(new Waypoint(90,64,power));

        }
        else if(rings == 4){
            //starts from 60, 348
            spline = new Spline(new Waypoint(93,275,power));
            spline.add(new Waypoint(106,206,power));
            //spline.add(new Waypoint(111,156,power));
            spline.add(new Waypoint(111, 121, power));
            spline.add(new Waypoint(107, 93, 0.75 * power));
            spline.add(new Waypoint(90,64, 0.5 * power));
        }
    }

    public void pickUpRingsMovement(int instance){
        if (instance == 1) {
//            spline = new Spline(new Waypoint(95,84,power));
//            spline.add(new Waypoint(89, 126, power));
            spline = new Spline(new Waypoint(89,140,power));

        }
        if (instance == 2) {
            spline = new Spline(new Waypoint(89, 175, power));
        }
    }

    public void ParkAtLaunchLine(){
        if (rings == 4){
            //spline = new Spline(new Waypoint(56, 214, power));
            spline = new Spline(new Waypoint(106, 290, power));
            spline.add(new Waypoint(134, 224, power));
//            spline.add(new Waypoint(72, 194, power));
//            spline.add(new Waypoint(94, 183, power));
//            spline.add(new Waypoint(115,185,power));
//            spline.add(new Waypoint(130, 197, power));



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
        chassisComp.spinToXDegree (0, 0.05, 0.1);

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
            chassisComp.spinToXDegree(0, 0.03, 0.1);
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
            chassisComp.spinToXDegree(0, 0.03, 0.1);
            spline.setCorrected();
        } else {
            // Correction is not done yet. Continue correction
            Waypoint destination = spline.getDestination();
            odometry.nextPointRampdown(destination.getX(),destination.getY(), 0.5 * destination.getPower());
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
//                    chassisComp.spinToXDegree(0, 0.03, 0.3);
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
        if(rings == 0){
            spline = new Spline(new Waypoint(89, 197, power));
        }
        else if(rings == 1){
           spline = new Spline(new Waypoint(89, 197, power));
        }
        else if(rings == 4){
            spline = new Spline(new Waypoint(89, 197, power));
        }
       // spline = new Spline(new Waypoint(98, 60, power));
       // spline.add(new Waypoint(150,120,power));
       // spline.add(new Waypoint(174,200,power));

        //first powershot position: (168, 196)
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