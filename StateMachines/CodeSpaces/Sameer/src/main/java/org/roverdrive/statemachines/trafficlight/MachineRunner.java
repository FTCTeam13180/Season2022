package org.roverdrive.statemachines.trafficlight;

import java.util.Scanner;

public class MachineRunner {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		// Create the state machine object
		StateMachine sm = new StateMachine();

		// Initialize the state machine
		sm.init();

		boolean done = false;
		while (!done)
		{
			System.out.print("Enter an event: 0 (timer is running), 1 (timer expired) or enter -1 to stop the program: ");
			int eventEnteredByUser = scanner.nextInt();

			// Check for terminating condition
			if (eventEnteredByUser == -1)
			{
				System.out.println("Goodbye");
				done = true;
			}
			else if (eventEnteredByUser == StateMachine.EVENT_TIMER_RUNNING || eventEnteredByUser == StateMachine.EVENT_TIMER_EXPIRED) {

				// An event has occured. Call the main loop of the state machine with the event.
				sm.loop(eventEnteredByUser);
			}
		}

		// Close any resources that were used
		scanner.close();
	}
}
