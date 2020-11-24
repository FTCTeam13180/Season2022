package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Yash Pradhan on 10/4/20
 */

// Not really needed - Rohan

public class ChassisComponent {
    private OpMode opMode;

    private DcMotor topl;
    private DcMotor topr;
    private DcMotor rearr;
    private DcMotor rearl;
    //no gear reduction ratio as of now
    final static double power_scale = 0;
    double wheel_diameter = 9.60798; //cm
    double cntsPerRotation = 1440;
    double cntsPerCm = (1/(Math.PI*wheel_diameter))*cntsPerRotation;

    ChassisComponent(OpMode op) {
        opMode = op;
    }

    public void init(){
        topr = opMode.hardwareMap.dcMotor.get("TopR");
        topl = opMode.hardwareMap.dcMotor.get("TopL");
        rearr = opMode.hardwareMap.dcMotor.get("BackR");
        rearl = opMode.hardwareMap.dcMotor.get("BackL");


        topr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rearr.setDirection(DcMotorSimple.Direction.REVERSE);
        topr.setDirection(DcMotorSimple.Direction.REVERSE);

        opMode.telemetry.addData("Chassis Component", "Initialized");
        opMode.telemetry.update();
    }


    public void setRunMode(DcMotor.RunMode runMode) {
        topl.setMode(runMode);
        topr.setMode(runMode);
        rearl.setMode(runMode);
        rearr.setMode(runMode);
    }

    public void setTargetPosition(double cms) {
        topl.setTargetPosition((int) (topl.getCurrentPosition() + (cms * cntsPerCm)));
        topr.setTargetPosition((int) (topr.getCurrentPosition() + (cms * cntsPerCm)));
        rearl.setTargetPosition((int) (rearl.getCurrentPosition() + (cms * cntsPerCm)));
        rearr.setTargetPosition((int) (rearr.getCurrentPosition() + (cms * cntsPerCm)));
    }

    public void mecanumDrive(double x, double y){
        double power = Math.sqrt(x * x + y * y);
        topr.setPower(power_scale*(y-x)/power);
        topl.setPower(power_scale*(x+y)/power);
        rearr.setPower(power_scale*(x+y)/power);
        rearl.setPower(power_scale*(y-x)/power);
    }

    public void moveForward(double power){
        mecanumDrive(0, power);
    }

    public void moveBackward(double power){
        mecanumDrive(0, -power);
    }

    public void shiftRight(double power){
        mecanumDrive(power, 0);
    }

    public void shiftLeft(double power){
        mecanumDrive(-power, 0);
    }

    public void spinRight(double power){
        topr.setPower(-Math.abs(power));
        rearr.setPower(-Math.abs(power));
        topl.setPower(Math.abs(power));
        rearl.setPower(Math.abs(power));
    }

    public void spinLeft(double power){
        topr.setPower(Math.abs(power));
        rearr.setPower(Math.abs(power));
        topl.setPower(-Math.abs(power));
        rearl.setPower(-Math.abs(power));
    }

    public void logCurrentPosition () {
        opMode.telemetry.addData("CurrentPosition:", "topl=%7d", topl.getCurrentPosition());
    }

    public void stop() {
        topr.setPower(0);
        topl.setPower(0);
        rearr.setPower(0);
        rearl.setPower(0);
    }

    public boolean isBusy() {
        return (topl.isBusy() || topr.isBusy() || rearl.isBusy() || rearr.isBusy());
    }
}