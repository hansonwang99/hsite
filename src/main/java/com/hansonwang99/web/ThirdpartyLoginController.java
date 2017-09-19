package com.hansonwang99.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hansonwang99.domain.result.Response;
import com.hansonwang99.domain.result.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/9/19.
 */
@RestController
@RequestMapping("/thirdpartlogin")
public class ThirdpartyLoginController extends BaseController {

    @ApiOperation(value="github登录 RC", notes="github登录")
    @RequestMapping(value="/github", method = RequestMethod.GET)
    public Response githubLogin(@RequestParam(value = "code") String code, HttpServletResponse response ) {

        String client_id = "f15278f5c4d3438a1696";
        String client_secret = "07f6fcfddf1897c18e1ceb819b08751e994ba0ed";
        String redirect_uri = "http://localhost/thirdpartlogin/github";
        String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token?client_id=" +client_id+ "&client_secret=" +client_secret+ "&code=" +code+ "&redirect_uri=" +redirect_uri;

        String res = HttpRequest.sendPost(ACCESS_TOKEN_URL,null);
        String access_token = res.split("&")[0];
        System.out.println(access_token);

        String USERINFO_URL = "https://api.github.com/user?"+access_token;
        String result = HttpRequest.sendGet(USERINFO_URL);
        System.out.println(result);

        JSONObject githubUserInfo = (JSONObject) JSON.parse(result);

        return result();
    }
}


class HttpRequest {

    /**
     * @param req_url
     * @return
     */
    public static String sendGet(String req_url) {

        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(req_url);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(false);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            //res = new String(buffer.toString().getBytes("iso-8859-1"),"utf-8");
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}