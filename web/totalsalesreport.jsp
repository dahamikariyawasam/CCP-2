<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="service.SalesService, service.ReportBuilder, util.SingletonDatabase" %>
<%@ page import="java.util.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
  <title>Total Sales Report - SYOS Admin</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      margin: 0;
    }

    nav {
      background-color: #004d00;
      color: white;
      padding: 15px 30px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    nav span {
      font-size: 20px;
      font-weight: bold;
    }

    nav a {
      color: white;
      text-decoration: none;
      margin-left: 20px;
      font-weight: bold;
    }

    nav a:hover {
      text-decoration: underline;
    }

    .container {
      padding: 40px;
    }

    h2 {
      color: #004d00;
      margin-bottom: 20px;
    }

    form {
      margin-bottom: 30px;
    }

    input[type="date"], button {
      padding: 10px;
      font-size: 16px;
      margin-right: 10px;
    }

    button {
      background-color: #004d00;
      color: white;
      border: none;
      cursor: pointer;
    }

    button:hover {
      background-color: #006600;
    }

    .report-heading {
      font-weight: bold;
      font-size: 18px;
      margin-top: 20px;
      margin-bottom: 10px;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      background: white;
      box-shadow: 0 0 8px #ccc;
    }

    th, td {
      padding: 12px;
      text-align: center;
      border: 1px solid #ddd;
    }

    th {
      background-color: #004d00;
      color: white;
    }

    .no-results {
      color: red;
      font-weight: bold;
      margin-top: 20px;
    }
  </style>
</head>
<body>

<nav>
  <span>SYOS Admin Panel</span>
  <div>
    <a href="admindashboard.jsp">🏠 Dashboard</a>
    <a href="reports.jsp">📊 All Reports</a>
    <a href="logout.jsp">🔓 Logout</a>
  </div>
</nav>

<div class="container">
  <h2>Total Sales Report</h2>

  <form method="get" action="totalsalesreport.jsp">
    <label>Select Date:</label>
    <input type="date" name="reportDate" required>
    <button type="submit">Generate Report</button>
  </form>

  <%
    String date = request.getParameter("reportDate");
    if (date != null && !date.isEmpty()) {
      SalesService salesService = new SalesService(SingletonDatabase.getInstance());
      List<Map<String, Object>> report = salesService.getTotalSalesByDate(date);

      if (!report.isEmpty()) {
        ReportBuilder reportBuilder = new ReportBuilder()
                .setReportType("Total Sales")
                .setDateRange(date, date);
  %>
  <div class="report-heading"><%= reportBuilder.build() %></div>

  <table>
    <tr>
      <th>Item Code</th>
      <th>Item Name</th>
      <th>Total Quantity</th>
      <th>Total Revenue (Rs.)</th>
    </tr>
    <%
      for (Map<String, Object> row : report) {
    %>
    <tr>
      <td><%= row.get("Item Code") %></td>
      <td><%= row.get("Item Name") %></td>
      <td><%= row.get("Total Quantity") %></td>
      <td><%= String.format("Rs. %.2f", row.get("Total Revenue")) %></td>
    </tr>
    <% } %>
  </table>
  <%
  } else {
  %>
  <p class="no-results">❌ No sales found for the selected date.</p>
  <%
      }
    }
  %>
</div>

</body>
</html>
