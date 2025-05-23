<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*, util.SingletonDatabase" %>
<!DOCTYPE html>
<html>
<head>
  <title>Reorder Report - SYOS</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      padding: 30px;
      background-color: #f9f9f9;
    }
    h2 {
      color: #004d00;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      background: white;
      margin-top: 20px;
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
    .warning {
      color: red;
      font-weight: bold;
    }
    nav {
      background-color: #004d00;
      color: white;
      padding: 15px;
      display: flex;
      justify-content: space-between;
    }
    nav a {
      color: white;
      font-weight: bold;
      text-decoration: none;
    }
  </style>
</head>
<body>

<nav>
  <span>SYOS Admin</span>
  <a href="admindashboard.jsp">🏠 Dashboard</a>
</nav>

<h2>Reorder Report</h2>

<table>
  <tr>
    <th>Product ID</th>
    <th>Name</th>
    <th>Stock Quantity</th>
    <th>Alert</th>
  </tr>

  <%
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = SingletonDatabase.getInstance().getConnection();
      String sql = "SELECT id, name, stock_quantity FROM products WHERE stock_quantity < 50 ORDER BY stock_quantity ASC";
      stmt = conn.prepareStatement(sql);
      rs = stmt.executeQuery();

      while (rs.next()) {
  %>
  <tr>
    <td><%= rs.getInt("id") %></td>
    <td><%= rs.getString("name") %></td>
    <td><%= rs.getInt("stock_quantity") %></td>
    <td class="warning">⚠ Reorder Needed</td>
  </tr>
  <%
    }

  } catch (Exception e) {
  %>
  <tr><td colspan="4">❌ Error loading data: <%= e.getMessage() %></td></tr>
  <%
    } finally {
      if (rs != null) rs.close();
      if (stmt != null) stmt.close();
      if (conn != null) conn.close();
    }
  %>
</table>

</body>
</html>
