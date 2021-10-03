package org.firstinspires.ftc.teamcode.component;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeComponent implements Component {
    private final double INTAKE_POWER = 1.0;
    private OpMode opMode;
    private Telemetry.Item log_intake;

    public IntakeComponent(OpMode op) {
        opMode = op;
    }

    public DcMotor intake;

    public void init() {
        intake = opMode.hardwareMap.dcMotor.get("Intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setDirection(DcMotor.Direction.FORWARD);

        log_intake = opMode.telemetry.addData("Intake:", "Initialized");
    }

    public void in() {
        intake.setPower(INTAKE_POWER);
        log_intake.setValue("IN");
    }

    public void expel() {
        intake.setPower(-INTAKE_POWER);
        log_intake.setValue("EXPEL");
    }

    public void stop() {
        intake.setPower(0);
        log_intake.setValue("STOPPED");
    }

    public boolean isBusy() {
        return intake.isBusy();
    }

}
