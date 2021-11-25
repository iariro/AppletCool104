package kumagai.cool104;

import java.util.Arrays;

/**
 * カードを５枚並べる場。
 * @author kumagai
 */
public class Table
{
	static public final int tableCardNum = 5;
	static public final int fullCardNum = 52;

	public final Card [] cards;
	public final Card [] yama;

	public int yamaIndex;

	/**
	 * 場の初期化。
	 * @param yama 山
	 */
	public Table(ShuffleCardCollection yama)
	{
		this.yama = new Card [yama.size()];

		cards = new Card [tableCardNum];

		for (int i=0 ; i<tableCardNum ; i++)
		{
			cards[i] = yama.remove(0).card;
		}

		for (int i=0 ; i<yama.size() ; i++)
		{
			this.yama[i] = yama.get(i).card;
		}
	}

	/**
	 * 場と山のカードをダンプ。
	 */
	public void printCards()
	{
		System.out.print("cards : ");
		for (int i=0 ; i<tableCardNum ; i++)
		{
			if (i > 0)
			{
				// ２個目以降。

				System.out.print("/");
			}

			System.out.print(cards[i]);
		}
		System.out.println();

		System.out.print("yama : ");
		for (int i=0 ; i<yama.length ; i++)
		{
			if (i > 0)
			{
				// ２個目以降。

				System.out.print("/");
			}

			System.out.print(yama[i]);
		}

		System.out.println();
	}

	/**
	 * 山の枚数を取得。
	 * @return 山の枚数
	 */
	public int getYamaNum()
	{
		return yama.length - yamaIndex;
	}

	/**
	 * ダンプ表示。
	 */
	public void dumpTable()
	{
		for (int i=0 ; i<tableCardNum ; i++)
		{
			if (i > 0)
			{
				// ２個目以降。

				System.out.printf("/");
			}

			if (cards[i] != null)
			{
				// カードあり。

				System.out.printf(cards[i].toString());
			}
			else
			{
				// カードなし。

				System.out.printf("---");
			}
		}
		System.out.printf(" %d\n", yamaIndex);
	}

	/**
	 * カードを１枚選択。
	 * @param index 0-4のインデックス
	 * @param previousCard 直前に選択したカード
	 * @param debug true=デバッグモード
	 * @return 選択したカード
	 */
	public Card selectCard(int index, Card previousCard, boolean debug)
	{
		if (previousCard == null ||
			(cards[index] != null && cards[index].canSelect(previousCard)))
		{
			// 選択できるカードである。

			if (debug)
			{
				// デバッグ表示あり。

				dumpTable();
			}

			Card previous = cards[index];

			if (yamaIndex + 1 < yama.length)
			{
				// まだ山にカードはある。

				cards[index] = yama[yamaIndex];
				yamaIndex++;
			}
			else
			{
				// もう山にカードはない。

				cards[index] = null;
			}

			if (debug)
			{
				// デバッグ表示あり。

				for (int i=0 ; i<index ; i++)
				{
					System.out.print("    ");
				}
				System.out.println(" ^");
			}

			return previous;
		}

		return null;
	}

	/**
	 * カードを戻す。
	 * @param index 0-4のインデックス
	 * @param card 戻すカード
	 */
	public void returnCard(int index, Card card)
	{
		cards[index] = card;

		if (yamaIndex > 0)
		{
			// 先頭ではない。

			yamaIndex--;
		}
	}

	/**
	 * 選択できるカードがあるかを判定。
	 * @param card 比較するカード
	 * @return true=選択できる／false=できない
	 */
	public boolean canSelect(Card card)
	{
		for (int i=0 ; i<tableCardNum ; i++)
		{
			if (cards[i] != null && cards[i].canSelect(card))
			{
				// 選択できる。

				return true;
			}
		}

		return false;
	}

	/**
	 * 再帰的にゲームを解く。
	 * @param depth 再帰の深さ
	 * @param previousCard 直前に選択したカード
	 * @param searchResult 検索結果
	 * @param debug true=デバッグモード
	 * @return 最大の深さ
	 */
	public int resolveRecursive
		(int depth, Card previousCard, SearchResult searchResult, boolean debug)
	{
		int ret = -1;

		if (depth < fullCardNum && searchResult.clearCount < 1000)
		{
			// まだ残りがある。

			if (canSelect(previousCard))
			{
				// ゲーム続行可能。

				int count = 0;

				for (int i=0 ; i<Table.tableCardNum ; i++)
				{
					Card card = selectCard(i, previousCard, debug);

					if (card != null)
					{
						// カードは選択できた。

						searchResult.selected[depth] = card;

						int d =
							resolveRecursive
								(depth + 1, card, searchResult, debug);

						if (d > ret)
						{
							// 現状どの値よりも大きい。

							ret = d;
						}

						returnCard(i, card);

						count++;
					}
				}

				if (ret < 0)
				{
					// 再帰呼び出し分なし＝末端。

					if (count > 0)
					{
						// １枚でも選択できた。

						ret = depth;
					}
				}
			}
		}
		else
		{
			// 残りがない。クリアーとみなす。

			if (searchResult.clearCount == 0)
			{
				// １個目。

				searchResult.clearPattern =
					Arrays.copyOf
						(searchResult.selected, searchResult.selected.length);
			}

			searchResult.clearCount++;
		}

		return ret;
	}
}
