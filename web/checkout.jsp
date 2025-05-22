<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
    if (cart == null) cart = new ArrayList<>();

    DecimalFormat df = new DecimalFormat("0.00");
    double subtotal = 0;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Checkout - SYOS</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f9f9f9;
            padding: 40px;
            margin: 0;
        }

        h1 {
            text-align: center;
            margin-bottom: 30px;
        }

        table {
            width: 100%;
            background-color: #fff;
            border-collapse: collapse;
            box-shadow: 0 0 10px #ccc;
            margin-bottom: 30px;
        }

        th, td {
            padding: 16px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f0f0f0;
        }

        .form-section {
            width: 100%;
            max-width: 400px;
            margin: 0 auto 30px;
            background: #fff;
            padding: 20px;
            box-shadow: 0 0 10px #ccc;
            border-radius: 10px;
        }

        .form-section input {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .submit-btn {
            width: 100%;
            background-color: #004d00;
            color: white;
            padding: 12px;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .submit-btn:hover {
            background-color: #006600;
        }

        .alert {
            text-align: center;
            font-weight: bold;
            margin-bottom: 20px;
            color: green;
        }

        .error {
            color: red;
        }
    </style>
</head>
<body>

<h1>Checkout</h1>

<% if (request.getParameter("success") != null) { %>
<div class="alert">✅ Order placed successfully! Change: Rs. <%= request.getParameter("change") %></div>
<% } else if (request.getParameter("error") != null) { %>
<div class="alert error">❌ Error: <%= request.getParameter("error") %></div>
<% } %>

<table>
    <thead>
    <tr>
        <th>Product</th>
        <th>Price</th>
        <th>Discount</th>
        <th>Quantity</th>
        <th>Total</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (Map<String, Object> item : cart) {
            String name = (String) item.get("name");
            double price = (Double) item.get("price");
            double discount = (Double) item.get("discount");
            int quantity = (int) item.getOrDefault("quantity", 1);

            double discountedPrice = price * (1 - discount / 100);
            double itemTotal = discountedPrice * quantity;
            subtotal += itemTotal;
    %>
    <tr>
        <td><%= name %></td>
        <td>Rs. <%= df.format(price) %></td>
        <td><%= df.format(discount) %> %</td>
        <td><%= quantity %></td>
        <td>Rs. <%= df.format(itemTotal) %></td>
    </tr>
    <% } %>
    </tbody>
</table>

<div class="form-section">
    <form action="checkout" method="post">
        <input type="text" name="customerName" placeholder="Customer Name" required>
        <input type="number" step="0.01" name="cash" placeholder="Cash Tendered" required>
        <input type="hidden" name="paymentMethod" value="Cash">
        <input type="hidden" name="totalAmount" value="<%= subtotal %>">
        <button class="submit-btn" type="submit">Pay Rs. <%= df.format(subtotal) %></button>
    </form>
</div>

</body>
</html>
