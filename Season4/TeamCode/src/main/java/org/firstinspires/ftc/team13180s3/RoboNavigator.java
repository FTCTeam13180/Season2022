package org.firstinspires.ftc.team13180s3;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

import static java.lang.Math.abs;

/**
 * Created by Shivam Adeshara on 12/24/2017.
 * Updated by RG on 8/11/19. - added methods that can make robot go any direction-
 */

public class RoboNavigator {

    public LinearOpMode opMode;

    private DcMotor topl;
    private DcMotor topr;
    private DcMotor rearr;
    private DcMotor rearl;
    private double navigatorPower;
    BNO055IMU imu;
    double delta=0;
    boolean logging = false;

    RoboNavigator (LinearOpMode op)
    {
        opMode = op;
    }

    public void init() {
        topl = opMode.hardwareMap.get(DcMotor.class, "Topl");
        topr = opMode.hardwareMap.get(DcMotor.class, "Topr");
        rearl = opMode.hardwareMap.get(DcMotor.class, "Rearl");
        rearr = opMode.hardwareMap.get(DcMotor.class, "Rearr");
        topr.setDirection(DcMotor.Direction.REVERSE);
        rearr.setDirection(DcMotor.Direction.REVERSE);
        topl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        navigatorPower = 1.0;
        if(logging) {
            opMode.telemetry.addData("RoboNavigator:", "Initialized");
        }
    }
    public void initIMU(){
        BNO055IMU.Parameters param = new BNO055IMU.Parameters();
        param.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
        param.gyroBandwidth = BNO055IMU.GyroBandwidth.HZ523;
        //param.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        //param.calibrationDataFile = "BNO055IMUCalibration.json";
        param.loggingEnabled      = true;
        param.loggingTag          = "IMU";
        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu123");
        imu.initialize(param);

        // Wait for gyroscope to be calibrated
        opMode.telemetry.addLine ("Starting Gyro Calibration");
        opMode.telemetry.update();
        while(!imu.isGyroCalibrated()) {
            opMode.sleep(50);
            opMode.idle();
        }
        opMode.telemetry.addLine ("Completed Gyro Calibration");
        Orientation imu_orientation =
                imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS);
        double imu_radian = imu_orientation.firstAngle;
        opMode.telemetry.addData ("Initialized at angle: ", "%f", imu_radian);

        opMode.telemetry.update();

    }

    public void setNavigatorPower (double p) {
        navigatorPower = p;
    }

    public double getNavigatorPower () {
        return navigatorPower;
    }

    /*
     ******************************
     * Basic Movement
     ******************************
     */

    public void stopMotor() {
        topl.setPower(0);
        topr.setPower(0);
        rearl.setPower(0);
        rearr.setPower(0);
        if(logging) {
            opMode.telemetry.addData ("RoboNavigator:", "stopMotor ()");
        }
    }

    public void moveForward(double power) {
        topr.setPower(abs(power));
        topl.setPower(abs(power));
        rearl.setPower(abs(power));
        rearr.setPower(abs(power));
        if(logging) {
            opMode.telemetry.addData ("RoboNavigator:", "moveForward (power=%f)", power);
        }
    }

    public void moveBackward(double power) {
        topr.setPower(-abs(power));
        topl.setPower(-abs(power));
        rearl.setPower(-abs(power));
        rearr.setPower(-abs(power));
        if(logging) {
            opMode.telemetry.addData ("RoboNavigator:", "moveBackward (power=%f)", power);
        }
    }

    public void shiftLeft (double power){
        topl.setPower(-abs(power));
        topr.setPower(abs(power));
        rearl.setPower(abs(power));
        rearr.setPower(-abs(power));
        if(logging) {
            opMode.telemetry.addData ("RoboNavigator:", "shiftLeft (power=%f)", power);
        }
    }

    public void shiftRight (double power){
        topl.setPower(abs(power));
        topr.setPower(-abs(power));
        rearl.setPower(-abs(power));
        rearr.setPower(abs(power));
        if(logging) {
            opMode.telemetry.addData ("RoboNavigator:", "shiftRight (power=%f)", power);
        }
    }

    public void turnRight (double power){
        topl.setPower(abs(power));
        topr.setPower(-abs(power));
        rearl.setPower(abs(power));
        rearr.setPower(-abs(power));
        if(logging) {
            opMode.telemetry.addData ("RoboNavigator:", "turnRight (power=%f)", power);
        }
    }

    public void turnLeft (double power){
        topl.setPower(-abs(power));
        topr.setPower(abs(power));
        rearl.setPower(-abs(power));
        rearr.setPower(abs(power));
        if(logging) {
            opMode.telemetry.addData ("RoboNavigator:", "turnLeft (power=%f)", power);
        }
    }


    /*
     ************************
     * Encoder based Basic Movement
     ************************
     */

    public void moveForward (double inches, long timeoutMs) {
        encoderDrive(DIRECTION.FORWARD, navigatorPower, inches * 2.54, timeoutMs);
    }

    public void moveForwardWithRampup (double inches, long timeoutMs, double rampupMs) {
        encoderDriveWithRampup(DIRECTION.FORWARD, navigatorPower, inches * 2.54, timeoutMs, rampupMs);
    }

    public void moveBackward (double inches, long timeoutMs) {
        encoderDrive(DIRECTION.BACKWARD, navigatorPower, inches * 2.54, timeoutMs);
    }

    public void shiftLeft (double inches, long timeoutMs) {
        encoderDrive(DIRECTION.SHIFT_LEFT, navigatorPower, inches * 2.54, timeoutMs);
    }

    public void shiftRight (double inches, long timeoutMs) {
        encoderDrive(DIRECTION.SHIFT_RIGHT, navigatorPower, inches * 2.54, timeoutMs);
    }

    public void turnLeft (double degrees, long timeoutMs) {
        encoderDrive(DIRECTION.TURN_LEFT, navigatorPower, degrees*10/9, timeoutMs);
    }

    public void turnRight (double degrees, long timeoutMs) {
        encoderDrive(DIRECTION.TURN_RIGHT, navigatorPower, degrees*10/9, timeoutMs);
    }


    /*
     *************************
     * OmniDrives
     *************************
     */
    public double getAngle(double x,double y){
        return Math.atan2(y,x);
    }

    public void AnyMecanum(double x,double y, double power_scale){
        double power = Math.sqrt(x * x + y * y);
        topr.setPower(power_scale*(y-x)/power);
        topl.setPower(power_scale*(x+y)/power);
        rearr.setPower(power_scale*(x+y)/power);
        rearl.setPower(power_scale*(y-x)/power);
    }

    public void AnyMecanumSwerve(double x,double y, double power_scale){
        double power = Math.sqrt(x * x + y * y);

        // Floor the y to certain value.
        // This controls how fast the robot makes the sharpest turns
        // Higher the value the faster robot will be at the turns
        y = (y > 0 && y < 0.9) ? 0.9 : y;
        y = (y < 0 && y > -0.9) ? -0.9 : y;

        // Ceil the turns to certain value
        // This controls how sharp the turns can be.
        // Higher the value, the sharper the turns. Lower value is better for smooth turns.
        x = (x > 0 && x > 0.9) ? 0.9 : x;
        x = (x < 0 && x < -0.9) ? -0.9 : x;

        // Reverse x direction for lateral inversion of turns when moving backwards.
//        if (y<0)
//            x = -x;

        topr.setPower(power_scale*(y-x)/power);
        topl.setPower(power_scale*(y+x)/power);
        rearr.setPower(power_scale*(y-x)/power);
        rearl.setPower(power_scale*(y+x)/power);
    }

    public void ResetImu(){
        /*
        Orientation imu_O=imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS);
        double imu_R=imu_O.firstAngle;
        delta=imu_R-delta;
        */
    }


    public void OmniImu(double x, double y, double power_scale){
        double input_radian = Math.atan2(y,x); //gets standard angle of joystick

        // Get IMU Orientation in radians for roll movement (around y-axis)
        Orientation imu_orientation =
                imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS);


        //double imu_radian = imu_orientation.firstAngle+delta;

        double imu_radian = imu_orientation.firstAngle;

        double corrected_radian = input_radian - imu_radian;

        opMode.telemetry.addData ("Joystick Input: ", "%f", input_radian);
        opMode.telemetry.addData ("CurrentPosition: ", "%f", imu_radian);
        opMode.telemetry.addData ("Diff: ", "%f", corrected_radian);
        opMode.telemetry.update();
        AnyMecanum(Math.cos(corrected_radian),Math.sin(corrected_radian), power_scale);
    }

    public void PrintImuRadians(){
        // Get IMU Orientation in radians for roll movement (around y-axis)
        Orientation imu_orientation =
                imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);

        double imu_radian = imu_orientation.firstAngle;

        opMode.telemetry.addData ("CurrentPosition: ", "%f", imu_radian);
        opMode.telemetry.addData("Orientation.toString", imu_orientation.toString());

        opMode.telemetry.update();
    }

    /*
     *********************
     * SwerveDrive
     *********************
     */
    public void SwerveDrive(double angle,
                            double turn, double power){
        double d1= power*Math.sin(angle-(Math.PI/4));
        double d2=power*Math.sin(angle+(Math.PI/4));


        topl.setPower(d1+turn);
        topr.setPower(d2-turn);
        rearl.setPower(d2+turn);
        rearr.setPower(d1-turn);
    }



    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     *
     */


    /*
     * Internal Encoder Drive implementation
     * Prefer not to use these functions directly. Instead use the Encoder based Basic Movement functions
     */

    private static final double ROBO_DIAMETER_CM = 62.86;
    private static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: Andymark Motor Encoder
    private static final double     DRIVE_GEAR_REDUCTION    = 0.776 ;     // This is < 1.0 if geared up
    private static final double     WHEEL_DIAMETER_CM   = 10.16 ;     // For figuring circumference
    private static final double     COUNTS_PER_CM         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_CM * 3.1415);
    private  static final double CMS_PER_DEGREE = 3.1415 * ROBO_DIAMETER_CM / 360;
    private  static final double COUNTS_PER_DEGREE = COUNTS_PER_CM * CMS_PER_DEGREE;
    private static final double SHIFT_SLIPPAGE_CORRECTION = 1.14;
    private static final double TURN_SLIPPAGE_CORRECTION = 90/89;
    private static final double STRAIGHT_SLIPPAGE_CORRECTION = 96/97.5;


    private void setRunMode (DcMotor.RunMode runMode) {
        topr.setMode(runMode);
        topl.setMode(runMode);
        rearr.setMode(runMode);
        rearl.setMode(runMode);
    }

    private void setPower (double power) {
        topr.setPower(abs(power));
        topl.setPower(abs(power));
        rearr.setPower(abs(power));
        rearl.setPower(abs(power));
    }

    private void logCurrentPosition () {
        if(logging) {
            opMode.telemetry.addData("CurrentPosition:",
                    "topr=%7d, topl=%7d, rearr=%7d, rearl=%7d",
                    topr.getCurrentPosition(),
                    topl.getCurrentPosition(),
                    rearr.getCurrentPosition(),
                    rearl.getCurrentPosition());
        }
    }

    private void setTargetPosition(DIRECTION direction, double cms) {
        // Determine new target position, and pass to motor controller
        if (direction == DIRECTION.FORWARD) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
        }
        else if (direction == DIRECTION.BACKWARD) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() - (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() - (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() - (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() - (cms * COUNTS_PER_CM) * STRAIGHT_SLIPPAGE_CORRECTION));
        } else if (direction == DIRECTION.SHIFT_RIGHT) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() + (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() - (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() - (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() + (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
        } else if (direction == DIRECTION.SHIFT_LEFT) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() - (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() + (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() + (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() - (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
        } else if (direction == DIRECTION.TURN_LEFT){
            topl.setTargetPosition((int) (topr.getCurrentPosition() - (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() + (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() - (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() + (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
        }
        else if (direction == DIRECTION.TURN_RIGHT) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() + (cms * COUNTS_PER_DEGREE * TURN_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() - (cms * COUNTS_PER_DEGREE * TURN_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() + (cms * COUNTS_PER_DEGREE * TURN_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() - (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
        }
    }



    private boolean isBusy () {
        return topr.isBusy() && topl.isBusy() && rearr.isBusy() && rearl.isBusy();
    }

    public enum DIRECTION {
        FORWARD,
        BACKWARD,
        SHIFT_LEFT,
        SHIFT_RIGHT,
        TURN_LEFT,
        TURN_RIGHT
    };

    public void encoderDrive(DIRECTION direction,
                             double speed,
                             double cms,
                             double timeoutMs) {

        if(logging) {
            opMode.telemetry.addData("RoboNavigator: ", "Resetting Encoders");    //
        }

        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(logging) {
            opMode.telemetry.addData("RoboNavigator:", "Encoders reset");
        }

        // Send telemetry message to indicate currentPosition
        logCurrentPosition();

        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

            // Set Target Position

            setTargetPosition(direction, cms);

            // Turn On RUN_TO_POSITION
            setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            ElapsedTime runtime = new ElapsedTime();

            runtime.reset();

            // Based on direction call corresponding Move function
            setPower (speed);

            // keep looping while we are still active, and there is time left, and motor is running.
            while (opMode.opModeIsActive() &&
                    (runtime.milliseconds() < timeoutMs) &&
                    (isBusy())) {


                // Display it for the driver.zz
                logCurrentPosition();
            }

            // Stop all motion;
            stopMotor();

            // Turn off RUN_TO_POSITION
            setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            //sleep to kill momentum

        }
    }
    public void encoderDriveRampDown(DIRECTION direction,
                             double speed,
                             double cms,
                             double timeoutMs, double rampDownCm) {

        if(logging) {
            opMode.telemetry.addData("RoboNavigator: ", "Resetting Encoders");    //
        }

        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(logging) {
            opMode.telemetry.addData("RoboNavigator:", "Encoders reset");
        }

        // Send telemetry message to indicate currentPosition
        logCurrentPosition();

        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

            // Set Target Position

            setTargetPosition(direction, cms);

            // Turn On RUN_TO_POSITION
            setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            ElapsedTime runtime = new ElapsedTime();

            runtime.reset();

            // Based on direction call corresponding Move function
            setPower (speed);

            // keep looping while we are still active, and there is time left, and motor is running.
            while (opMode.opModeIsActive() &&
                    (runtime.milliseconds() < timeoutMs) &&
                    (isBusy())) {

                double gradualAcceleration=(topl.getTargetPosition()-topl.getCurrentPosition())/(rampDownCm*COUNTS_PER_CM);
                setPower(gradualAcceleration);
                // Display it for the driver.zz
                logCurrentPosition();
            }

            // Stop all motion;
            stopMotor();

            // Turn off RUN_TO_POSITION
            setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            //sleep to kill momentum

        }
    }
    public void encoderDriveWithRampup(DIRECTION direction,
                             double speed,
                             double cms,
                             double timeoutMs, double rampupTimeMs) {

        if(logging) {
            opMode.telemetry.addData("RoboNavigator: ", "Resetting Encoders");    //
        }

        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(logging) {
            opMode.telemetry.addData("RoboNavigator:", "Encoders reset");
        }

        // Send telemetry message to indicate currentPosition
        logCurrentPosition();

        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {

            // Set Target Position

            setTargetPosition(direction, cms);

            // Turn On RUN_TO_POSITION
            setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            ElapsedTime runtime = new ElapsedTime();

            runtime.reset();

            double elapsedTimeMs;
            double rspeed;

            // keep looping while we are still active, and there is time left, and motor is running.
            while (opMode.opModeIsActive() &&
                    (runtime.milliseconds() < timeoutMs) &&
                    (isBusy())) {
                elapsedTimeMs = runtime.milliseconds();
                if (elapsedTimeMs < rampupTimeMs) {
                    double rampFactor = elapsedTimeMs / rampupTimeMs;
                    rspeed = speed * rampFactor;
                    // Based on direction call corresponding Move function
                    setPower (rspeed);
                }//if rampup time has passed, use set speed
                else {
                    // Based on direction call corresponding Move function
                    setPower (speed);
                }

                // Display it for the driver.zz
                logCurrentPosition();
            }

            // Stop all motion;
            stopMotor();

            // Turn off RUN_TO_POSITION
            setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }


    public void setLogging(boolean value) {
        logging = value;
    }
}

