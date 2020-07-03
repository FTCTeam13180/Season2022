package org.roverdrive.statemachines.license;

/**
 * Class representing the state machinr
 */
public class StateMachine {

	// Enumumeration of states
	private enum State
	{
		NON_DRIVER,
		DRIVERS_ED_TAKEN,
		PERMITTED_DRIVER,
		ELIGIBLE_FOR_ROAD_TEST,
		LICENSED_DRIVER
	}

	// The current state of the state machine
	private State currentState;

	// Initialization code. Ususally called from the robot initialization control on Android phone.
	// Set the initial state of the state machine.
	public void init() {
		currentState = State.NON_DRIVER;
	}

	/**
	 * Main loop of the state machine. Process the event based on the current state of the machine.
	 * If applicable, execute the actions for the event, and transition the machine to the state.
	 * 
	 * @param event the event that just occured
	 */
	public void loop(int eventId)
	{
		Event event = Event.getEvent(eventId);

		// Main switch based on current state
		switch (currentState) 
		{
			// Cases for each state of the state machine.
			case NON_DRIVER:

				// Check for applicable events
				if (event == Event.COMPLETED_DRIVERS_ED)
				{
					// Execute actions
					System.out.println("Congratulations on completing Driver's Ed. You are now considered as Driver's Ed Taken");

					// Transition the machine to the next state
					currentState = State.DRIVERS_ED_TAKEN;
				}

				// Break the execution at the end of the block for each state.
				break;

			case DRIVERS_ED_TAKEN:

				// Check for applicable events
				if (event == Event.PASSED_WRITTEN_TEST)
				{
					// Execute actions
					System.out.println("Congratulations on passing the written test. You are now considered as a Permitted Driver");

					// Transition the machine to the next state
					currentState = State.PERMITTED_DRIVER;
				}
				else if (event == Event.FAILED_WRITTEN_TEST)
				{
					// Execute actions
					System.out.println("Sorry you failed the written test. You are still considered as Driver's Ed Taken");

					// No state transition
				}

				// Break the execution at the end of the block for each state.
				break;

			case PERMITTED_DRIVER:
				if (event == Event.DROVE_50_HOURS)
				{
					// Execute actions
					System.out.println("Congratulations on driving 50 hours. You are now considered as Eligible for road test");

					// Transition the machine to the next state
					currentState = State.ELIGIBLE_FOR_ROAD_TEST;
				}
				else if (event == Event.SIX_MONTHS_ELAPSED)
				{
					// Execute actions
					System.out.println("Six months have elapsed. Sorry, you have to go back to being considered as Driver's Ed Taken");

					// Transition the machine to the next state
					currentState = State.DRIVERS_ED_TAKEN;
				}

				// Break the execution at the end of the block for each state.
				break;

			case ELIGIBLE_FOR_ROAD_TEST:
				if (event == Event.PASSED_ROAD_TEST)
				{
					// Execute actions
					System.out.println("Congratulations on passing the road test. You are now considered as Licensed driver");

					// Transition the machine to the next state
					currentState = State.LICENSED_DRIVER;
				}
				else if (event == Event.FAILED_ROAD_TEST)
				{
					// Execute actions
					System.out.println("Sorry, you failed the road test. You have to go back to being considered a Non Driver");

					// Transition the machine to the next state
					currentState = State.NON_DRIVER;
				}

				// Break the execution at the end of the block for each state.
				break;

		}
	}
}
