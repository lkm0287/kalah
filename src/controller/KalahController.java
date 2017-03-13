package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entities.Game;
import entities.Player;

/**
 * Servlet implementation class KalahController
 */
@WebServlet("/kalah")
public class KalahController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		//initial get request return the fresh jsp
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
				HttpSession session = request.getSession();
				session.setMaxInactiveInterval(1800);
				Game game = new Game();
				game.start(firstPlayerName, secondPlayerName);
				session.setAttribute("game", game);
				setGameAttributes(request, game, Boolean.FALSE);
			} else {
				doGet(request, response);
			}
		}
		
		//if game move form do move
		if(request.getParameter("moveForm").equals("true")) {
			Game game = (Game)request.getSession().getAttribute("game");
			
			//handle if game session expires - restart game
			if(game == null) {
				doGet(request, response);
				return;
			}
			
			Player player = game.getPlayerByTurn(); 
			String playerMoveString = request.getParameter("playerMove");
			Integer playerMoveInteger = null;
			try {
				playerMoveInteger = Integer.parseInt(playerMoveString);
			} catch (Exception e) {
				//move the integer to an invalid value to be caught by the validator below
				playerMoveInteger = 0;
			}

			if (playerMoveInteger != null && player != null) {
				//validate move
				boolean validMove = game.validateMove(player, playerMoveInteger);
				if(validMove) {
					game.move(player, playerMoveInteger);	
				} else {
					setGameAttributes(request, game, Boolean.TRUE);
				}
			}
			
			//handle if game has ended
			Boolean gameEnd = game.checkEndGameStateForPlayer(player);
			if(gameEnd) {
				request.setAttribute("gameReady", false);
				request.setAttribute("gameFinished", true);
				request.setAttribute("winnerName", player.getName());
			} else {
				setGameAttributes(request, game, Boolean.FALSE);
			}
		}
		request.getRequestDispatcher("/kalah.jsp").forward(request, response);
	}

	private void setGameAttributes(HttpServletRequest request, Game game, Boolean nonValidMove) {
		request.setAttribute("playerToMove", game.getPlayerByTurn().getName());
		request.setAttribute("gameReady", true);
		request.setAttribute("gameFinished", false);
		request.setAttribute("nonValidMove", nonValidMove);
		request.setAttribute("houses", game.getBoard().getHouses());
		request.setAttribute("players", game.getPlayers());
	}

}
