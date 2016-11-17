#Duell Game

Duell is a two-player chess variant played with dice on a board of 9x8 squares. Players take turns moving one of their dice in order to capture their opponent's pieces, with the ultimate aim of capturing the opponent's king to win the game.

Full description of the game is available in: https://en.wikipedia.org/wiki/Duell_(chess)

Class Descriptions
1.	Dice: 
This class holds all the information needed for a dice and it consists of all the functionalities that a die can perform. The primary functionalities include rolling of the dices.
2.	Board:
This is the model class of the board of the game. It handles all the functionalities performed on the board. The primary functionalities include handling movement of a die and performing various verifications of the movement.
3.	Algorithm:
This class holds the logic of the game. This is an automation provided for the computer player. However, it can also be used for human. The algorithm plays offense and tries to win the game.
4.	Computer:
This class holds the functionalities for a computer player. It is inherited from Player class. So, the functionalities are customized such that the computer will play the game using the offensive algorithm against the opponent. 
5.	Human:
This class holds the functionalities for a human player. It is inherited from Player class. So, the functionalities are customized such that user can make moves or choose from many different options.
6.	Player:
This is a base class for all the players. This class cannot have an instance by itself. It will function as a base class to as many players as the user needs.
7.	FileHandler:
This class handles the actions for the files. It assists in saving and opening the game such that it assists other classes by rendering files to save or load a game.
8.	GamePlay:
This class handles a single round of the tournament. Once the tournament starts, this class will start the game and acts as the controller for human, board and model. 
9.	Location:
This class holds the location of the board. This class is not human friendly. So, indexes start from 0.
10.	GameEndInfo:
This class start the tournament of Duell. It prompts the user if they want to start a new tournament or start from the point they left the game. 
11.	TreeNode:
This class holds the information of a die and its current location. This provides as a node if algorithm goes many steps further and wants to go through various depth.
12.	AppLauncher:
This class holds the information of the start of the whole game. It is the controller class for launching the application.
13.	TournamentEndInfo:
This class is the end controller of the application. 

##Algorithm of automated game:
Following algorithm is used to make a move:
1.	Check if the player can win the game by looping through each and every dice of the player and confirming if the movement is possible for the given location.
2.	Check if the king is in threat by opponent.
a.	If yes, then check if the threat can be minimized by eating the opponent. If it can be eaten, then eat the opponent.
b.	If no, then block the path from the opponent to the king.
3.	Check if opponent’s players can be eaten. Loop through each and every dices and check if you can eat any opponents.
4.	Make a safe and offensive move. This will move the player to the closest square to the opponent where the die is safe. After the movement, it is sure that opponent will not be able to eat the recently moved die.

##How to run
If you don't have android studio, it is highly suggested that you download Android Studio. Then, once you have installed Android Studio, go to File --> Open Project --> open the main project.

##Candid shots
