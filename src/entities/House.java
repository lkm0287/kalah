package entities;

import java.util.ArrayList;
import java.util.List;

public class House {
	
	private List<Stone> stones = new ArrayList<Stone>();
	private Player owner;
	
	public House(Player owner) {
		this.owner = owner;
	}
	
	public House(int numberOfStones) {
		setUpStones();
	}
	
	public List<Stone> getStones() {
		return stones;
	}

	public void setStones(List<Stone> stones) {
		this.stones = stones;
	}

	public Player getOwner() {
		return owner;
	}

	private void setUpStones() {
		stones = new ArrayList<Stone>();
		for(int i = 1; i < 7; i++) {
			stones.add(new Stone());
		}
	}

}
