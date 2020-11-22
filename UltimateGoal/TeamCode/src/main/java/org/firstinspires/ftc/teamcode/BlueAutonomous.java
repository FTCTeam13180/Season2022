package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

@Autonomous(name = "BlueAutonomous", group = "UltimateGoalAutonomous")
public class BlueAutonomous extends OpMode {

    private AutonomousTasks auto;

    @Override
    public void init() {
        auto = new AutonomousTasks(this);
        auto.init();
    }

    @Override
    public void init_loop(){
        auto.numberOfRings();
        auto.setRingNumber(auto.numOfRings);
    }

    @Override
    public void loop(){
        auto.run();
    }

}
