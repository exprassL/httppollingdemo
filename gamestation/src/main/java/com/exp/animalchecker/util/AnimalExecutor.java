package com.exp.animalchecker.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Exprass
 *
 */
public class AnimalExecutor {

	private static Log logger = LogFactory.getLog(AnimalExecutor.class);

	/**
	 * 开启一个CloseableHttpClient实例。
	 * 
	 * @return 一个CloseableHttpClient实例
	 */
	public static CloseableHttpClient conncet() {
		return HttpClients.createDefault();
	}

	/**
	 * 关闭CloseableHttpClien实例。
	 * 
	 * @param client
	 *            指定要关闭的CloseableHttpClient实例
	 */
	public static void disconnect(CloseableHttpClient client) {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭响应CloseableHttpResponse。
	 * 
	 * @param res
	 *            指定要关闭的响应
	 */
	public static void closeResponse(CloseableHttpResponse res) {
		try {
			res.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * post请求。
	 * 
	 * @param client
	 *            http客户端实例
	 * @param url
	 *            带参数（如有）的地址：url?k1=v1&k2=v2...
	 * @param paramJson
	 *            请求参数，json格式
	 * @param connectionRequestTimeout
	 *            从连接池中获取可用连接的超时设置
	 * @param connectionTimeout
	 *            连接目标Url的超时设置
	 * @param socketTimeout
	 *            Response响应的超时设置（读取数据超时）
	 * @return http响应状态码
	 * @throws ConnectTimeoutException
	 *             连接超时
	 * @throws SocketTimeoutException
	 *             响应超时
	 * @throws Exception
	 */
	@SuppressWarnings({ "boxing", "resource" })
	public static int postUrl(CloseableHttpClient client, String url, String paramJson, int connectionRequestTimeout,
			int connectionTimeout, int socketTimeout)
					throws ConnectTimeoutException, SocketTimeoutException, Exception {
		Integer status = null;
		HttpPost post = new HttpPost(url);
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
				.setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build();
		post.setConfig(config);
		
		JSONObject json = JSON.parseObject(paramJson);
		List <NameValuePair> nvps = new ArrayList <>();
		Iterator<String> it = json.keySet().iterator();
		String tempKey;
		while(it.hasNext()) {
			tempKey = it.next();
			nvps.add(new BasicNameValuePair(tempKey, json.getString(tempKey)));
		}
		post.setEntity(new UrlEncodedFormEntity(nvps));
		
		CloseableHttpResponse res = client.execute(post);
		logger.info(status = res.getStatusLine().getStatusCode());
		closeResponse(res);
		return status;
	}

	/**
	 * Get请求。
	 * 
	 * @param client
	 *            http客户端实例
	 * @param url
	 *            带参数（如有）的地址：url?k1=v1&k2=v2...
	 * @param connectionRequestTimeout
	 *            从连接池中获取可用连接的超时设置
	 * @param connectionTimeout
	 *            连接目标Url的超时设置
	 * @param socketTimeout
	 *            Response响应的超时设置（读取数据超时）
	 * @return http响应状态码
	 * @throws ConnectTimeoutException
	 *             连接超时
	 * @throws SocketTimeoutException
	 *             响应超时
	 * @throws Exception
	 */
	@SuppressWarnings({ "boxing", "resource" })
	public static int getUrl(CloseableHttpClient client, String url, int connectionRequestTimeout,
			int connectionTimeout, int socketTimeout)
					throws ConnectTimeoutException, SocketTimeoutException, Exception {
		Integer status = null;
		HttpGet get = new HttpGet(url);
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
				.setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build();
		get.setConfig(config);
		CloseableHttpResponse res = null;
		res = client.execute(get);
		logger.info(status = res.getStatusLine().getStatusCode());
		closeResponse(res);
		return status;
	}

}
