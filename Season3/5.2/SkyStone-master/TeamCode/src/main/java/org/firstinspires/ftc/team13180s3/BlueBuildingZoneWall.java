package org.firstinspires.ftc.team13180s3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="BlueBuildingZoneWall", group="autonomusGroup1")
public class BlueBuildingZoneWall extends LinearOpMode {
    private BuildingZoneFullAuto fullAuto;

    @Override
    public void runOpMode() {
        fullAuto = new BuildingZoneFullAuto(
                this,
                BuildingZoneFullAuto.Alliance.BLUE,
                BuildingZoneFullAuto.Parking.WALL);



        waitForStart();

        while (opModeIsActive()) {
            fullAuto.run();
            break;
        }
    }
}
