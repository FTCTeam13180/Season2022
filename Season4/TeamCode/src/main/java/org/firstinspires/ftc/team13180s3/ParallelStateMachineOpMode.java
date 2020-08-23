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
    public Servo hook;
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

    Drive df;
    MoveHook mh;

    public void init(){

        //initializing drive forward motors
        topr = opMode.hardwareMap.get(DcMotor.class, "Topr");
        topl = opMode.hardwareMap.get(DcMotor.class, "Topl");
        rearr = opMode.hardwareMap.get(DcMotor.class, "Rearr");
        rearl = opMode.hardwareMap.get(DcMotor.class, "Rearl");

        //initializing hook servo
        hook = opMode.hardwareMap.get(Servo.class, "Hook");

        df = new Drive(this, DIRECTION.FORWARD, POWER, CMS, TIMOUTMS);
        mh = new MoveHook(hook, TIMOUTMS, this);

    }


    public void loop(){
        df.run();
    }

}
