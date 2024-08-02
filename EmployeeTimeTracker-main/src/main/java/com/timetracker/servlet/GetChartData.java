package com.timetracker.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.timetracker.dao.UserDAO;

@WebServlet("/GetChartData")
public class GetChartData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String chart_type = request.getParameter("type");
		HttpSession session = request.getSession(false);
		int id = Integer.parseInt((String) session.getAttribute("user"));
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		UserDAO user = new UserDAO();
		List tasks = new ArrayList<String>();
		List no_of_hours = new ArrayList<Integer>();
		Map<String, List> values = new HashMap<String, List>();
		try {
			if (chart_type.equals("daily")) {
				
			ResultSet r = user.getDailyChartData(id);
			while (r.next()) {
				tasks.add(r.getString(1));
				no_of_hours.add(r.getInt(2));
			}
			values.put("tasks", tasks);
			values.put("no_of_hours", no_of_hours);
			}
			if (chart_type.equals("weekly")) {
				ResultSet r = user.getWeeklyChartData(id);
				while (r.next()) {
					tasks.add(r.getString(1));
					no_of_hours.add(r.getInt(2));
				}
				values.put("date", tasks);
				values.put("no_of_hours", no_of_hours);
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject json = new JSONObject(values);

		out.print(json.toString());
		out.flush();
	}
}

//{
//labels: [],
//data: []
//}