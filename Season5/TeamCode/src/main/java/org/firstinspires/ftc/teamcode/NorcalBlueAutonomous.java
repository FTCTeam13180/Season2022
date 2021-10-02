package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "NorcalBlueAutonomous", group = "UltimateGoalAutonomous")
public class NorcalBlueAutonomous extends OpMode {

    private NorcalAutonomousTasks auto;

    @Override
    public void init() {
        this.telemetry.setAutoClear(false);
        auto = new NorcalAutonomousTasks(this);
        auto.init();
    }

    @Override
    public void init_loop(){
        auto.detectNumberOfRings();
    }

    @Override
    public void loop(){
        auto.run();
    }

}
