package com.newlecture.web.controller.admin.notice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;

@MultipartConfig(
		//location = "/tmp", // 디스크
		fileSizeThreshold = 1024*1024, // 1mb 가 넘으면 디스크를 쓰자
		maxFileSize = 1024*1024*50,	// 하나의 파일 싸이즈가 몇이냐 맥스값
		maxRequestSize = 1024*1024*50*5	//전체요청에 대한 맥스값
		)

@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		request
		.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp")
		.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		
		System.out.println(title);
		String content = request.getParameter("content");
		String isOpen = request.getParameter("open");
		
		Part filePart = request.getPart("file");
		filePart.getInputStream();
		
		String realPath = request.getServletContext().getRealPath("/upload");
		System.out.println(realPath);
		
		boolean pub = false;
		if(isOpen != null)
			pub = true;
		
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setPub(pub);
		notice.setWriterId("newlec");
		
		NoticeService service = new NoticeService();
		//int result = service.insertNotice(notice);
		
		response.sendRedirect("list");
	}
}
