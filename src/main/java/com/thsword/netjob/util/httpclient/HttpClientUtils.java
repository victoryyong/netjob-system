package com.thsword.netjob.util.httpclient;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.security.cert.X509Certificate;

import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.thsword.utils.file.FileUtil;
import com.thsword.utils.object.UUIDUtil;

/**
 * 对HTTPClient的封装
 */
public class HttpClientUtils {

    private static final String ENCODING = "UTF-8";

    private static HttpClient client = null;
    private static SchemeRegistry schemeRegistry;        //协议控制
    private static PoolingClientConnectionManager ccm;  //HttpClient连接池(多连接的线程安全的管理器)

    static {
        try {
            /*
             * 与https请求相关的操作
             */
            SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
            sslContext.init(null, new TrustManager[]{HttpClientUtilWithMyX509TrustMananer.getMyX509TrustManager()}, new SecureRandom());
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
            /*
             * 定义访问协议
             */
            schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));//http
            schemeRegistry.register(new Scheme("https", 443, socketFactory));//https
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        
        // 连接池管理
        ccm = new PoolingClientConnectionManager(schemeRegistry);
        ccm.setDefaultMaxPerRoute(20);//每个路由的最大连接数
        ccm.setMaxTotal(400);//最大总连接数

        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);//连接超时时间（ms）
        httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT,  2000);//操作超时时间（ms）
        httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,HttpVersion.HTTP_1_1);//设置http1.1或http1.0

        client = new DefaultHttpClient(ccm, httpParams);//一个客户端就有一个连接池
    }

    /**
     * get请求
     * @param url       请求URL
     * @param paramMap    请求参数
     * @param headerMap    请求头信息
     */
    public static String get(String url,
                             Map<String, String> paramMap, 
                             Map<String, String> headerMap) throws ClientProtocolException, 
                                                                    IOException {
        /*
         * 拼接URL与参数
         */
        if (MapUtils.isNotEmpty(paramMap)) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (String key : paramMap.keySet()) {
                params.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
            String queryString = URLEncodedUtils.format(params,ENCODING);
            if (url.indexOf("?") > -1) {//存在?，表示这时的URL已经带参数了
                url += "&" + queryString;
            } else {
                url += "?" + queryString;
            }
        }

        HttpGet httpGet = new HttpGet(url);

        /*
         * 设置头信息
         */
        if (MapUtils.isNotEmpty(headerMap)) {
            Set<String> keySet = headerMap.keySet();
            for (String key : keySet) {
                httpGet.addHeader(key, headerMap.get(key));
            }
        }

        String result = "";
        
        HttpResponse response = client.execute(httpGet);    //发出get请求
        StatusLine status = response.getStatusLine();        //获取返回的状态码
        HttpEntity entity = response.getEntity();            //获取返回的响应内容
        if (status.getStatusCode() == HttpStatus.SC_OK) {    //200
            result = EntityUtils.toString(entity, ENCODING);
        } 
        
        httpGet.abort();//中止请求，连接被释放回连接池
        return result;
    }
    
    /**
     * get请求文件
     * @param url       请求URL
     * @param paramMap    请求参数
     * @param headerMap    请求头信息
     */
    public static InputStream getInputStream(String url) {
    	HttpGet httpGet=new HttpGet(url); 
    	CloseableHttpClient closeableHttpClient=null; //1、创建实例
    	CloseableHttpResponse closeableHttpResponse=null; //3、执行
    	try {
    		closeableHttpClient=HttpClients.createDefault();
    		closeableHttpResponse=closeableHttpClient.execute(httpGet);
    		HttpEntity httpEntity=closeableHttpResponse.getEntity(); //4、获取实体
    		if(httpEntity!=null){
    			//InputStream inputStream=httpEntity.getContent();
    			byte[] bytes = EntityUtils.toByteArray(httpEntity);
    			InputStream sbs = new ByteArrayInputStream(bytes); 
    			return sbs;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				closeableHttpResponse.close();
				closeableHttpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
    }

    /**
     * post请求
     * @param url        //请求URL
     * @param paramMap    //请求参数
     * @param headerMap    //请求头信息
     */
    public static String post(String url,
                              Map<String, String> paramMap, 
                              Map<String, String> headerMap) throws ClientProtocolException, 
                                                                      IOException {
        HttpPost httpPost = new HttpPost(url);
        /*
         * 处理参数
         */
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (MapUtils.isNotEmpty(paramMap)) {
            Set<String> keySet = paramMap.keySet();
            for (String key : keySet) {
                params.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
        }

        /*
         * 设置头信息
         */
        if (MapUtils.isNotEmpty(headerMap)) {
            Set<String> keySet = headerMap.keySet();
            for (String key : keySet) {
                httpPost.addHeader(key, headerMap.get(key));
            }
        }

        String result = "";
        
        httpPost.setEntity(new UrlEncodedFormEntity(params, ENCODING));//设置参数
        HttpResponse response = client.execute(httpPost);               //发出post请求
        StatusLine status = response.getStatusLine();                   //获取返回的状态码
        HttpEntity entity = response.getEntity();                       //获取响应内容
        if (status.getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(entity, ENCODING);
        }
        
        httpPost.abort();//中止请求，连接被释放回连接池
        return result;
    }
    
    /**
     * post请求
     * @param url        //请求URL
     * @param paramMap    //请求参数
     * @param headerMap    //请求头信息
     * @param fileKey    //文件key
     * @param inputStream    //文件inputstream
     */
    public static String post(String url,
                              Map<String, String> paramMap, 
                              Map<String, String> headerMap,String fileKey,InputStream inputStream) throws ClientProtocolException, 
                                                                      IOException {
        HttpPost httpPost = new HttpPost(url);
        /*
         * 设置头信息
         */
        if (MapUtils.isNotEmpty(headerMap)) {
            Set<String> keySet = headerMap.keySet();
            for (String key : keySet) {
                httpPost.addHeader(key, headerMap.get(key));
            }
        }
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //第一个参数为 相当于 Form表单提交的file框的name值 第二个参数就是我们要发送的InputStream对象了
        //第三个参数是文件名
        //3)
        builder.addBinaryBody(fileKey, inputStream, ContentType.create("multipart/form-data"), UUIDUtil.get32UUID()+".png");
        //4)构建请求参数 普通表单项

        StringBody stringBody = null;
        if (MapUtils.isNotEmpty(paramMap)) {
            Set<String> keySet = paramMap.keySet();
            for (String key : keySet) {
            	stringBody = new StringBody(paramMap.get(key),ContentType.MULTIPART_FORM_DATA);
                builder.addPart(key, stringBody);
            }
        }
        HttpEntity httpEntity = builder.build();
        httpPost.setEntity(httpEntity);
        String result = "";
        HttpResponse response = client.execute(httpPost);               //发出post请求
        StatusLine status = response.getStatusLine();                   //获取返回的状态码
        HttpEntity entity = response.getEntity();                       //获取响应内容
        if (status.getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(entity, ENCODING);
        }
        
        httpPost.abort();//中止请求，连接被释放回连接池
        return result;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        try {
        	//http
        	//System.out.println(HttpClientUtils.post("http://www.cppblog.com/iuranus/archive/2010/07/04/119311.html", null, null));
            
        	//https
        	//System.out.println(HttpClientUtils.get("https://www.baidu.com/", null, null));
            
        	//读文件
        	//InputStream inputStream = HttpClientUtils.getInputStream("http://editerupload.eepw.com.cn/201809/61001537857032.jpg");
            //FileUtil.upload(inputStream, "C:/Users/Lenovo/Desktop/idcard2.jpg");
            
            //写文件
        	//InputStream inputStream = HttpClientUtils.getInputStream("http://wy-168.cn/netjob/file/banner/20181111/REwb1541946825395.jpg");
        	InputStream inputStream = HttpClientUtils.getInputStream("http://image07.71.net/image07/07/49/50/14/10ed48d1-1dac-41aa-8c12-e063daffaf4e.jpg");
        	Map<String, String> obj = new HashMap<String, String>();
        	obj.put("key", "02b1201f0adcab5f811c13eb8a2c4829");
        	obj.put("cardType", "2008");
            System.out.println(HttpClientUtils.post("http://v.juhe.cn/certificates/query.php", obj,null,"pic",inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}