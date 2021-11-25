package kumagai.cool104;

/**
 * １ゲームのシミュレーションメイン。
 */
public class OneGameMain
{
	/**
	 * エントリポイント。
	 * @param args [0]=ランダムシード
	 */
	public static void main(String[] args)
	{
		if (args.length >= 1)
		{
			// 引数あり。

			int seed = Integer.valueOf(args[0]);

			ShuffleCardCollection shuffleCardCollection =
				new ShuffleCardCollection(new FullCardCollection(), seed);

			Table table = new Table(shuffleCardCollection);

			SearchResult searchResult = new SearchResult();

			table.resolveRecursive(0, null, searchResult, true);

			if (searchResult.clearPattern != null)
			{
				// クリア情報あり。

				System.out.printf("%2d : ", seed);
				searchResult.printSelectedCard();
			}
		}
	}
}
