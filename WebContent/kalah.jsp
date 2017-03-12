<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value="/kalah.css" />" rel="stylesheet">
<title>Kalah Game</title>
</head>
<body>
	<h1>Kalah Game</h1>
	<c:if test="${gameReady && !gameFinished}">
		<h2>Game Board</h2>
		<b id="north">${players[0].name}</b>
		<table>
			<tr>
				<td> 6 </td>
				<td> 5 </td>
				<td> 4 </td>
				<td> 3 </td>
				<td> 2 </td>
				<td> 1 </td>
			</tr>
			<tr id="north">
				<td>${houses[5].stones.size()}</td>
				<td>${houses[4].stones.size()}</td>
				<td>${houses[3].stones.size()}</td>
				<td>${houses[2].stones.size()}</td>
				<td>${houses[1].stones.size()}</td>
				<td>${houses[0].stones.size()}</td>
			</tr>
			<tr>
				<td id="north">${houses[6].stones.size()}</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td id="south">${houses[13].stones.size()}</td>
			</tr>
			<tr id="south">
				<td>${houses[7].stones.size()}</td>
				<td>${houses[8].stones.size()}</td>
				<td>${houses[9].stones.size()}</td>
				<td>${houses[10].stones.size()}</td>
				<td>${houses[11].stones.size()}</td>
				<td>${houses[12].stones.size()}</td>
			</tr>
			<tr>
				<td>1</td>
				<td>2</td>
				<td>3</td>
				<td>4</td>
				<td>5</td>
				<td>6</td>
			</tr>
		</table>
		<b id="south">${players[1].name}</b>
	</c:if>
	<br />
	<c:choose>
		<c:when test="${!gameReady && !gameFinished}">
			<h2>Please enter player names:</h2>
			<form action="/kalah/kalah" method="POST">
				<p>First player name:</p>
				<input type="text" name="firstPlayerName"> <br />
				<p>Second player name:</p>
				<input type="text" name="secondPlayerName" /> <input type="hidden"
					name="playersForm" value="true"> <input type="hidden"
					name="moveForm" value="false"> <input type="submit"
					value="Submit" />
			</form>
		</c:when>
		<c:when test="${gameReady && !gameFinished}">
			<h2>Player Move</h2>
			<p>${playerToMove} enter pit number to move:</p>
			<form action="/kalah/kalah" method="POST">
				<input type="text" name="playerMove" /> <input type="hidden"
					name="playersForm" value="false"> <input type="hidden"
					name="moveForm" value="true"> <input type="submit"
					value="Submit" />
			</form>
			<c:if test="${nonValidMove}">
				<p>Invalid move entered. Please enter another move.</p>
			</c:if>
		</c:when>
		<c:when test="${!gameReady && gameFinished}">
			<h2>Winner:</h2>
			<p>${winnerName}!</p>
			<form action="/kalah/kalah" method="GET">
				<input type="submit" value="Play Again." />
			</form>
		</c:when>
		<c:otherwise>
			<h2>Something went wrong!</h2>
		</c:otherwise>
	</c:choose>
</body>
</html>