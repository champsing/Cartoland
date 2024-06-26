package cartoland.mini_games;

import cartoland.utilities.Algorithm;

import java.util.Arrays;

/**
 * {@code OneATwoBGame} is the backend of the 1A2B game, it can process the entire game with all fields and methods.
 * This game will generate a {@link #ANSWER_LENGTH} digits long number. Leading 0 is allowed. Players need to guess
 * the number, if the place of a digit is right, that is an A; if the digit is right but the place is wrong, that is a B.
 *
 * @since 1.1
 * @see cartoland.commands.OneATwoBCommand The frontend of the 1A2B game.
 * @author Alex Cai
 */
public class OneATwoBGame extends MiniGame
{
	public static final int ANSWER_LENGTH = 4; //答案的長度

	private final int[] answer = new int[ANSWER_LENGTH]; //答案
	private final boolean[] metBefore = new boolean[10];
	private final boolean[] inAnswer = { false,false,false,false,false,false,false,false,false,false };
	private int guesses = 0;

	public OneATwoBGame()
	{
		int[] zeroToNine = { 0,1,2,3,4,5,6,7,8,9 };
		Algorithm.shuffle(zeroToNine); //洗牌0 ~ 9

		for (int i = 0; i < ANSWER_LENGTH; i++) //產生答案
		{
			answer[i] = zeroToNine[i];
			inAnswer[answer[i]] = true;
		}
	}

	@Override
	public String gameName()
	{
		return "one_a_two_b";
	}

	private final int[] ab = new int[2];

	public int[] calculateAAndB(int input)
	{
		guesses++; //增加猜測數
		ab[0] = ab[1] = 0; //初始化A和B
		Arrays.fill(metBefore, false);

		for (int i = ANSWER_LENGTH - 1, digits = input; i >= 0; i--) //從後面檢測回來
		{
			int digit = digits % 10; //取最右邊一位數字
			if (metBefore[digit]) //遇過這個數字了
				return null; //回傳null 不必擲出exception 也不必記錄

			metBefore[digit] = true; //記錄遇過這個數字了

			if (answer[i] == digit) //數字和位置一樣
				ab[0]++; //a++
			else if (inAnswer[digit]) //只有數字中 位置錯了
				ab[1]++; //b++

			digits /= 10; //下一個數字
		}

		recordBuilder.append(String.format("%0" + ANSWER_LENGTH + "d", input))
				.append(" = ")
				.append(ab[0])
				.append('A')
				.append(ab[1])
				.append("B"); //記錄下來
		if (ab[0] != ANSWER_LENGTH) //如果還要繼續
			recordBuilder.append('\n'); //記錄換行

		return ab; //結果
	}

	public int getGuesses()
	{
		return guesses;
	}

	public String getAnswerString()
	{
		return Arrays.toString(answer);
	}
}