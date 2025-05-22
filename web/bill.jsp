<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, java.text.DecimalFormat" %>

<%
    DecimalFormat df = new DecimalFormat("0.00");
    List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("billCart");
    if (cart == null) cart = new ArrayList<>();

    double total = 0;
    double cash = (double) session.getAttribute("billCash");
    double change = (double) session.getAttribute("billChange");
    int serialNumber = (int) session.getAttribute("billSerial");
    String date = (String) session.getAttribute("billDate");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Invoice - SYOS</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f2fff2;
            padding: 40px;
            color: #003300;
        }

        .header {
            background-color: #006600;
            color: white;
            padding: 20px;
            text-align: center;
            border-radius: 8px;
        }

        .store-info {
            text-align: center;
            margin-top: 10px;
        }

        h1, h3 {
            margin: 10px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 30px 0;
            box-shadow: 0 0 10px #ccc;
            background-color: white;
        }

        th, td {
            padding: 14px;
            border-bottom: 1px solid #ccc;
            text-align: center;
        }

        th {
            background-color: #e6ffe6;
            color: #004d00;
        }

        .totals {
            width: 320px;
            margin-left: auto;
            border: 1px solid #ccc;
            padding: 20px;
            background: white;
            border-radius: 6px;
        }

        .totals div {
            margin: 10px 0;
            font-size: 16px;
        }

        .footer-note {
            text-align: center;
            margin-top: 40px;
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>

<div class="header">
    <h1>SYNEX Outlet Store</h1>
</div>

<div class="store-info">
    <p>Colombo, Sri Lanka</p>
    <p>Phone: +94 112 345 678 | Email: support@synos.lk</p>
</div>

<h3 style="text-align:center;">Invoice #<%= serialNumber %> | Date: <%= date %></h3>

<table>
    <thead>
    <tr>
        <th>Item</th>
        <th>Price (Rs.)</th>
        <th>Discount (%)</th>
        <th>Quantity</th>
        <th>Total (Rs.)</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (Map<String, Object> item : cart) {
            String name = (String) item.get("name");
            double price = (Double) item.get("price");
            double discount = (Double) item.get("discount");
            int quantity = (int) item.get("quantity");

            double discountedPrice = price * (1 - discount / 100);
            double subtotal = discountedPrice * quantity;
            total += subtotal;
    %>
    <tr>
        <td><%= name %></td>
        <td><%= df.format(price) %></td>
        <td><%= df.format(discount) %></td>
        <td><%= quantity %></td>
        <td><%= df.format(subtotal) %></td>
    </tr>
    <% } %>
    </tbody>
</table>

<div class="totals">
    <div><strong>Total Amount:</strong> Rs. <%= df.format(total) %></div>
    <div><strong>Cash Tendered:</strong> Rs. <%= df.format(cash) %></div>
    <div><strong>Change:</strong> Rs. <%= df.format(change) %></div>
</div>

<div class="footer-note">
    Thank you for shopping at SYNEX Outlet Store!
</div>

</body>
</html>
