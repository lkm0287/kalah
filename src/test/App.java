package test;

import java.util.List;
import java.util.Scanner;

import entities.Game;
import entities.House;

public class App {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		boolean firstNameEntered = false;
		boolean secondNameEntered = false;
		String playerOneName = "";
		String playerTwoName = "";
		
        while (!firstNameEntered) {

            System.out.print("Enter player 1 name : ");
            playerOneName = scanner.nextLine();
            
            System.out.println("Player 1 name : " + playerOneName);
            System.out.println("-----------\n");
            
            firstNameEntered = true;
        }
        
        while (!secondNameEntered) {

            System.out.print("Enter player 2 name : ");
            playerTwoName = scanner.nextLine();
            
            System.out.println("Player 2 name : " + playerTwoName);
            System.out.println("-----------\n");
            
            secondNameEntered = true;
        }
        
        //set up game
        Game game = new Game();
        game.start(playerOneName, playerTwoName);
        
        //output board
        outputBoard(game);
        
		//while game not end listen for moves
        boolean gameEnd = false;
        int nextMove;
        int playerNumber = 0;
        while (!gameEnd) {

            System.out.print(game.getPlayers().get(playerNumber).getName() + " enter pit number to move : ");
            nextMove = scanner.nextInt();
            
            boolean validMove = game.validateMove(game.getPlayers().get(playerNumber), nextMove);
            if(validMove) {
            	boolean anotherTurn = game.move(game.getPlayers().get(playerNumber), nextMove);
            	outputBoard(game);
            	System.out.println("");
            	
            	if (game.checkEndGameStateForPlayer(game.getPlayers().get(playerNumber))) {
            		gameEnd = true;
            		break;
            	}
            	
            	if (!anotherTurn) {
            		if(playerNumber == 0) {
            			playerNumber = 1;
            		} else {
            			playerNumber = 0;
            		}
            	}
            } else {
                System.out.println("Invalid move!");
            }
        }
        
		//at end state who won/drew
        int playerNorthScore = game.getBoard().getHouses().get(6).getStones().size();
        int playerSouthScore = game.getBoard().getHouses().get(13).getStones().size();
        
        if(playerNorthScore > playerSouthScore) {
        	System.out.println("Player " + game.getPlayers().get(0).getName() + " wins!");
        } else if (playerNorthScore < playerSouthScore) {
        	System.out.println("Player " + game.getPlayers().get(1).getName() + " wins!");
        } else {
        	System.out.println("Draw!");
        }
        
        scanner.close();

	}

	private static void outputBoard(Game game) {
		StringBuffer board = new StringBuffer();
		List<House> houses = game.getBoard().getHouses();
		board.append("-----------\n");
		board.append("Player: " + game.getPlayers().get(0).getName());
		board.append("\n");
		board.append("6 5 4 3 2 1");
		board.append("\n");
		for(int i = 5; i >= 0 ; i--) {
			board.append(houses.get(i).getStones().size());
			board.append(" ");
		}
		board.append("\n");
		board.append(houses.get(6).getStones().size()).append("         ").append(houses.get(13).getStones().size());
		board.append("\n");
		for(int i = 7; i <= 12 ; i++) {
			board.append(houses.get(i).getStones().size());
			board.append(" ");
		}
		board.append("\n");
		board.append("1 2 3 4 5 6");
		board.append("\n");
		board.append("Player: " + game.getPlayers().get(1).getName());
		board.append("\n");
		board.append("-----------\n");
		System.out.println(board);
	}

}
