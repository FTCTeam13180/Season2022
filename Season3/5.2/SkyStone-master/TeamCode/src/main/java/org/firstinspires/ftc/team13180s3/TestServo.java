package org.firstinspires.ftc.team13180s3;
//Rohan Gulati -10/5/2019
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TestServo", group="manualmode")
public class TestServo extends LinearOpMode{
    public LinearOpMode opMode = this;
    private Servo servo_1;

    public void runOpMode()  {
        waitForStart();
        servo_1=opMode.hardwareMap.get(Servo.class,"Servo");
        while (opModeIsActive()){

            double rx=gamepad1.left_stick_x;
            if(rx>0) {
                servo_1.setPosition(1.0);
            }
            else if(rx<0) {
                servo_1.setPosition(-1.0);
            }
            else{
                servo_1.setPosition(0);
            }


        }
    }
}
