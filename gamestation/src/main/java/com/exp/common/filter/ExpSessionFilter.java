package com.exp.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class ExpSessionFilter implements Filter {

	private static Log logger = LogFactory.getLog(ExpSessionFilter.class);
	
	private static PathMatcher pathMatcher = new AntPathMatcher();
	private static String[] excludedPatterns = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		excludedPatterns = filterConfig.getInitParameter("excluded").split(","); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		String contextPath = req.getContextPath();
		String requestURI = req.getRequestURI();
		String uri = StringUtils.substringAfter(requestURI, contextPath);

		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("user") == null) { //$NON-NLS-1$
			if (isExcluded(uri)) {
				chain.doFilter(req, response);
			} else {
				logger.info("未登录，去往登录页"); //$NON-NLS-1$
				request.getRequestDispatcher("/html/main.html").forward(request, response); //$NON-NLS-1$
			}
		} else {
			chain.doFilter(req, response);
		}

	}

	@Override
	public void destroy() {
	}

	/**
	 * 判断当前资源是否是无需Session验证的。
	 * @param uri
	 * @return
	 */
	private static boolean isExcluded(String uri) {
		for (String pattern : excludedPatterns) {
			if (pathMatcher.match(pattern, uri)) {
				return true;
			}
		}
		return false;
	}
}
