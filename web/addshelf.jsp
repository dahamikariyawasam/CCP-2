<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add to Product Shelf - SYOS</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 30px;
        }

        h2 {
            color: #004d00;
        }

        form {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px #ccc;
            width: 400px;
            margin-bottom: 20px;
        }

        input, button {
            width: 100%;
            padding: 10px;
            margin-top: 10px;
            box-sizing: border-box;
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

        .message {
            font-weight: bold;
            margin-bottom: 15px;
        }

        .success {
            color: green;
        }

        .error {
            color: red;
        }

        nav {
            background: #004d00;
            color: white;
            padding: 15px;
            display: flex;
            justify-content: space-between;
        }

        nav a {
            color: white;
            text-decoration: none;
            font-weight: bold;
        }
    </style>
</head>
<body>

<nav>
    <span>SYOS Admin</span>
    <a href="admindashboard.jsp">🏠 Dashboard</a>
</nav>

<%
    String success = request.getParameter("success");
    String error = request.getParameter("error");
    if (success != null) {
%>
<div class="message success">✅ <%= success %></div>
<% } else if (error != null) { %>
<div class="message error">❌ <%= error %></div>
<% } %>

<h2>Add Stock to Product Shelf</h2>
<form action="add-to-shelf" method="post">
    Product ID: <input type="number" name="productId" required>
    Product Name: <input type="text" name="productName" required>
    Quantity to Move: <input type="number" name="quantity" required>
    Product Price (Rs.): <input type="number" step="0.01" name="price" required>
    Stock Alert Level: <input type="number" name="alertLevel" required>
    Discount (%): <input type="number" step="0.01" name="discount" value="0.0">
    <button type="submit">📦 Move to Product Shelf</button>
</form>

</body>
</html>
