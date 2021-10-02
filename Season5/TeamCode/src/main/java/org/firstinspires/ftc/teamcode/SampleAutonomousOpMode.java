package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "SampleAutonomousOpMode", group = "POC")
public class SampleAutonomousOpMode extends OpMode {

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
   // MoveToLaunchPositionParallelAction moveToLaunchPosition;

    @Override
    public void init() {
        telemetry.addData("SampleAutonomousOpMode", "Start Initializing");

        //mm = new MoveMotor(this, DIRECTION.FORWARD, POWER, CMS, TIMOUTMS);
        //ms = new MoveServo(servo, TIMOUTMS, this);
        //moveToLaunchPosition = new MoveToLaunchPositionParallelAction();
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop(){
        mm.run();
        ms.run();
        //moveToLaunchPosition.execute();
    }

}
