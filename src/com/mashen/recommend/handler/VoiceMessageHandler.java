package com.mashen.recommend.handler;

import com.mashen.weixin.handler.MessageHandler;
import com.mashen.weixin.message.WeixinMessageUtil;
import com.mashen.weixin.message.WeixinRequest;
import com.mashen.weixin.message.WeixinTextResponse;
import com.test.JsonTest;

public class VoiceMessageHandler implements MessageHandler {
		
	@Override
	public boolean match(WeixinRequest request) throws Throwable {
		if (request.getMsgType().equals("voice")) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String processMsg(WeixinRequest request) throws Throwable {
		//String cityName[] = {"北京","上海","广州","深圳"};
		String voiceStr = request.getRecognition();
		String split = voiceStr.substring(0, voiceStr.length()-1);
		System.out.println(split.length());
		WeixinTextResponse response = new WeixinTextResponse();
		String weatherInfo = JsonTest.getWeatherInfo(split);
		//必须设置首发双方名
		response.setFromUserName(request.getToUserName());
		response.setToUserName(request.getFromUserName());
		response.setContent(weatherInfo);
		return WeixinMessageUtil.toWeixinResponseXml(response);

	}
}
