package org.roverdrive.statemachines.trafficlight;

import org.roverdrive.statemachines.trafficlight.StateMachine.State;

public class TrafficLightTest {

	private static StateMachine sm;
	private static int passed;
	private static int failed;
	private static int total;

    private static void setUp() {
		// Create the state machine object
		sm = new StateMachine();
    }

    private static void testRedRunning() {
		sm.setState(State.RED);
		sm.loop(StateMachine.EVENT_TIMER_RUNNING);
		State newState = sm.getState();
		
		if (State.RED.equals(newState)) {
			System.out.println("Red-Running passed");
			passed++;
		} else {
			System.out.println("Red-Running failed");
			failed++;
		}
		total++;
    }

    private static void testRedExpired() {
		sm.setState(State.RED);
		sm.loop(StateMachine.EVENT_TIMER_EXPIRED);
		State newState = sm.getState();

		if (State.GREEN.equals(newState)) {
			System.out.println("Red-Expired passed");
			passed++;
		} else {
			System.out.println("Red-Expired failed");
			failed++;
		}
		total++;
    }

    private static void testGreenRunning() {
		sm.setState(State.GREEN);
		sm.loop(StateMachine.EVENT_TIMER_RUNNING);
		State newState = sm.getState();
		
		if (State.GREEN.equals(newState)) {
			System.out.println("Green-Running passed");
			passed++;
		} else {
			System.out.println("Green-Running failed");
			failed++;
		}
		total++;
    }

    private static void testGreenExpired() {
		sm.setState(State.GREEN);
		sm.loop(StateMachine.EVENT_TIMER_EXPIRED);
		State newState = sm.getState();

		if (State.YELLOW.equals(newState)) {
			System.out.println("Green-Expired passed");
			passed++;
		} else {
			System.out.println("Green-Expired failed");
			failed++;
		}
		total++;
    }

    private static void testYellowRunning() {
		sm.setState(State.YELLOW);
		sm.loop(StateMachine.EVENT_TIMER_RUNNING);
		State newState = sm.getState();
		
		if (State.YELLOW.equals(newState)) {
			System.out.println("Yellow-Running passed");
			passed++;
		} else {
			System.out.println("Yellow-Running failed");
			failed++;
		}
		total++;
    }

    private static void testYellowExpired() {
		sm.setState(State.YELLOW);
		sm.loop(StateMachine.EVENT_TIMER_EXPIRED);
		State newState = sm.getState();

		if (State.RED.equals(newState)) {
			System.out.println("Yellow-Expired passed");
			passed++;
		} else {
			System.out.println("Yellow-Expired failed");
			failed++;
		}
		total++;
    }

    private static void printStats() {
    	System.out.println("-------------------------------------------------------");
    	System.out.println("PASSED tests: " + passed);
       	System.out.println("FAILED tests: " + failed);
       	System.out.println("TOTAL tests: " + total);
      	System.out.println("-------------------------------------------------------");
    }
	public static void main(String[] args) {

		setUp();
		testRedRunning();
		testRedExpired();
		testGreenRunning();
		testGreenExpired();
		testYellowRunning();
		testYellowExpired();
		printStats();

	}
}
