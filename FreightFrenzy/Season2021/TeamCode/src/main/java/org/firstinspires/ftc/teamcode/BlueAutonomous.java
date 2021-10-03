package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "BlueAutonomous", group = "UltimateGoalAutonomous")
public class BlueAutonomous extends OpMode {

    private AutonomousTasks auto;

    @Override
    public void init() {
        this.telemetry.setAutoClear(false);
        auto = new AutonomousTasks(this);
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
