package com.mashen.recommend.handler;

import com.mashen.weixin.api.Media;
import com.mashen.weixin.api.WeixinApi;
import com.mashen.weixin.handler.MessageHandler;
import com.mashen.weixin.message.WeixinImageResponse;
import com.mashen.weixin.message.WeixinMessageUtil;
import com.mashen.weixin.message.WeixinRequest;

public class ImageMessageHandler implements MessageHandler {

	@Override
	public boolean match(WeixinRequest request) throws Throwable {
		if ("img".equalsIgnoreCase(request.getContent())) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String processMsg(WeixinRequest request) throws Throwable {
		WeixinImageResponse response = new WeixinImageResponse();
		response.setFromUserName(request.getToUserName());
		response.setToUserName(request.getFromUserName());
		Media media = WeixinApi.upload("C:/Users/Administrator/Desktop/微信公众号/14 - 10(2).png", Media.TYPE_IMAGE);
		response.addMediaId(media.getMedia_id());
		return WeixinMessageUtil.toWeixinResponseXml(response);
	}

}
