Author Information
-------------------------------------
My Name: Mark Van der Merwe
Partner: Thomas Oh
Homework: 8 Stochastic Testing - Poker
Date:    March 24, 2017

Project Summary
-------------------------------------
This purpose of this project was to learn about stochastic testing and random number generators.
In this project, we program the games of 5 card poker and Texas Hold'em, and we calculate the
odds of each hand appearing stochastically and exhaustively. This program will be able to compare
one hand to another as see which one wins, and this will help calculate the stat for our analysis.
We create a poor and better random generator for the analysis.

Notes to the TA's
-------------------------------------
To perform our Texas Hold'Em analysis, we created another function that could use our odds_to_win
function to test random hands against each hand in the scope provided (i.e., Ace w/ Ace, Ace w/ Heart).
This tests 1500 hands against the current one (by looking at 1500 possibilities on the table). We
then average all the likelihoods of winnings to get an estimate of that hand's likelihood of winning at
any time.

Pledge
-------------------------------------
I pledge that the work done here was my own and that I have learned how to write
	      this program (such that I could throw it out and restart and finish it in a timely
	      manner).  I am not turning in any work that I cannot understand, describe, or
	      recreate.  Any sources (e.g., web sites) other than the lecture that I used to
	      help write the code are cited in my work.  When working with a partner, I have
	      contributed an equal share and understand all the submitted work.  Further, I have
	      helped write all the code assigned as pair-programming and reviewed all code that
	      was written separately.
	                      Mark Van der Merwe
	                      
Design Choices
-------------------------------------
No major design choices made

Major Problems (Solved) That Could Be Talked About
-------------------------------------
So in order to calculate the rank of a hand, we used a helper method per rank to see if that
specific rank existed. We decided to draw out a tree that showed what could possibly happen if
something was or wasn't true. We saw that is a flush was true, only a royal flush, straight flush,
and flush were possible after that and crossed out all other options. From there we checked largest
amount of cards to least, because the program could find a pair, but there could still be a four of
a kind. However, if there is a four of a kind, a pair does not matter.