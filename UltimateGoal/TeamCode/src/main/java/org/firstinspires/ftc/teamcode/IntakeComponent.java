package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class IntakeComponent {
    private final double INTAKE_POWER = 1.0;
    private OpMode opMode;

    IntakeComponent(OpMode op) {
        opMode = op;
    }

    private DcMotor intake;

    public void init() {
        intake = opMode.hardwareMap.get(DcMotor.class, "Intake");
        intake.setDirection(DcMotor.Direction.FORWARD);
        opMode.telemetry.addData("Intake:", "Initialized");
    }

    public void in() {
        intake.setPower(INTAKE_POWER);
        opMode.telemetry.addData("Intake", "IN");
    }

    public void expel() {
        intake.setPower(-INTAKE_POWER);
        opMode.telemetry.addData("Intake", "EXPEL");
    }

    public void stop() {
        intake.setPower(0);
        opMode.telemetry.addData("Intake:", "Stopped");
    }

    public boolean isbusy() {
        return intake.isBusy();
    }

}
