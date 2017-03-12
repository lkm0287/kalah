package enums;

public enum Turn {
	TURN(true),
	NOT_TURN(false);
	
	private final Boolean isTurn;
	
	Turn(Boolean isTurn) {
		this.isTurn = isTurn;
	}
	
	public Boolean isTurn() {
		return this.isTurn;
	}
}
