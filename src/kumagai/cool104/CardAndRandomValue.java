package kumagai.cool104;

import java.util.Random;

/**
 * カードとランダム値の対のオブジェクト。
 * @author kumagai
 */
public class CardAndRandomValue
{
	public final Card card;
	public final int randomValue;

	/**
	 * 指定の値をメンバーに割り当て。
	 * @param card カード
	 * @param random ランダムオブジェクト
	 */
	public CardAndRandomValue(Card card, Random random)
	{
		this.card = card;
		this.randomValue = random.nextInt();
	}
}
