package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "TestChassis", group ="teleop")
public class TestChassis extends LinearOpMode {
    DcMotor frontL;
    DcMotor frontR;
    DcMotor rearR;
    DcMotor rearL;
    DcMotor verticalLeft;
    DcMotor verticalRight;
    DcMotor horiz;
    double cntsPerRotation = 3440;
    double wheelDiameter = 3.7*2.54;
    double cntspercm = (1/(Math.PI*wheelDiameter))*cntsPerRotation;
    BNO055IMU imu;
    public void initIMU(){
        BNO055IMU.Parameters param = new BNO055IMU.Parameters();
        param.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
        param.gyroBandwidth = BNO055IMU.GyroBandwidth.HZ523;
        //param.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        param.calibrationDataFile = "BNO055IMUCalibration.json";
        param.loggingEnabled      = true;
        param.loggingTag          = "IMU";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(param);

        // Wait for gyroscope to be calibrated
        telemetry.addLine ("Starting Gyro Calibration");
        telemetry.update();
        while(!imu.isGyroCalibrated()) {}
        telemetry.addLine ("Completed Gyro Calibration");
        Orientation imu_orientation =
                imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS);
        double imu_radian = imu_orientation.firstAngle;
        telemetry.addData ("Initialized at angle: ", "%f", imu_radian);

        telemetry.update();
    }
    public void runOpMode(){
        initIMU();
        frontL = hardwareMap.get(DcMotor.class, "Topl");
        frontR= hardwareMap.get(DcMotor.class, "Topr");
        rearL = hardwareMap.get(DcMotor.class, "Rearl");
        rearR = hardwareMap.get(DcMotor.class, "Rearr");

        frontL.setDirection(DcMotor.Direction.REVERSE);
        rearL.setDirection(DcMotor.Direction.REVERSE);

        frontL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        int n = (int) (cntsPerRotation/(Math.PI*wheelDiameter));

        //double delay_reduction = 1500;
        //n-=delay_reduction;

        double percentAtSlowSpeed = 0.2;
        double power=0.3;
        double fastPower = 1.0;
        double slowPower = 0.7;
        double straightMargin = 125;
        boolean slow = true;
        double sideReduction = 0.90;
        int slowedSide = 0; //-1: /left, 0: even, 1: right
        setChassisMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setChassisMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();

        double curX = 45;
        double curY = 45;
        double targetX=180;
        double targetY=180;
        while(opModeIsActive()){
            double currentAngle = Math.toDegrees(normalizeIMU(imu.getAngularOrientation().firstAngle));
            telemetry.addData("curAngle",currentAngle);
            double mag = Math.hypot(targetX-curX,targetY-curY);
            double target = Math.toDegrees(normalizeTarget(targetY-curY,targetX-curX));
            telemetry.addData("Target: ",target);
            double delta = target - currentAngle;
            if(delta<0)delta+=2*Math.PI;
            delta = 90-delta;
            telemetry.addData("Delta degrees: ",delta);
            delta = Math.toRadians(delta);
            telemetry.addData("Delta radians: ",delta);
            double x = Math.cos(delta)*mag;
            double y = Math.sin(delta)*mag;
            double m = Math.max(x,y);
            double x_cnts = x*cntspercm;
            double y_cnts = y*cntspercm;

            x/=m;
            y/=m;
            telemetry.addData("Delta: ",Math.toDegrees(delta));
            telemetry.addData("x",x);
            telemetry.addData("y",y);
            telemetry.update();
            sleep(1000);

            double cap = Math.max(Math.abs(x+y),Math.abs(y-x));
            frontR.setPower(power*(((y-x)/cap)));
            frontL.setPower(power*((y+x)/cap));
            rearR.setPower(power*((y+x)/cap));
            rearL.setPower(power*((y-x)/cap));

            while((frontR.getCurrentPosition()<y_cnts || rearL.getCurrentPosition()<x_cnts)&&opModeIsActive()){
                telemetry.addData("mag",mag);
                telemetry.addData("targetTheta",delta);
                telemetry.addData("Ycnts",y_cnts);
                telemetry.addData("Xcnts",x_cnts);
                telemetry.addData("frontR",power*(((y-x)/cap)));
                telemetry.addData("frontL",power*((y+x)/cap));
                telemetry.addData("Right Encoder",frontR.getCurrentPosition());
                telemetry.addData("Left Encoder",rearL.getCurrentPosition());
                telemetry.addData("frontl Encoder",frontL.getCurrentPosition());
                telemetry.addData("rearr encoder",rearR.getCurrentPosition());
                telemetry.addData("Angle",Math.toDegrees(currentAngle));
                telemetry.update();
            }
            stopMotor();

        break;
        }
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

    public void stopMotor(){
        rearL.setPower(0);
        rearR.setPower(0);
        frontR.setPower(0);
        frontL.setPower(0);


    }
    /*
    public void straightLine(double cms, double power){
        setChassisMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        double r = frontR.getCurrentPosition();
        double l = rearL.getCurrentPosition();
        telemetry.addData("Right Encoder",r);
        telemetry.addData("Left Encoder",l);
        telemetry.addData("reduced side (L:-1, M:0, R:1): ", slowedSide);
        telemetry.addData("difference (r-l)",r-l);
        telemetry.addData("Target: ",n);
        telemetry.update();
        if(((r<percentAtSlowSpeed*n || l<percentAtSlowSpeed*n))&&slow){
            slow=false;
            power = fastPower;
        }

        if(l-r>straightMargin && slowedSide!=-1){
            setChassisPower(sideReduction * power, power);
            slowedSide = -1;
        }
        else if(r-l>straightMargin && slowedSide!=1){
            setChassisPower(power, sideReduction*power);
            slowedSide = 1;
        }
        else if ( (r-l<straightMargin) && ((l-r)<straightMargin) && slowedSide!=0){
            setChassisPower(power);
            slowedSide = 0;
        }
        if(r>=n || l>=n){
            stopMotor();
        }
    }
    */

    public void setChassisPower(double p){
        rearL.setPower(p);
        rearR.setPower(p);
        frontR.setPower(p);
        frontL.setPower(p);

    }
    public void setChassisPower(double l,double r){
        rearL.setPower(l);
        rearR.setPower(r);
        frontR.setPower(r);
        frontL.setPower(l);

    }
    public void setOdometryMode(DcMotor.RunMode r){
        verticalLeft.setMode(r);
        verticalRight.setMode(r);
        horiz.setMode(r);
    }
    public void setChassisMode(DcMotor.RunMode r){
        frontL.setMode(r);
        frontR.setMode(r);
        rearL.setMode(r);
        rearR.setMode(r);
    }
}


/***
TEST DIFF ENCODER DISTANCES VVV
 ***/

/*
            telemetry.addData("n: ",n);
            telemetry.update();
            if(gamepad2.dpad_up){
                telemetry.addLine("A");
                telemetry.update();
                setChassisPower(1.0);
                while(frontR.getCurrentPosition()<60*n){

                    telemetry.addData("dist ",frontR.getCurrentPosition());
                    telemetry.update();
                }
                    setChassisPower(0);
                    setChassisMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    setChassisMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
            if(gamepad2.dpad_right){
                telemetry.addData("Y",120*n);
                telemetry.addData("cur",frontR.getCurrentPosition());
                telemetry.update();
                setChassisPower(1.0);
                while(frontR.getCurrentPosition()<120*n){
                    telemetry.addData("dist ",frontR.getCurrentPosition());
                    telemetry.update();
                }
                setChassisPower(0);
                setChassisMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                setChassisMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            if(gamepad2.dpad_down){
                telemetry.addLine("X");
                telemetry.update();
                setChassisPower(1.0);
                while(frontR.getCurrentPosition()<180*n){
                    telemetry.addData("dist ",frontR.getCurrentPosition());
                    telemetry.update();
                }
                setChassisPower(0);
                setChassisMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                setChassisMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }

 */



/**
 * TEST IMU vv
 **/

/*
 /*
            double y = gamepad1.left_stick_y*-1;
            double x = gamepad1.left_stick_x;

            if(gamepad2.dpad_up){
                frontL.setPower(1.0);
                frontR.setPower(0.0);
                rearL.setPower(0.0);
                rearR.setPower(0.0);
            }
            else if(gamepad2.dpad_down){
                frontL.setPower(0.0);
                frontR.setPower(1.0);
                rearL.setPower(0.0);
                rearR.setPower(0.0);
            }
            else if(gamepad2.dpad_left){
                frontL.setPower(0.0);
                frontR.setPower(0.0);
                rearL.setPower(1.0);
                rearR.setPower(0.0);
            }
            else if(gamepad2.dpad_right){
                frontL.setPower(0.0);
                frontR.setPower(0.0);
                rearL.setPower(0.0);
                rearR.setPower(1.0);
            }
            if(gamepad2.b){
                frontL.setPower(0.0);
                frontR.setPower(0.0);
                rearL.setPower(0.0);
                rearR.setPower(0.0);
            }

            double currentAngle = normalizeIMU(imu.getAngularOrientation().firstAngle);
            if(gamepad1.right_bumper){setChassisPower(1,-1);}
            else if(gamepad1.left_bumper){setChassisPower(-1,1);}

            else if(Math.hypot(x,y)>0.1) {
                telemetry.addLine("In there ahaha");
                double mag = Math.hypot(x,y);

                double target = normalizeTarget(y,x);
                telemetry.addData("Target: ",Math.toDegrees(target));
                double delta = target - currentAngle;
                if(delta<0)delta+=2*Math.PI;
                telemetry.addData("Delta: ",Math.toDegrees(delta));

                delta = 90-delta;
                x = Math.cos(delta)*mag;
                y = Math.sin(delta)*mag;

                double cap = Math.max(Math.abs(x+y),Math.abs(y-x));

                frontR.setPower(power*(((y-x)/cap)));
                frontL.setPower(power*((y+x)/cap));
                rearR.setPower(power*((y+x)/cap));
                rearL.setPower(power*((y-x)/cap));

            }

 */
