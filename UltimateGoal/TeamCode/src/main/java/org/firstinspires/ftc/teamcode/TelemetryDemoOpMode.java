package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "TelemetryDemo", group = "POC")
public class TelemetryDemoOpMode extends LinearOpMode {
    int count = 0;
    ElapsedTime runtime;
    Telemetry.Line automonousGameplayLine;
    Telemetry.Line automonousGamePlaySMLine;
    Telemetry.Line parallelActionLine;
    Telemetry.Line parallelActionSMLine;
    Telemetry.Line serialTaskLine;
    Telemetry.Line serialTaskSMLine;
    Telemetry.Line componentLine;
    Telemetry.Line odometryLine;

    Telemetry.Item agCurrItem;
    Telemetry.Item agNextItem;
    Telemetry.Item stCurrItem;
    Telemetry.Item stNextItem;
    Telemetry.Item cpCurrItem;
    Telemetry.Item cpNextItem;
    Telemetry.Item odCurrItem;
    Telemetry.Item odNextItem;

    @Override
    public void runOpMode() {
        runtime = new ElapsedTime();
        runtime.reset();
        telemetry.setAutoClear(false);
        telemetry.addData("Status", "In init, Elapsed Time = " + runtime.milliseconds());
        automonousGameplayLine      = telemetry.addLine("1.GP  -");
        //automonousGamePlaySMLine    = telemetry.addLine("2.GPSM-");
        //parallelActionLine          = telemetry.addLine("3.PA  -");
        //parallelActionSMLine        = telemetry.addLine("4.PASM-");
        serialTaskLine              = telemetry.addLine("2.ST  -");
        //serialTaskSMLine            = telemetry.addLine("3.STSM-");
        componentLine               = telemetry.addLine("3.C   -");
        odometryLine                = telemetry.addLine("4.ODOM-");

        agCurrItem = automonousGameplayLine.addData("Curr",0);
        agNextItem = automonousGameplayLine.addData("Next",0);
        stCurrItem = serialTaskLine.addData("Curr",0);
        stNextItem = serialTaskLine.addData("Next",0);
        cpCurrItem = componentLine.addData("Curr",0);
        cpNextItem = componentLine.addData("Next",0);
        odCurrItem = odometryLine.addData("Curr",0);
        odNextItem = odometryLine.addData("Next",0);

        try {
            for (int ag = 1; ag < 7; ag++) {
                agCurrItem.setValue(ag);
                agNextItem.setValue(ag + 1);
                telemetry.update();
                Thread.sleep(500);
                for (int st = 1; st < 7; st++) {
                    stCurrItem.setValue(st);
                    stNextItem.setValue(st + 1);
                    telemetry.update();
                    Thread.sleep(500);
                    for (int cp = 1; cp < 7; cp++) {
                        cpCurrItem.setValue(cp);
                        cpNextItem.setValue(cp + 1);
                        telemetry.update();
                        Thread.sleep(500);
                        for (int od = 1; od < 7; od++) {
                            odCurrItem.setValue(od);
                            odNextItem.setValue(od + 1);
                            telemetry.update();
                            Thread.sleep(500);
                        }
                    }
                }
            }
        }catch (InterruptedException e) {
                e.printStackTrace();
        }
        while(true) {

        }
    }

    /*
    @Override
    public void loop() {
        telemetry.addData("Status",
                "Loop count = " + count++ + " , Elapsed Time = " + runtime.milliseconds());
    }
    */
}
