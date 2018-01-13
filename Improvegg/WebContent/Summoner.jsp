
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

	<head>
	
		<meta charset="ISO-8859-1">
		<title>improve.gg</title>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
		<link rel="stylesheet" href="common.css" type="text/css">
		<link rel="stylesheet" href="css//summoner.css" type="text/css">
		
	</head>
	
	<body>
	
		<nav class="navbar navbar-expand-lg justify-content-between">
	 		<a href="home" class="navbar-brand">
	 			<h1>
	 				<b>improve.gg</b>
	 			</h1>
	 		</a>
	  		<c:if test="${not loggato}">
	  			<a href="checkLogin" role="button" class="btn btn-primary" >Login</a>
  			</c:if>
  			<c:if test="${loggato}">
  				<div>
  					<p class="login-label"><b> ${messaggio} </b></p>
  					<a href="checkLogout" class="btn btn-danger" role="button">Logout</a>
  				</div>
  			</c:if>
		</nav>
			
		<nav class="navbar navbar-expand-lg navbar-dark">
 			<a class="navbar-brand text-light active"><b>Menu</b></a>
  			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    			<span class="navbar-toggler-icon"></span>
  			</button>
  			<div class="collapse navbar-collapse" id="navbarSupportedContent">
    			<ul class="navbar-nav mr-auto">
      				<li class="nav-item">
       					<a class="nav-link" href="home">Home <span class="sr-only">(current)</span></a>
      				</li>
      				<li class="nav-item">
        				<a class="nav-link" href="#">Favourites</a>
      				</li>
      				<li class="nav-item">
       					<a class="nav-link" href="#">Pro Players</a>
      				</li>
    			</ul>	
  			</div>
		</nav>
		
		<c:if test="${trovato}">
			<div class="contianer-fluid main-container">
				<hr class="my-2">
				<div class="header">
					<h2><b>${sumName}</b></h2>
					<p>Recent Matches</p>
				</div>
				<div class="container-fluid matches-container">
					<c:if test="${not emptyMatches}">
						<c:forEach var="partita" items="${partita}">
							<hr class="my-2">
						 	<div class="container row match-container justify-content-between">
						 		<div class="game-stats col">
						 			${partita.gameMode}
						 		</div>
						 		<div class="summ-stats col">
						 			${partita.champName}
						 		</div>
						 		<div class="kda col">
						 			${partita.kda}
						 		</div>
						 		<div class="container items col">
							 		<div class="container items1">
							 			${partita.item0} ${partita.item1} ${partita.item2}	
							 		</div>
							 		<div class="container items2">
							 			${partita.item3} ${partita.item4} ${partita.item5}
							 		</div>
							 	</div>
						 		<div class="teams col">
						 			<div class="team-row row">
							 			<div class="container team1">
								 			sum1 sum2 sum3 sum4 sum5
								 		</div>
								 		<div class="container team2">
								 			sum6 sum7 sum8 sum9 sum0
								 		</div>
								 	</div>
						 		</div>
						 	</div>
						</c:forEach>
					</c:if>
					<c:if test="${emptyMatches}">
						<hr class="my-2">
					 	<h2>
							<b>
								${causa}
							</b>
						</h2>
					</c:if>
					<hr class="my-2">
				</div>
			</div>
		</c:if>
		<c:if test="${not trovato}">
			<div class="contianer-fluid main-container">
				<hr class="my-2">
				<div class="header">
					<h2>
						<b>
							${causa}
						</b>
					</h2>
				</div>
			</div>
		</c:if>
		
		<div class="container">
			<hr class="my-4">
		</div>
		
	  	<footer>
      		<div>
				<p>Page created by AntyJ</p>
			</div>
		</footer>
		
		<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
		
	</body>
	
</html>