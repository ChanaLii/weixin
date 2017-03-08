package com.test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import com.mashen.common.util.ConfigUtil;
import com.mashen.common.util.ImageUtil;
import com.mashen.weixin.api.ImageCustomMessage;
import com.mashen.weixin.api.Media;
import com.mashen.weixin.api.QRTicket;
import com.mashen.weixin.api.QRTicketRequest;
import com.mashen.weixin.api.UserInfo;
import com.mashen.weixin.api.WeixinApi;
import com.mashen.weixin.message.WeixinImageResponse;
import com.mashen.weixin.message.WeixinRequest;

public class CreatePosterTask implements Runnable{
	
	//创建构造器接收WeixinRequest对象
	WeixinRequest request;
	public CreatePosterTask(WeixinRequest request) {
		this.request = request;
	}
	
	@Override
	public void run() {
		WeixinImageResponse response = new WeixinImageResponse();
		UserInfo user;
		try {
			user = WeixinApi.getUserInfo(request.getFromUserName());
			// 获取用户头像
			BufferedImage headImg = ImageUtil.getImage(new URL(user.getHeadimgurl()));
			// 获取用户二维码
			QRTicketRequest qrrequest = new QRTicketRequest();
			qrrequest.addSceneId("1");
			QRTicket qrTicket = WeixinApi.getQrTicket(qrrequest);

			// 拼接二维码地址
			String qrshow = ConfigUtil.get("qrshow");

			BufferedImage qrImg = ImageUtil.getImage(new URL(String.format(qrshow, qrTicket.getTicket())));

			BufferedImage bgImg = ImageUtil.getImage("C:/Users/Administrator/Desktop/bg.jpg");

			// 拼接头像
			ImageUtil.merge(bgImg, headImg, 100, 700, 120, 120);

			// 拼接二维码
			ImageUtil.merge(bgImg, qrImg, 330, 700, 120, 120);

			ImageUtil.saveImage("C:/Users/Administrator/Desktop/微信公众号/bg1.jpg", bgImg, "jpg");

			Media media = WeixinApi.upload("C:/Users/Administrator/Desktop/微信公众号/bg1.jpg", Media.TYPE_IMAGE);
			
			ImageCustomMessage message = new ImageCustomMessage();
			message.setMediaId(media.getMedia_id());
			message.setTouser(request.getFromUserName());
			//调用客服API
			WeixinApi.sendCustomMessage(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
}
