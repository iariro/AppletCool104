package kumagai.cool104;

/**
 * Cool 104シミュレーションプログラムメイン部。
 * @author kumagai
 */
public class SouatariMain
{
	/**
	 * エントリポイント。
	 * @param args 未使用
	 */
	static public void main(String[] args)
	{
		if (args.length >= 1)
		{
			int tonum = Integer.valueOf(args[0]);

			FullCardCollection fullCardCollection = new FullCardCollection();

			int [] stat = new int [Table.fullCardNum];

			for (int i=0 ; i<tonum ; i++)
			{
				ShuffleCardCollection cardAndRandomValueCollection =
					new ShuffleCardCollection(fullCardCollection, i);

				Table table = new Table(cardAndRandomValueCollection);

				SearchResult searchResult = new SearchResult();

				int max = table.resolveRecursive(0, null, searchResult, false);

				if (max >= 0)
				{
					// １手でも進められた。

					stat[max]++;
				}

				if (searchResult.clearPattern != null)
				{
					// クリアできた。

					System.out.printf("%2d : ", i);
					searchResult.printSelectedCard();
				}
			}
			System.out.println();

			for (int i=0 ; i<Table.fullCardNum ; i++)
			{
				if (stat[i] > 0)
				{
					System.out.printf("%2d : %d\n", i + 1, stat[i]);
				}
				else
				{
					System.out.printf("%2d : \n", i + 1);
				}
			}
		}
	}
}
