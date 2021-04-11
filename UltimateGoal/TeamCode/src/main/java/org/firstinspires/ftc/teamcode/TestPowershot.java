package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.LauncherComponent;
import org.firstinspires.ftc.teamcode.component.StackerComponent;

@TeleOp(name = "TestPowershot", group ="teleop")
public class TestPowershot extends LinearOpMode {

    enum PowerShot{
        POWER_SHOT_LEFT,
        POWER_SHOT_MIDDLE,
        POWER_SHOT_RIGHT,
        DONE
    }

    LauncherComponent launcherComp;
    StackerComponent stackerComponent;
    ChassisComponent chassisComponent;
    Odometry odometry;
    Waypoint LEFT_STUMP;
    Waypoint MIDDLE_STUMP;
    Waypoint RIGHT_STUMP;
    PowerShot powerShot;

    double pastTime = 0;
    double time = 0;
    boolean run = false;
    int count = 0;
    boolean gamepad2_left_bumper_being_pressed = false;
    boolean gamepad2_a_being_pressed = false;
    double rpm = 1000;

    public void runOpMode() {
        powerShot= PowerShot.POWER_SHOT_LEFT;
        LEFT_STUMP = new Waypoint(131, 360, 0);
        MIDDLE_STUMP = new Waypoint(150, 360, 0);
        RIGHT_STUMP = new Waypoint(169, 360, 0);

        launcherComp = new LauncherComponent(this);
        launcherComp.init();
        stackerComponent = new StackerComponent(this);
        stackerComponent.init();
        chassisComponent = new ChassisComponent(this);
        chassisComponent.init();
        chassisComponent.initIMU();
        odometry = new Odometry(this, chassisComponent, 97, 45);
        odometry.init();

        telemetry.setAutoClear(false);
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {

            if (gamepad2.left_bumper) {
                if (!gamepad2_left_bumper_being_pressed) {
                    gamepad2_left_bumper_being_pressed = true;
                    rpm += 50;
                    launcherComp.setTargetRPM(rpm);
                }
            }
            else {
                gamepad2_left_bumper_being_pressed = false;
            }

            if (gamepad2.a){
                if (!gamepad2_a_being_pressed) {
                    gamepad2_a_being_pressed = true;
                    rpm -= 50;
                    launcherComp.setTargetRPM(rpm);
                }
            } else {
                gamepad2_a_being_pressed = false;
            }

            if (gamepad2.right_bumper){
                this.telemetry.addData("", "RPM: %.0f ANGLE: %f",  launcherComp.getRPM(), chassisComponent.getAngle());
                switch (powerShot){
                    case POWER_SHOT_LEFT:
                        shootPowerShot(LEFT_STUMP.getX(), LEFT_STUMP.getY());
                        powerShot = PowerShot.POWER_SHOT_MIDDLE;
                        break;
                    case POWER_SHOT_MIDDLE:
                        shootPowerShot(MIDDLE_STUMP.getX(), MIDDLE_STUMP.getY());
                        powerShot = PowerShot.POWER_SHOT_RIGHT;
                        break;
                    case POWER_SHOT_RIGHT:
                        shootPowerShot(RIGHT_STUMP.getX(), RIGHT_STUMP.getY());
                        powerShot = PowerShot.POWER_SHOT_LEFT;
                        break;
                    case DONE:
                        break;
                }
            }
            if(gamepad2.dpad_up){
                stackerComponent.stackerUp();
            }
            if (gamepad2.dpad_down){
                stackerComponent.stackerDown();
            }


            if (gamepad2.x && !run) {
//                launcherComp.shoot();
                runtime.reset();
                run = true;
                count = 0;
            }
/*            if (run){
                time = runtime.milliseconds();
                if (time - pastTime >= 20) {
                    count++;
//                    this.telemetry.addLine("RPM:" + launcherComp.getRPM() + " Time: " + time);
                    this.telemetry.addData("Count:", "%d Time: %.0f  RPM: %.0f ANGLE: %.0f", count, time, launcherComp.getRPM(), chassisComponent.getAngle()/Math.PI/2);
                    pastTime = runtime.milliseconds();
                }
             }

 */
            if (gamepad2.y)
                launcherComp.stop();
            telemetry.update();
        }
    }

    public void shootPowerShot(double x, double y){
        launcherComp.setTargetRPM(1700);
        final double MARGIN = 8;
        double robotX = odometry.getCurrentX();
        double robotY = odometry.getCurrentY();
        double thetaRobot = chassisComponent.getAngle();
        Waypoint marginLeft = new Waypoint(x - MARGIN, y, 0);
        Waypoint marginRight = new Waypoint(x + MARGIN, y, 0);
        double distanceLX = Math.abs(marginLeft.getX() - robotX);
        double distanceY = Math.abs(marginRight.getY() - robotY);
        double thetaL = Math.atan(distanceLX/distanceY);
        double distanceRX = Math.abs(marginRight.getX() - robotX);
        double thetaR = Math.atan(distanceRX/distanceY);
        double thetaShoot = (thetaL + thetaR)/2;

        if (   (thetaL <= thetaRobot && thetaRobot <= thetaR)
                || (thetaL >= thetaRobot && thetaRobot >= thetaR)){
            stackerComponent.safeWhack();
        } else {
            // if (thetaRobot > thetaShoot || thetaRobot < thetaShoot){
            chassisComponent.spinToXDegree(thetaShoot, 0, 0.5);
            stackerComponent.safeWhack();
        }
        this.telemetry.addData("", "thetaL: %f thetaR: %f thetaRobot: %f thetaShoot: %f",
                thetaL, thetaR, thetaRobot, thetaShoot);
    }
}
