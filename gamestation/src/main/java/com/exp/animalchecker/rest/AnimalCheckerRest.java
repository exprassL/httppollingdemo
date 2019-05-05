package com.exp.animalchecker.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jboss.resteasy.annotations.providers.jaxb.json.BadgerFish;

import com.alibaba.fastjson.JSONObject;
import com.exp.animalchecker.pojo.AnimalCheckerGame;
import com.exp.animalchecker.service.AnimalCheckerService;
import com.exp.system.pojo.User;

@Path("/game/animalchecker")
public class AnimalCheckerRest {

	private AnimalCheckerService animalCheckerService = null;

	public AnimalCheckerService getAnimalCheckerService() {
		return this.animalCheckerService;
	}

	public void setAnimalCheckerService(AnimalCheckerService animalCheckerService) {
		this.animalCheckerService = animalCheckerService;
	}
	
	@GET
	@Path("/prepare")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public JSONObject prepare(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user"); //$NON-NLS-1$
		JSONObject json = this.animalCheckerService.prepare(user);
		return json;
	}

	@GET
	@Path("/listen")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public JSONObject listen(@Context HttpServletRequest request) {
		String gameId = request.getParameter("gameId"); //$NON-NLS-1$
		String status = request.getParameter("status"); //$NON-NLS-1$
		JSONObject json = this.animalCheckerService.listen(gameId, status, String.valueOf(((User)request.getSession().getAttribute("user")).getId())); //$NON-NLS-1$
		return json;
	}

	@POST
	@Path("/action")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public JSONObject act(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user"); //$NON-NLS-1$
		String gameId = request.getParameter("gameId"); //$NON-NLS-1$
		String locationX = request.getParameter("locationX"); //$NON-NLS-1$
		String locationY = request.getParameter("locationY"); //$NON-NLS-1$
		String locationX0 = request.getParameter("locationX0"); //$NON-NLS-1$
		String locationY0 = request.getParameter("locationY0"); //$NON-NLS-1$
		JSONObject json = this.animalCheckerService.act(user, gameId, locationX, locationY, locationX0, locationY0);
		return json;
	}

	@POST
	@Path("/runtimeout")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public JSONObject runTimeout(@Context HttpServletRequest request) {
		String gameId = request.getParameter("gameId"); //$NON-NLS-1$
		return this.animalCheckerService.runTimeout(gameId);
	}

	@SuppressWarnings("static-method")
	@GET
	@Path("/reset")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public String reset(@Context HttpServletRequest request) {
		String flag = request.getParameter("flag"); //$NON-NLS-1$
		JSONObject json = new JSONObject();
		json.put("BQ", AnimalCheckerGame.getCreatedGameBQ()); //$NON-NLS-1$
		json.put("prepared", AnimalCheckerGame.getPreparedGames()); //$NON-NLS-1$
		String retStr = json.toJSONString();
		if(flag != null) {
			AnimalCheckerGame.getCreatedGameBQ().clear();
			AnimalCheckerGame.getPreparedGames().clear();
		}
		return retStr;
	}

	
}
