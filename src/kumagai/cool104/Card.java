package kumagai.cool104;

/**
 * カードオブジェクト。
 * @author kumagai
 */
public class Card
{
	public final CardType cardType;
	public final int number;

	/**
	 * 指定の値をメンバーに割り当てる。
	 * @param cardType カードの柄
	 * @param number 1-13の番号
	 */
	public Card(CardType cardType, int number)
	{
		this.cardType = cardType;
		this.number = number;
	}

	/**
	 * 柄か数字が同じで選択可能かを判定。
	 * @param previousSelect 比較するカード
	 * @return true=選択可能／false=不能
	 */
	public boolean canSelect(Card previousSelect)
	{
		if (previousSelect != null)
		{
			// 比較するカードが指定されている。

			return
				cardType == previousSelect.cardType ||
				number == previousSelect.number;
		}
		else
		{
			// 比較するカードが指定されていない。

			return true;
		}
	}

	/*
	 * (非 Javadoc)
	 * 「H01」といった短い形式の文字列化。
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return String.format("%c%02d", cardType.toString().charAt(0), number + 1);
	}
}
