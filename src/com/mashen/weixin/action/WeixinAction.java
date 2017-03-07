package com.mashen.weixin.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mashen.common.util.ConfigUtil;
import com.mashen.common.util.HandlerUtil;
import com.mashen.common.util.SignUtil;
import com.mashen.weixin.handler.MessageHandler;
import com.mashen.weixin.message.WeixinMessageUtil;
import com.mashen.weixin.message.WeixinRequest;

/**
 * Servlet implementation class WeixinAction
 */
@WebServlet("/WeixinAction")
public class WeixinAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<String> msgIdList=new ArrayList<String>();
	private List<MessageHandler> handlerList=HandlerUtil.getHandlerList();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeixinAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * GET请求用于开发验证
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String signature=request.getParameter("signature");
		String timestamp=request.getParameter("timestamp");
		String nonce=request.getParameter("nonce");
		String echostr=request.getParameter("echostr");
		System.out.println("signature:"+signature);
		System.out.println("timestamp:"+timestamp);
		System.out.println("nonce:"+nonce);
		System.out.println("echostr:"+echostr);
		if(signature!=null){
			if(SignUtil.validSign(signature,ConfigUtil.get("token"), timestamp, nonce)){
				response.getWriter().write(echostr);
			}else{
				response.getWriter().write("验证失败！");
			}
		}else{
			response.getWriter().write("参数错误！");
		}
	}

	/**
	 * POST方法用于接收微信服务器发送过来的消息
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream input=request.getInputStream();
		String msgId=null;
		try {
			WeixinRequest weixinReq=WeixinMessageUtil.toWeixinRequest(input);
			//消息去重处理
			if("event".equals(weixinReq.getMsgType())){
				msgId=weixinReq.getFromUserName()+weixinReq.getCreateTime();
			}else{
				msgId=weixinReq.getMsgId();
			}
			if(msgIdList.remove(msgId)){//消息重复
				response.getWriter().write("");
				return;
			}
			msgIdList.add(msgId);
			String content=weixinReq.getContent();
			System.out.println(content);
			System.out.println("eventkey:"+weixinReq.getEventKey());
		    for(MessageHandler msgHandler : handlerList){
		    	if(msgHandler.match(weixinReq)){
		    		String result=msgHandler.processMsg(weixinReq);
		    		response.getWriter().write(result);
		    		break;
		    	}
		    }
		
		} catch (Throwable e) {
			e.printStackTrace();
			response.getWriter().write("");
		}finally{
			msgIdList.remove(msgId);
		}
		
	}

}
