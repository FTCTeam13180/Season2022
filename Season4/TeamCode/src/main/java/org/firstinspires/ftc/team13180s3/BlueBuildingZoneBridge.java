package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="BlueBuildingZoneBridge", group="autonomusGroup1")
public class BlueBuildingZoneBridge extends LinearOpMode {
    private BuildingZoneFullAuto fullAuto;

    @Override
    public void runOpMode() {
        fullAuto = new BuildingZoneFullAuto(
                this,
                BuildingZoneFullAuto.Alliance.BLUE,
                BuildingZoneFullAuto.Parking.BRIDGE);

        waitForStart();

        while (opModeIsActive()) {
            fullAuto.run();
            break;
        }
    }
}

