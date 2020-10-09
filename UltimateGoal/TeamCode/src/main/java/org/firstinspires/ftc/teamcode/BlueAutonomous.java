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
        int rings = 0;
        int i = 0;
        if(updatedRecognitions != null) {
            for (Recognition recognition : updatedRecognitions) {
            //Recognition recognition = updatedRecognitions.get(0);
                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                if(recognition.getLabel() == Detector.LABEL_SECOND_ELEMENT){
                    rings = 1;
                }
                else if(recognition.getLabel() == Detector.LABEL_FIRST_ELEMENT){
                    rings = 4;
                }
            }
        }
        else {
            rings = 0;
        }
        return rings;
    }

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        detect = new Detector(this);
        detect.init();
    }

    @Override
    public void init_loop(){
        int rings = ringNumber();
        telemetry.addData("Number of rings = ", rings);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop(){

    }

}
