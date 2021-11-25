package kumagai.cool104;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * Cool 104 GUIメイン。
 */
public class Cool104Main
	extends JFrame
{
	static private LineBorder border = new LineBorder(Color.BLUE, 1, true);

	/**
	 * エントリポイント。
	 * @param args [0]=seed（省略可）
	 */
	public static void main(String[] args)
	{
		int seed = 23;

		if (args.length >= 1)
		{
			// 引数指定されている。

			seed = Integer.valueOf(args[0]);
		}

		new Cool104Main(seed);
	}

	private final JTextField textSeed;
	private final JLabel labelSucceedCount;
	private final JLabel labelPreviousCard;
	private final JLabel [] labelTableCard;
	private final ImageIcon [][] cardImage;
	private final JLabel messageLabel;

	private Card previousCard;
	private SearchResult searchResult;
	private Table table;
	private int succeedCount;
	private boolean clearRoute;

	/**
	 * 画面構築。
	 * @param seed ランダムシード値
	 */
	public Cool104Main(int seed)
	{
		super("Cool 104");

		setSize(600, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// カード画像読み込み。
		cardImage = new ImageIcon [13][];

		for (int i=0 ; i<13 ; i++)
		{
			int j = 0;

			cardImage[i] = new ImageIcon [4];

			for (CardType type : CardType.values())
			{
				cardImage[i][j] =
					new ImageIcon(
						String.format(
							"image/%c%02d.png",
							type.toString().charAt(0),
							i + 1));
				j++;
			}
		}

		// 画面構築。
		setLayout(new BorderLayout());

		// 上部操作パネル。
		JPanel topControlPanel = new JPanel();
		add(topControlPanel, BorderLayout.NORTH);

		textSeed = new JTextField(5);
		topControlPanel.add(textSeed);
		textSeed.setText(Integer.toString(seed));
		textSeed.setHorizontalAlignment(JTextField.RIGHT);

		JButton buttonNextGame = new JButton("次のゲーム");
		topControlPanel.add(buttonNextGame);
		buttonNextGame.addActionListener(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int seed = Integer.valueOf(textSeed.getText());

					newGame(seed);
				}
			});

		labelSucceedCount = new JLabel();
		topControlPanel.add(labelSucceedCount);

		// カードパネル。
		JPanel cardPanel = new JPanel();
		add(cardPanel, BorderLayout.CENTER);

		cardPanel.setLayout(new GridLayout(2, Table.tableCardNum));

		labelPreviousCard = new JLabel();
		labelPreviousCard.setHorizontalAlignment(JLabel.CENTER);
		cardPanel.add(new JLabel());
		cardPanel.add(new JLabel());
		cardPanel.add(labelPreviousCard);
		cardPanel.add(new JLabel());
		cardPanel.add(new JLabel());
		cardPanel.setPreferredSize(new Dimension(100, 150));

		// メッセージパネル。
		JPanel messagePanel = new JPanel();
		add(messagePanel, BorderLayout.SOUTH);

		messageLabel = new JLabel("PLAY");
		messagePanel.add(messageLabel);

		labelTableCard = new JLabel[Table.tableCardNum];

		for (int i=0 ; i<Table.tableCardNum ; i++)
		{
			labelTableCard[i] = new JLabel();

			final int finali = i;

			labelTableCard[i].addMouseListener(
				new MouseAdapter()
				{
					/**
					 * クリック時。
					 */
					@Override
					public void mouseClicked(MouseEvent event)
					{
						selectCard(finali);
					}
				});
		}

		for (int i=0 ; i<Table.tableCardNum ; i++)
		{
			cardPanel.add(labelTableCard[i]);

			labelTableCard[i].setHorizontalAlignment(JLabel.CENTER);
		}

		newGame(seed);

		setVisible(true);
	}

	/**
	 * 新しいゲーム開始。
	 * @param seed ランダムシード値
	 */
	private void newGame(int seed)
	{
		// カード情報生成。
		ShuffleCardCollection shuffleCardCollection =
			new ShuffleCardCollection(new FullCardCollection(), seed);

		table = new Table(shuffleCardCollection);

		succeedCount = 0;

		labelPreviousCard.setIcon(new ImageIcon());

		// 解く
		searchResult = new SearchResult();

		table.resolveRecursive(0, null, searchResult, false);

		clearRoute = searchResult.clearPattern != null;

		for (int i=0 ; i<Table.tableCardNum ; i++)
		{
			int number = table.cards[i].number;
			int type = table.cards[i].cardType.ordinal();

			labelTableCard[i].setIcon(cardImage[number][type]);

			if (searchResult.clearPattern != null &&
				searchResult.clearPattern[succeedCount] == table.cards[i])
			{
				// クリアできるカード。

				labelTableCard[i].setBorder(border);
			}
			else
			{
				// クリアできるカードではない。

				labelTableCard[i].setBorder(null);
			}
		}

		previousCard = null;
		labelSucceedCount.setText(Integer.toString(succeedCount));
		messageLabel.setText("PLAY");
	}

	/**
	 * カード選択時処理。
	 * @param index 0-4のインデックス
	 */
	private void selectCard(int index)
	{
		if (succeedCount >= Table.fullCardNum)
		{
			// 全カードを使いきった。

			return;
		}

		boolean clearRoute = this.clearRoute;

		if (this.clearRoute)
		{
			// クリアルートに従っていた。

			if (searchResult.clearPattern[succeedCount] !=
				table.cards[index])
			{
				// クリアルートに従ってない。

				clearRoute = false;
			}
		}

		Card card = table.selectCard(index, previousCard, false);

		if (card != null)
		{
			// 選択できた。

			this.clearRoute = clearRoute;

			if (! this.clearRoute)
			{
				// クリアルートに従ってない。

				for (int i=0 ; i<Table.tableCardNum ; i++)
				{
					labelTableCard[i].setBorder(null);
				}
			}

			previousCard = card;

			if (table.cards[index] != null)
			{
				// そこにカードあり。

				int number = table.cards[index].number;
				int type = table.cards[index].cardType.ordinal();

				labelTableCard[index].setIcon(cardImage[number][type]);
			}
			else
			{
				// そこにカードなし。

				labelTableCard[index].setIcon(null);
			}

			labelPreviousCard.setIcon(
				cardImage[card.number][card.cardType.ordinal()]);

			// 枠線切り替え。
			labelTableCard[index].setBorder(null);
			succeedCount++;

			if (succeedCount >= Table.fullCardNum)
			{
				// 全カードを使いきった。

				messageLabel.setText("CLEAR!!");
			}
			else if (! table.canSelect(card))
			{
				// 選択できるカードがない。

				messageLabel.setText("GAME OVER");
			}

			labelSucceedCount.setText(Integer.toString(succeedCount));

			if (searchResult.clearPattern != null &&
				succeedCount < searchResult.clearPattern.length)
			{
				// 範囲内。

				if (clearRoute)
				{
					// クリアルートに従っている。

					for (int i=0 ; i<Table.tableCardNum ; i++)
					{
						if (searchResult.clearPattern[succeedCount] ==
							table.cards[i])
						{
							// クリアできるカード。

							labelTableCard[i].setBorder(border);
						}
					}
				}
			}
		}
	}
}
