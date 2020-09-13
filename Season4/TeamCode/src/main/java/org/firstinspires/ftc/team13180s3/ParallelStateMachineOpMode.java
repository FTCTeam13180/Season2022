package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ParallelRunHookAndDrive", group = "POC")
public class ParallelStateMachineOpMode extends OpMode {

    public DcMotor topr;
    public DcMotor topl;
    public DcMotor rearr;
    public DcMotor rearl;
    public DcMotor motor;
    public Servo servo;
    public static final double POWER = 1.0;
    public static final double CMS = 60 * 2.54;
    public static final double TIMOUTMS = 20000;

    public enum DIRECTION {
        FORWARD,
        BACKWARD,
        SHIFT_LEFT,
        SHIFT_RIGHT,
        TURN_LEFT,
        TURN_RIGHT
    }

    public OpMode opMode;

    ParallelStateMachineOpMode(OpMode op) {
        opMode = op;
    }

    MoveMotor mm;
    MoveServo ms;

    public void init(){

        //initializing drive forward motor
        motor = opMode.hardwareMap.get(DcMotor.class, "motor");

        //initializing hook servo
        servo = opMode.hardwareMap.get(Servo.class, "servo");

        mm = new MoveMotor(this, DIRECTION.FORWARD, POWER, CMS, TIMOUTMS);
        ms = new MoveServo(servo, TIMOUTMS, this);

    }


    public void loop(){
        mm.run();
        ms.run();
    }

}
