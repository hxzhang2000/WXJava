package com.Tool.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.Weixin.po.TextMessage;
import com.thoughtworks.xstream.XStream;

/**
 * 消息的格式转换
 * @author Betty
 *
 */
public class MessageUtil {

	public static String MESSAGE_TEXT = "text";
	public static String MESSAGE_IMAGE = "image";
	public static String MESSAGE_VOICE = "voice";
	public static String MESSAGE_VIDEO = "video";
	public static String MESSAGE_LINK = "link";
	public static String MESSAGE_LOCATION = "location";
	public static String MESSAGE_EVENT = "event";
	public static String MESSAGE_SUBSCRIBE = "subscribe";
	public static String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static String MESSAGE_CLICK = "CLICK";
	public static String MESSAGE_VIEW = "VIEW";
	
	/**
	 * XML转换为map集合
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) 
			throws IOException, DocumentException{
		Map<String,String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		
		List<Element> list = root.elements();
		
		for(Element e : list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	
	/**
	 * 文本对象转换为XML
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	
	public static String initText(String toUserName, String fromUserName, String content){
		TextMessage textMessage = new TextMessage();
		textMessage.setFromUserName(toUserName);
		textMessage.setToUserName(fromUserName);
		textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setContent(content);
		
		return textMessageToXml(textMessage);
	}
	
	public static String textWelcome(){
		return "欢迎您的关注！";
	}
}
