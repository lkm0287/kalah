package entities;

import java.util.ArrayList;
import java.util.List;

import enums.PlayerDirection;
import enums.Turn;

public class Game {

	private List<Player> players;
	private Board board;
	
	public List<Player> getPlayers() {
		return players;
	}

	//make the players and board be injected
	public void start(String playerOneName, String playerTwoName) {
		setUpPlayers(playerOneName, playerTwoName);
		this.board = new Board(players);
	}
	
	public Board getBoard() {
		return board;
	}

	private void setUpPlayers(String playerOneName, String playerTwoName) {
		this.players = new ArrayList<Player>();
		Player playerOne = new Player(playerOneName, Turn.TURN.isTurn(), PlayerDirection.NORTH.isNorth());
		Player playerTwo = new Player(playerTwoName, Turn.NOT_TURN.isTurn(), PlayerDirection.NOT_NORTH.isNorth());
		this.players.add(playerOne);
		this.players.add(playerTwo);
	}
	
	public boolean move(Player player, int pitNumber) {
		
		//take stones from pit
		int numberOfStonesToSow = getNumberOfStonesInHouse(player.getIsNorth(), pitNumber);
		emptyHouse(player.getIsNorth(), pitNumber);
		
		int locationOfHouseInList = pitNumber-1;
		
		//if player is south player then they start from other side of board
		if(!player.getIsNorth()) {
			locationOfHouseInList = locationOfHouseInList + 7;
		}
		
		//get the location of the next place to sow
		int locationOfNextHouseToSowInListOfHouses = getLocationOfNextHouseToSowInListOfHouses(player, locationOfHouseInList);
		boolean anotherTurn = false;
		
		//while there are stones to sow:
		while(numberOfStonesToSow > 0) {
			
			//add a stone to the next house
			board.getHouses().get(locationOfNextHouseToSowInListOfHouses).getStones().add(new Stone());
			
			//remove a stone from the pile
			numberOfStonesToSow--;
			
			//get the location of the last stone sowed
			int locationOfLastStoneSowed = locationOfNextHouseToSowInListOfHouses;
			
			//if it was the last stone sowed:
			if (numberOfStonesToSow == 0) {
				
				//if moved to own empty pit:
				if (board.getHouses().get(locationOfLastStoneSowed).getStones().size() == 1 && isPlayersHouse(player.getIsNorth(), locationOfLastStoneSowed)) {
					
					//get opposite pit number
					int oppositePitNumber = getOppositePitFromLocation(player.getIsNorth(), locationOfLastStoneSowed);
					
					//get number of stones from opposite pit
					int numberOfStonesToMoveFromOpposition = getNumberOfStonesInHouse(!player.getIsNorth(), oppositePitNumber);
					
					//add stones to kalah
					addStonesToKalah(player.getIsNorth(), numberOfStonesToMoveFromOpposition);
					addStonesToKalah(player.getIsNorth(), 1);
					
					//empty stones from player's pit and opposite pit
					emptyHouse(player.getIsNorth(), convertLocationToPit(player.getIsNorth(), locationOfLastStoneSowed));
					emptyHouse(!player.getIsNorth(), oppositePitNumber);
				} else {
					//else if last stone in own kalah then set another turn
					anotherTurn = isLastStoneInOwnKalah(player.getIsNorth(), locationOfLastStoneSowed);
				}
			} else {
				locationOfNextHouseToSowInListOfHouses = getLocationOfNextHouseToSowInListOfHouses(player, locationOfNextHouseToSowInListOfHouses);
			}
		}
		
		//player finished move so return whether they get another turn
		//also set the other player's turn
		if (anotherTurn == false) {
			player.setTurn(Turn.NOT_TURN.isTurn());
			setOtherPlayerTurn(player);
		} else {
			player.setTurn(Turn.TURN.isTurn());
		}
		
		return player.getTurn();
	}
	
	private void setOtherPlayerTurn(Player player) {
		for(Player p: this.getPlayers()) {
			if(!p.getIsNorth().equals(player.getIsNorth())) {
				p.setTurn(Boolean.TRUE);
			}
		}
	}

	private int convertLocationToPit(boolean isPlayerNorth, int locationOfLastStoneSowed) {
		if(!isPlayerNorth) {
			return locationOfLastStoneSowed - 6;
		} else {
			return locationOfLastStoneSowed + 1;	
		}
	}

	private boolean isLastStoneInOwnKalah(Boolean isNorth, int locationOfLastStoneSowed) {
		if (isNorth && locationOfLastStoneSowed == 6) {
			return true;
		} else if (!isNorth && locationOfLastStoneSowed == 13) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isPlayersHouse(Boolean isNorth, int locationOfLastStoneSowed) {
		if(isNorth && locationOfLastStoneSowed < 6) {
			return true;
		} else if (!isNorth && locationOfLastStoneSowed > 6 && locationOfLastStoneSowed < 13) {
			return true;
		} else {
			return false;
		}
	}

	private void addStonesToKalah(Boolean isNorth, int numberOfStonesToMoveFromOpposition) {
		int kalahLocation;
		if (isNorth) {
			kalahLocation = 6;
		} else {
			kalahLocation = 13;
		}
		
		for(int i = 0; i < numberOfStonesToMoveFromOpposition; i++) {
			board.getHouses().get(kalahLocation).getStones().add(new Stone());
		}
	}

	private int getOppositePitFromLocation(boolean isNorthPlayer, int pitNumber) {
		if(isNorthPlayer) {
			if(pitNumber == 0) {
				return 6;
			} else if (pitNumber == 1) {
				return 5;
			} else if (pitNumber == 2) {
				return 4;
			} else if (pitNumber == 3) {
				return 3;
			} else if (pitNumber == 4) {
				return 2;
			} else {
				return 1;
			}
		} else {
			if(pitNumber == 7) {
				return 6;
			} else if (pitNumber == 8) {
				return 5;
			} else if (pitNumber == 9) {
				return 4;
			} else if (pitNumber == 10) {
				return 3;
			} else if (pitNumber == 11) {
				return 2;
			} else {
				return 1;
			}
		}
	}

	private int getLocationOfNextHouseToSowInListOfHouses(Player player, int houseLocationInList) {
		boolean isPlayerNorth = player.getIsNorth();
		if (isPlayerNorth) {
			//avoid opposite kalah
			if(houseLocationInList + 1 == 13) {
				return 0;
			} else {
				//else go to next position
				return houseLocationInList + 1;
			}
		} else {
			//avoid opposite kalah
			if(houseLocationInList + 1 == 6) {
				return 7;
			} else if (houseLocationInList + 1 == 14) {
				//don't go off board
				return 0;
			} else {
				//go to next position
				return houseLocationInList + 1;
			}
		}
	}

	public boolean validateMove(Player player, int pitNumber) {
		boolean isPlayerNorth = player.getIsNorth();
		if (pitNumber < 1 || pitNumber > 6) {
			return false;
		} else {
			if (getNumberOfStonesInHouse(isPlayerNorth, pitNumber) < 1) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	private int getHouseNumberInListOfHouses(boolean isPlayerNorth, int pitNumber) {
		if(!isPlayerNorth) {
			return pitNumber + 6;
		} else {
			return pitNumber - 1;	
		}
	}
	
	private int getNumberOfStonesInHouse(boolean isPlayerNorth, int pitNumber) {
		int realHouseNumber = getHouseNumberInListOfHouses(isPlayerNorth, pitNumber);
		if (board.getHouses().get(realHouseNumber).getStones().isEmpty()) {
			return 0;
		} else {
			return board.getHouses().get(realHouseNumber).getStones().size();
		}
	}
	
	private void emptyHouse(boolean isPlayerNorth, int pitNumber) {
		int realHouseNumber = getHouseNumberInListOfHouses(isPlayerNorth, pitNumber);
		board.getHouses().get(realHouseNumber).getStones().clear();;
	}
	
	public boolean checkEndGameStateForPlayer(Player player) {
		boolean endGame = true;
		if(player.getIsNorth()) {
			endGame = checkPlayersHousesEmpty(0, 6);
		} else {
			endGame = checkPlayersHousesEmpty(7, 13);
		}
		return endGame;
	}
	
	private boolean checkPlayersHousesEmpty(int startHouse, int endHouse) {
		boolean allHousesEmpty = true;
		for(int i = startHouse; i < endHouse; i++) {
			List<Stone> stones = board.getHouses().get(i).getStones();
			if(!stones.isEmpty()) {
				allHousesEmpty = false;
			}
		}
		return allHousesEmpty;
	}
	
	public Player getPlayerByTurn() {
		Player player = null;
		
		for(Player playerInList : players) {
			if (playerInList.getTurn().equals(Boolean.TRUE)) {
				player = playerInList;
			}
		}
		
		return player;
	}

}