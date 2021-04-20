package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.IntakeComponent;


public class Odometry{

    /**
     * INIT STEPS
     **/

    OpMode opMode;
    ChassisComponent chassisComp;
    IntakeComponent intakeComp;

    public DcMotor x_encoder;
    public DcMotor y_right_encoder;
    public DcMotor y_left_encoder;

    //constants
    double wheel_diameter = 3.8; //cm
    double cntsPerRotation = 1440;
    double cntsPerCm = (1/(Math.PI*wheel_diameter))*cntsPerRotation;

    // Odometry position variables
    double currentXCounts;
    double currentYRightCounts;
    double currentYLeftCounts;
    double global_X;
    double global_Y;
    double init_X;
    double init_Y;
    double last_X;
    double last_Y;
    double rampdown_cap = 60;
    double ACCURACY_THRESHOLD = 7.5; //cm
    double ACCURACY_THRESHOLD_RAMPDOWN= 3.5; //cm
    double r = 3;

    // Telemetry Items
    private Telemetry.Item log_global_X_Y;


    double y_cnts;
    double x_cnts;

    Odometry (OpMode op, ChassisComponent chassisComponent, IntakeComponent intakeComponent, double i_x, double i_y){
        opMode=op;
        global_X = this.init_X = last_X = i_x;
        global_Y = this.init_Y = last_Y = i_y;
        chassisComp= chassisComponent;
        intakeComp = intakeComponent;
        log_global_X_Y = opMode.telemetry.addData("global_X_Y:", "(%.1f, %.1f)", global_X, global_Y);
    }

    public void init(){
        x_encoder = opMode.hardwareMap.dcMotor.get("x_encoder");
        y_right_encoder = opMode.hardwareMap.dcMotor.get("y_right_encoder");
        y_left_encoder = intakeComp.intake; // We are using intake motors encoder port for this encoder

        x_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        y_right_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        y_left_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        x_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        y_right_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        y_left_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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
        readCurrentPosition();
//        double frontR = getCurrentY();
//        double frontL = getCurrentX();
//        global_Y = init_Y + (frontR/cntsPerCm);
//        global_X = init_X + (frontL/cntsPerCm);
        double cm_to_target = Math.hypot(y-global_Y, x-global_X);

        double rampdown_factor;
        //double rampstart_factor;
        //rampdown_factor = .7 * cm_to_target / rampdown_cap;
        rampdown_factor = Math.log(cm_to_target/rampdown_cap) + 1;

        //rampstart_factor =
        //rampdown_factor = Math.log(cm_to_target/rampdown_cap) + 1;
        rampdown_factor = Math.min(rampdown_factor, 1);
        rampdown_factor = Math.max(rampdown_factor, .4);
        //opMode.telemetry.addData("global_Y: ", global_Y);
        //opMode.telemetry.addData("global_X: ", global_X);
        //opMode.telemetry.addData("target_x: ", x);
        //opMode.telemetry.addData("target_y: ", y);
        //opMode.telemetry.update();
            chassisComp.mecanumDriveAuto(x-global_X,y-global_Y,power * rampdown_factor);
    }
    public void nextPoint(double x, double y, double power){
        readCurrentPosition();
//        double frontR = getCurrentY();
//        double frontL = getCurrentX();
//        global_Y = init_Y + (frontR/cntsPerCm);
//        global_X = init_X + (frontL/cntsPerCm);
        //opMode.telemetry.addData("global_Y: ", global_Y);
        //opMode.telemetry.addData("global_X: ", global_X);
        //opMode.telemetry.addData("target_x: ", x);
        //opMode.telemetry.addData("target_y: ", y);
        //opMode.telemetry.update();
        chassisComp.mecanumDriveAuto(x-global_X,y-global_Y, power);
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
    public void nextPointv2(double start_x, double start_y, double last_x, double last_y, double power)
    {
        double slope = (last_y - start_y) / (last_x - start_x);
        double c = start_y - start_x * slope;
        nextPointCalled(slope, c, last_x, last_y, power);
    }
    public void nextPointCalled(double m, double c, double last_x, double last_y, double power){
        readCurrentPosition();
        double x1;
        double y1;
        double x2;
        double y2;
        double x_to_put;
        double y_to_put;
        double a = global_X;
        double b = global_Y;
        double constant = c - b;
        double d = m * m + 1;
        double e = 2 * constant * m - 2 * a;
        double f  = a * a + constant * constant - r * r;
        //while ((e * e - 4 * d * f) < 0)
        //    r += .5;
        //while ((e * e - 4 * d * f) >= 0)
        //    r -= .1;
        //r += .1;
        x1 = (-e + Math.sqrt(e*e - 4*d*f)) / (2*d);
        y1 = m*x1 + c;
        x2 = (-e - Math.sqrt(e*e - 4*d*f)) / (2*d);
        y2 = m*x2 + c;
        double distance1 = Math.sqrt(Math.pow((last_x - x1), 2) + Math.pow((last_y - y1), 2));
        double distance2 = Math.sqrt(Math.pow((last_x - x2), 2) + Math.pow((last_y - y2), 2));
        if (distance1 <= distance2){
            x_to_put = x1;
            y_to_put = y1;
        }
        else {
            x_to_put = x2;
            y_to_put = y2;
        }
        chassisComp.mecanumDriveAuto(x_to_put-a,y_to_put-b, power);
        opMode.telemetry.addData("x_to_put, y_to_put, x_to_put-a, y_to_put-b, m, c:", "(%.1f, %.1f, %.1f, %.1f, %.1f, %.1f)", x_to_put, y_to_put, x_to_put-a, y_to_put - b, m, c);
    }
    public boolean isFinished(double x, double y) {
        readCurrentPosition();
//        global_X = init_X + getCurrentX()/cntsPerCm;
//        global_Y= init_Y + getCurrentY()/cntsPerCm;

       if (Math.hypot(y-global_Y, x-global_X) <= ACCURACY_THRESHOLD)
           return true;
       return false;
    }

    public boolean isFinishedRampdown(double x, double y){
        readCurrentPosition();
//        global_X = init_X + getCurrentX()/cntsPerCm;
//        global_Y= init_Y + getCurrentY()/cntsPerCm;

        if (Math.hypot(y-global_Y, x-global_X) <= ACCURACY_THRESHOLD_RAMPDOWN)
            return true;
        return false;
    }

    public void readCurrentPosition() {
        currentXCounts = x_encoder.getCurrentPosition();
        currentYRightCounts = y_right_encoder.getCurrentPosition();
        // y Left encoder is installed in opposite direction so negate the value
        currentYLeftCounts = - (y_left_encoder.getCurrentPosition());

        global_X = init_X + currentXCounts/cntsPerCm;
        global_Y= init_Y + (currentYRightCounts+currentYLeftCounts)/(2*cntsPerCm);
        log_global_X_Y.setValue("(%.1f, %.1f)", global_X, global_Y);
    }

    public double getCurrentX(){
        return x_encoder.getCurrentPosition();
    }

    public double getCurrentY(){
        // y Left encoder is installed in opposite direction so negate the value
        return (y_right_encoder.getCurrentPosition() - y_left_encoder.getCurrentPosition())/2;
    }


    public void updateLog(String calledFrom)
    {
        /*
        opMode.telemetry.addData("CalledFrom: ", calledFrom);
        opMode.telemetry.addData("global_X: ", global_X);
        opMode.telemetry.addData("global_Y: ", global_Y);
        opMode.telemetry.addData("XCounts:",currentXCounts);
        opMode.telemetry.addData("YRightCounts:",currentYRightCounts);
        opMode.telemetry.addData("YLeftCounts:",currentYLeftCounts);

         */
    }
    public void displayPosition(){
        opMode.telemetry.addLine("x: " + currentXCounts);
        opMode.telemetry.addLine("yRight: " + currentYRightCounts);
        opMode.telemetry.addLine("yLeft: " + currentYLeftCounts);
        opMode.telemetry.update();
    }
}