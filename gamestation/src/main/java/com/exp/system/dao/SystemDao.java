package com.exp.system.dao;

import com.exp.common.base.dao.BaseDao;
import com.exp.system.pojo.User;

public class SystemDao extends BaseDao {
	public String testConection() {
		return getSqlSession().selectOne("system.testConnection"); //$NON-NLS-1$
	}

	public User getUserByPhoneNum(String phone) {
		return getSqlSession().selectOne("system.getUserByPhoneNum", phone); //$NON-NLS-1$
	}

	public void registerBySelf(String phone, String code, String name, String password) {
		User user = new User();
		user.setPhoneNum(phone);
		user.setUserCode(code);
		user.setUserName(name);
		user.setUserPwd(password);
		getSqlSession().insert("system.registerBySelf", user); //$NON-NLS-1$
	}
}
