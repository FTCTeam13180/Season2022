package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import java.util.List;

@TeleOp(name="TestDetector", group="manualmode")
public class TestDetector extends LinearOpMode {
    public Detector detect;
    private RoboNavigator robo;
    @Override
    public void runOpMode(){
        detect = new Detector(this);
        detect.init();
        robo = new RoboNavigator(this);
        robo.init();
        robo.setLogging(false);
        waitForStart();
        boolean foundSkytone = false;
        while (opModeIsActive()){
            if(gamepad1.b){
                List<Recognition> blocks = detect.scan();
                int i=0;
                for(Recognition recog: blocks){
                    if(i==0){
                        telemetry.addData("Image Width: ", recog.getImageWidth());
                        telemetry.addData("Image Height",recog.getImageHeight());
                    }
                    telemetry.addData("Block Number: ",i);
                    telemetry.addData("Width",recog.getWidth());
                    telemetry.addData("Label",recog.getLabel());
                    telemetry.addLine();
                }
                telemetry.update();
            }
            else if(gamepad1.a){
                List<Recognition> blocks = detect.scan();
                int numblocks = blocks.size();
                int i = 0;
                telemetry.addData("Number Of Blocks", numblocks);
                if(numblocks < 2){
                    robo.shiftRight(3,1000);
                    blocks = detect.scan();
                    numblocks = blocks.size();
                }
                if(numblocks == 2){
                    for(Recognition recog: blocks){
                        telemetry.addData("Image Width: ", recog.getImageWidth());
                        telemetry.addData("Image Height",recog.getImageHeight());
                        telemetry.addData("Label", recog.getLabel());
                        telemetry.addData("Left", recog.getLeft());
                        telemetry.addData("Right", recog.getRight());
                        telemetry.addData("I", i);


                        if(recog.getLabel().equalsIgnoreCase(Detector.LABEL_SECOND_ELEMENT)){
                            float x = recog.getRight();

                            //second closest to bridge if i == 1;
                            if(i == 1){
                                telemetry.addLine("Position 2");
                                robo.shiftRight(5,1000);
                                foundSkytone = true;
                                telemetry.addData("Found skytone. i=", i);
                                telemetry.addData("X=",x);
                                break;
                            }
                            //Closer to bridge if i == 0
                            else if (i == 0){
                                telemetry.addLine("Position 3");
                                robo.shiftRight(20,1000);
                                foundSkytone = true;
                                telemetry.addData("found skytone i=", i);
                                telemetry.addData("X=",x);
                                break;
                            }
                        }
                        i++;
                    }
                    if(foundSkytone) {
                        telemetry.update();
                        robo.moveForward(15,10000);
                    }
                    else{
                        robo.shiftLeft(6,1000);
                        robo.moveForward(18,10000);
                    }
                }


            }

        }

        detect.shutdown();
        //6.5 in
    }
}

