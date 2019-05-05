package com.exp.animalchecker.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.exp.common.pojo.gamepojo.ChessGame;
import com.exp.common.pojo.gamepojo.Game;
import com.exp.common.pojo.gamepojo.GamePlayer;
import com.exp.common.util.ArrayUtil;

public class AnimalCheckerGame extends ChessGame {

	private static final long serialVersionUID = 1L;
	private static final int BOARDROWS = 7;
	private static final int BOARDCOLS = 6;
	public static final String ELEPHANT = "象"; //$NON-NLS-1$
	public static final String TIGER = "虎"; //$NON-NLS-1$
	public static final String WOLF = "狼"; //$NON-NLS-1$
	public static final String DOG = "狗"; //$NON-NLS-1$
	public static final String CAT = "猫"; //$NON-NLS-1$
	public static final String MOUSE = "鼠"; //$NON-NLS-1$
	public static final String FOOTBALL = "球"; //$NON-NLS-1$
	public static final String ALL = "象虎狼狗猫鼠球"; //$NON-NLS-1$

	public static Chessman[] chessmen = new Chessman[BOARDROWS * BOARDCOLS];

	/**
	 * 创建人为defense，设置人为offense；
	 * 由offense首先发起进攻。
	 */
	private GamePlayer lastPlayer = new GamePlayer();

	private static class AnimalCheckerGamePool {

		public static ScheduledThreadPoolExecutor  selfCheckExecutor = new ScheduledThreadPoolExecutor(20);
		/**
		 * 已创建game的阻塞队列，有人接受对战时将取出一个game并setup。 FIFO。
		 */
		public static ArrayBlockingQueue<AnimalCheckerGame> createdGameBQ = new ArrayBlockingQueue<>(20);
		/**
		 * 已准备好（setup）的game。
		 */
		public static Map<String, AnimalCheckerGame> preparedGames = new HashMap<>();
		
		/**
		 * 每次action后，变化的chessman。
		 */
		public static Map<String, List<Chessman>> changedChessmen = new HashMap<>();
	}

	public static ArrayBlockingQueue<AnimalCheckerGame> getCreatedGameBQ() {
		return AnimalCheckerGamePool.createdGameBQ;
	}

	public static Map<String, AnimalCheckerGame> getPreparedGames() {
		return AnimalCheckerGamePool.preparedGames;
	}
	
	public static Map<String, List<Chessman>> getChangedChessmen() {
		return AnimalCheckerGamePool.changedChessmen;
	}

	public GamePlayer getLastPlayer() {
		return this.lastPlayer;
	}

	public void setLastPlayer(GamePlayer lastPlayer) {
		this.lastPlayer = lastPlayer;
	}

	static {
		String[] arr = new String[] {MOUSE, MOUSE, MOUSE, MOUSE, MOUSE, CAT, CAT, CAT, CAT, DOG, DOG, DOG, DOG, WOLF, WOLF, WOLF, TIGER, TIGER, ELEPHANT, FOOTBALL, FOOTBALL};
		for(int i = 0; i < arr.length; i++) {
			chessmen[i] = new Chessman(arr[i]).asOffense();
			chessmen[chessmen.length - 1 - i] = new Chessman(arr[i]).asDefense();
		}
		AnimalCheckerGamePool.selfCheckExecutor.setKeepAliveTime(20, TimeUnit.SECONDS);
		AnimalCheckerGamePool.selfCheckExecutor.allowCoreThreadTimeOut(true);
	}

	public static AnimalCheckerGame prepare(String createrId) {
		return new AnimalCheckerGame(createrId);
	}

	private AnimalCheckerGame(String createrId) {
		this.setCreaterId(createrId);
		this.setExpire(System.currentTimeMillis() + 60000L);
	}

	public AnimalCheckerGame setup(String playerId) {
		this.chessboard = new Chessman[BOARDROWS][BOARDCOLS];
		Chessman[] copy = chessmen.clone();
		for(int i = 0; i < copy.length; i++) {
			try {
				copy[i] = (Chessman)copy[i].clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		Chessman[] disordered = ArrayUtil.disorderRandomly(copy);
		int index = 0;
		for (int i = 0; i < BOARDROWS; i++) {
			for (int j = 0; j < BOARDCOLS; j++) {
				disordered[index].getPlayer().setPlayerId(disordered[index].getPlayer().getSide() == GamePlayer.OFFENSE ? playerId : this.getLastPlayer().getPlayerId());
				disordered[index].setChessmanId(index);
				this.chessboard[i][j] = disordered[index++].place(i, j);
			}
		}
		this.setStatus(Game.READYTOMOVE); // 可以开始翻开/移动棋子了
		
		final AnimalCheckerGame currentGame = this;
		
		currentGame.setExpire(System.currentTimeMillis() + 30000L);
		getChangedChessmen().put(currentGame.gameId, new ArrayList<Chessman>());
		
		AnimalCheckerGamePool.selfCheckExecutor.execute(new Runnable() {
			@Override
			public void run() {
				long expire = currentGame.getExpire();
				synchronized (currentGame) {
					while(System.currentTimeMillis() < expire) {
						if(currentGame.getStatus() == Game.CLOSED) {
							return;
						}
						try {
							currentGame.wait(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						expire = currentGame.getExpire();
					}
					if(currentGame.getStatus() != Game.CLOSED) {
						currentGame.setWinner(currentGame.getLastPlayer());
						currentGame.setStatus(Game.CLOSED);
					}
				}
			}
		});
		return this;
	}
}
