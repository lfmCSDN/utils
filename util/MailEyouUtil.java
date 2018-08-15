package com.os.util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

/**
* 发送邮件到易邮服务器工具类
* --------------------------------------------------------------------------------------------------
* 使用方法：
* 1_将本类放在com.itheima.shop.util包下
* 2_导入mail.jar到工程的/WEB-INF/lib/下
* 3_写一个Demo01类带main函数，调用
  MailEyouUtil.sendMail("发件人邮箱","收件人邮箱","邮件主题","支持HTML格式的邮件内容")
* 如：MailEyouUtil.sendMail("admin@abc.com","zhaojun@abc.com","测试一下","<b>你好</b>")
* --------------------------------------------------------------------------------------------------
*/
public final class MailEyouUtil {
	
	/**
	 * 不允许new出工具类
	 */
	private MailEyouUtil(){}
	
	/**
	 * 发送邮件
	 * 参数一：发件人邮箱
	 * 参数二：收件人邮箱
	 * 参数三：邮件主题
	 * 参数四：邮件内容
	 */
	public static void sendMail(String fromEmail, String toEmail, String subject, String emailMsg) throws Exception {
		//1_建立Java程序与易邮邮件服务器的连接对象
		Properties props = new Properties();
		props.put("mail.smtp.host", "127.0.0.1");//易邮邮件服务器中SMTP服务器的域名或IP
		Session session = Session.getInstance(props,null);
		
		//2_创建一封邮件
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromEmail));//发件人邮箱
		message.setRecipient(RecipientType.TO, new InternetAddress(toEmail));//收件人邮箱
		message.setSubject(subject);//主题
		message.setContent(emailMsg, "text/html;charset=UTF-8");//内容和格式编码
		
		//3_发送邮件
		Transport.send(message);
	}
	
}








