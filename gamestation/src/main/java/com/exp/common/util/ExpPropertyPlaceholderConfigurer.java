package com.exp.common.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ExpPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {

		try {
			String encryptedKeys = props.getProperty("sys.conf.encryptedkey"); //$NON-NLS-1$
			for (String encryptedKey : encryptedKeys.split(",")) { //$NON-NLS-1$
				String value = props.getProperty(encryptedKey);
				if (value != null) {
					props.setProperty(encryptedKey, AESEncryption.decrypt(value));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("初始化配置失败，服务不能正常启动。", e); //$NON-NLS-1$
		}

		super.processProperties(beanFactoryToProcess, props);
	}
}
