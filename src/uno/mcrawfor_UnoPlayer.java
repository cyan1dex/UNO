/*
Michael Crawford
Implementation of UnoPlayer; Uno algorithm
*/

package uno;

import java.util.List;

public class mcrawfor_UnoPlayer implements UnoPlayer 
{

	private int[] handColors = {0, 0, 0 ,0};
	private int[] handNumberValues = {0, 0, 0, 0};	

	public int play( List<Card> hand, Card upCard, Color calledColor, GameState state )
	{
		boolean endgame = false;
		int finishingPlayer = endGame(hand, state);
		countHand(hand);
		countHandNumbers(hand);

		if(finishingPlayer != -1) 
			endgame = true;

		if(upCard.getRank() == UnoPlayer.Rank.DRAW_TWO)
		{
			if (playCard(hand, UnoPlayer.Rank.DRAW_TWO) != -1) 
				return playCard(hand, UnoPlayer.Rank.DRAW_TWO);	
		}

		if (endgame) 
			return endGamePlay( hand, upCard, calledColor, state, finishingPlayer );
		
		return standardPlay( hand, upCard, calledColor, state, finishingPlayer );
	}
	
	/* Right now this is the laziest part, but it ain't bad. */
	public Color callColor( List<Card> hand ) 
	{
		/*
		Don't call a color that you know someone with a few cards left has a lot of (probablity).
		Call a color that you have high value numbers in vs. a lot of individual cards.
		*/		
		int numReds = handColors[0]; 
		int numBlues = handColors[1]; 
		int numGreens = handColors[2]; 
		int numYellows = handColors[3];

		int valueReds = handNumberValues[0];
		int valueBlues = handNumberValues[1];
		int valueGreens = handNumberValues[2];
		int valueYellows = handNumberValues[3];

		if (valueReds > 10)
			return UnoPlayer.Color.RED;
		if (valueBlues > 10)
			return UnoPlayer.Color.BLUE;
		if (valueGreens > 10)
			return UnoPlayer.Color.GREEN;
		if (valueYellows > 10)
			return UnoPlayer.Color.YELLOW;

		if ((numReds > numBlues) && (numReds > numYellows) && (numReds > numGreens)) 
			return UnoPlayer.Color.RED;
		else if ((numBlues > numReds) && (numBlues > numYellows) && (numBlues > numGreens)) 
			return UnoPlayer.Color.BLUE;
		else if ((numYellows > numReds) && (numYellows > numBlues) && (numYellows > numGreens)) 
			return UnoPlayer.Color.YELLOW;
		else if ((numGreens > numReds) && (numGreens > numBlues) && (numGreens > numYellows))
			return UnoPlayer.Color.GREEN;
		return UnoPlayer.Color.RED;
	}
	
	/* Returns whether or not the game is finishing and if so, which player is ahead */
	private int endGame( List<Card> hand, GameState state )
	{
		int tempInt = -1;
		
		for (int i = 0; i < state.getNumCardsInHandsOfUpcomingPlayers().length; i++)
	        {
        		if (state.getNumCardsInHandsOfUpcomingPlayers()[i] < 4)
        		{	
				if (tempInt != -1)
				{
					if (state.getNumCardsInHandsOfUpcomingPlayers()[i] < state.getNumCardsInHandsOfUpcomingPlayers()[tempInt])
						tempInt = i;
				}
				else tempInt = i;
          		}
		}
		
		if (tempInt != -1) 
			return tempInt;

        	if (state.getPlayedCards().size() > 80)
        	{
	       		tempInt = 0;
		        for (int i = 0; i < state.getTotalScoreOfUpcomingPlayers().length; i++)
            		{
	            		if (state.getTotalScoreOfUpcomingPlayers()[i] > state.getTotalScoreOfUpcomingPlayers()[tempInt])
	                		tempInt = i;
            		}
        	}

		return tempInt;
	}	

	/* Play the highest value of the same colored card you can, or switch to a different color with the same rank.
		Use REVERSES and SKIPS every now and then, also */
	private int standardPlay( List<Card> hand, Card upCard, Color calledColor, GameState state, int finishingPlayer )
	{
		int highestSameColored = playCard(hand, upCard.getColor());
		
		if (highestSameColored == -1)
			highestSameColored = playCard(hand, calledColor);

		if (hand.get(highestSameColored).getNumber() >= playCard(hand, upCard.getNumber()))
			return highestSameColored;

		else if (hand.get(highestSameColored).getNumber() < playCard(hand, upCard.getNumber()))
			return playCard(hand, upCard.getNumber());

		else if (highestSameColored != -1)
			return highestSameColored;

		else if (playCard(hand, UnoPlayer.Rank.SKIP) != -1)
			return playCard(hand, UnoPlayer.Rank.SKIP);

		else if (playCard(hand, UnoPlayer.Rank.REVERSE) != -1)
			return playCard(hand, UnoPlayer.Rank.REVERSE);

		return -1;
	}

	/* Try to ditch high value cards and hinder other player.  If you don't have any high value cards play as usual */
	private int endGamePlay( List<Card> hand, Card upCard,  Color calledColor, GameState state, int finishingPlayer )
	{	
		int tempInt;

		tempInt = playCard(hand, UnoPlayer.Rank.WILD_D4);
		if (tempInt != -1) return tempInt;

		tempInt = playCard(hand, UnoPlayer.Rank.WILD);
		if (tempInt != -1) return tempInt;

		tempInt = playCard(hand, UnoPlayer.Rank.DRAW_TWO);
		if (tempInt != -1) return tempInt;

		tempInt = playCard(hand, UnoPlayer.Rank.SKIP);
		if (tempInt != -1 && finishingPlayer == 0) return tempInt;
		
		tempInt = playCard(hand, UnoPlayer.Rank.REVERSE);
		if (tempInt != -1 && finishingPlayer == 0) return tempInt; // Make sure 0 is the next player up.  Also you could improve this.

		return standardPlay(hand, upCard, calledColor, state, finishingPlayer);		
	}

	/* Returns a card of a certain rank */
	private int playCard( List<Card> hand, Rank cardRank )
	{
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).getRank() == cardRank) 
				return i;
		}
		return -1;
	}
	
	/* This finds the highest ranked card of a given color */
	private int playCard( List<Card> hand, Color cardColor )
	{
		boolean foundOne = false;
		int currentHighest = 0;

		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).getNumber() > hand.get(currentHighest).getNumber() && hand.get(i).getColor() == cardColor)
			{
				currentHighest = i;
				foundOne = true;
			}
		}

		if (foundOne)
			return currentHighest;
		else
			return -1;
	}

	/* Finds different color same number cards, returns the other color */
	private int playCard( List<Card> hand, int number )
	{
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).getNumber() == number)
				return i;
		}
		return -1;
	}
	
	/* Populates the handColors array, seeing how many cards of each color is in the hand */
	private void countHand ( List<Card> hand )
	{
                for (int i = 0; i < hand.size(); i++)
                {
                        if (hand.get(i).getColor() == UnoPlayer.Color.RED)
                                handColors[0]++;
                        else if (hand.get(i).getColor() == UnoPlayer.Color.BLUE)
                                handColors[1]++;
                        else if (hand.get(i).getColor() == UnoPlayer.Color.GREEN)
                                handColors[2]++;
                        else if (hand.get(i).getColor() == UnoPlayer.Color.YELLOW)
                                handColors[3]++;
                }
	}

	/* Populates the handNumberValues array, seeing which color has the most points */
	private void countHandNumbers( List<Card> hand )
	{
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).getColor() == UnoPlayer.Color.RED)
				handNumberValues[0] += hand.get(i).getNumber();
			else if (hand.get(i).getColor() == UnoPlayer.Color.BLUE)
				handNumberValues[1] += hand.get(i).getNumber();
			else if (hand.get(i).getColor() == UnoPlayer.Color.GREEN)
				handNumberValues[2] += hand.get(i).getNumber();
			else if (hand.get(i).getColor() == UnoPlayer.Color.YELLOW)
				handNumberValues[3] += hand.get(i).getNumber();	
		}	
	}
}
