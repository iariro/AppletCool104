package kumagai.cool104;

/**
 * 検索結果オブジェクト。
 * @author kumagai
 */
public class SearchResult
{
	public final Card [] selected;

	public Card [] clearPattern;
	public int clearCount;

	/**
	 * メンバーを初期化。
	 */
	public SearchResult()
	{
		selected = new Card [Table.fullCardNum];
	}

	/**
	 * 選択されたカードを表示。
	 */
	public void printSelectedCard()
	{
		for (int j=0 ; j<clearPattern.length ; j++)
		{
			if (j > 0)
			{
				System.out.print("/");
			}
			System.out.print(clearPattern[j].toString());
		}
		System.out.println();
	}
}
