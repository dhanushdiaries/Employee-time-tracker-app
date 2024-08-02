package com.timetracker.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.timetracker.dao.UserDAO;

/**
 * Servlet implementation class DeleteTaskServlet
 */
@WebServlet("/DeleteTaskServlet")
public class DeleteTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int task_id = Integer.parseInt(request.getParameter("id"));
		UserDAO admin = new UserDAO();
		try {
			if (admin.deleteTask(task_id)) {
				response.setContentType("text/html");
				response.getWriter().println("Task Deleted Successfully");
				request.getRequestDispatcher("ManagerDashboard.jsp").include(request, response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

