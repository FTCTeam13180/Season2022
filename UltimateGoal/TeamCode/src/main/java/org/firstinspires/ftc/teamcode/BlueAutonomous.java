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

    private Detector detect;

    private int ringNumber() {
        List<Recognition> updatedRecognitions = detect.scan();
        int i = 0;
        for (Recognition recognition : updatedRecognitions) {
            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                    recognition.getLeft(), recognition.getTop());
            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                    recognition.getRight(), recognition.getBottom());
        }
        return 1;
    }

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        detect = new Detector(this);
        detect.init();
        int rings = ringNumber();
        telemetry.addData("Number of rings = ", rings);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop(){

    }

}
