package com.Weixin.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.Tool.util.CheckUtil;
import com.Tool.util.MessageUtil;
import com.Weixin.po.TextMessage;

public class WeixinServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr"); 

		PrintWriter  out = resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		
		try {
			Map<String,String> map = MessageUtil.xmlToMap(req);
			
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			String msg = null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
				TextMessage textMessage = new TextMessage();
				textMessage.setFromUserName(toUserName);
				textMessage.setToUserName(fromUserName);
				textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setContent("您发送的消息是："+content);
				
				msg = MessageUtil.textMessageToXml(textMessage);
				
				System.out.println(msg);
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.textWelcome());
				}
			}
			out.print(msg);

		} catch (DocumentException e) {
			e.printStackTrace();
		} finally{
			out.close();
		}
	}

}
