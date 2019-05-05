package com.exp.common.pojo.gamepojo;

import java.io.Serializable;

public class ChessGame extends Game {

	private static final long serialVersionUID = 1L;

	protected Chessman[][] chessboard;
	
	protected long expire;
	
	protected GamePlayer winner;
	
	public Chessman[][] getChessboard() {
		return this.chessboard;
	}

	public void setChessboard(Chessman[][] chessboard) {
		this.chessboard = chessboard;
	}

	public long getExpire() {
		return this.expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public GamePlayer getWinner() {
		return this.winner;
	}

	public void setWinner(GamePlayer winner) {
		this.winner = winner;
	}

	/**
	 * 棋类游戏必备的棋子元素。
	 * @author Exprass
	 *
	 */
	protected static class Chessman implements Serializable, Cloneable {
		private static final long serialVersionUID = 1L;

		private static GamePlayer offense = new GamePlayer().asOffense();
		private static GamePlayer defense = new GamePlayer().asDefense();

		int chessmanId;
		String chessmanName;
		int[] location = new int[2];
		GamePlayer player;
		boolean open = false;

		public Chessman(String chessmanName ) {
			super();
			this.chessmanName = chessmanName;
		}

		public int getChessmanId() {
			return this.chessmanId;
		}

		public void setChessmanId(int chessmanId) {
			this.chessmanId = chessmanId;
		}

		public String getChessmanName() {
			return this.chessmanName;
		}

		public void setChessmanName(String chessmanName) {
			this.chessmanName = chessmanName;
		}

		public int[] getLocation() {
			return this.location;
		}

//		public void setLocation(int[] location) {
//			this.location = location;
//		}

		public GamePlayer getPlayer() {
			return this.player;
		}

		public void setPlayer(GamePlayer player) {
			this.player = player;
		}

		public boolean isOpen() {
			return this.open;
		}

		public void setOpen(boolean open) {
			this.open = open;
		}

		public Chessman place(int x, int y) {
			this.location[0] = x;
			this.location[1] = y;
			return this;
		}

		public Chessman asOffense() {
			this.player = offense;
			return this;
		}
		
		public Chessman asDefense() {
			this.player = defense;
			return this;
		}

		@Override
		public Object clone() throws CloneNotSupportedException {
			Chessman chessman = (Chessman)super.clone();
			chessman.chessmanName = this.chessmanName.substring(0);
			chessman.player = (GamePlayer)this.player.clone();
			return super.clone();
		}
	}
	
	public String getChessmanNameLocated(int x, int y) {
		return this.chessboard[x][y].getChessmanName();
	}
	
	public GamePlayer getChessmanPlayerLocated(int x, int y) {
		return this.chessboard[x][y].getPlayer();
	}
	
	public boolean isChessmanOpenLocated(int x, int y) {
		return this.chessboard[x][y].isOpen();
	}
	
	public void openChessmanLocated(int x, int y) {
		this.chessboard[x][y].setOpen(true);
	}

	public Chessman destroyChessmanLocated(int x, int y) {
		this.chessboard[x][y].setChessmanName(null);
		this.chessboard[x][y].setPlayer(null);
		return this.chessboard[x][y];
	}
	
	/**
	 * 移动，位置替换。
	 * 更改[fromX, fromY]位置的坐标为[toX, toY]。下标不在此处调整。
	 * @param toX
	 * @param toY
	 * @param fromX
	 * @param fromY
	 * @return
	 */
	public Chessman move(int toX, int toY, int fromX, int fromY) {
		this.getChessboard()[fromX][fromY].getLocation()[0] = toX;
		this.getChessboard()[fromX][fromY].getLocation()[1] = toY;
		return this.chessboard[fromX][fromY];
	}

	public static Chessman generateAsDestroyed(int x, int y) {
		Chessman chessman = new Chessman(null);
		chessman.setPlayer(null);
		chessman.getLocation()[0] = x;
		chessman.getLocation()[1] = y;
		return chessman;
	}

	/**
	 * 判断当前用户是否胜利。
	 * 
	 * 剩下的chessman都归当前用户所有，则胜利，反之没有。
	 * @param playerId
	 * @return
	 */
	public boolean winOrNot(String playerId) {
		Chessman temp;
		for(int i = 0; i < this.getChessboard().length; i++) {
			for(int j = 0; j < this.getChessboard()[i].length; j++) {
				temp = this.getChessboard()[i][j];
				if(temp != null && !temp.getPlayer().getPlayerId().equals(playerId)) {
					return false;
				}
			}
		}
		return true;
	}
}
