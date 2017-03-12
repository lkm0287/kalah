package entities;

import java.util.ArrayList;
import java.util.List;

public class Board {
	
	private List<House> houses;
	
	public Board(List<Player> players) {
		setUpBoard(players);
	}
	
	private void setUpBoard(List<Player> players) {
		this.houses = new ArrayList<House>();
		
		for(int houseNumber = 0; houseNumber < 14; houseNumber++) {
			if (houseNumber == 6){
				houses.add(new House(players.get(0)));
			} else if (houseNumber == 13) {
				houses.add(new House(players.get(1)));
			} else {
				houses.add(new House(6));
			}
		}
	}

	public List<House> getHouses() {
		return houses;
	}

}