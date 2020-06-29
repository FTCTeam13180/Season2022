package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="RedBuildingZoneBridge", group="autonomusGroup1")
public class RedBuildingZoneBridge extends LinearOpMode {
    private BuildingZoneFullAuto fullAuto;

    @Override
    public void runOpMode() {
        fullAuto = new BuildingZoneFullAuto(
                this,
                BuildingZoneFullAuto.Alliance.RED,
                BuildingZoneFullAuto.Parking.BRIDGE);

        waitForStart();

        while (opModeIsActive()) {
            fullAuto.run();
            break;
        }
    }
}

