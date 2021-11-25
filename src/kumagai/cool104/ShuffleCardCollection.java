package kumagai.cool104;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * シャッフル済みカードコレクション。
 * @author kumagai
 */
public class ShuffleCardCollection
	extends ArrayList<CardAndRandomValue>
{
	/**
	 * シャッフル状態のカードコレクションを構築する。
	 * @param cards カードコレクション
	 * @param seed ランダムシード
	 */
	public ShuffleCardCollection(ArrayList<Card> cards, int seed)
	{
		Random random = new Random(seed);

		for (Card card : cards)
		{
			add(new CardAndRandomValue(card, random));
		}

		Collections.sort(
			this,
			new Comparator<CardAndRandomValue>()
			{
				@Override
				public int compare(CardAndRandomValue b1, CardAndRandomValue b2)
				{
					return Integer.compare(b1.randomValue, b2.randomValue);
				}
			});
	}
}
