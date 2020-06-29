package org.firstinspires.ftc.team13180s3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Elevator {
    public LinearOpMode opMode;
    private CRServo inOutWinch;
    private DcMotor upDownWinchL;
    private DcMotor upDownWinchR;
    public int height=0;
    public double gearRatio = 0.5;
    public double circumference = 0.7;
    public int levels = 5;
    public int outDistance=0;

    private final double MSECS_PER_ROTATION = 840;
    private double curr_inout_pos;
    private double max_inout_pos = 3.0;

    Elevator(LinearOpMode op) {
        opMode = op;
    }

    public void init() {
        inOutWinch = opMode.hardwareMap.get(CRServo.class, "horizontalWinch");
        upDownWinchL=opMode.hardwareMap.get(DcMotor.class,"verticalWinchL");
        upDownWinchR=opMode.hardwareMap.get(DcMotor.class,"verticalWinchR");

        curr_inout_pos = 0;
    }

    //
    // InOutWinch Controls used by telescopic arm
    //
    private void goOut(double power) {
        inOutWinch.setPower(abs(power));
    }
    private void goIn(double power) {
        inOutWinch.setPower(-abs(power));
    }
    private void stopInOut() {
        inOutWinch.setPower(0);
    }

    public void goOutByRotations(double power, double rotations) {
        if (curr_inout_pos >= max_inout_pos)
            return;
        double diff = (curr_inout_pos + rotations <= max_inout_pos) ? rotations : (curr_inout_pos + rotations - max_inout_pos);
        goOut(power);
        opMode.sleep((long)(diff * MSECS_PER_ROTATION));
        stopInOut();
        curr_inout_pos += diff;
    }
    public void goInByRotations(double power, double rotations) {
        if (curr_inout_pos <= 0)
            return;
        double diff = (curr_inout_pos - rotations >= 0) ? rotations : (curr_inout_pos);
        goIn(power);
        opMode.sleep((long)(diff * MSECS_PER_ROTATION));
        stopInOut();
        curr_inout_pos -= diff;
    }

    public void goUpByRotations(double power, double rotations) {
        goUp(power);
        opMode.sleep((long)(rotations * MSECS_PER_ROTATION));
        stopUpDown();
    }
    
    public void goUpByInches(double power, double inches){
        goUp(power);
        opMode.sleep((long)((inches/((circumference*3.14)*(levels-1)*(gearRatio))) * MSECS_PER_ROTATION));
        stopUpDown();
    }
    public void goDownByRotations(double power, double rotations) {
        goDown(power);
        opMode.sleep((long)(rotations * MSECS_PER_ROTATION));
        stopUpDown();
    }
    public void goDownByInches(double power, double inches){
        goDown(power);
        opMode.sleep((long)((inches/((circumference*3.14)*(levels-1)*(gearRatio))) * MSECS_PER_ROTATION));
        stopUpDown();
    }
    //
    // UpDownWinch Controls used by elevator arm
    //
    public void goUp(double power){

        upDownWinchL.setPower(abs(power));
        upDownWinchR.setPower(-abs(power));
    }
    public void useEncoder(){
        upDownWinchL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upDownWinchL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        upDownWinchR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upDownWinchR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
    public double currentEncoderPosition(){
        return upDownWinchL.getCurrentPosition();
    }

    public void goDown(double power){

        upDownWinchL.setPower(-abs(power));
        upDownWinchR.setPower(abs(power));
    }

    public void stopUpDown(){
        upDownWinchL.setPower(0);
        upDownWinchR.setPower(0);
    }

    public void resetLevel() {

        upDownWinchL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upDownWinchR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void goUpOneLevel() {
        upDownEncoderDrive (1.0, 2.54 * 4, 3000);
    }

    public void goDownOneLevel() {
        upDownEncoderDrive (1.0, -2.54 * 4, 3000);
    }

    private static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    private static final double     DRIVE_GEAR_REDUCTION    = 4/3 ;     // This is < 1.0 if geared UP
    private static final double     WHINCH_DIAMETER_IN   = 0.675 ;     // For figuring circumference
    static final double     COUNTS_PER_IN         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHINCH_DIAMETER_IN * 3.1415*2);

    public void upDownEncoderDrive(double speed,
                                   double cms,
                                   double timeoutMs) {
        int newWinchUpTarget1;
        int newWinchUpTarget2;
        height-=cms;
        opMode.telemetry.addData("Elevator:", "Resetting Encoders");    //
        opMode.telemetry.update();

        upDownWinchL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upDownWinchL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        upDownWinchR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upDownWinchR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        opMode.telemetry.addData("Path0", "Starting at %7d", upDownWinchL.getCurrentPosition());
        opMode.telemetry.update();


        // Ensure that the opmode is still active
        if (opMode.opModeIsActive()) {
            // Determine new target position, and pass to motor controller
            newWinchUpTarget1 = upDownWinchL.getCurrentPosition() + (int) (cms * COUNTS_PER_IN);
            newWinchUpTarget2 = upDownWinchR.getCurrentPosition() + (int) (-cms * COUNTS_PER_IN);
            upDownWinchL.setTargetPosition(newWinchUpTarget1);
            upDownWinchR.setTargetPosition(newWinchUpTarget2);

            // Turn On RUN_TO_POSITION
            upDownWinchL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            ElapsedTime runtime = new ElapsedTime();

            runtime.reset();

            goUp(speed*(Math.abs(cms)/cms));

            // keep looping while we are still active, and there is time left, and motor is running.
            while (opMode.opModeIsActive() &&
                    (runtime.seconds() < timeoutMs) &&
                    (upDownWinchL.isBusy())&&(upDownWinchR.isBusy())) {

                // Display it for the driver.
                opMode.telemetry.addData("Path1", "Running to %7d", newWinchUpTarget1);
                opMode.telemetry.addData("Path2", "Running at %7d",
                        upDownWinchR.getCurrentPosition(), upDownWinchL.getCurrentPosition());

                opMode.telemetry.update();
                opMode.sleep(2000);
            }

            // Stop all motion;
            stopUpDown();

            // Turn off RUN_TO_POSITION
            upDownWinchL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            upDownWinchR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }
    }
    double rotations = 3; //used to be 6, but for qual 3, geared 2:1

    public void playposition(){
        goOutByRotations(1.0,max_inout_pos);
    }

    public void homeposition(){
        goInByRotations(1.0,curr_inout_pos);
    }

    public void goOutbyOne(){
        goOutByRotations(1.0,1);
    }

    public void goInbyOne(){
        goInByRotations(1.0,1);
    }
}