package com.itheima.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Properties;

/**
 * 阿里云短信发送工具类
 *
 * @author yunfei
 * @date 2022年09月17日 14:54
 */
public class SMSUtils {

    /* DI的前提是当前的这个Bean要在容器中 */
    /*@Value("aliyun.sms.ak")
    private String ak;*/

    private static String ak;
    private static String sk;
    private static String signName;
    private static String templateCode;


    /* Java中通常都会使用类加载去读取文件 */
    static {
        try {
            Properties properties = new Properties();
            properties.load(SMSUtils.class.getClassLoader().getResourceAsStream("aliyun.properties"));
            ak = properties.getProperty("aliyun.sms.ak");
            sk = properties.getProperty("aliyun.sms.sk");
            signName = properties.getProperty("aliyun.sms.signName");
            templateCode = properties.getProperty("aliyun.sms.templateCode");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static SendSmsResponse sendSM(String phoneNum, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ak, sk);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNum);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse response = null;
        try {
            response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
        return response;
    }
}
