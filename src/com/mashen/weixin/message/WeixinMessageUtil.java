package com.mashen.weixin.message;

import java.io.InputStream;
import java.io.InputStreamReader;
import com.mashen.common.util.ConfigUtil;
import com.thoughtworks.xstream.XStream;

public class WeixinMessageUtil {
	public static WeixinRequest  toWeixinRequest(InputStream input) throws Throwable{
		XStream xs=new XStream();
		xs.alias("xml", WeixinRequest.class);
		WeixinRequest request=(WeixinRequest)xs.fromXML(new InputStreamReader(
				input,ConfigUtil.get("charset")));
		return request;
	}
	
	public static String toWeixinResponseXml(WeixinTextResponse response){
		XStream xs=new XStream();
		xs.alias("xml", WeixinTextResponse.class);
		return xs.toXML(response);
	}
	
	public static String toWeixinResponseXml(WeixinImageResponse response){
		XStream xs=new XStream();
		xs.alias("xml", WeixinImageResponse.class);
		return xs.toXML(response);
	}
}
