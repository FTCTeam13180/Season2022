package org.roverdrive.statemachines.license;

/**
 * Class representing the events
 */
public enum Event {

	// Enumumeration of events
	COMPLETED_DRIVERS_ED(1, "Completed Driver's Ed"), 
	FAILED_WRITTEN_TEST (2, "Failed written test"), 
	PASSED_WRITTEN_TEST (3, "Passed written test"), 
	SIX_MONTHS_ELAPSED (4, "6 months elapsed"), 
	DROVE_50_HOURS (5, "Drove 50 hours"), 
	FAILED_ROAD_TEST(6, "Failed road test"), 
	PASSED_ROAD_TEST(7, "Passed road test");

	// Integer ID of the event. User enters this ID on the keyboard to simulate the occurance of this event
	private int id;
	// The user-friendly display name of the event
	private String displayName;

	private Event(int id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public int getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	// Generate the prompt for user to enter the ID for an event
	public static String getPrompt() {
		StringBuilder sb = new StringBuilder();
		for (Event event : Event.values()) {
			sb.append("Enter ").append(event.getId()).append(" for event ").append(event.getDisplayName()).append("\n");
		}
		return sb.toString();
	}

	// Check if the ID is a valid ID for any event
	public static boolean isValidEvent(int eventId) {
		for (Event event : Event.values()) {
			if (eventId == event.getId()) {
				return true;
			}
		}
		return false;
	}

	// Get the event corresponding to the event ID
	public static Event getEvent(int eventId) {
		for (Event event : Event.values()) {
			if (eventId == event.getId()) {
				return event;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.displayName;
	}
}
