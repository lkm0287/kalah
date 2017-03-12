package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.Game;
import entities.Player;

/**
 * Servlet implementation class KalahController
 */
@WebServlet("/kalah")
public class KalahController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Game game;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KalahController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//initial get request return the fresh jsp to the user
		request.setAttribute("gameReady", false);
		request.setAttribute("gameFinished", false);
        request.getRequestDispatcher("/kalah.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//if player names form check valid and create new game
		if(request.getParameter("playersForm").equals("true")) {
			String firstPlayerName = request.getParameter("firstPlayerName");
			String secondPlayerName = request.getParameter("secondPlayerName");
			
			if(firstPlayerName.length() > 0 && secondPlayerName.length() > 0) {
				game = new Game();
				game.start(firstPlayerName, secondPlayerName);
				request.setAttribute("gameReady", true);
				request.setAttribute("gameFinished", false);
				request.setAttribute("playerToMove", game.getPlayerByTurn().getName());
				request.setAttribute("nonValidMove", false);
				request.setAttribute("houses", game.getBoard().getHouses());
				request.setAttribute("players", game.getPlayers());
			} else {
				doGet(request, response);
			}
		}
		
		//if game move form do move
		if(request.getParameter("moveForm").equals("true")) {
			Player player = game.getPlayerByTurn(); 
			String playerMoveString = request.getParameter("playerMove");
			Integer playerMoveInteger = null;
			try {
				playerMoveInteger = Integer.parseInt(playerMoveString);
			} catch (Exception e) {
				//move the integer to an invalid value to be caught by the validator below
				playerMoveInteger = 0;
			}

			Boolean anotherMove = null;
			if (playerMoveInteger != null && player != null) {
				//validate move
				boolean validMove = game.validateMove(player, playerMoveInteger);
				if(validMove) {
					anotherMove = game.move(player, playerMoveInteger);	
				} else {
					request.setAttribute("playerToMove", player.getName());
					request.setAttribute("gameReady", true);
					request.setAttribute("gameFinished", false);
					request.setAttribute("nonValidMove", true);
					request.setAttribute("houses", game.getBoard().getHouses());
					request.setAttribute("players", game.getPlayers());
				}
			}
			
			//handle if game has ended
			Boolean gameEnd = game.checkEndGameStateForPlayer(player);
			if(gameEnd) {
				request.setAttribute("gameReady", false);
				request.setAttribute("gameFinished", true);
				request.setAttribute("winnerName", player.getName());
				request.setAttribute("houses", game.getBoard().getHouses());
				request.setAttribute("players", game.getPlayers());
			} else {
				if (anotherMove != null && anotherMove == true) {
					request.setAttribute("playerToMove", player.getName());
					request.setAttribute("gameReady", true);
					request.setAttribute("gameFinished", false);
					request.setAttribute("nonValidMove", false);
					request.setAttribute("houses", game.getBoard().getHouses());
					request.setAttribute("players", game.getPlayers());
				} else if (anotherMove != null && anotherMove == false) {
					Player nextPlayer = game.getPlayerByTurn();
					request.setAttribute("playerToMove", nextPlayer.getName());
					request.setAttribute("gameReady", true);
					request.setAttribute("gameFinished", false);
					request.setAttribute("nonValidMove", false);
					request.setAttribute("houses", game.getBoard().getHouses());
					request.setAttribute("players", game.getPlayers());
				} else {
					//handle anotherMove as null
				}
			}
		}
		request.getRequestDispatcher("/kalah.jsp").forward(request, response);
	}

}
