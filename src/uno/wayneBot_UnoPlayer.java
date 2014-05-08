package uno;

import java.util.List;

public class wayneBot_UnoPlayer implements UnoPlayer
{

	@Override
	public int play(List<Card> hand, Card upCard, Color calledColor,GameState state) 
	{
		boolean okO=false;
		boolean okU=false;
		boolean ok = false;
		boolean ok1=false;
		
		if (addMoar(state)==true)
		{
			for (int a =0; a<hand.size(); a++)
			{
				if ((hand.get(a).getRank()==Rank.WILD_D4))
				{
					okO=true;
					return a;
				}
				else if (hand.get(a).getRank().DRAW_TWO== Rank.DRAW_TWO && hand.get(a).getColor() == upCard.getColor())
				{
					okO=true;
					return a;
				}
				
			}
		}
		
		else if (okO==false && toSwitch2(state)==true)
		{
			for (int u=0; u<hand.size();u++)
			{
				if (hand.get(u).getRank()==Rank.REVERSE)
				{
					okU=true;
					return u;
				}
			}
		}
		
		else if (okU==false && isMostPlayed(state)==mostColorinHand(hand))
		{
			for (int i=0; i<hand.size(); i++)
			{
				if (hand.get(i).getColor()== isMostPlayed(state) && hand.get(i).canPlayOn(upCard, calledColor))
				{
					ok=true;
					return i;
				}
			}
		}
		else if (ok==false)
		{
			for (int c=0; c<hand.size(); c++)
			{
			
				if (hand.get(c).getColor()== isMostPlayed(state) && hand.get(c).canPlayOn(upCard, calledColor))
				{
					ok1=true;
					return c; 
				}
			}
		}
		
		if ( ok==false && ok1==false )
		{
			
			for (int j=0; j<hand.size(); j++)
			{
				if (hand.get(j).canPlayOn(upCard, calledColor))
					return j;
			}
		}
		
		return -1;
	}

	@Override
	public Color callColor(List<Card> hand) 
	{
		return Color.BLUE;
		
	}
	
	public Color isMostPlayed (GameState state)
	{
		int blue = 0;
		int green=0;
		int red=0;
		int yellow=0;
		Color temps = null;
		
		for (int i=0; i< state.getPlayedCards().size(); i++)
		{
			Color temp= state.getPlayedCards().get(i).getColor();
			
			if (temp==Color.BLUE)
				blue++;
			
			else if (temp==Color.GREEN)
				green++;
			
			else if (temp==Color.RED)
				red++;
			
			else if (temp==Color.YELLOW)
				yellow++;
			
			else if (temp==Color.NONE)
			{}
		}	
		
		if (blue> green && blue>red && blue>yellow)
			temps= Color.BLUE;
		
		else if (green>blue && green>red && green>yellow)
			temps=Color.GREEN;
		
		else if (red>blue && red>green && red>yellow)
			temps=Color.RED;
		
		else if (yellow>blue && yellow>green && yellow>red)
			temps=Color.YELLOW;
		
		
		
		return temps;	
	}
	
	public Color mostColorinHand (List<Card> hand)
	{
		int blue = 0;
		int green=0;
		int red=0;
		int yellow=0;
		Color tempr=null;
		
		for (int i=0; i< hand.size(); i++)
		{
			Color temp= hand.get(i).getColor();
			
			if (temp==Color.BLUE)
				blue++;
			
			else if (temp==Color.GREEN)
				green++;
			
			else if (temp==Color.RED)
				red++;
			
			else if (temp==Color.YELLOW)
				yellow++;
			
			else if (temp==Color.NONE)
			{}
		}
		
		if (blue> green && blue>red && blue>yellow)
			tempr= Color.BLUE;
		
		else if (green>blue && green>red && green>yellow)
			tempr=Color.GREEN;
		
		else if (red>blue && red>green && red>yellow)
			tempr=Color.RED;
		
		else if (yellow>blue && yellow>green && yellow>red)
			tempr=Color.YELLOW;
		
		return tempr;
	}
	
	public boolean toSwitch (GameState state)
	{
		if (state.getPlayedCards().size()>=40 && state.getNumCardsInHandsOfUpcomingPlayers()[0] < state.getNumCardsInHandsOfUpcomingPlayers()[3])
		{
			return true;
		}
		else
		{
		return false;
		}
	}
	
	public boolean toSwitch2 (GameState state)
	{
		if (state.getNumCardsInHandsOfUpcomingPlayers()[0] <=5)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public boolean addMoar (GameState state)
	{
		if (state.getNumCardsInHandsOfUpcomingPlayers()[0] <=3)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public int GetSpecialCards (List<Card> hand)
	{
		int total=0;
		for (Card c: hand)
		{
			if (c.getRank() != Rank.NUMBER)
				total++;
		}
		return total;
	}
	
	
	
	

}
