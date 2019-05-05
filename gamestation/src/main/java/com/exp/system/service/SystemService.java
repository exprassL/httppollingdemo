package com.exp.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.exp.common.base.service.BaseService;
import com.exp.common.util.AESEncryption;
import com.exp.common.util.MD5Encryption;
import com.exp.common.util.RSAEncryption;
import com.exp.system.dao.SystemDao;
import com.exp.system.pojo.User;

public class SystemService extends BaseService {

	private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9][0-9]{9}$"); //$NON-NLS-1$
	
	private SystemDao systemDao = null;

	public SystemDao getSystemDao() {
		return this.systemDao;
	}

	public void setSystemDao(SystemDao systemDao) {
		this.systemDao = systemDao;
	}

	@Transactional
	public String testConection() {
		return this.systemDao.testConection();
	}

	public JSONObject login(String phone, String password, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		if(StringUtils.isBlank(phone) || StringUtils.isBlank(password)) {
			return super.fail(json, "手机号或密码为必填项"); //$NON-NLS-1$
		}
		User user = this.systemDao.getUserByPhoneNum(phone);
		if(user == null || !password.equals(MD5Encryption.getMD5(AESEncryption.decrypt(user.getUserPwd())))) {
			return super.fail(json, "手机号或密码错误"); //$NON-NLS-1$
		}
		request.getSession().setAttribute("user", user); //$NON-NLS-1$
		return super.succeed(json);
	}
	
	public JSONObject register(String phone, String name, String password, String password2) {
		JSONObject json = new JSONObject();
		if(StringUtils.isBlank(phone) || StringUtils.isBlank(name) || StringUtils.isBlank(password) || StringUtils.isBlank(password2)) {
			return super.fail(json, "手机号、用户名及两次密码为必填项"); //$NON-NLS-1$
		}
		if(!PHONE_PATTERN.matcher(phone).matches()) {
			return super.fail(json, "手机号格式错误"); //$NON-NLS-1$
		}
		try {
			password = RSAEncryption.decrypt(password);
			password2 = RSAEncryption.decrypt(password2);
			if(!password.equals(password2)) {
				return super.fail(json, "两次输入密码必须一致"); //$NON-NLS-1$
			}
			password = AESEncryption.encrypt(password);
			password2 = AESEncryption.encrypt(password2);
		} catch (Exception e) {
			e.printStackTrace();
			return super.fail(json, "系统错误。"); //$NON-NLS-1$
		}
		this.systemDao.registerBySelf(phone, phone, name, password);
		return super.succeed(json);
	}

	public JSONObject logout(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		HttpSession session = request.getSession(false);
		String resultMsg;
		if (session == null) {
			resultMsg = "未登录。"; //$NON-NLS-1$
			return json;
		}
		if (session.getAttribute("user") == null) { //$NON-NLS-1$
			resultMsg = "未登录。"; //$NON-NLS-1$
		} else {
			resultMsg = "已登出。"; //$NON-NLS-1$
		}
		session.invalidate();
		return super.succeed(json, resultMsg);
	}

	public JSONObject listGames() {
		JSONObject json = new JSONObject();
		List<Map<String, String>> gameList = new ArrayList<>();
		Map<String, String> game = new HashMap<>();
		game.put("gameHTML", "<li><a href=\"animalchecker.html?r=" + Math.random() + "\">斗兽棋简易版</a></li>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		gameList.add(game);
		return super.succeed(json, gameList);
	}
}
