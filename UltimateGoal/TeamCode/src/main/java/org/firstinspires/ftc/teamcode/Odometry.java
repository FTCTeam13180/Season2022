package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;



public class Odometry{

    /**
     * INIT STEPS
     **/

    OpMode opMode;
    BNO055IMU IMU;

    //constants
    double wheel_diameter = 9.60798; //cm
    double cntsPerRotation = 3440;
    double cntsPerCm = (1/(Math.PI*wheel_diameter))*cntsPerRotation;

    // Odometry position variables
    double global_X;
    double global_Y;
    double start_X;
    double start_Y;
    double y_cnts;
    double x_cnts;



    DcMotor frontR, rearR, rearL, frontL;
    Odometry (OpMode op, double init_x, double init_y){
        opMode=op;
        global_X=start_X=init_x;
        global_Y=start_Y=init_y;
    }
    public void init(){
        initDriveHardwareMap();
        initIMU();
    }
    public void initDriveHardwareMap(){
        frontR = opMode.hardwareMap.dcMotor.get("Topr");
        frontL = opMode.hardwareMap.dcMotor.get("Topl");
        rearR = opMode.hardwareMap.dcMotor.get("Rearr");
        rearL = opMode.hardwareMap.dcMotor.get("Rearl");

        frontR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rearL.setDirection(DcMotorSimple.Direction.REVERSE);
        frontL.setDirection(DcMotorSimple.Direction.REVERSE);

        opMode.telemetry.addData("Status", "Hardware Map Init Complete");
        opMode.telemetry.update();
    }
    public void initIMU(){
        BNO055IMU.Parameters param = new BNO055IMU.Parameters();
        param.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
        param.gyroBandwidth = BNO055IMU.GyroBandwidth.HZ523;
        //param.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        param.calibrationDataFile = "BNO055IMUCalibration.json";
        param.loggingEnabled      = true;
        param.loggingTag          = "IMU";
        IMU = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        IMU.initialize(param);

        // Wait for gyroscope to be calibrated
        opMode.telemetry.addLine ("Starting Gyro Calibration");
        opMode.telemetry.update();
        while(!IMU.isGyroCalibrated()) {}
        opMode.telemetry.addLine ("Completed Gyro Calibration");
        Orientation imu_orientation =
                IMU.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS);
        double imu_radian = imu_orientation.firstAngle;
        opMode.telemetry.addData ("Initialized at angle: ", "%f", imu_radian);

        opMode.telemetry.update();
    }
    //______________________________________________________________________________________________


    /**
    * MOTION METHODS BELOW
    **/


    public void moveTo(double theta, double magnitude, double power){
        opMode.telemetry.addLine("MoveTo");
        opMode.telemetry.addData("theta",theta);
        opMode.telemetry.addData("mag",magnitude);
        opMode.telemetry.addData("power",power);
        theta = Math.toRadians(theta);
        double x = Math.cos(theta)*magnitude;
        double y = Math.sin(theta)*magnitude;

        double cap = Math.max(Math.abs(x+y),Math.abs(y-x));
        opMode.telemetry.addData("frontR_speed: ", power*(((y-x)/cap)));
        opMode.telemetry.addData("frontL_speed: ", power*(((y+x)/cap)));
        opMode.telemetry.update();
        x_cnts = x*cntsPerCm;
        y_cnts = y*cntsPerCm;


        /*

        frontR: right encoder
        rearL: left encoder
        rearR: back encoder

         */

        frontR.setPower(power*(((y-x)/cap)));
        frontL.setPower(power*((y+x)/cap));
        rearR.setPower(power*((y+x)/cap));
        rearL.setPower(power*((y-x)/cap));

        //while( !( (frontR.getCurrentPosition()>Y_cnts || rearL.getCurrentPosition()>Y_cnts) && rearR.getCurrentPosition()>X_cnts) ){}
        //stopChassisMotor();
        //setChassisMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setChassisPower(double p){
        frontL.setPower(p);
        frontR.setPower(p);
        rearL.setPower(p);
        rearR.setPower(p);

    }
    public void setChassisPower(double l,double r){
        frontL.setPower(l);
        frontR.setPower(r);
        rearL.setPower(l);
        rearR.setPower(r);
    }

    public void stopChassisMotor(){
        frontL.setPower(0);
        frontR.setPower(0);
        rearL.setPower(0);
        rearR.setPower(0);
    }
    public void setChassisMode(DcMotor.RunMode r){
        frontL.setMode(r);
        frontR.setMode(r);
        rearL.setMode(r);
        rearR.setMode(r);
    }
    public void setZeroPower(){
        frontL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    public void nextPoint(double x, double y,double power){

        global_Y = start_Y + frontR.getCurrentPosition()/cntsPerCm;
        global_X = start_X + rearR.getCurrentPosition()/cntsPerCm;

        opMode.telemetry.addData("global_Y: ", global_Y);
        opMode.telemetry.addData("global_X: ", global_X);
        opMode.telemetry.addData("target_x: ", x);
        opMode.telemetry.addData("target_y: ", y);


        double mag = Math.hypot(global_X-x,global_Y-y);
        double currentAngle = normalizeIMU(IMU.getAngularOrientation().firstAngle);
        double target = Math.toDegrees(normalizeTarget(global_Y-y,global_X-x));
        double delta = target - currentAngle;

        if(delta<0)delta+=2*Math.PI;
        delta = 90-delta;
        opMode.telemetry.addData("R: ", mag);
        opMode.telemetry.addData("theta: ", delta);
        opMode.telemetry.update();
        moveTo(delta, mag,power);
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





}
