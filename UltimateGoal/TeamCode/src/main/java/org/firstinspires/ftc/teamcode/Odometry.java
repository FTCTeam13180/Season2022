package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;


public class Odometry{

    /**
     * INIT STEPS
     **/

    OpMode opMode;
    ChassisComponent chassisComp;
    //constants
    double wheel_diameter = 3.8; //cm
    double cntsPerRotation = 1440;
    double cntsPerCm = (1/(Math.PI*wheel_diameter))*cntsPerRotation;

    // Odometry position variables
    double global_X;
    double global_Y;
    double init_X;
    double init_Y;
    double last_X;
    double last_Y;
    double rampdown_cap = 60;
    double ACCURACY_THRESHOLD = 10; //cm
    double ACCURACY_THRESHOLD_RAMPDOWN= 2.5; //cm


    double y_cnts;
    double x_cnts;



      Odometry (OpMode op, ChassisComponent chassisComponent,double i_x, double i_y){
        opMode=op;
        global_X = this.init_X = last_X = i_x;
        global_Y = this.init_Y = last_Y = i_y;
        chassisComp= chassisComponent;
    }
    //______________________________________________________________________________________________


    /**
     * MOTION METHODS BELOW
     **/




    /**
     * Creates vector by converting current & target positions into polar coordinates accounting for angle with origin at current position
     * Output is resultant polar coordinate (r,theta) which is inputted into MoveTo() which executes motion
     **/

    public void nextPointRampdown(double x, double y, double power){
        double frontR = chassisComp.topr.getCurrentPosition();
        double frontL = getCurrentX();
        global_Y = init_Y + (frontR/cntsPerCm);
        global_X = init_X + (frontL/cntsPerCm);
        double cm_to_target = Math.hypot(y-global_Y, x-global_X);

        double rampdown_factor;
        //double rampstart_factor;
        rampdown_factor = .5 * cm_to_target / rampdown_cap;
        //rampstart_factor =
        //rampdown_factor = Math.log(cm_to_target/rampdown_cap) + 1;
        rampdown_factor = Math.min(rampdown_factor, 1);
        rampdown_factor = Math.max(rampdown_factor, .4);
        //opMode.telemetry.addData("global_Y: ", global_Y);
        //opMode.telemetry.addData("global_X: ", global_X);
        //opMode.telemetry.addData("target_x: ", x);
        //opMode.telemetry.addData("target_y: ", y);
        //opMode.telemetry.update();
            chassisComp.mecanumDrive(x-global_X,y-global_Y, 0,power * rampdown_factor, true);
    }
    public void nextPoint(double x, double y, double power){
        double frontR = chassisComp.topr.getCurrentPosition();
        double frontL = getCurrentX();
        global_Y = init_Y + (frontR/cntsPerCm);
        global_X = init_X + (frontL/cntsPerCm);
        //opMode.telemetry.addData("global_Y: ", global_Y);
        //opMode.telemetry.addData("global_X: ", global_X);
        //opMode.telemetry.addData("target_x: ", x);
        //opMode.telemetry.addData("target_y: ", y);
        //opMode.telemetry.update();
        chassisComp.mecanumDrive(x-global_X,y-global_Y, 0, power, true);
    }

    public static double normalizeTarget(double y, double x){
        double delta = Math.atan2(y, x);
        if(delta<0) delta+= 2*Math.PI;
        delta = 2.5*Math.PI - delta;
        if(delta>2*Math.PI) delta -=2*Math.PI;
        return delta;
    }
    public static double normalizeIMU(double theta){
        theta =-theta;
        return (theta<0)? 2*Math.PI+theta: theta;
    }

    public boolean isFinished(double x, double y){
       global_X = init_X + getCurrentX()/cntsPerCm;
       global_Y= init_Y + chassisComp.topr.getCurrentPosition()/cntsPerCm;

       if (Math.hypot(y-global_Y, x-global_X) <= ACCURACY_THRESHOLD)
           return true;
       return false;
    }

    public boolean isFinishedRampdown(double x, double y){
        global_X = init_X + getCurrentX()/cntsPerCm;
        global_Y= init_Y + chassisComp.topr.getCurrentPosition()/cntsPerCm;

        if (Math.hypot(y-global_Y, x-global_X) <= ACCURACY_THRESHOLD_RAMPDOWN)
            return true;
        return false;
    }

    public double getCurrentX(){
        return chassisComp.topl.getCurrentPosition();
    }
    public double getCurrentY(){
        double yRight = chassisComp.topr.getCurrentPosition();

        double yLeft = chassisComp.rearl.getCurrentPosition();

        return (yRight + yLeft)/2;
    }
    public void updateLog(String calledFrom)
    {
        double frontR = chassisComp.topr.getCurrentPosition();
        double frontL = getCurrentX();
        global_Y = init_Y + (frontR/cntsPerCm);
        global_X = init_X + (frontL/cntsPerCm);
        opMode.telemetry.addData("CalledFrom: ", calledFrom);
        opMode.telemetry.addData("global_Y: ", global_Y);
        opMode.telemetry.addData("global_X: ", global_X);
        opMode.telemetry.addData("counts frontL:",frontR);
        opMode.telemetry.addData("counts frontR:",frontL);
    }
    public void displayPosition(){
        opMode.telemetry.addLine("yRight: " + chassisComp.topr.getCurrentPosition());
        opMode.telemetry.addLine("yLeft: " + chassisComp.rearl.getCurrentPosition());
        opMode.telemetry.addLine("x: " + getCurrentX());
        opMode.telemetry.update();
    }
}