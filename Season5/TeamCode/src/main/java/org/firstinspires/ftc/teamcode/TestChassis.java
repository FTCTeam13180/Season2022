package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.IntakeComponent;

@TeleOp(name = "TestChassis", group ="teleop")
public class TestChassis extends LinearOpMode {

    Odometry odometry;
    ChassisComponent chassisComponent;
    IntakeComponent intakeComp;
    public void runOpMode(){
        chassisComponent =new ChassisComponent(this);
        chassisComponent.init();
        intakeComp = new IntakeComponent(this);
        //odometry= new Odometry(this,chassisComp, intakeComp, 80,160);
        //odometry.init();

        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();

        while(opModeIsActive()){
            //odometry.nextPoint(180,160, 0.5);
            //telemetry.update();
            if (Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1
                    || (Math.abs(gamepad1.right_stick_x) > 0.1)) {
                double x = gamepad1.left_stick_x;
                double y = -gamepad1.left_stick_y; // note: joystick y is reversed.
                double turn = -gamepad1.right_stick_x; //for driver specifically arnav who has practiced the other way
                double power = Math.sqrt(x * x + y * y);
                power = (power > 0) ? power : Math.abs(turn);
                chassisComponent.fieldCentricDrive(x, y, power, false, turn);
            } else if (gamepad1.dpad_up) {
                chassisComponent.moveForward(0.5);
            } else if (gamepad1.dpad_down) {
                chassisComponent.moveBackward(0.5);
            } else {
                chassisComponent.stop();
            }

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