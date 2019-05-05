package com.exp.common.pojo.gamepojo;

public class GamePlayer implements Cloneable {

	public static final String OFFENSE = "OFFENSE"; //$NON-NLS-1$
	public static final String DEFENSE = "DEFENSE"; //$NON-NLS-1$
	
	private String playerId;
	private String playerName;
	private String side;

	public String getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getSide() {
		return this.side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public GamePlayer asOffense() {
		this.side = OFFENSE;
		return this;
	}
	
	public GamePlayer asDefense() {
		this.side = DEFENSE;
		return this;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		GamePlayer gamePlayer = (GamePlayer)super.clone();
		gamePlayer.playerId = this.playerId == null ? null : this.playerId.substring(0);
		gamePlayer.playerName = this.playerName == null ? null : this.playerName.substring(0);
		gamePlayer.side = this.side.substring(0);
		return super.clone();
	}
}
