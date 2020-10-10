package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "TestChassis", group ="autonomousGroup1")
public class TestChassis extends LinearOpMode {
    DcMotor frontL;
    DcMotor frontR;
    DcMotor rearR;
    DcMotor rearL;
    DcMotor verticalLeft;
    DcMotor verticalRight;
    DcMotor horiz;
    double cntsPerRotation = 1440;
    double wheelDiameter = 3.5/2.54;
    @Override
    public void runOpMode(){

        frontL = hardwareMap.get(DcMotor.class, "Topl");
        frontR= hardwareMap.get(DcMotor.class, "Topr");
        rearL = hardwareMap.get(DcMotor.class, "Rearl");
        rearR = hardwareMap.get(DcMotor.class, "Rearr");

        frontR.setDirection(DcMotor.Direction.REVERSE);
        rearR.setDirection(DcMotor.Direction.REVERSE);

        frontL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        setChassisMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        int n = (int) ((72/(Math.PI*wheelDiameter))*cntsPerRotation);
        double delay_reduction = 1500;
        n-=delay_reduction;
        double percentAtSlowSpeed = 0.2;
        double power;
        double fastPower = 1.0;
        double slowPower = 0.7;
        double straightMargin = 125;
        boolean slow = true;
        double sideReduction = 0.90;
        int slowedSide = 0; //-1: /left, 0: even, 1: right
        setChassisMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ElapsedTime runtime = new ElapsedTime();

        waitForStart();
        runtime.reset();
        power = slowPower;
        setChassisPower(power);
        while(opModeIsActive()){
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
    }
    public void stopMotor(){
        rearL.setPower(0);
        rearR.setPower(0);
        frontR.setPower(0);
        frontL.setPower(0);


    }
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
