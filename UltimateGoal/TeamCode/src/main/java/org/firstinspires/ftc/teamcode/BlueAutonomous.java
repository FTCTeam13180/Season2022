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
    int numOfRings = 0;
    private AutonomousTasks auto;
    Odometry odometry;

    private void numberOfRings() {
        List<Recognition> updatedRecognitions = detect.scan();
        numOfRings = 0;
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
                    numOfRings = 1;
                }
                else if(recognition.getLabel() == Detector.LABEL_FIRST_ELEMENT){
                    numOfRings = 4;
                }
                else {
                    numOfRings = 0;
                }
            }
        }
        else {
            numOfRings = 0;
        }
    }


    @Override
    public void init() {
       // telemetry.setAutoClear(false);
        telemetry.addData("Status", "Initializing");
        //detect = new Detector(this);
        //detect.init();

        odometry = new Odometry(this,120,45);
        odometry.init();
        telemetry.addData("Status", "Odometry Initialized");

        auto = new AutonomousTasks(this, odometry);
    }

    @Override
    public void init_loop(){
        //numberOfRings();
        numOfRings = 1;
        telemetry.addData("Number of rings = ", numOfRings);
        telemetry.addData("Status", "Initialized");
        auto.setRingNumber(numOfRings);
    }

    @Override
    public void loop(){
        auto.run();
    }

}
