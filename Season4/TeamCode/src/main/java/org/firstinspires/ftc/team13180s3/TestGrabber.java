package org.firstinspires.ftc.team13180s3;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestGrabber", group="manualmode")
public class TestGrabber extends LinearOpMode {

    public Grabber grabber;
    @Override
    public void runOpMode() {
        double spinnerPosition = 0;
        grabber =new Grabber(this);
        grabber.init();
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                grabber.grabIn();
            }
            else if (gamepad1.b){
                grabber.release();
            }
            else if (gamepad1.x){
                //need to know from hardwarreteam whatis the starting position
                grabber.rotateToDegrees_0();
            }
            else if (gamepad1.y) {
                grabber.rotateToDegrees_90();
            }
            else if (gamepad1.b) {
                grabber.rotateToDegrees_180();
            }
            else if (gamepad1.left_bumper) {
                grabber.rotateIncrementalAntiClockwise();
            }
            else if (gamepad1.right_bumper) {
                grabber.rotateIncrementalClockwise();
            }
        }
    }
}