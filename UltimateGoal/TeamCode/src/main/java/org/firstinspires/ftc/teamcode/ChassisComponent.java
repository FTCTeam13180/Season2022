package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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
    private static final double COUNTS_PER_MOTOR_REV = 1120 ;    // eg: Andymark Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 0.776 ;     // This is < 1.0 if geared up
    private static final double WHEEL_DIAMETER_CM = 10.16 ;
    private static final double COUNTS_PER_CM  = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * 3.1415);
    private static final double STRAIGHT_SLIPPAGE_CORRECTION = 96/97.5;

    public void init() {
        topl = opMode.hardwareMap.get(DcMotor.class, "Topl");
        topr = opMode.hardwareMap.get(DcMotor.class, "Topr");
        rearl = opMode.hardwareMap.get(DcMotor.class, "Rearl");
        rearr = opMode.hardwareMap.get(DcMotor.class, "Rearr");
        topl.setDirection(DcMotor.Direction.FORWARD);
        topr.setDirection(DcMotor.Direction.FORWARD);
        rearl.setDirection(DcMotor.Direction.FORWARD);
        rearr.setDirection(DcMotor.Direction.FORWARD);
        topl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opMode.telemetry.addData("Chassis:", "Initialized");
    }

    public void move(double toprPower, double toplPower, double rearrPower, double rearlPower) {
        topr.setPower(toplPower);
        topl.setPower(toprPower);
        rearr.setPower(rearrPower);
        rearl.setPower(rearlPower);
    }

    public void setRunMode(DcMotor.RunMode runMode) {
        topl.setMode(runMode);
        topr.setMode(runMode);
        rearl.setMode(runMode);
        rearr.setMode(runMode);
    }

    public void setTargetPosition(double cms) {
        topl.setTargetPosition((int) (topl.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
        topr.setTargetPosition((int) (topr.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
        rearl.setTargetPosition((int) (rearl.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
        rearr.setTargetPosition((int) (rearr.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
    }

    public void logCurrentPosition () {
        opMode.telemetry.addData("CurrentPosition:", "topl=%7d", topl.getCurrentPosition());
    }

    public void stop() {
        move(0, 0, 0, 0);
    }

    public boolean isBusy() {
        return (topl.isBusy() || topr.isBusy() || rearl.isBusy() || rearr.isBusy());
    }
}