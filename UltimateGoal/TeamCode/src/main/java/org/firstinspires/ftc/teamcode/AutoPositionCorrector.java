package org.firstinspires.ftc.teamcode;

public class AutoPositionCorrector {

    private static double TOLERANCE = 1.5;
    private static int MAX_LOG_SIZE = 30;
    private RobotPosition[] positionLogRingBuffer = new RobotPosition[MAX_LOG_SIZE];
    private int nextIndex = 0;
    private int numElements = 0;
    private Waypoint destination;

    public AutoPositionCorrector(Waypoint p) {
        destination = p;
    }

    public void logPosition(double x, double y) {
        RobotPosition position = new RobotPosition(x,y);
        positionLogRingBuffer[nextIndex] = position;
        incrementNext();
        incrementNumElements();
    }

    public boolean correctionDone() {
        return getMeanDeviation() <= TOLERANCE;
    }

    private void incrementNext() {
        nextIndex++;
        nextIndex %= MAX_LOG_SIZE;
    }

    private void incrementNumElements() {
        numElements++;
        if (numElements > MAX_LOG_SIZE)
            numElements = MAX_LOG_SIZE;
    }

    private double getMeanDeviation() {
        double deviation = 0;
        for (int i = 0; i < numElements; i++) {
            RobotPosition rp = positionLogRingBuffer[i];
            deviation += Math.hypot(destination.getY()-rp.getY(), destination.getX()-rp.getX());
        }
        return deviation/numElements;
    }
}
