<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="service.BillService, service.ReportBuilder, util.SingletonDatabase" %>
<%@ page import="java.util.*, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Total Bill Report - SYOS</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background-color: #f4f4f4;
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
    </div>
</nav>

<div class="container">
    <h2>Total Bill Report</h2>

    <form method="get" action="totalbillreport.jsp">
        Select Date: <input type="date" name="billDate" required>
        <button type="submit">Generate Report</button>
    </form>

    <%
        String billDate = request.getParameter("billDate");
        if (billDate != null && !billDate.isEmpty()) {
            service.BillService billService = new service.BillService(SingletonDatabase.getInstance());
            List<Map<String, Object>> bills = billService.getAllBills();

            List<Map<String, Object>> filtered = new ArrayList<>();
            for (Map<String, Object> b : bills) {
                String date = b.get("Bill Date").toString().substring(0, 10);
                if (date.equals(billDate)) {
                    filtered.add(b);
                }
            }

            if (!filtered.isEmpty()) {
                ReportBuilder builder = new ReportBuilder()
                        .setReportType("Total Bills")
                        .setDateRange(billDate, billDate);
    %>
    <div class="report-heading"><%= builder.build() %></div>
    <table>
        <tr>
            <th>Serial #</th>
            <th>Item Name</th>
            <th>Quantity</th>
            <th>Full Price</th>
            <th>Discount (%)</th>
            <th>Total Price</th>
            <th>Cash</th>
            <th>Change</th>
            <th>Date</th>
        </tr>
        <%
            for (Map<String, Object> bill : filtered) {
        %>
        <tr>
            <td><%= bill.get("Serial Number") %></td>
            <td><%= bill.get("Item Name") %></td>
            <td><%= bill.get("Quantity") %></td>
            <td>Rs. <%= bill.get("Full Price") %></td>
            <td><%= bill.get("Discount") %></td>
            <td>Rs. <%= bill.get("Total Price") %></td>
            <td>Rs. <%= bill.get("Cash Tendered") %></td>
            <td>Rs. <%= bill.get("Change Amount") %></td>
            <td><%= bill.get("Bill Date") %></td>
        </tr>
        <% } %>
    </table>
    <% } else { %>
    <p class="no-results">❌ No bills found for the selected date.</p>
    <% } } %>

</div>

</body>
</html>
