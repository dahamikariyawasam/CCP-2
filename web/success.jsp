<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%
    Map<String, String> orderDetails = (Map<String, String>) session.getAttribute("orderDetails");
    if (orderDetails == null) {
        response.sendRedirect("home.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Order Confirmation - SYOS</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f9f9f9;
            padding: 50px;
            text-align: center;
        }

        .box {
            background: white;
            padding: 40px;
            max-width: 500px;
            margin: auto;
            border-radius: 10px;
            box-shadow: 0 0 10px #ccc;
        }

        h1 {
            color: green;
        }

        .details {
            text-align: left;
            margin-top: 30px;
        }

        .details p {
            margin: 10px 0;
        }

        .home-btn {
            margin-top: 30px;
            background-color: #004d00;
            color: white;
            padding: 12px 20px;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
        }

        .home-btn:hover {
            background-color: #006600;
        }
    </style>
</head>
<body>

<div class="box">
    <h1>🎉 Order Placed Successfully!</h1>
    <p>Thank you for shopping with us.</p>

    <div class="details">
        <p><strong>Name:</strong> <%= orderDetails.get("customerName") %></p>
        <p><strong>Address:</strong> <%= orderDetails.get("address") %></p>
        <p><strong>Payment Method:</strong> <%= orderDetails.get("paymentMethod") %></p>
        <p><strong>Total Amount:</strong> Rs. <%= orderDetails.get("totalAmount") %></p>
    </div>

    <a class="home-btn" href="home.jsp">Back to Home</a>
</div>

</body>
</html>
