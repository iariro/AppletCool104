package kumagai.cool104;

import java.util.*;

/**
 * 全カードのコレクション。
 * @author kumagai
 */
public class FullCardCollection
	extends ArrayList<Card>
{
	/**
	 * 全カードのコレクションを構築。
	 */
	public FullCardCollection()
	{
		for (int i=0 ; i<13 ; i++)
		{
			for (CardType cardType : CardType.values())
			{
				add(new Card(cardType, i));
			}
		}
	}
}
