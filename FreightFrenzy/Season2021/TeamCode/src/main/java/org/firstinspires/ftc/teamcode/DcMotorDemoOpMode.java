package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "DcMotorDemo", group = "POC")
public class DcMotorDemoOpMode extends LinearOpMode {
    int count = 0;
    ElapsedTime runtime;

    DcMotor motor;

    @Override
    public void runOpMode() {
        runtime = new ElapsedTime();
        runtime.reset();
        telemetry.setAutoClear(false);
        telemetry.addData("Status", "In init, Elapsed Time = " + runtime.milliseconds());
        motor = hardwareMap.get(DcMotor.class, "motor");
        //doStopAndResetEncoderDemo();
        //doRunUsingEncoderDemo();
        doRunToPositionDemo();
        return;
    }

    private void doStopAndResetEncoderDemo() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("Status", "In doStopAndResetEncoderDemo, Elapsed Time = " + runtime.milliseconds()
                + "DcMotor.getPosition = " + motor.getCurrentPosition());
        telemetry.update();
        while (true) {

        }
    }

    private void doRunUsingEncoderDemo() {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        do {
            telemetry.addData("Status", "In doRunUsingEncoderDemo, Elapsed Time = " + runtime.milliseconds()
                    + "DcMotor.getPosition = " + motor.getCurrentPosition());
            telemetry.update();
            motor.setPower(0.1);
        } while (motor.getCurrentPosition() < 100);

    }

    private void doRunWithoutEncoderDemo() {
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        do {
            telemetry.addData("Status", "In doRunWithoutEncoderDemo, Elapsed Time = " + runtime.milliseconds()
                    + "DcMotor.getPosition = " + motor.getCurrentPosition());
            telemetry.update();
            motor.setPower(0.1);
        } while (motor.getCurrentPosition() < 100);

    }

    private void doRunToPositionDemo() {
        motor.setTargetPosition(motor.getCurrentPosition() + 100);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Status", "In doRunToPositionDemo, Elapsed Time = " + runtime.milliseconds()
                + " DcMotor.getPosition = " + motor.getCurrentPosition() + " DcMotor.targetPosition = " + (motor.getCurrentPosition()+100));

        motor.setPower(0.1);
        while(motor.isBusy()) {
            telemetry.addData("Status", "In doRunToPositionDemo, Elapsed Time = " + runtime.milliseconds()
                    + "DcMotor.getPosition = " + motor.getCurrentPosition());
            telemetry.update();
        }
    }
}
