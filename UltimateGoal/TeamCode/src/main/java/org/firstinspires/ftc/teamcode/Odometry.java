package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;



public class Odometry{

    /**
     * INIT STEPS
     **/

    OpMode opMode;
    ChassisComponent chassisComp;
    //constants
    double wheel_diameter = 10; //cm
    double cntsPerRotation = 3440;
    double cntsPerCm = (1/(Math.PI*wheel_diameter))*cntsPerRotation;

    // Odometry position variables
    double global_X;
    double global_Y;
    double init_X;
    double init_Y;
    double last_X;
    double last_Y;

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

    public void nextPoint(double x, double y,double power){
        double frontR = chassisComp.topr.getCurrentPosition();
        double frontL = chassisComp.topl.getCurrentPosition();
        global_Y = init_Y + (frontR/cntsPerCm);
        global_X = init_X + (frontL/cntsPerCm);

        opMode.telemetry.addData("global_Y: ", global_Y);
        opMode.telemetry.addData("global_X: ", global_X);
        opMode.telemetry.addData("target_x: ", x);
        opMode.telemetry.addData("target_y: ", y);
        opMode.telemetry.update();
        chassisComp.fieldCentricDrive(x-global_X,y-global_Y,true);

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

        //double xDelta = Math.abs(global_X-last_X);
        //double yDelta = Math.abs(global_Y-last_Y);
        double current_mag = Math.hypot(global_Y-last_Y,global_X-last_X);
        double target_mag = Math.hypot(x-last_X,y-last_Y);
        opMode.telemetry.addData("current - mag",current_mag);
        opMode.telemetry.addData("target - mag",target_mag);
        return (current_mag>=target_mag);

    }
}