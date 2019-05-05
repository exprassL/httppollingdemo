package com.exp.system.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.providers.jaxb.json.BadgerFish;

import com.alibaba.fastjson.JSONObject;
import com.exp.system.service.SystemService;

@Path("/game/system")
public class SystemRest {

	private static Log logger = LogFactory.getLog(SystemRest.class);

	private SystemService systemService = null;

	public SystemService getSystemService() {
		return this.systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	@GET
	@Path("/testdbc")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public JSONObject test(@Context HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("msg", "success"); //$NON-NLS-1$ //$NON-NLS-2$
		json.put("result", this.systemService.testConection()); //$NON-NLS-1$
		json.put("key1", request.getParameter("key1")); //$NON-NLS-1$ //$NON-NLS-2$
		json.put("key2", request.getParameter("key2")); //$NON-NLS-1$ //$NON-NLS-2$
		logger.info(json.toJSONString());
		return json;
	}

	@POST
	@Path("/login")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public JSONObject login(@Context HttpServletRequest request) {
		String phone = request.getParameter("phone"); //$NON-NLS-1$
		String password = request.getParameter("password"); //$NON-NLS-1$
		return this.systemService.login(phone, password, request);
	}
	
	@POST
	@Path("/register")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public JSONObject register(@Context HttpServletRequest request) {
		String phone = request.getParameter("phone"); //$NON-NLS-1$
		String name = request.getParameter("name"); //$NON-NLS-1$
		String password = request.getParameter("password"); //$NON-NLS-1$
		String password2 = request.getParameter("password2"); //$NON-NLS-1$
		return this.systemService.register(phone, name, password, password2);
	}

	@GET
	@Path("/listall")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public JSONObject listGames(@SuppressWarnings("unused") @Context HttpServletRequest request) {
		return this.systemService.listGames();
	}

	@GET
	@Path("/logout")
	@Produces("application/json; charset=utf-8")
	@BadgerFish
	public JSONObject logout(@Context HttpServletRequest request) {
		return this.systemService.logout(request);
	}
}