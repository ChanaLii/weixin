package com.mashen.recommend.handler;

import com.mashen.weixin.handler.MessageHandler;
import com.mashen.weixin.message.WeixinMessageUtil;
import com.mashen.weixin.message.WeixinRequest;
import com.mashen.weixin.message.WeixinTextResponse;

public class TextMessageHandler implements MessageHandler {

	@Override
	public boolean match(WeixinRequest request) throws Throwable {
		if ("text".equalsIgnoreCase(request.getContent())) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String processMsg(WeixinRequest request) throws Throwable {
		WeixinTextResponse response = new WeixinTextResponse();
		response.setFromUserName(request.getToUserName());
		response.setToUserName(request.getFromUserName());
		response.setContent("欢迎关注Coding公众号！");
		return WeixinMessageUtil.toWeixinResponseXml(response);
	}

}
