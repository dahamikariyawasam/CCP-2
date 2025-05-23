<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Warehouse Management - SYOS</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background-color: #f4f4f4;
        }

        header {
            background-color: #004d00;
            color: white;
            padding: 20px;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
        }

        nav {
            background-color: #e0e0e0;
            padding: 10px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        nav a {
            color: #004d00;
            text-decoration: none;
            font-weight: bold;
        }

        .container {
            display: flex;
            justify-content: space-around;
            align-items: flex-start;
            padding: 30px;
            gap: 40px;
        }

        form {
            background: #fff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px #bbb;
            width: 100%;
            max-width: 400px;
        }

        h2 {
            color: #004d00;
            margin-top: 0;
            text-align: center;
        }

        input, button {
            width: 100%;
            padding: 10px;
            margin-top: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            border: 1px solid #ccc;
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

        .success {
            color: green;
            font-weight: bold;
            text-align: center;
        }

        .error {
            color: red;
            font-weight: bold;
            text-align: center;
        }
    </style>
</head>
<body>

<header>SYOS Admin Panel</header>

<nav>
    <span>👋 Welcome, Admin</span>
    <a href="admindashboard.jsp">🏠 Admin Dashboard</a>
</nav>

<%-- Show success or error message --%>
<%
    String success = request.getParameter("success");
    String error = request.getParameter("error");
    if (success != null) {
%>
<div class="success">✅ Success: <%= success %></div>
<%
} else if (error != null) {
%>
<div class="error">❌ Error: <%= error %></div>
<%
    }
%>

<div class="container">
    <!-- Add New Item -->
    <form action="warehouse" method="post">
        <h2>Add New Item</h2>
        <input type="hidden" name="action" value="add">
        <label>Product ID:</label>
        <input type="number" name="productId" required>
        <label>Item Name:</label>
        <input type="text" name="itemName" required>
        <label>Batch Date:</label>
        <input type="date" name="batchDate" required>
        <label>Expiry Date:</label>
        <input type="date" name="expiryDate" required>
        <label>Quantity:</label>
        <input type="number" name="quantity" required>
        <label>Warehouse ID:</label>
        <input type="number" name="warehouseId" required>
        <button type="submit">➕ Add Item</button>
    </form>

    <!-- Update Quantity -->
    <form action="warehouse" method="post">
        <h2>Update Quantity</h2>
        <input type="hidden" name="action" value="update">
        <label>Product ID:</label>
        <input type="number" name="productId" required>
        <label>Quantity to Add:</label>
        <input type="number" name="quantity" required>
        <button type="submit">📦 Update Quantity</button>
    </form>
</div>

</body>
</html>
