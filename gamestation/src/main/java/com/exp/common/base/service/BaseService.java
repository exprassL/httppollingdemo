package com.exp.common.base.service;

import com.alibaba.fastjson.JSONObject;

public abstract class BaseService {

	@SuppressWarnings({ "static-method", "boxing" })
	protected JSONObject succeed(JSONObject json, Object data) {
		json.put("success", true); //$NON-NLS-1$
		json.put("code", 200); //$NON-NLS-1$
		json.put("msg", "操作成功"); //$NON-NLS-1$ //$NON-NLS-2$
		json.put("data", data); //$NON-NLS-1$
		return json;
	}
	
	@SuppressWarnings({ "static-method", "boxing" })
	protected JSONObject succeed(JSONObject json) {
		json.put("success", true); //$NON-NLS-1$
		json.put("code", 200); //$NON-NLS-1$
		json.put("msg", "操作成功"); //$NON-NLS-1$ //$NON-NLS-2$
		return json;
	}
	
	@SuppressWarnings({ "boxing", "static-method" })
	protected JSONObject fail(JSONObject json, String cause) {
		json.put("success", false); //$NON-NLS-1$
		json.put("code", 400); //$NON-NLS-1$
		json.put("msg", "操作失败"); //$NON-NLS-1$ //$NON-NLS-2$
		json.put("cause", cause); //$NON-NLS-1$
		return json;
	}
	
	@SuppressWarnings({ "static-method", "boxing" })
	protected JSONObject error(JSONObject json, Exception e) {
		json.put("success", false); //$NON-NLS-1$
		json.put("code", 401); //$NON-NLS-1$
		json.put("msg", "操作失败"); //$NON-NLS-1$ //$NON-NLS-2$
		json.put("error", e.getCause()); //$NON-NLS-1$
		json.put("cause", e.getCause()); //$NON-NLS-1$
		return json;
	}
}
