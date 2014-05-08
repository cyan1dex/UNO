package uno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class flaVius_UnoPlayer implements UnoPlayer
{
	// public enum Positions {Number, Draw2, Reverse, Skip, Wild, W4};

	int								amtToWorry	= 4;
	ArrayList<ArrayList<CardInfo>>	cards		= new ArrayList<ArrayList<CardInfo>>(6);

	@Override
	public int play(List<Card> hand, Card upCard, Color calledColor,
			GameState state)
	{
		cards		= new ArrayList<ArrayList<CardInfo>>(6);
		InitializeHand(hand, upCard, calledColor);
		Collections.sort(cards.get(0));

		if (state.getNumCardsInHandsOfUpcomingPlayers()[0] < 3) // play D4
		{
			if (cards.get(5).size() > 0)
				return cards.get(5).get(0).pos;
		}
		if (state.getNumCardsInHandsOfUpcomingPlayers()[0] < hand.size() / 2 //check to play for special cards
				|| state.getNumCardsInHandsOfUpcomingPlayers()[0] < amtToWorry)
		{
			if (cards.get(1).size() > 0)
				return cards.get(1).get(0).pos;
			else if (cards.get(2).size() > 0)
				return cards.get(2).get(0).pos;
			else if (cards.get(3).size() > 0)
				return cards.get(3).get(0).pos;
		}
		if (cards.get(0).size() == 0) // Play the wild  
		{
			if (cards.get(4).size() > 0)
				return cards.get(4).get(0).pos;
		}
		for (int i = 0; i < cards.size(); i++)
		{
			if (cards.get(i).size() != 0)
				return cards.get(i).get(0).pos;
		}
		return -1;

	}

	@Override
	public Color callColor(List<Card> hand)
	{
		int red = 0;
		int green = 0;
		int blue = 0;
		int yellow = 0;

		for (Card c : hand)
		{
			switch (c.getColor())
			{
				case RED:
					red++;
				case BLUE:
					blue++;
				case YELLOW:
					yellow++;
				case GREEN:
					green++;
			}
		}

		if (red >= green && red >= blue && red >= yellow)
			return Color.RED;
		else if (blue >= green && blue >= yellow && blue >= red)
			return Color.BLUE;
		else if (green >= red && green >= blue && green >= yellow)
			return Color.GREEN;
		else
			return Color.YELLOW;

	}

	public void InitializeHand(List<Card> hand, Card upCard, Color called)
	{
		InitializeCardArray();

		int pos = 0;

		for (Card c : hand)
		{
			CardInfo info = new CardInfo(pos, c);

			if (c.canPlayOn(upCard, called))
			{
				switch (c.getRank())
				{
					case NUMBER:
					{
						PopulateHand(info, 0);
						break;
					}
					case DRAW_TWO:
					{
						PopulateHand(info, 1);
						break;
					}
					case REVERSE:
					{
						PopulateHand(info, 2);
						break;
					}
					case SKIP:
					{
						PopulateHand(info, 3);
						break;
					}
					case WILD:
					{
						PopulateHand(info, 4);
						break;
					}
					case WILD_D4:
					{
						PopulateHand(info, 5);
						break;
					}
					default:
					{
						break;
					}
				}
			}
			pos++;
		}
	}

	public void PopulateHand(CardInfo ci, int pos)
	{
		cards.get(pos).add(ci);
	}

	public void InitializeCardArray()
	{
		for (int p = 0; p < 6; p++)
		{

			ArrayList<CardInfo> cs = new ArrayList<CardInfo>();
			cards.add(cs);
		}
	}
}

class CardInfo implements Comparable
{
	int		pos;
	Card	c;

	public CardInfo(int p, Card c)
	{
		pos = p;
		this.c = c;
	}

	@Override
	public int compareTo(Object arg0)
	{
		if (c.getNumber() < ((CardInfo) arg0).c.getNumber() )
			return 1;
		else if (c.getNumber()== ((CardInfo) arg0).c.getNumber())
			return -1;

		return -1;
	}
}
