package org.firstinspires.ftc.team13180s3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Grabber {
    public LinearOpMode opMode;

    //grab servo for actual open/close mechanism
    private Servo grab;

    //spinner servo for the rotation on grabber
    private Servo spinner;

    private Servo capstone;

    private final double FINE_ROTATION_INCREMENT = 0.1;

    Grabber(LinearOpMode op) {
        opMode = op;
    }

    public void init() {
        grab = opMode.hardwareMap.get(Servo.class, "Grabber");
        spinner = opMode.hardwareMap.get(Servo.class, "Spinner");
        capstone = opMode.hardwareMap.get(Servo.class, "Capstone");

    }

    public void grabIn() {
        grab.setPosition(1);
    }

    public void release() {
        grab.setPosition(0);
    }

    public void dropCapstone() {
        capstone.setPosition(1);
    }

    public void holdCapstone() {
        capstone.setPosition(0);
    }

    public void rotateToDegrees_0()
    {
        spinner.setPosition(0);
    }

    public void rotateToDegrees_90()
    {

        spinner.setPosition(0.5);
    }

    public void rotateToDegrees_180()
    {

        spinner.setPosition(1.0);
    }

    public void rotateIncrementalClockwise() {
        double currPosition = spinner.getPosition();

        currPosition += FINE_ROTATION_INCREMENT;

        if (currPosition > 1.0)
            currPosition = 1.0;

        spinner.setPosition(currPosition);
    }

    public void rotateIncrementalAntiClockwise()
    {
        double currPosition = spinner.getPosition();

        currPosition -= FINE_ROTATION_INCREMENT;

        if (currPosition < 0)
            currPosition = 0;

        spinner.setPosition(currPosition);
    }

}

