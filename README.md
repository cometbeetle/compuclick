# COMPUCLICK

**COMPUCLICK** is an arithmetic-based video game that requires the player to solve equations before time runs out. 

![COMPUCLICK Title Screen](https://raw.githubusercontent.com/cometbeetle/compuclick/main/docs/GitHub-image.PNG)

The project was built with Java version 21 using [IntelliJ IDEA](https://www.jetbrains.com/idea/) by JetBrains. 

### Instructions

The game is designed to be intuitive to play, and works like this: 

1.	Upon game startup, the player must select a **difficulty level**. The characteristics of each level are explained soon. 

2.	The first round begins, and a randomly generated equation appears at the top of the screen. Two answer choices (one correct, and one incorrect) will begin to randomly float around the screen. To advance to the next round, the player must click the correct answer before time runs out. A countdown timer is also displayed. 

3.	If the player clicks the **wrong answer**, a sound plays, and the game ends. If the player’s score is a new high score for the selected difficulty, the player is prompted to enter his or her name, and the score is saved to a file. The player can then restart the game by pressing the `R` key. 

4.	If the player clicks the **correct answer**, **1 point** is added to the score, the game advances to the next round, and the timer resets depending on difficulty.
    - For each new round, a new equation is generated. 
    -	When advancing to the next round, there is a **50% chance** that an additional answer choice will begin to float around the screen. This has the effect of increasing the total number of choices as the game progresses, making it harder. 
    - **Every 4 points**, the answer choices’ motion becomes faster and more erratic. 

    - **Every 25 points**, the equation difficulty increases, and the number of answer choices is reset to 2 (but will still increase). 
      - ***0-24 points:*** two integers 0 to 9 can be added, subtracted, or multiplied. 
      - ***25-49 points:*** three integers 0 to 9 can be added, subtracted, or multiplied. Order of operations applies. 
      -	***\>= 50 points:*** two integers, the first from 0 to 9, and the second from 0 to 99, can be added, subtracted, or multiplied. There is a **10% chance** of receiving a **very hard** problem containing trigonometric functions or calculus. 

5.	Each **difficulty level** changes the game behavior as follows:
    - ***Easy***
      - The user gets **20 seconds** to solve every equation, every round.  
    - ***Normal***
      - The user gets **20 seconds** to solve the *first* equation, but slightly less each round for every subsequent equation. 
      - Every **25 points**, the cycle repeats, resetting the time per round back to **20 seconds**. 
    - ***Hard***
      - The user gets **20 seconds** to solve the first equation, but slightly less each round, indefinitely.
    - ***Impossible***
      - Same behavior as hard mode, but the player can only click on the correct answer, and nowhere else on the screen. Clicking any wrong answer or the background will end the game. 

6.	Clickable **powerups** will appear randomly every 15 to 30 seconds, each **lasting 10 seconds**. Powerups are cumulative (you can have more than one active a time), and work as follows:
    - **The *Slow* powerup (blue):** 
      - The countdown timer moves at half speed, and the answer choices slow their random movements. 
      - The background and timer are tinted blue while this powerup is active.
    - **The *Points x2* powerup (red):** 
      - Each correct answer is worth **2 points** instead of 1. 
      - The background and answer choices are tinted red while this powerup is active. 
    - **The *Easier* powerup (green):**
      - The current equation is immediately made easier by 1 level (only available when equation difficulty is not lowest). 
      - The background and current equation are tinted green while this powerup is active.

### Additional Game Features:
  - Randomly generated equations (except for “very hard” equations)
  - Original soundtrack
  - Correct answer ding sound
  -	CSV file-based current high score display 
  -	High score file corruption detection
  -	Proper handling of window resizing


<br><br>


**COMPUCLICK**

`© Ethan Mentzer, 2023, University of Rochester.`
