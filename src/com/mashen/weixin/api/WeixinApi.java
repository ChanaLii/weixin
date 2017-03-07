package com.mashen.weixin.api;

import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.mashen.common.util.ConfigUtil;
import com.mashen.common.util.HttpUtil;
import com.mashen.common.util.HttpUtil;

public class WeixinApi {
	private static final Object LOCK=new Object();
	private static final String POST="POST";
	private static final String GET="GET";
	private static final String APPID=ConfigUtil.get("appID");
	private static final String SECRET=ConfigUtil.get("appsecret");
	private static final String CHARSET=ConfigUtil.get("charset");
	private static final String GET_TOKEN="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	private static AccessToken token;
	public static AccessToken getAccessToken() throws IOException{
		if(token!=null&&!token.isExpired()){
			return token;
		}
		synchronized (LOCK) {
			if(token!=null&&!token.isExpired()){
				return token;
			}
			String jsonResult=HttpUtil.sendHttpRequest(String.format(GET_TOKEN,APPID,SECRET), 
					GET,null, CHARSET);
			System.out.println(jsonResult);
			token=JSONObject.parseObject(jsonResult,AccessToken.class);
			System.out.println(token.getAccess_token());
			return token;
			
		}
	}
	
	private static final String GET_USERINFO="https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

	public static UserInfo getUserInfo(String openid)  throws IOException{
		AccessToken token=getAccessToken();
		String jsonResult=HttpUtil.sendHttpRequest
				(String.format(GET_USERINFO,token.getAccess_token(),openid), 
				GET,null, CHARSET);
		System.out.println(jsonResult);
		UserInfo user=JSONObject.parseObject(jsonResult,UserInfo.class);
		System.out.println(user.getHeadimgurl());
		return user;
	}
	private static final String POST_MENU=" https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
	public static Result createMenu(Menu menu)   throws IOException{
		AccessToken token=getAccessToken();
		String jsonRequet=JSONObject.toJSONString(menu);
		System.out.println(jsonRequet);
		String jsonResult=HttpUtil.sendHttpRequest
				(String.format(POST_MENU,token.getAccess_token()), 
				POST,jsonRequet, CHARSET);
		System.out.println(jsonResult);
		return JSONObject.parseObject(jsonResult, Result.class);
	}
	
	private static final String POST_UPLOAD="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";
	
	public static Media upload(String filepath,String type)  throws Exception{
		AccessToken token=getAccessToken();
		long start=System.currentTimeMillis();
		String jsonResult=HttpUtil.upload(String.format(POST_UPLOAD,
				token.getAccess_token(),type), 
				filepath, CHARSET);
		System.out.println(jsonResult);
		System.out.println("上传耗时："+(System.currentTimeMillis()-start)/1000+"秒");
		return JSONObject.parseObject(jsonResult, Media.class);
	}
	
	private static final String POST_QRTICKET="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
	public static QRTicket getQrTicket(QRTicketRequest request)  throws Exception{
		AccessToken token=getAccessToken();
		String jsonRequet=JSONObject.toJSONString(request);
		System.out.println(jsonRequet);
		String jsonResult=HttpUtil.sendHttpRequest
				(String.format(POST_QRTICKET,token.getAccess_token()), 
				POST,jsonRequet, CHARSET);
		System.out.println(jsonResult);
		return JSONObject.parseObject(jsonResult, QRTicket.class);
		
	}
	
	private static final String POST_CUSTOMMSG="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
	
	public static void sendCustomMessage(CustomMessage message) throws Exception{
		AccessToken token=getAccessToken();
		String jsonRequet=JSONObject.toJSONString(message);
		System.out.println(jsonRequet);
		String jsonResult=HttpUtil.sendHttpRequest
				(String.format(POST_CUSTOMMSG,token.getAccess_token()), 
				POST,jsonRequet, CHARSET);
		System.out.println(jsonResult);
	}
	
	
	public static void main(String[] args) {
		try {
			/*TextCustomMessage message=new TextCustomMessage();
			message.setContent("测试客服消息");
			message.setTouser("oQNb8wmOoUNkLBYAhJlFqoPnQFfE");*/
			/*ImageCustomMessage  message=new ImageCustomMessage();
			message.setTouser("oQNb8wmOoUNkLBYAhJlFqoPnQFfE");
			Media md=upload("d:/1.jpg",Media.TYPE_IMAGE);
			message.setMediaId(md.getMedia_id());*/
			
			//sendCustomMessage(message);
			/*QRTicketRequest request=new QRTicketRequest();
			request.addSceneId("1");
			getQrTicket(request);*/
			//upload("d:/1.jpg",Media.TYPE_IMAGE);
			//WeixinApi.getAccessToken();
			//getUserInfo("oQNb8wmOoUNkLBYAhJlFqoPnQFfE");
			createMenu(Menu.getDefalutMenu());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
