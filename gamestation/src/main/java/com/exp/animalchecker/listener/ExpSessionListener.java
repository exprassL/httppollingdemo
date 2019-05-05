package com.exp.animalchecker.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.exp.system.pojo.User;

public class ExpSessionListener implements HttpSessionListener, HttpSessionAttributeListener {

	private static Log logger = LogFactory.getLog(ExpSessionListener.class);

	@Override
	public void attributeAdded(HttpSessionBindingEvent se) {
		if (se.getSession().getAttribute("forcedlogout") != null) { //$NON-NLS-1$
			logger.info("SESSION[" + se.getSession().getId() + "]被标记，" + "用户[" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ ((User) se.getSession().getAttribute("user")).getUserName() + "]被迫下线于" //$NON-NLS-1$ //$NON-NLS-2$
					+ new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date())); //$NON-NLS-1$
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent se) {
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent se) {
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.info("SESSION[" + se.getSession().getId() + "]创建于" //$NON-NLS-1$ //$NON-NLS-2$
				+ new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date())); //$NON-NLS-1$
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		logger.info("SESSION[" + se.getSession().getId() + "]销毁于" //$NON-NLS-1$ //$NON-NLS-2$
				+ new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date())); //$NON-NLS-1$
	}

}
