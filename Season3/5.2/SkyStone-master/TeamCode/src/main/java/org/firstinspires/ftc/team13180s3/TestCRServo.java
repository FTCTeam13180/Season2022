package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="TestCRServo", group="manualmode")
public class TestCRServo extends LinearOpMode{
    public LinearOpMode opMode = this;
    private CRServo servo_1;

    public void runOpMode()  {
        waitForStart();
        servo_1=opMode.hardwareMap.get(CRServo.class,"CRServo");
        while (opModeIsActive()){

            double rx=gamepad1.left_stick_x;
            if(rx>0) {
                servo_1.setPower(0.8);
            }
            else if(rx<0) {
                servo_1.setPower(-0.8);
            }
            else{
                servo_1.setPower(0);
            }


        }
    }

}
