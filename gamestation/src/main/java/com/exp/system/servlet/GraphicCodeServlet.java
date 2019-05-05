package com.exp.system.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.exp.common.pojo.ValidationCodePojo;

public class GraphicCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final char[] CODESEQUENCE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9' };
	private Font mFont = new Font("Times New Roman", Font.PLAIN, 17); //$NON-NLS-1$
	private StringBuilder myRand = new StringBuilder();
	
	static {
		System.setProperty("java.awt.headless", "true"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		int r = fc > 255 ? 255 : fc + random.nextInt(bc > 255 ? 255 : bc - fc > 255 ? 255 : fc);
		int g = fc > 255 ? 255 : fc + random.nextInt(bc > 255 ? 255 : bc - fc > 255 ? 255 : fc);
		int b = fc > 255 ? 255 : fc + random.nextInt(bc > 255 ? 255 : bc - fc > 255 ? 255 : fc);
		return new Color(r, g, b);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Pragma", "No-cache"); //$NON-NLS-1$ //$NON-NLS-2$
		response.setHeader("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
		response.setDateHeader("Expires", 0); //$NON-NLS-1$
		response.setContentType("image/jpeg"); //$NON-NLS-1$

		int width = 70, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 102));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(this.mFont);

		g.setColor(getRandColor(160, 200));

		this.myRand.delete(0, this.myRand.length());
		for (int i = 0; i < 4; i++) {
			char cRand = CODESEQUENCE[random.nextInt(62)];
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(String.valueOf(cRand), 15 * i + 10, 16);
			this.myRand.append(cRand);
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("graphicCode", ValidationCodePojo.newInstance(this.myRand.toString(), System.currentTimeMillis() + 5 * 60 * 1000L)); //$NON-NLS-1$
		g.dispose();
		ImageIO.write(image, "JPEG", response.getOutputStream()); //$NON-NLS-1$
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.service(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.service(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException
	 *             if an error occurs
	 */
	@Override
	public void init() throws ServletException {
		// Put your code here
	}

}
