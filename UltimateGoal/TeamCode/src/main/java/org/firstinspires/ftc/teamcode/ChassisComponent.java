package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Yash Pradhan on 10/4/20
 */

// Not really needed - Rohan

public class ChassisComponent {
    private OpMode opMode;

    public DcMotor topl;
    public DcMotor topr;
    public DcMotor rearr;
    public DcMotor rearl;
    BNO055IMU IMU;
    //no gear reduction ratio as of now
    final static double power_scale = 1.0;
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

        topr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        topr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        topl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        topr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        topl.setDirection(DcMotorSimple.Direction.REVERSE);
        rearl.setDirection(DcMotorSimple.Direction.REVERSE);

        opMode.telemetry.addData("Chassis Component", "Initialized");
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
/*        Orientation imu_orientation =
                IMU.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        double imu_radian = imu_orientation.firstAngle;
        opMode.telemetry.addData ("Initialized at angle: ", "%f", imu_radian);
*/
        //opMode.telemetry.update();
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

    public void mecanumDrive(double x, double y, boolean auto){
        // need to roundoff near 1.0 values to avoid unnecessary veering
        if(!auto) {
            if (x > 0.9) {
                x = 1.0;
                y = 0;
            } else if (x < -0.9) {
                x = -1.0;
                y = 0;
            } else if (y > 0.9) {
                y = 1.0;
                x = 0;
            } else if (y < -0.9) {
                y = -1.0;
                x = 0;
            }
        }
        double cap = Math.max(Math.abs(x+y),Math.abs(y-x));;
        topr.setPower(power_scale*(y-x)/cap);
        topl.setPower(power_scale*(x+y)/cap);
        rearr.setPower(power_scale*(x+y)/cap);
        rearl.setPower(power_scale*(y-x)/cap);
    }
    public double getAngle(){
        double angle_correction= Math.PI/2;
        return IMU.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS).thirdAngle + angle_correction;
    }
    public void fieldCentricDrive(double x, double y,boolean auto){
        double power = Math.sqrt(x * x + y * y);

        double controlAngle = Math.atan2(y, x);
        double RobotAngle = getAngle(); // Corrected for IMU orientation on robot.
        double newAngle = AngleUnit.normalizeRadians(controlAngle - RobotAngle + Math.PI/2);
        double newY = Math.sin(newAngle) * power;
        double newX = Math.cos(newAngle) * power;

        opMode.telemetry.addData("controlAngle: ", controlAngle);
        opMode.telemetry.addData("robotAngle: ", RobotAngle);
        opMode.telemetry.addData("newAngle: ", newAngle);
        opMode.telemetry.addData("newX: ", newX);
        opMode.telemetry.addData("newY: ", newY);

        mecanumDrive(newX, newY,auto);
    }

    public void moveForward(double power){
        mecanumDrive(0, Math.abs(power),false);
    }

    public void moveBackward(double power){
        mecanumDrive(0, -Math.abs(power),false);
    }

    public void shiftRight(double power){
        mecanumDrive(power, 0,false);
    }

    public void shiftLeft(double power){
        mecanumDrive(-power, 0,false);
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
    public void setMotorPower(double topl, double topr, double rearl, double rearr){
        this.topl.setPower(topl);
        this.topr.setPower(topr);
        this.rearl.setPower(rearl);
        this.rearr.setPower(rearr);
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