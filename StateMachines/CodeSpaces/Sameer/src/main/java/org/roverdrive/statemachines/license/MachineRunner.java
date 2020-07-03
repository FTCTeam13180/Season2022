package org.roverdrive.statemachines.license;

import java.util.Scanner;

/**
 * Main entry point for the state machine exercise
 */
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
			System.out.println(Event.getPrompt());
			int eventEnteredByUser = scanner.nextInt();

			// Check for terminating condition
			if (eventEnteredByUser == -1)
			{
				System.out.println("Goodbye");
				done = true;
			}
			else if (Event.isValidEvent(eventEnteredByUser)) {

				// An event has occured. Call the main loop of the state machine with the event.
				sm.loop(eventEnteredByUser);
			}
		}

		// Close any resources that were used
		scanner.close();
	}

	private static void printPrompt() {
		System.out.println("Enter -1 to stop, or one of the following events:");
		System.out.println("1 for Completed Driver's Ed");
		System.out.println("2 for Failed written test");
		System.out.println("3 for Passed written test");
		System.out.println("4 for 6 months elapsed");
		System.out.println("5 for Drove 50 hours");
		System.out.println("6 for Failed road test");
		System.out.println("7 for Passed road test");
	}

}
