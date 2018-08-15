package com.os.util;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * AJAX专用向客户端输出内容工具类
 */
public final class PrintUtil {

	/**
	 * 不允许new出工具类
	 */
	private PrintUtil(){}
	
	/**
	 * 向客户端输出内容
	 */
	public static void toClient(String content,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(content);
		pw.flush();
		pw.close();
	}
	
}
