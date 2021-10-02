package org.firstinspires.ftc.teamcode;

/*
 * Copyright (c) 2019 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.component.ChassisComponent;
import org.firstinspires.ftc.teamcode.component.GrabberComponent;
import org.firstinspires.ftc.teamcode.component.IntakeComponent;
import org.firstinspires.ftc.teamcode.component.LauncherComponent;
import org.firstinspires.ftc.teamcode.component.StackerComponent;
import org.firstinspires.ftc.teamcode.component.WingsComponent;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import java.util.*;

@TeleOp
public class WebcamOpenCv extends LinearOpMode
{
OpenCvCamera webcam;
    private OpMode op;
    private ChassisComponent chassisComponent;
    private IntakeComponent intakeComponent;
    private GrabberComponent grabberComponent;
    private LauncherComponent launcherComponent;
    private WingsComponent wingsComponent;
    private Telemetry.Item log_angle;


    private StackerComponent stackerComponent;

    double power = 1.0;
    private static double POWERSHOT_RPM = 1750;
    private static double HIGHGOAL_RPM = 1950;
    private static double RPM_TOLERANCE = 20;
    private static double ONE_WHACK_TIMEOUT_MS = 1000;

    boolean powershot_mode = false;
    boolean gamepad2_y_being_pressed = false;
    boolean gamepad1_x_being_pressed = false;
    double angle = 0;


@Override
public void runOpMode()
{
/*
 * Instantiate an OpenCvCamera object for the camera we'll be using.
 * In this sample, we're using a webcam. Note that you will need to
 * make sure you have added the webcam to your configuration file and
 * adjusted the name here to match what you named it in said config file.
 *
 * We pass it the view that we wish to use for camera monitor (on
 * the RC phone). If no camera monitor is desired, use the alternate
 * single-parameter constructor instead (commented out below)
 */
int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
    grabberComponent = new GrabberComponent(this);
    grabberComponent.init();

    chassisComponent = new ChassisComponent(this);
    chassisComponent.init();
    chassisComponent.initIMU();

    launcherComponent = new LauncherComponent(this);
    launcherComponent.init();

    intakeComponent = new IntakeComponent(this);
    intakeComponent.init();

    wingsComponent = new WingsComponent(this);
    wingsComponent.init();


    stackerComponent = new StackerComponent(this);
    stackerComponent.init();
    log_angle = this.telemetry.addData("log_angle:", chassisComponent.getAngle() * 180 / Math.PI - 90);

    SmartWhack smartWhack = new SmartWhack(this, launcherComponent, stackerComponent);
    SamplePipeline pipeline = new SamplePipeline();
    webcam.setPipeline(pipeline);

// OR...  Do Not Activate the Camera Monitor View
//webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));

/*
 * Specify the image processing pipeline we wish to invoke upon receipt
 * of a frame from the camera. Note that switching pipelines on-the-fly
 * (while a streaming session is in flight) *IS* supported.
 */

/*
 * Open the connection to the camera device. New in v1.4.0 is the ability
 * to open the camera asynchronously, and this is now the recommended way
 * to do it. The benefits of opening async include faster init time, and
 * better behavior when pressing stop during init (i.e. less of a chance
 * of tripping the stuck watchdog)
 *
 * If you really want to open synchronously, the old method is still available.
 */
webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
{
@Override
public void onOpened()
{
/*
 * Tell the webcam to start streaming images to us! Note that you must make sure
 * the resolution you specify is supported by the camera. If it is not, an exception
 * will be thrown.
 *
 * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
 * supports streaming from the webcam in the uncompressed YUV image format. This means
 * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
 * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
 *
 * Also, we specify the rotation that the webcam is used in. This is so that the image
 * from the camera sensor can be rotated such that it is always displayed with the image upright.
 * For a front facing camera, rotation is defined assuming the user is looking at the screen.
 * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
 * away from the user.
 */
    webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);


}
});

telemetry.addLine("Running");
telemetry.update();

/*
 * Wait for the user to press start on the Driver Station
 */
waitForStart();

    while (opModeIsActive())
    {
    /*
     * Send some stats to the telemetry
     */
        if (gamepad1.a) {
            chassisComponent.initIMU();
        }
        if (gamepad1.x) {
            log_angle.setValue(chassisComponent.getAngle() * 180 / Math.PI - 90);
        }

        if (Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1
                || (Math.abs(gamepad1.right_stick_x) > 0.1)) {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y; // note: joystick y is reversed.
            double turn = -gamepad1.right_stick_x; //for driver specifically arnav who has practiced the other way
            double power = Math.sqrt(x * x + y * y);
            power = (power > 0) ? power : Math.abs(turn);
            chassisComponent.fieldCentricDrive(x, y, power, false, turn);
        } else if (gamepad1.dpad_up) {
            chassisComponent.moveForward(power * 0.5);
        } else if (gamepad1.dpad_down) {
            chassisComponent.moveBackward(power * 0.5);
        } else {
            chassisComponent.stop();
        }

        if (gamepad1.right_bumper) {
            intakeComponent.expel();
        } else if (gamepad1.left_bumper) {
            intakeComponent.in();
        } else {
            intakeComponent.stop();
        }

        // Wings Controls
        if (gamepad1.x) {
            if (!gamepad1_x_being_pressed) {
                gamepad1_x_being_pressed = true;
                wingsComponent.toggleWings();
            }
        } else
            gamepad1_x_being_pressed = false;


        //
        //gamepad 2
        //

        if (gamepad2.x) {
            stackerComponent.stackerDump();
        }
        if (gamepad2.left_bumper) {
            stackerComponent.safeWhack();
        } else if (gamepad2.dpad_up) {
            stackerComponent.stackerUp();
            wingsComponent.wingsUp();
        } else if (gamepad2.dpad_down) {
            stackerComponent.stackerDown();
            wingsComponent.wingsDown();
        }

        if (gamepad2.right_bumper) {
            // Note: safeWhackThree sleeps for a while which introduces race condition between Driver 1 stopping
            // the robot right before driver 2 presses safeWhackThree. Robot continues to move during safeWhackThree
            // if driver 1 stop does not get registered in time.
            // Fix: Force robot to stop moving before we go in this long (sleep) thread.
            // Do this only if launcher is also running to ensure the intent is to shoot rather than unjam.
            if (gamepad2.right_stick_y < 0)
                chassisComponent.stop();

            if (powershot_mode)
                smartWhack.whack(1, POWERSHOT_RPM, POWERSHOT_RPM + RPM_TOLERANCE, ONE_WHACK_TIMEOUT_MS);
            else {
                smartWhack.whack(3, HIGHGOAL_RPM, HIGHGOAL_RPM + RPM_TOLERANCE, 1500);
                stackerComponent.stackerDown();
            }
        }


        if (gamepad2.y) {
            if (!gamepad2_y_being_pressed) {
                gamepad2_y_being_pressed = true;
                powershot_mode = !powershot_mode;
            }
        } else
            gamepad2_y_being_pressed = false;

        if (gamepad2.right_stick_y < 0) {
            if (powershot_mode)
                launcherComponent.setTargetRPM(POWERSHOT_RPM);
            else {
                launcherComponent.setTargetRPM(HIGHGOAL_RPM);
            }
        } else if (gamepad2.right_stick_y > 0) {
            launcherComponent.reverse();
        } else {
            launcherComponent.stop();
        }

        //wobble arm controls
        if (gamepad2.dpad_left) {
            grabberComponent.clawOpen();
        } else if (gamepad2.dpad_right) {
            grabberComponent.clawClose();
        }
        if (Math.abs(gamepad2.left_stick_y) > 0.1) {
            if (gamepad2.left_stick_y < 0) {
                grabberComponent.clawClose();
                grabberComponent.armStraight();
            }
            if (gamepad2.left_stick_y > 0) {
                grabberComponent.armDown();
            }
        }


        this.telemetry.update();
    /*
     * NOTE: stopping the stream from the camera early (before the end of the OpMode
     * when it will be automatically stopped for you) *IS* supported. The "if" statement
     * below will stop streaming from the camera when the "A" button on gamepad 1 is pressed.
     */
        if(gamepad2.a)
        {
            //webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            launcherComponent.setTargetRPM(pipeline.getRPM());
            stackerComponent.stackerUp();
            sleep(100);
            String result = pipeline.getResult();
            telemetry.addData("Result2:", result);
            telemetry.update();
            double rpm = 1750;
            while (!result.equals("shoot"))
            {
                if (result.equals("left"))
                    chassisComponent.spinLeft(.1);
                else if (result.equals("right"))
                    chassisComponent.spinRight(.1);
                result = pipeline.getResult();
                rpm = pipeline.getRPM();
            }
            launcherComponent.setTargetRPM(rpm);
            chassisComponent.stop();
            angle = chassisComponent.getAngle();
            chassisComponent.spinToXDegree(angle + Math.toRadians(-10) - Math.PI/2, .03, .4);
            chassisComponent.spinToXDegree(angle + Math.toRadians(-10) - Math.PI/2, .02, .2);
            chassisComponent.stop();
            /*stackerComponent.safeWhack();
            sleep(500);
            stackerComponent.safeWhack();
            sleep(500);
            stackerComponent.safeWhack();*/
            smartWhack.whack(3, rpm - 25, rpm + 25, 3000);
            //webcam.stopStreaming();
            launcherComponent.stop();
            stackerComponent.stackerDown();
        }
    }
}

/*
 * An example image processing pipeline to be run upon receipt of each frame from the camera.
 * Note that the processFrame() method is called serially from the frame worker thread -
 * that is, a new camera frame will not come in while you're still processing a previous one.
 * In other words, the processFrame() method will never be called multiple times simultaneously.
 *
 * However, the rendering of your processed image to the viewport is done in parallel to the
 * frame worker thread. That is, the amount of time it takes to render the image to the
 * viewport does NOT impact the amount of frames per second that your pipeline can process.
 *
 * IMPORTANT NOTE: this pipeline is NOT invoked on your OpMode thread. It is invoked on the
 * frame worker thread. This should not be a problem in the vast majority of cases. However,
 * if you're doing something weird where you do need it synchronized with your OpMode thread,
 * then you will need to account for that accordingly.
 */
class SamplePipeline extends OpenCvPipeline
{
    boolean viewportPaused;
    double xAverage;
    double yAverage;
    String result;
    double rpm;

    public SamplePipeline(){result = "left"; rpm = 1700;}

/*
 * NOTE: if you wish to use additional Mat objects in your processing pipeline, it is
 * highly recommended to declare them here as instance variables and re-use them for
 * each invocation of processFrame(), rather than declaring them as new local variables
 * each time through processFrame(). This removes the danger of causing a memory leak
 * by forgetting to call mat.release(), and it also reduces memory pressure by not
 * constantly allocating and freeing large chunks of memory.
 */

@Override
    public Mat processFrame(Mat input)
    {
        /*
         * IMPORTANT NOTE: the input Mat that is passed in as a parameter to this method
         * will only dereference to the same image for the duration of this particular
         * invocation of this method. That is, if for some reason you'd like to save a copy
         * of this particular frame for later use, you will need to either clone it or copy
         * it to another Mat.
         */

        /*
         */
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV);

        Scalar lowerblack = new Scalar(100, 100, 150);         // lower color border for BLACK
        Scalar upperblack = new Scalar(140, 200, 255);      // upper color border for BLACK

        double x_average = 0;
        double y_average = 0;

        Core.inRange(input, lowerblack, upperblack, input); // select only blue pixels
        /*for (int i = 0; i < input.rows(); i++)
        {
            for (int j = 0; j < input.cols(); j++)
            {
                if (input.get(i, j)[0] == 255)
                {
                    x_average += j;
                    y_average += i;
                    num_x++;
                    num_y++;
                }
            }
        }*/
        Moments moments = Imgproc.moments(input);
        double tempX;
        double tempY;
        tempX = moments.get_m10() / moments.get_m00();
        tempY = moments.get_m01() / moments.get_m00();
        if (Double.isNaN(tempX))
            x_average = 0;
        else
            x_average = tempX;
        if(Double.isNaN(tempY))
            y_average = 0;
        else
            y_average = tempY;
        //x_average /= num_x;
        //y_average /= num_y;
        Imgproc.line(input, new Point(x_average, 0), new Point(x_average, input.cols()), new Scalar(60,255,255), 3);
        Imgproc.line(input, new Point(0, y_average), new Point(input.rows(), y_average), new Scalar(60,255,255), 3);
        xAverage = x_average;
        yAverage = y_average;
        //double percent = Core.countNonZero(input) / (input.rows() * input.cols());

        if (x_average < (input.cols() / 2 - 50))
            result = "left";
        else if (x_average > (input.cols() / 2 + 50))
            result = "right";
        else
            result = "shoot";
        if (y_average < input.rows() / 3)
            rpm = 1825;
        else if (y_average < input.rows() / 1.75)
            rpm = 1750;
        else
            rpm = 1675;

/**
 * NOTE: to see how to get data from your pipeline to your OpMode as well as how
 * to change which stage of the pipeline is rendered to the viewport when it is
 * tapped, please see {@link PipelineStageSwitchingExample}
 */

    return input;
    }


@Override
    public void onViewportTapped()
    {
    /*
     * The viewport (if one was specified in the constructor) can also be dynamically "paused"
     * and "resumed". The primary use case of this is to reduce CPU, memory, and power load
     * when you need your vision pipeline running, but do not require a live preview on the
     * robot controller screen. For instance, this could be useful if you wish to see the live
     * camera preview as you are initializing your robot, but you no longer require the live
     * preview after you have finished your initialization process; pausing the viewport does
     * not stop running your pipeline.
     *
     * Here we demonstrate dynamically pausing/resuming the viewport when the user taps it
     */

        viewportPaused = !viewportPaused;

        if(viewportPaused)
        {
        webcam.pauseViewport();
        }
        else
        {
        webcam.resumeViewport();
        }
    }

    public double getRPM()
    {
        return rpm;
    }

    public String getResult()
    {
        return result;
    }
}
}
