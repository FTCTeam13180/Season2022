package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.abs;

public class Drive implements BasicCommand {

    enum State {
        INIT,
        EXECUTE,
        STOP
    }

    private OpMode op;
    private ParallelStateMachineOpMode.DIRECTION direction;
    private double speed;
    private double cms;
    private double timeoutMs;
    private DcMotor topr;
    private DcMotor topl;
    private DcMotor rearr;
    private DcMotor rearl;
    State state = State.INIT;
    ElapsedTime runtime;
    private static final double ROBO_DIAMETER_CM = 62.86;
    private static final double COUNTS_PER_MOTOR_REV = 1120 ;    // eg: Andymark Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 0.776 ;     // This is < 1.0 if geared up
    private static final double WHEEL_DIAMETER_CM = 10.16 ;
    private static final double COUNTS_PER_CM  = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * 3.1415);
    private static final double STRAIGHT_SLIPPAGE_CORRECTION = 96/97.5;
    private static final double SHIFT_SLIPPAGE_CORRECTION = 1.14;
    private static final double TURN_SLIPPAGE_CORRECTION = 90/89;
    private  static final double CMS_PER_DEGREE = 3.1415 * ROBO_DIAMETER_CM / 360;
    private  static final double COUNTS_PER_DEGREE = COUNTS_PER_CM * CMS_PER_DEGREE;

    private void setPower (double power) {
        topr.setPower(abs(power));
        topl.setPower(abs(power));
        rearr.setPower(abs(power));
        rearl.setPower(abs(power));
    }

    public void stopMotor() {
        topl.setPower(0);
        topr.setPower(0);
        rearl.setPower(0);
        rearr.setPower(0);
    }

    private void setRunMode (DcMotor.RunMode runMode) {
        topr.setMode(runMode);
        topl.setMode(runMode);
        rearr.setMode(runMode);
        rearl.setMode(runMode);
    }

    private void logCurrentPosition () {
        op.telemetry.addData("CurrentPosition:", "topr=%7d, topl=%7d, rearr=%7d, rearl=%7d",
            topr.getCurrentPosition(),
            topl.getCurrentPosition(),
            rearr.getCurrentPosition(),
            rearl.getCurrentPosition());
    }


    public void init(){
         op.telemetry.addData("Drive: ", "Resetting Encoders");
         setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
         op.telemetry.addData("Drive:", "Encoders reset");

         // Set Target Position
         setTargetPosition(direction, cms);

         // Turn On RUN_TO_POSITION
         setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

         // reset the timeout time and start motion.
         runtime = new ElapsedTime();

         runtime.reset();
    }

    public void execute(){
        // Based on direction call corresponding Move function
        setPower (speed);
        logCurrentPosition();

    }

    public void stop(){
        // Stop all motion;
        stopMotor();

        // Turn off RUN_TO_POSITION
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public Drive(ParallelStateMachineOpMode opMode, ParallelStateMachineOpMode.DIRECTION direction, double speed, double cms, double timeoutMs){
         this.topr = opMode.topr;
         this.topl = opMode.topl;
         this.rearr = opMode.rearr;
         this.rearl = opMode.rearl;
         this.direction = direction;
         this.speed = speed;
         this.cms = cms;
         this.timeoutMs = timeoutMs;
         op = opMode;
    }

    private boolean isBusy() {
        return topr.isBusy() || topl.isBusy() || rearr.isBusy() || rearl.isBusy();
    }

    private void setTargetPosition(ParallelStateMachineOpMode.DIRECTION direction, double cms) {
        // Determine new target position, and pass to motor controller
        if (direction == ParallelStateMachineOpMode.DIRECTION.FORWARD) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() + (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
        }
        else if (direction == ParallelStateMachineOpMode.DIRECTION.BACKWARD) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() - (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() - (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() - (cms * COUNTS_PER_CM * STRAIGHT_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() - (cms * COUNTS_PER_CM) * STRAIGHT_SLIPPAGE_CORRECTION));
        } else if (direction == ParallelStateMachineOpMode.DIRECTION.SHIFT_RIGHT) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() + (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() - (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() - (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() + (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
        } else if (direction == ParallelStateMachineOpMode.DIRECTION.SHIFT_LEFT) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() - (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() + (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() + (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() - (cms * COUNTS_PER_CM * SHIFT_SLIPPAGE_CORRECTION)));
        } else if (direction == ParallelStateMachineOpMode.DIRECTION.TURN_LEFT){
            topl.setTargetPosition((int) (topr.getCurrentPosition() - (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() + (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() - (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() + (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
        }
        else if (direction == ParallelStateMachineOpMode.DIRECTION.TURN_RIGHT) {
            topl.setTargetPosition((int) (topr.getCurrentPosition() + (cms * COUNTS_PER_DEGREE * TURN_SLIPPAGE_CORRECTION)));
            topr.setTargetPosition((int) (topl.getCurrentPosition() - (cms * COUNTS_PER_DEGREE * TURN_SLIPPAGE_CORRECTION)));
            rearl.setTargetPosition((int) (rearr.getCurrentPosition() + (cms * COUNTS_PER_DEGREE * TURN_SLIPPAGE_CORRECTION)));
            rearr.setTargetPosition((int) (rearl.getCurrentPosition() - (cms * COUNTS_PER_DEGREE *TURN_SLIPPAGE_CORRECTION)));
        }
    }



    public void run() {

        switch(state){

            case INIT:
                init();
                state = State.EXECUTE;
                break;

            case EXECUTE:
                execute();
                if (!isBusy() && (runtime.milliseconds() >= timeoutMs)) {
                    state = State.STOP;
                }
                break;

            case STOP:
                stop();
                break;


        }
    }

}
