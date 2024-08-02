package com.timetracker.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.timetracker.model.Task;

public class UserDAO {
	static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeetimetrackerapp", "root", "root");
		} catch (Exception e) {
			System.out.println(e);
		}
		return conn;
	}

	public int loginCustomer(String username, String pwd) throws SQLException {
		Connection c = null;
		try {
			c = getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String q = "SELECT id FROM User WHERE name=? and password=?;";
		PreparedStatement ps = c.prepareStatement(q);
		ps.setString(1, username);
		ps.setString(2, pwd);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getInt("id");
		} else {
			return -1;
		}
	}
	
	public boolean checkManager(int id) throws SQLException {
		Connection c = null;
		try {
			c = getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String q = "select type from User where id=?;";
		PreparedStatement ps = c.prepareStatement(q);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			System.out.println(rs.getString(1));
			return rs.getString(1).equals("manager") ? true : false;
		} else {
			return false;
		}
	}

	public boolean createTask(Task t) throws SQLException, ParseException {
		Connection c = null;
		try {
			c = getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String q = "INSERT INTO Task (emp_id, date, start, end, no_of_hours, task_name, description, manager_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement ps = c.prepareStatement(q);
		ps.setInt(1, t.getEmpId());
		System.out.println(t.getDate());
		ps.setString(2, t.getDate());
		ps.setTime(3, new Time(new SimpleDateFormat("HH:mm").parse(t.getStart()).getTime()));
		ps.setTime(4, new Time(new SimpleDateFormat("HH:mm").parse(t.getEnd()).getTime()));
		ps.setInt(5, t.getNo_of_hours());
		ps.setString(6, t.getTaskName());
		ps.setString(7, t.getDescription());
		ps.setInt(8, t.getManagerId());
		ps.setString(9, t.getStatus());
		int result = ps.executeUpdate();
		if (result > 0)
			return true;
		else
			return false;
	}

	public ResultSet getTasks(int empId) throws SQLException {
		Connection c = null;
		try {
			c = getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String q = "SELECT * FROM Task WHERE emp_id=?;";
		PreparedStatement st = c.prepareStatement(q);
		st.setInt(1, empId);
		ResultSet rs = st.executeQuery();
		return rs;
	}
	public ResultSet getManagers(int id) throws SQLException {
		Connection c = null;
		try {
			c = getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String q = "SELECT id, name FROM (SELECT * FROM User WHERE type='manager') as t where id!=?;";
		PreparedStatement st = c.prepareStatement(q);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		return rs;
	}
	
	public boolean deleteTask(int id) throws SQLException {
		Connection c = null;
		try {
			c = getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String q = "DELETE FROM Task WHERE id=?";
		PreparedStatement st = c.prepareStatement(q);
		st.setInt(1, id);
		int result = st.executeUpdate();
		return (result > 0) ? true : false;
	}
	
	public ResultSet getDailyChartData(int id) throws SQLException {
		Connection c = null;
		try {
			c = getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String q = "SELECT task_name, no_of_hours FROM Task where emp_id=?;";
		PreparedStatement st = c.prepareStatement(q);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		return rs;
	}
	
	public ResultSet getWeeklyChartData(int id) throws SQLException {
		Connection c = null;
		try {
			c = getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String q = "SELECT date, sum(no_of_hours) FROM Task where emp_id=? group by date;";
		PreparedStatement st = c.prepareStatement(q);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		return rs;
	}
	
	public int getTotalHours(int id, String date) throws SQLException {
		Connection c = null;
		try {
			c = getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String q = "select t.s_hours from (SELECT date, sum(no_of_hours) as s_hours FROM Task where emp_id=? group by date) as t where date=?;";
		PreparedStatement st = c.prepareStatement(q);
		st.setInt(1, id);
		st.setString(2, date);
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		}
		else {			
			return -1;
		}
	}
}
