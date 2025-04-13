<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Random"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.awt.Color"%>
<%@ page import="java.awt.Font"%>
<%@ page import="java.awt.Graphics"%>
<%@ page import="java.awt.image.BufferedImage"%>
<%@ page import="javax.imageio.ImageIO"%>
<%
	int width = 80;
	int height = 32;
	//create the image
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	Graphics g = image.getGraphics();
	// set the background color
	g.setColor(new Color(30,30,30));
	g.fillRect(0, 0, width, height);
	// draw the border
	g.setColor(Color.YELLOW);
	g.drawRect(0, 0, width - 1, height - 1);
	// create a random instance to generate the codes
	Random rdm = new Random();
	//rdm.nextInt()  返回int取值范围内的伪随机数，[2147483648 , 2147483647]
	String hash1 = Integer.toHexString(rdm.nextInt());//将传入的数值十进制转成十六进制
	// make some confusion
	for (int i = 0; i < 50; i++) {
		int x = rdm.nextInt(width);
		int y = rdm.nextInt(height);
		g.drawOval(x, y, 0, 0);
	}
	// 生成随机码
	String capstr = hash1.substring(0, 4);
	session.setAttribute("sessionCode", capstr);
	g.setColor(new Color(255, 255, 0));
	g.setFont(new Font("Candara", Font.BOLD, 24));
	g.drawString(capstr, 8, 24);
	g.dispose();
	response.setContentType("image/jpeg");
	out.clear();
	out = pageContext.pushBody();
	OutputStream strm = response.getOutputStream();
	ImageIO.write(image, "jpeg", strm);
	strm.close();
%>