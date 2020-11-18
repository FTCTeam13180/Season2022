package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Yash Pradhan on 10/4/20
 */

// Not really needed - Rohan

public class ChassisComponent {
    public OpMode opMode;
    ChassisComponent(OpMode op) {
        opMode = op;
    }

    private DcMotor topl;
    private DcMotor topr;
    private DcMotor rearr;
    private DcMotor rearl;
    //no gear reduction ratio as of now
    double wheel_diameter = 9.60798; //cm
    double cntsPerRotation = 1440;
    double cntsPerCm = (1/(Math.PI*wheel_diameter))*cntsPerRotation;

    /// CR-1 START ///
    /// Remove all the following constants ///
   //private static final double COUNTS_PER_MOTOR_REV = 1120 ;    // eg: Andymark Motor Encoder
    //private static final double DRIVE_GEAR_REDUCTION = 0.776 ;     // This is < 1.0 if geared up
    //private static final double WHEEL_DIAMETER_CM = 10.16 ;
    //private static final double COUNTS_PER_CM  = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * 3.1415);
    //private static final double STRAIGHT_SLIPPAGE_CORRECTION = 96/97.5;
    /// CR-1 END ///

    public void initDriveHardwareMap(){
        topr = opMode.hardwareMap.dcMotor.get("TopR");
        topl = opMode.hardwareMap.dcMotor.get("TopL");
        rearr = opMode.hardwareMap.dcMotor.get("BackR");
        rearl = opMode.hardwareMap.dcMotor.get("BackL");

        /// CR-2 Start ///
        /// Remove all the RunMOde set calls. These are needed for autonomous mode so we should set them in Odometry code only. ///
        topr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        topr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        topl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        /// CR-2 End ///

        topr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rearr.setDirection(DcMotorSimple.Direction.REVERSE);
        topr.setDirection(DcMotorSimple.Direction.REVERSE);
        /// CR-3 Start ///
        /// We need to reverse direction only for right motors and not for left motor, remove the following line ///
        topl.setDirection(DcMotorSimple.Direction.REVERSE);
        /// CR-3 End ///

        opMode.telemetry.addData("Status", "Hardware Map Init Complete");
        opMode.telemetry.update();
    }

    /// CR-4 Start ///
    /// Remove this method, we won't need it ///
    public void move(double toprPower, double toplPower, double rearrPower, double rearlPower) {
        topr.setPower(toplPower);
        topl.setPower(toprPower);
        rearr.setPower(rearrPower);
        rearl.setPower(rearlPower);
    }
    /// CR-4 End ///

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

    public void mecanumDrive(double x,double y, double power_scale){
        double power = Math.sqrt(x * x + y * y);
        topr.setPower(power_scale*(y-x)/power);
        topl.setPower(power_scale*(x+y)/power);
        rearr.setPower(power_scale*(x+y)/power);
        rearl.setPower(power_scale*(y-x)/power);
    }

    public void logCurrentPosition () {
        opMode.telemetry.addData("CurrentPosition:", "topl=%7d", topl.getCurrentPosition());
    }

    public void stop() {
        /// CR-5: Instead of calling move, which we are removing, setPower explicitly for all 4 motors to 0 here. ///
        move(0, 0, 0, 0);
    }

    public boolean isBusy() {
        return (topl.isBusy() || topr.isBusy() || rearl.isBusy() || rearr.isBusy());
    }
}