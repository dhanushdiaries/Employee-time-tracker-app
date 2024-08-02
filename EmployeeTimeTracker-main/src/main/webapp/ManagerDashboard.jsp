<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.timetracker.dao.*,java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Employee Time Tracker - Manager Dashboard</title>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.5.1/chart.min.js"></script>
<link rel="stylesheet" href="manager.css">
</head>
<body>
	<%
	if (request.getSession(false).getAttribute("user-type") != null) {
		if (request.getSession(false).getAttribute("user-type").equals("admin"))
			response.sendRedirect("index.jsp");
	} else {
		response.sendRedirect("index.jsp");
	}
	%>
	<h1>Manager Dashboard</h1>
	<form action="LogoutServlet" method="post">
		<input type="submit" value="Logout">
	</form>
	<a href="addTask.jsp">Add Task</a>
	<div>
		<h2>Users</h2>
		<div class="table-wrapper">
		<table class="user-table" border="1">
			<tr>
				<th>ID</th>
				<th>Employee ID</th>
				<th>Date</th>
				<th>Start</th>
				<th>End</th>
				<th>Number of Hours</th>
				<th>Task Name</th>
				<th>Task Description</th>
				<th>Manager</th>
				<th>Status</th>
				<th colspan="2">Action</th>
			</tr>
			<%
			HttpSession s = request.getSession(false);
			int id = Integer.parseInt((String) s.getAttribute("user"));
			UserDAO user = new UserDAO();
			ResultSet rs = user.getTasks(id);
			while (rs.next()) {
			%>
			<tr>
				<td><%=rs.getInt(1)%></td>
				<td><%=rs.getInt(2)%></td>
				<td><%=rs.getDate(3)%></td>
				<td><%=rs.getTime(4)%></td>
				<td><%=rs.getTime(5)%></td>
				<td><%=rs.getInt(6)%></td>
				<td><%=rs.getString(7)%></td>
				<td><%=rs.getString(8)%></td>
				<% if (rs.getInt(9) > 0) { %>
				<td><%=rs.getInt(9)%></td>
				<% } else { %>
				<td> - </td>
				<% } %>
				<td><%=rs.getString(10)%></td>
				<td><a href="editUser.jsp?id=<%=rs.getString(1)%>">Edit</a></td>
				<td><a href="DeleteTaskServlet?id=<%=rs.getString(1)%>">Delete</a></td>
			</tr>
			<%
			}
			%>
		</table>
		</div>
		<div class="chart-wrapper">
	<div class="chart-container">
		<canvas id="dailyCanvas"></canvas>
	</div>
	<div class="chart-container">
		<canvas id="weeklyCanvas"></canvas>
	</div>
	</div>
	</div>
	<script type="text/javascript">
	var tasks;
	var d_data;
	var date;
	var w_data;
		fetch('GetChartData?type=daily').then(res => res.json()).then(data => {
			console.log(data);
			tasks=data["tasks"];
			d_data=data["no_of_hours"];
			});
		fetch('GetChartData?type=weekly').then(res => res.json()).then(data => {
			console.log(data);
			date=data["date"];
			w_data=data["no_of_hours"];
			drawChart();});
	const c = document.querySelector('#dailyCanvas');
	const wc = document.querySelector('#weeklyCanvas');
	function drawChart() {			
	      new Chart(c, {
	        type: 'doughnut',
	        data: {
	          labels: tasks,
	          datasets: [{
	            label: 'Daily Task',
	            data: d_data,
	            borderRadius: [10, 	10],
	            backgroundColor: getRandomColor(d_data?.length),
	          }]
	        },
	      });
	      new Chart(wc, {
		        type: 'bar',
		        data: {
		          labels: date,
		          datasets: [{
		            label: 'Weekly Task',
		            data: w_data,
		            borderRadius: [10, 	10],
		            backgroundColor: getRandomColor(w_data.length),
		          }]
		        },
		      });
	    }
	function getRandomColor(count) {
		var colors = [];
		for (var i = 0; i < count; i++) {
		   var letters = '0123456789ABCDEF'.split('');
		   var color = '#';
		      for (var x = 0; x < 6; x++) {
		          color += letters[Math.floor(Math.random() * 16)];
		      }
		      colors.push(color);
		      }
		   return colors;
		}
	</script>
</body>
</html>