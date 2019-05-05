package com.exp.common.pojo.gamepojo;

import java.io.Serializable;
import java.util.Random;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Random RANDOM = new Random();
	/**
	 * 0：已有人接受挑战并完成设置，可以开始对战;
	 */
	public static final int READYTOMOVE = 0;
	/**
	 * -1： 新创建的game；
	 */
	public static final int NEWCREATED = -1;
	/**
	 * -4：game关闭;
	 */
	public static final int CLOSED = -4;

	protected String gameId;
	
	protected int status;
	
	protected String createrId;
	
	protected Game() {
		this.gameId = Long.toString(System.currentTimeMillis()) + RANDOM.nextInt(1000);
		this.status = NEWCREATED;
	}

	public String getGameId() {
		return this.gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreaterId() {
		return this.createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	
	
}
