package enums;

public enum PlayerDirection {
	NORTH(true),
	NOT_NORTH(false);
	
	private final Boolean isNorth;
	
	PlayerDirection(Boolean isNorth) {
		this.isNorth = isNorth;
	}
	
	public Boolean isNorth() {
		return this.isNorth;
	}
}
