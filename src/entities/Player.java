package entities;

public class Player {
	
	private String name;
	private Boolean isNorth;
	private Boolean turn;
	
	public Player(String name, Boolean turn, Boolean isNorth) {
		this.name = name;
		this.turn = turn;
		this.isNorth = isNorth;
	}
	
	public Boolean getTurn() {
		return turn;
	}

	public void setTurn(Boolean turn) {
		this.turn = turn;
	}
	
	public Boolean getIsNorth() {
		return isNorth;
	}
	
	public String getName() {
		return name;
	}

}
