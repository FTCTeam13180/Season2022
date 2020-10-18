package org.firstinspires.ftc.teamcode;

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

    MoveMotor mm;
    MoveServo ms;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        servo = hardwareMap.get(Servo.class, "servo");

        motor = hardwareMap.get(DcMotor.class, "motor");

        //initializing hook servo
        mm = new MoveMotor(this, DIRECTION.FORWARD, POWER, CMS, TIMOUTMS);
        ms = new MoveServo(servo, TIMOUTMS, this);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop(){
        mm.run();
        ms.run();
    }

}
