package uno;

import java.util.List;

public class CappelloV2_UnoPlayer implements UnoPlayer
{
	// 2 of each number (18) + 1 zero (1) + 1 of skip, reverse, draw 2 (3) = 22
	// of each color
	// 4 wild + 4 draw 4

	// make 2D-Array that stores choices

	CardInfo[][] choices;
	int count = 0;
	Color cardColor = Color.NONE;
	boolean dumpCards = false;
	int blue = 0;
	int red = 0;
	int yellow = 0;
	int green = 0;
	int myB = 0;
	int myY = 0;
	int myR = 0;
	int myG = 0;
	Color rareC = Color.NONE;

	// puts playable

	class CardInfo
	{
		public Card card;
		public int pos;

		public CardInfo(Card c, int p)
		{
			this.card = c;
			pos = p;
		}
	}

	// populates 2D array of playable cards
	// 0 = number, 1 = reverse, 2 = skip, 3 = draw two, 4 = wild, 5 = wild four
	public void PopulateChoices(List<Card> hand, Card upCard, Color calledColor, GameState state)
	{
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		int e = 0;
		int f = 0;

		for (int x = 0; x < hand.size(); x++)
		{
			if (hand.get(x).canPlayOn(upCard, calledColor))
			{
				if (hand.get(x).getRank() == Rank.NUMBER)
				{
					choices[0][a] = new CardInfo(hand.get(x), x);
					a++;
				} else if (hand.get(x).getRank() == Rank.REVERSE)
				{
					choices[1][b] = new CardInfo(hand.get(x), x);
					b++;
				} else if (hand.get(x).getRank() == Rank.SKIP)
				{
					choices[2][c] = new CardInfo(hand.get(x), x);
					c++;
				} else if (hand.get(x).getRank() == Rank.DRAW_TWO)
				{
					choices[3][d] = new CardInfo(hand.get(x), x);
					d++;
				} else if (hand.get(x).getRank() == Rank.WILD)
				{
					choices[4][e] = new CardInfo(hand.get(x), x);
					e++;
				} else if (hand.get(x).getRank() == Rank.WILD_D4)
				{
					choices[5][f] = new CardInfo(hand.get(x), x);
					f++;
				}
			}
		}
	}

	public Color rareColor(List<Card> hand, GameState state)
	{
		for (int x = 0; x > hand.size(); x++)
		{
			if (state.getPlayedCards().get(x).getColor() == Color.BLUE)
			{
				blue++;
			}
			if (state.getPlayedCards().get(x).getColor() == Color.GREEN)
			{
				green++;
			}
			if (state.getPlayedCards().get(x).getColor() == Color.YELLOW)
			{
				yellow++;
			}
			if (state.getPlayedCards().get(x).getColor() == Color.RED)
			{
				red++;
			}
		}
		if (blue > green && blue > red && blue > yellow)
		{
			return Color.BLUE;
		} else if (green > blue && green > red && green > yellow)
		{
			return Color.GREEN;
		} else if (red > blue && red > green && red > yellow)
		{
			return Color.RED;
		} else if (yellow > blue && yellow > red && yellow > green)
		{
			return Color.YELLOW;
		}
		return Color.BLUE;
	}

	public int numOfSpecial(List<Card> hand)
	{
		for (int x = 0; x < hand.size(); x++)
		{
			if (hand.get(x).getRank() != Rank.NUMBER)
			{
				count++;
			}
		}
		return count;
	}

	// if someone has less cards than the # of special cards in your hand
	public boolean dumpCards(List<Card> hand, GameState state)
	{
		if (state.getNumCardsInHandsOfUpcomingPlayers()[0] < numOfSpecial(hand))
		{
			return true;
		}
		return false;
	}

	public int pickCard(CardInfo[][] cards)
	{
		for (int x = 0; x < cards.length; x++)
		{
			if (x != 0 && dumpCards == true) // special cards dump
			{
				if (cards[5][0] != null) // dump draw fours
				{
					return cards[5][0].pos;
				} else if (cards[4][0] != null) // dump wild
				{
					return cards[4][0].pos;
				} else if (cards[3][0] != null) // dump draw 2
				{
					return cards[3][0].pos;
				} else if (cards[2][0] != null) // dump skip
				{
					return cards[2][0].pos;
				} else if (cards[1][0] != null) // dump reverse
				{
					return cards[1][0].pos;
				}
			}
			if (cards[x][0] != null)
			{
				if (x == 0)
				{
					if (cards[x][0].card.getColor() == Color.BLUE)
					{
						myB++;
					} else if (cards[x][0].card.getColor() == Color.GREEN)
					{
						myG++;
					} else if (cards[x][0].card.getColor() == Color.YELLOW)
					{
						myY++;
					} else if (cards[x][0].card.getColor() == Color.RED)
					{
						myR++;
					}
					for (int y = 0; y < cards[x].length; y++)
					{
						if (cards[x][y] != null)
						{
							if (cards[x][y].card.getColor() == Color.BLUE && myB > myR && myB > myY && myB > myG)
							{
								return cards[x][y].pos;
							} else if (cards[x][y].card.getColor() == Color.GREEN && myG > myR && myG > myY && myG > myB)
							{
								return cards[x][y].pos;
							} else if (cards[x][y].card.getColor() == Color.RED && myR > myB && myR > myY && myR > myG)
							{
								return cards[x][y].pos;
							} else
								return cards[x][0].pos;
						}
					}
				}
				return cards[x][0].pos;
			}
		}
		return -1;
	}

	public int play(List<Card> hand, Card upCard, Color calledColor, GameState state)
	{
		choices = new CardInfo[6][hand.size()];
		PopulateChoices(hand, upCard, calledColor, state);
		dumpCards(hand, state);
		rareC = rareColor(hand, state);
		return pickCard(choices);
	}

	@Override
	public Color callColor(List<Card> hand)
	{
		return rareC;
	}
}