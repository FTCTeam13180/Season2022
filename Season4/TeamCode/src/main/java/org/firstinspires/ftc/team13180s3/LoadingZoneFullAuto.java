package org.firstinspires.ftc.team13180s3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;


public class LoadingZoneFullAuto {
    private LinearOpMode opMode;
    private RoboNavigator robotNavigator;
    private Hook hook;
    private Grabber grab;
    private Detector detect;
    private Elevator elevator;
    private Alliance alliance;
    private Parking parking;
    private double NAVIGATOR_POWER=0.5;
    boolean foundSkytone = false;
    public enum Alliance {
        BLUE,
        RED
    }

    public enum Parking {
        BRIDGE,
        WALL
    }

    LoadingZoneFullAuto (LinearOpMode op, Alliance al, Parking pa) {
        this.opMode = op;
        this.alliance = al;
        this.parking = pa;
    }

    private int skystone() {
        opMode.sleep(250);
        List<Recognition> blocks = detect.scan();

        if (blocks == null || blocks.size() != 2) {
            if(alliance == Alliance.RED){
                robotNavigator.shiftRight(3, 10000);
            }
            else{
                robotNavigator.shiftRight(3, 10000);
            }
            opMode.sleep(250);
            blocks = detect.scan();
        }

        int numblocks = 0;

        if (blocks!= null)
            numblocks = blocks.size();

        int position = 0;

        // Align to the skystone
        if (numblocks == 2) {
            for (Recognition recog : blocks) {
                if ((recog.getLabel().equalsIgnoreCase(Detector.LABEL_SECOND_ELEMENT))&&(((recog.getImageWidth()/recog.getImageHeight()))<1.6)) {
                    foundSkytone = true;

                    //Closer to bridge if i == 0
                    if (recog.getLeft() > 200) {
                        if (alliance == Alliance.RED) {
                            position = 0;
                            robotNavigator.shiftRight(14.5, 10000);
                        } else {
                            position = 0;
                            robotNavigator.shiftLeft(11.5, 10000);
                        }
                    }
                    //second closest to bridge if i == 1;
                    else if (recog.getLeft() < 100) {
                        if (alliance == Alliance.RED) {
                            position = 1;
                            robotNavigator.shiftRight(5, 10000);
                        } else {
                            position = 2;
                            robotNavigator.shiftRight(4, 10000);
                        }
                    }

                    opMode.telemetry.addData("found skytone i=", position);
                    break;
                }
            }
        }
        else{
            // Did not find skytone, so assume skystone is the 3rd block
            opMode.telemetry.addData("Skystone: ", "Not Detected");
            if (alliance == Alliance.RED) {
                position = 2;
                robotNavigator.shiftLeft(7, 10000);
            } else {
                position = 2;
                robotNavigator.shiftRight(4, 10000);
            }
        }

        if(!foundSkytone){
            opMode.telemetry.addData("Skystone: ", "Not Found, but 2 normal stones were");
            if (alliance == Alliance.RED) {
                position = 2;
                robotNavigator.shiftLeft(4, 10000);
            } else {
                position = 1;
                robotNavigator.shiftLeft(7, 10000);
            }
        }
        opMode.telemetry.update();
        robotNavigator.setNavigatorPower(0.7);
        robotNavigator.moveForward(23.5, 10000);
        robotNavigator.setNavigatorPower(1.0);
        return position;
    }

    public void init(){
        robotNavigator = new RoboNavigator(opMode);
        robotNavigator.init();
        robotNavigator.setNavigatorPower(1);
        hook = new Hook(opMode);
        hook.init();
        hook.detach();

        grab = new Grabber(opMode);
        grab.init();
        grab.release();

        elevator = new Elevator(opMode);
        elevator.init();

        detect = new Detector(opMode);
        detect.init();



        opMode.telemetry.addLine("Front of Robot Should be forward. Since forward is forward");
        opMode.telemetry.update();
    }
    public void run() {
        elevator.playposition();
        grab.rotateToDegrees_0();
        robotNavigator.setNavigatorPower(1.0);
        robotNavigator.moveForward(6,1000);
        int position=skystone();

        grab.grabIn();
        opMode.sleep(250);
        elevator.goUpByInches(1,0.8);
        robotNavigator.moveBackward(7,1000);
        if(alliance==Alliance.BLUE){
            robotNavigator.turnLeft(90,5000);
        }
        else{
            robotNavigator.turnRight(90,5000);
        }
        //moving to foundation after gettign first skystone based on position
        if(position==0){
            robotNavigator.moveForward(73,50000);
        }
        else if(position==1){
            robotNavigator.moveForward(81,50000);
        }
        else{
            robotNavigator.moveForward(89,50000);
        }
        elevator.goUpByInches(1,3);
        if(alliance==Alliance.BLUE)
            robotNavigator.turnRight(90,5000);
        else
            robotNavigator.turnLeft(90,5000);
        hook.halfattach(.4);
 //       opMode.sleep(100);
        robotNavigator.setNavigatorPower(0.6);
        robotNavigator.moveForward(12,10000);
        hook.attach();
        opMode.sleep(100);

        robotNavigator.moveForward(1.5,5000);
        robotNavigator.setNavigatorPower(1.0);
        grab.release();
        opMode.sleep(100);
        robotNavigator.moveBackward(30,5000);

        if(alliance == Alliance.RED){
            robotNavigator.shiftLeft(24,5000);
            robotNavigator.shiftRight(6,5000);
        }
        else{
            robotNavigator.shiftRight(24,5000);
            robotNavigator.shiftLeft(6,5000);
        }
        //problems
        int turnAngle=115;
        //have to test turn angle
        if(alliance == Alliance.RED){
            robotNavigator.turnRight(turnAngle,10000);
        }
        else{
            robotNavigator.turnLeft(turnAngle,10000);
        }
        hook.detach();
        robotNavigator.moveForward(25,10000);
        robotNavigator.moveBackward(10,10000);
        elevator.goDownByInches(1,3);
        if(position==1){
            robotNavigator.moveBackward(94,50000);
        }
        else if(position==0){
            robotNavigator.moveBackward(86,50000);
        }
        else{
            robotNavigator.moveBackward(60,50000);
        }
        if(alliance == Alliance.RED){
            robotNavigator.turnLeft(90,10000);
        }
        else{
            robotNavigator.turnRight(90,10000);
        }
        robotNavigator.moveForward(10,50000);
        grab.grabIn();
        opMode.sleep(250);
        robotNavigator.moveBackward(10,50000);
        if(alliance == Alliance.RED){
            robotNavigator.turnRight(90,10000);
        }
        else{
            robotNavigator.turnLeft(90,10000);
        }
        if(position==1){
            robotNavigator.moveForward(72,50000);
        }
        else if(position==0){
            robotNavigator.moveForward(64,50000);
        }
        else{
            robotNavigator.moveForward(48,50000);
        }
        grab.release();
        opMode.sleep(100);
        robotNavigator.moveBackward(16,20000);
        //REEEEEEEEEEEEEEEEEEEEEEEE WE'RE DONE HHASJHAHAHAHAHAHAHAHHAHAHHAHHAHREEEEEEEEEEEEEEEEEEEEEEEEEE OOOOOHOHOOOOOHOHOHOOHOHOOHOHOHOHOHO HAHAHHAHAHAHA ~Rohan Gulati


    }

}