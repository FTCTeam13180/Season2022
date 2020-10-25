package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;

public class IntakeComponent {
    public OpMode opMode;
    IntakeComponent (OpMode op) { opMode = op; }

    private DcMotor intake;
    public void init() {
        intake = opMode.hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        opMode.telemetry.addData("intake:", "Initialized");
    }
    public void setPower(double intakePower){
        intake.setPower(Math.abs(intakePower));
        opMode.telemetry.addData("intake:", "Running");
    }

    public void stop(){
        intake.setPower(0);
        opMode.telemetry.addData("intake:", "Stopped");
    }

    public boolean isbusy(){
        return intake.isBusy();
    }
 }
