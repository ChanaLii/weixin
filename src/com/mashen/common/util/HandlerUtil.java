package com.mashen.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.mashen.weixin.handler.MessageHandler;

public class HandlerUtil {
	private static List<MessageHandler> handlerList=new ArrayList<MessageHandler>();
	
	static{
		InputStream input=HandlerUtil.class
				.getClassLoader()
				.getResourceAsStream("handler.conf");
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		String line=null;
		try {
			while((line=reader.readLine())!=null){
				if(!"".equals(line.replaceAll("\\s",""))){
					MessageHandler handler=(MessageHandler)Class.forName(line)
							.newInstance();
					handlerList.add(handler);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	public static List<MessageHandler> getHandlerList(){
		return handlerList;
	}
}
