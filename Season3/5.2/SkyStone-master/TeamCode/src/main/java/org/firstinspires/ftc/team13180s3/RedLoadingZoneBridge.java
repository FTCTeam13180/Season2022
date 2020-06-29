package org.firstinspires.ftc.team13180s3;
//osmehting
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="RedLoadingZoneBridge", group="autonomusGroup1")
public class RedLoadingZoneBridge extends LinearOpMode {
    private LoadingZoneFullAuto fullAuto;
    @Override
    public void runOpMode() {
        fullAuto= new LoadingZoneFullAuto(this, LoadingZoneFullAuto.Alliance.RED, LoadingZoneFullAuto.Parking.BRIDGE);
        fullAuto.init();
        waitForStart();
        while(opModeIsActive()){
            fullAuto.run();
            break;
        }

    }
}
