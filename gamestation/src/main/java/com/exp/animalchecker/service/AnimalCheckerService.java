package com.exp.animalchecker.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.exp.animalchecker.pojo.AnimalCheckerGame;
import com.exp.common.base.service.BaseService;
import com.exp.common.pojo.gamepojo.ChessGame;
import com.exp.common.pojo.gamepojo.Game;
import com.exp.common.pojo.gamepojo.GamePlayer;
import com.exp.system.pojo.User;

/**
 * @author Exprass
 *
 */
public class AnimalCheckerService extends BaseService {

	private static Log logger = LogFactory.getLog(AnimalCheckerService.class);

	public JSONObject prepare(User user) {
		JSONObject json = new JSONObject();
		String playerId = String.valueOf(user.getId());
		AnimalCheckerGame game = AnimalCheckerGame.getCreatedGameBQ().poll();// 尝试从队列中获取（并删除）已创建的game
		if(game != null && !game.getCreaterId().equals(playerId) && (System.currentTimeMillis() + 10000L) < game.getExpire()) {// 获取成功，且距离过期时间超过10秒，接受对战
			game.setup(playerId); // 将game设置好
			super.succeed(json, game).put("side", GamePlayer.OFFENSE); // status //$NON-NLS-1$
		}else {// 获取失败
			if(game != null) {
				AnimalCheckerGame.getPreparedGames().remove(game.getGameId());
			}
			game = AnimalCheckerGame.prepare(playerId);// 创建一个game，发起对战
			if (AnimalCheckerGame.getCreatedGameBQ().offer(game)) {// 将game放到队列，等待回应：是否成功？
				AnimalCheckerGame.getPreparedGames().put(game.getGameId(), game);// 将game放入Map
				game.getLastPlayer().setPlayerId(playerId);
				game.getLastPlayer().setPlayerName(user.getUserName());
				super.succeed(json, game).put("side", GamePlayer.DEFENSE); // status //$NON-NLS-1$
			} else {
				super.fail(json, "当前在线人数已满。"); //$NON-NLS-1$
				game = null;
			}
		}
		return json;
	}

	@SuppressWarnings("boxing")
	public JSONObject listen(String gameId, String status, String playerId) {
		JSONObject json = new JSONObject();
		int previousStatus = Integer.parseInt(status);
		AnimalCheckerGame game = AnimalCheckerGame.getPreparedGames().get(gameId);
		int currentStatus = game.getStatus();
		JSONObject data = new JSONObject();
		data.put("gameId", gameId); //$NON-NLS-1$
		while (currentStatus <= previousStatus) {
			if (currentStatus == Game.CLOSED) {
				if(previousStatus != -1) {
					data.put("winner", game.getWinner().getPlayerId().equals(playerId) ? "你赢了!" : "你输了!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}else{
					data.put("winner", "匹配失败！"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				AnimalCheckerGame.getPreparedGames().remove(gameId);
				AnimalCheckerGame.getCreatedGameBQ().remove(game);
				break;
			}
			currentStatus = game.getStatus();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		data.put("changed", AnimalCheckerGame.getChangedChessmen().get(gameId)); //$NON-NLS-1$
		data.put("status", game.getStatus()); //$NON-NLS-1$
		data.put("myturn", !game.getLastPlayer().getPlayerId().equals(playerId)); //$NON-NLS-1$
		return super.succeed(json, data);
	}

	@SuppressWarnings("boxing")
	public JSONObject act(User user, String gameId, String locationX, String locationY, String locationX0,
			String locationY0) {
		JSONObject json = new JSONObject();
		AnimalCheckerGame game = AnimalCheckerGame.getPreparedGames().get(gameId);
		String playerId = String.valueOf(user.getId());
		if (game.getLastPlayer().getPlayerId().equals(playerId)) {
			return super.fail(json, "还没轮到你，稍等..."); //$NON-NLS-1$
		}
		synchronized (game) {
			AnimalCheckerGame.getChangedChessmen().get(gameId).clear();
			// 执行攻击并将结果储存以待返回
			Map<String, Object> map = doAction(playerId, game, gameId, locationX, locationY, locationX0, locationY0);
			if ((Boolean)map.get("success")) { //$NON-NLS-1$
				game.setExpire(System.currentTimeMillis() + 30000L);
				game.getLastPlayer().setPlayerId(playerId);
				game.getLastPlayer().setPlayerName(user.getUserName());
				
				if(game.winOrNot(playerId)) {
					game.setStatus(Game.CLOSED);
					game.setWinner(game.getLastPlayer());
				}else{
					game.setStatus(game.getStatus() + 1);
				}

				JSONObject data = new JSONObject();
				data.put("changed", AnimalCheckerGame.getChangedChessmen().get(gameId)); //$NON-NLS-1$
				data.put("status", game.getStatus()); //$NON-NLS-1$
				data.put("gameId", gameId); //$NON-NLS-1$
				super.succeed(json, data);
			} else {
				super.fail(json, map.get("cause").toString()); //$NON-NLS-1$
			}
			game.notify();
		}
		logger.info(json);
		return json;
	}

	@SuppressWarnings("boxing")
	private static Map<String, Object> doAction(String playerId, AnimalCheckerGame game, String gameId, String locationX,
			String locationY, String locationX0, String locationY0) {
		Map<String, Object> map = new HashMap<>();
		try {
			int locX = Integer.parseInt(locationX);
			int locY = Integer.parseInt(locationY);
			if (game.getChessboard()[locX][locY] != null && !game.isChessmanOpenLocated(locX, locY)) {
				// 当前选中内容不为空且为未知项：翻转chessman，返回显示
				game.openChessmanLocated(locX, locY);
				AnimalCheckerGame.getChangedChessmen().get(gameId).add(game.getChessboard()[locX][locY]);
				map.put("success", true); //$NON-NLS-1$
				return map;
			}
			int locX0 = Integer.parseInt(locationX0);
			int locY0 = Integer.parseInt(locationY0);
			int square = (locX - locX0) * (locX - locX0) + (locY - locY0) * (locY - locY0);
			if (square == 1) {
				// 相邻
				if (game.getChessboard()[locX0][locY0] != null
						&& game.getChessmanPlayerLocated(locX0, locY0).getPlayerId().equals(playerId)
						&& game.isChessmanOpenLocated(locX0, locY0)) {
					if (game.getChessboard()[locX][locY] == null) {
						// move
						game.getChessboard()[locX][locY] = game.move(locX, locY, locX0, locY0);
						AnimalCheckerGame.getChangedChessmen().get(gameId).add(game.getChessboard()[locX][locY]);
						game.getChessboard()[locX0][locY0] = null;
						AnimalCheckerGame.getChangedChessmen().get(gameId)
								.add(ChessGame.generateAsDestroyed(locX0, locY0));
						map.put("success", true); //$NON-NLS-1$
						return map;
					}
					if (game.getChessboard()[locX][locY] != null
							&& !game.getChessmanPlayerLocated(locX, locY).getPlayerId().equals(playerId)
							&& game.isChessmanOpenLocated(locX, locY)) {
						// try to beat
						if (game.getChessmanNameLocated(locX0, locY0) == AnimalCheckerGame.MOUSE
								&& game.getChessmanNameLocated(locX, locY) == AnimalCheckerGame.ELEPHANT) {
							// my mouse beats enemy's elephant
							game.move(locX0, locY0, locX, locY);
							AnimalCheckerGame.getChangedChessmen().get(gameId)
									.add(game.destroyChessmanLocated(locX, locY));
							game.getChessboard()[locX][locY] = game.move(locX, locY, locX0, locY0);
							AnimalCheckerGame.getChangedChessmen().get(gameId).add(game.getChessboard()[locX][locY]);
							game.getChessboard()[locX0][locY0] = null;
							map.put("success", true); //$NON-NLS-1$
							return map;
						}
						if (AnimalCheckerGame.ALL
								.indexOf(game.getChessmanNameLocated(locX0, locY0)) <= AnimalCheckerGame.ALL
										.indexOf(game.getChessmanNameLocated(locX, locY))
								&& !(game.getChessmanNameLocated(locX0, locY0) == AnimalCheckerGame.ELEPHANT
										&& game.getChessmanNameLocated(locX, locY) == AnimalCheckerGame.MOUSE)
								&& !(game.getChessmanNameLocated(locX0, locY0) == AnimalCheckerGame.FOOTBALL
										&& game.getChessmanNameLocated(locX, locY) == AnimalCheckerGame.FOOTBALL)
								&& !(game.getChessmanNameLocated(locX0, locY0) == AnimalCheckerGame.MOUSE
										&& game.getChessmanNameLocated(locX, locY) == AnimalCheckerGame.FOOTBALL)) {
							/*
							 * 以强击弱，或者先发制人 except： 1.a football can not beat
							 * another one 2.a mouse can not beat a football
							 * 3.an elephant can not beat a mouse
							 * 
							 */
							game.move(locX0, locY0, locX, locY);
							AnimalCheckerGame.getChangedChessmen().get(gameId).add(game.destroyChessmanLocated(locX, locY));
							game.getChessboard()[locX][locY] = game.move(locX, locY, locX0, locY0);
							AnimalCheckerGame.getChangedChessmen().get(gameId).add(game.getChessboard()[locX][locY]);
							game.getChessboard()[locX0][locY0] = null;
							map.put("success", true); //$NON-NLS-1$
							return map;
						}
					}
				}
				map.put("success", false); //$NON-NLS-1$
				map.put("cause", "你走错了。"); //$NON-NLS-1$ //$NON-NLS-2$
				return map;
			}
			int product = (locX - locX0) * (locY - locY0);
			if (product == 0 && square >= 4) {
				// 同一行或同一列，且间隔一个或多个chessman
				int obstacle = 0;
				if (locX == locX0) {
					int lesser = locY > locY0 ? locY0 : locY;
					int greater = locY < locY0 ? locY0 : locY;
					while (++lesser < greater) {
						if (game.getChessboard()[locX][lesser] != null/* && game.getChessmanNameLocated(locX, lesser) != null*/) {
							obstacle++;
						}
						if (obstacle > 1) {
							map.put("success", false); //$NON-NLS-1$
							map.put("cause", "障碍太多了。"); //$NON-NLS-1$ //$NON-NLS-2$
							return map;
						}
					}
				} else if (locY == locY0) {
					int lesser = locX > locX0 ? locX0 : locX;
					int greater = locX < locX0 ? locX0 : locX;
					while (++lesser < greater) {
						if (game.getChessboard()[lesser][locY] != null/* && game.getChessmanNameLocated(lesser, locY) != null*/) {
							obstacle++;
						}
						if (obstacle > 1) {
							map.put("success", false); //$NON-NLS-1$
							map.put("cause", "障碍太多了。"); //$NON-NLS-1$ //$NON-NLS-2$
							return map;
						}
					}
				}
				if (obstacle == 1 && game.getChessmanNameLocated(locX0, locY0) != null
						&& game.isChessmanOpenLocated(locX0, locY0)
						&& game.getChessmanNameLocated(locX0, locY0) == AnimalCheckerGame.FOOTBALL
						&& game.getChessmanPlayerLocated(locX0, locY0).getPlayerId().equals(playerId)
						&& game.getChessboard()[locX][locY] != null && game.isChessmanOpenLocated(locX, locY)
						&& !game.getChessmanPlayerLocated(locX, locY).getPlayerId().equals(playerId)) {
					// beat
					game.move(locX0, locY0, locX, locY);
					AnimalCheckerGame.getChangedChessmen().get(gameId).add(game.destroyChessmanLocated(locX, locY));
					game.getChessboard()[locX][locY] = game.move(locX, locY, locX0, locY0);
					AnimalCheckerGame.getChangedChessmen().get(gameId).add(game.getChessboard()[locX][locY]);
					game.getChessboard()[locX0][locY0] = null;
					map.put("success", true); //$NON-NLS-1$
					return map;
				}
			}
			map.put("success", false); //$NON-NLS-1$
			map.put("cause", "你走错了。"); //$NON-NLS-1$ //$NON-NLS-2$
			return map;
		} catch (NumberFormatException e) {
			logger.info(e);
			map.put("success", false); //$NON-NLS-1$
			map.put("cause", "系统异常。"); //$NON-NLS-1$ //$NON-NLS-2$
			return map;
		}
	}

	public JSONObject runTimeout(String gameId) {
		JSONObject json = new JSONObject();
		AnimalCheckerGame game = AnimalCheckerGame.getPreparedGames().get(gameId);
		if(game != null) {
			game.setStatus(Game.CLOSED);
			game.setWinner(game.getLastPlayer());
		}
		return super.succeed(json);
	}
}
