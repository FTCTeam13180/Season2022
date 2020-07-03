package org.roverdrive.statemachines.trafficlight;

public class StateMachine {

	// Enumumeration of states
	enum State
	{
		RED,
		GREEN,
		YELLOW
	}

	// Constants for the events
	public static final int EVENT_TIMER_RUNNING = 0;
	public static final int EVENT_TIMER_EXPIRED = 1;
	
	// The current state of the state machine
	private State currentState;

	// Initialization code. Ususally called from the robot initialization control on Android phone.
	// Set the initial state of the state machine.
	public void init() {
		currentState = State.RED;
	}

	/**
	 * Main loop of the state machine. Process the event based on the current state of the machine.
	 * If applicable, execute the actions for the event, and transition the machine to the state.
	 * 
	 * @param event the event that just occured
	 */
	public void loop(int event)
	{
		// Main switch based on current state
		switch (currentState) 
		{
			// Cases for each state of the state machine.
			case RED:

				// Check for applicable events
				if (event == EVENT_TIMER_EXPIRED)
				{
					// Execute actions
					System.out.println("Perform action switch off the red light");
					System.out.println("Perform action switch on the green light");

					// Transition the machine to the next state
					currentState = State.GREEN;
				}
				else if (event == EVENT_TIMER_RUNNING)
				{
					// do nothing
				}

				// Break the execution at the end of the block for each state.
				break;

			case GREEN:

				// Check for applicable events
				if (event == EVENT_TIMER_EXPIRED)
				{
					// Execute actions
					System.out.println("Perform action switch off the green light");
					System.out.println("Perform action switch on the yellow light");

					// Transition the machine to the next state
					currentState = State.YELLOW;
				}
				else if (event == EVENT_TIMER_RUNNING)
				{
					// do nothing
				}

				// Break the execution at the end of the block for each state.
				break;

			case YELLOW:
				if (event == EVENT_TIMER_EXPIRED)
				{
					// Execute actions
					System.out.println("Perform action switch off the yellow light");
					System.out.println("Perform action switch on the red light");

					// Transition the machine to the next state
					currentState = State.RED;
				}
				else if (event == EVENT_TIMER_RUNNING)
				{
					// do nothing
				}

				// Break the execution at the end of the block for each state.
				break;

		}
	}
	
	State getState() {
		return currentState;
	}
	void setState(State state) {
		currentState = state;
	}
}
