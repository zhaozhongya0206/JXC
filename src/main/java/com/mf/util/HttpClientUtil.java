package com.mf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;

public abstract class HttpClientUtil {
	
	private static Log log = LogFactory.getLog(HttpClientUtil.class);
    
	static{
    	//注册 HTTPS 协议
    	ProtocolSocketFactory factory = new Protocol4Https();
        Protocol.registerProtocol("https", new Protocol("https", factory, 443));
    }
    
	/**
     * 转换成queryString
     * @param params
     *            存档参数名与参数值的集合
     * @return queryString
     */
    public static String getQueryString(Map<String, String> params) {
        String queryString = null;
        NameValuePair[] nvp = generateQueryParams(params);
        if(nvp != null){
        	queryString = EncodingUtil.formUrlEncode(nvp, "UTF-8");
        }
        return queryString == null ? "" : queryString;
    }
    
    /**
     * 生成请求键值对
     * @param params
     * @return
     */
    public static NameValuePair[] generateQueryParams(Map<String,String> params){
    	NameValuePair[] nvp = null;
    	if (params != null) {
            nvp = new NameValuePair[params.size()];
            int index = 0;
            for (String key : params.keySet()) {
                nvp[index++] = new NameValuePair(key, params.get(key));
            }
    	}
    	return nvp;
    }

    /**
     * 向目标发出一个GET请求并得到响应数据
     * @param url
     *            目标
     * @param params
     *            参数集合
     * @param timeout
     *            超时时间
     * @return 响应数据
     * @throws IOException 
     */
    public static String sendGet(String url, Map<String, String> params, int timeout) throws Exception  {    	
        GetMethod method = new GetMethod(url);
        if (params != null) {
            method.setQueryString(getQueryString(params));
        }

        String content = executeMethod(method, timeout);
        return content;
    }
    
    /**
     * 向目标发出一个Post请求并得到响应数据
     * @param url
     *            目标
     * @param params
     *            参数集合
     * @param timeout
     *            超时时间
     * @return 响应数据
     * @throws IOException
     */
    public static String sendPost(String url, NameValuePair[] data, int timeout) throws Exception {
    	log.info("url:"+url);
        PostMethod method = new PostMethod(url);
        method.setRequestBody(data);
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");  
        String content = "";
        try {
            content = executeMethod(method, timeout);
        }
        catch(Exception e){
        	throw e;
        }
        
        return content;
    }

    /**
     * 向目标发出一个Post请求并得到响应数据
     * @param url
     *            目标
     * @param params
     *            参数集合
     * @param timeout
     *            超时时间
     * @return 响应数据
     * @throws IOException
     */
    public static String sendPost(String url, Map<String,String> params, int timeout) throws Exception {
    	NameValuePair[] nvp = generateQueryParams(params);
    	return sendPost(url, nvp, timeout);
    }
    
    
    
    /**
  	 * 执行JSON post请求
  	 * @param url
  	 * @param jsonObj json字符串
  	 * @param headers 请求头
  	 * @param exceptedStatus 期望返回码,如果返回码不匹配,则抛异常
  	 * @return
  	 * @throws HttpRequestException
  	 */
  	public static String doStrJSONPost(String url, String jsonStr, int exceptedStatus) throws Exception{
  		HttpPost httpPost = new HttpPost(url);
  		//post请求参数
  		StringEntity stringEntity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
  		httpPost.setEntity(stringEntity);
  		return execute(httpPost, exceptedStatus);
  	}
  	
  	
  	
  	public static String doPost(String url, String body, String appClient, String appSecert) throws Exception {
        HttpURLConnection conn = null;
        String[][] headers = {{"Content-Type", "application/json;charset=utf-8"}, {"client_appkey", appClient}, {"client_secret", appSecert}};
        try {
            conn = getConnect(url, "POST", headers, 3000, 10000);
            return readResponse(conn, body, "utf-8");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static HttpURLConnection getConnect(String url, String method, String[][] headers, int connectTimeout, int readTimeout) throws Exception {
        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(connectTimeout * 1000);
        conn.setReadTimeout(readTimeout * 1000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);

        for (String[] header : headers) {
            conn.setRequestProperty(header[0], header[1]);
        }

        return conn;
    }

    private static String readResponse(HttpURLConnection conn, String body, String charset) throws Exception {
        OutputStream os = null;
        OutputStreamWriter osw = null;
        PrintWriter pw = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            if (body != null) {
                os = conn.getOutputStream();
                osw = new OutputStreamWriter(os, charset);
                pw = new PrintWriter(osw);
                pw.print(body);
                pw.flush();
            }

            conn.connect();
            if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
                System.out.println("HttpRequest fail! ResponseCode: " + conn.getResponseCode() + ", Url: {}" + conn.getURL());
                return null;
            }

            is = conn.getInputStream();
            isr = new InputStreamReader(is, charset);
            br = new BufferedReader(isr);
            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (osw != null) {
                osw.close();
            }
            if (os != null) {
                os.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

  	
  	
  	/**
	 * 执行请求
	 * @param request
	 * @param exceptedStatus
	 * @return
	 * @throws HttpRequestException
	 */
	private static String execute(HttpUriRequest request, int exceptedStatus) throws Exception{
		String content = null;
		HttpResponse response = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			response = httpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity httpEntity = response.getEntity();
			if (status == exceptedStatus) {
				content = EntityUtils.toString(httpEntity, "UTF-8");
			} else {
				throw new Exception(String.format("illegal status:%d,url:%s",status,request.getURI().toString()));
			}
		} catch (IOException e) {
			throw new Exception(String.format("request[%s] error",request.getURI().toString()),e);
		}finally {
			if(response != null	){
				// 释放连接,使得连接得以复用
				EntityUtils.consumeQuietly(response.getEntity());
			}
		}
		return content;
	}
    
    /**
     * 执行请求参数,在请求结束时释放资源
     * @param method
     * @param timeout
     * @return	当响应结果为200时返回响应内容字节数组,否则返回空字节数组
     * @throws Exception
     */
    private static String executeMethod(HttpMethodBase method, int timeout) throws Exception {
    	BufferedReader br = null;
    	StringBuffer respBody = new StringBuffer();
    	HttpClient client = new HttpClient();
    	try {
	    	method.addRequestHeader("Connection", "close");
	        HttpConnectionManagerParams params = client.getHttpConnectionManager().getParams();
	        params.setConnectionTimeout(timeout);
	        params.setSoTimeout(timeout);
	        params.setStaleCheckingEnabled(false);
	        int status = client.executeMethod(method);         
	        if(HttpStatus.SC_OK == status){
	        	Long startTime = System.currentTimeMillis();
	        	br = new BufferedReader(new InputStreamReader(
						method.getResponseBodyAsStream(), method.getRequestCharSet()));
				String line = br.readLine();
				while (line != null) {
					respBody.append(line);
					line = br.readLine();
				}
	        	log.info("genenrate response content time: " + (System.currentTimeMillis() - startTime) + "ms");
	        }
            return respBody.toString();
        } 
    	finally {
            if (br != null) {
                br.close();
            }
            client.getHttpConnectionManager().closeIdleConnections(0);
			method.releaseConnection();
        }
    }
    
    public static String sendByPostForApplicationJson(String httpUrl,Object requestBody)throws Exception {
		 try{
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setUseCaches(false);
	        connection.setInstanceFollowRedirects(true);
	        
	        connection.setRequestProperty("Content-Type","application/json");
	        connection.connect();
	
	        OutputStream os = connection.getOutputStream();
	        os.write(JSON.toJSONString(requestBody).getBytes());
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String lines;
	        StringBuffer sbf = new StringBuffer();
	        while ((lines = reader.readLine()) != null) {
	            lines = new String(lines.getBytes(), "utf-8");
	            sbf.append(lines);
	        }
	         String result = sbf.toString();    
	         connection.disconnect();
	         return result;
	    }catch(Exception e){
	    	log.error("发送POST请求出错", e);
			throw new Exception("发送POST请求出错");
	    }finally{
	        
	    }
	}
}


/**
 * 内部类,支持 Https 请求发送格式
 * @author xing_zhao
 *
 */
class Protocol4Https implements SecureProtocolSocketFactory {
    
    private SSLContext sslcontext = null;
    
    private SSLContext createSSLContext() {
        SSLContext sslcontext=null;
        try {
            sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslcontext;
    }
    
    private SSLContext getSSLContext() {
        if (this.sslcontext == null) {
            this.sslcontext = createSSLContext();
        }
        return this.sslcontext;
    }
    
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
            throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(
                socket,
                host,
                port,
                autoClose
            );
    }

    public Socket createSocket(String host, int port) throws IOException,
            UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(
                host,
                port
            );
    }
    
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
            throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    public Socket createSocket(String host, int port, InetAddress localAddress,
            int localPort, HttpConnectionParams params) throws IOException,
            UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        } else {
            Socket socket = socketfactory.createSocket();
            SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
            SocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
            return socket;
        }
    }
    
    private static class TrustAnyTrustManager implements X509TrustManager {       
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
   
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
   
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public boolean isClientTrusted(X509Certificate[] arg0) {
            return false;
        }

        public boolean isServerTrusted(X509Certificate[] arg0) {
            return false;
        }
    }
    
}
