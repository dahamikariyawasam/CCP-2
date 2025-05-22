<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
    if (cart == null) cart = new ArrayList<>();

    DecimalFormat df = new DecimalFormat("0.00");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Shopping Cart - SYOS</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #004d00;
            color: white;
            padding: 20px;
            text-align: center;
            font-size: 28px;
        }

        nav {
            background-color: #e0e0e0;
            padding: 10px;
            display: flex;
            justify-content: center;
            gap: 20px;
        }

        nav a {
            text-decoration: none;
            color: #004d00;
            font-weight: bold;
            padding: 8px 12px;
            border-radius: 5px;
        }

        nav a:hover {
            background-color: #004d00;
            color: white;
        }

        h1 {
            text-align: center;
            margin: 30px 0 20px;
        }

        table {
            width: 90%;
            margin: 0 auto 30px;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 0 10px #ccc;
        }

        th, td {
            padding: 16px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f2f2f2;
        }

        .total-section {
            display: flex;
            justify-content: flex-end;
            margin: 20px 40px;
        }

        .total-box {
            background-color: white;
            box-shadow: 0 0 10px #ccc;
            padding: 20px;
            width: 300px;
            border-radius: 8px;
        }

        .total-box div {
            margin: 10px 0;
            font-size: 16px;
        }

        .checkout-btn {
            background-color: #007bff;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
        }

        .checkout-btn:hover {
            background-color: #0056b3;
        }

        .delete-link {
            color: red;
            text-decoration: none;
            font-weight: bold;
        }

        .delete-link:hover {
            text-decoration: underline;
        }

        input[type="number"] {
            width: 60px;
        }
    </style>

    <script>
        function updateCart() {
            let rows = document.querySelectorAll("tbody tr");
            let total = 0;

            rows.forEach(row => {
                const price = parseFloat(row.querySelector(".price").dataset.value);
                const discount = parseFloat(row.querySelector(".discount").dataset.value);
                const quantity = parseInt(row.querySelector(".qty").value);

                const discountedPrice = price * (1 - discount / 100);
                const subtotal = discountedPrice * quantity;

                row.querySelector(".subtotal").textContent = "Rs. " + subtotal.toFixed(2);
                total += subtotal;
            });

            document.getElementById("subtotal").textContent = "Rs. " + total.toFixed(2);
            document.getElementById("total").textContent = "Rs. " + total.toFixed(2);
        }
    </script>
</head>
<body>

<header>Shopping Cart - SYOS</header>

<nav>
    <a href="home.jsp">Home</a>
    <a href="checkout.jsp">Checkout</a>
</nav>

<h1>Your Shopping Cart</h1>

<table>
    <thead>
    <tr>
        <th>Product</th>
        <th>Price (Rs.)</th>
        <th>Discount (%)</th>
        <th>Quantity</th>
        <th>Subtotal (Rs.)</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (int i = 0; i < cart.size(); i++) {
            Map<String, Object> item = cart.get(i);
            String name = (String) item.get("name");
            double price = (Double) item.get("price");
            double discount = (Double) item.get("discount");
            int quantity = (int) item.getOrDefault("quantity", 1);

            double discountedPrice = price * (1 - discount / 100);
            double itemSubtotal = discountedPrice * quantity;
    %>
    <tr>
        <td><%= name %></td>
        <td class="price" data-value="<%= price %>"><%= df.format(price) %></td>
        <td class="discount" data-value="<%= discount %>"><%= df.format(discount) %></td>
        <td><input type="number" class="qty" value="<%= quantity %>" min="1" onchange="updateCart()"></td>
        <td class="subtotal">Rs. <%= df.format(itemSubtotal) %></td>
        <td><a class="delete-link" href="RemoveFromCartServlet?index=<%= i %>">Delete</a></td>
    </tr>
    <% } %>
    </tbody>
</table>

<div class="total-section">
    <div class="total-box">
        <div><strong>Subtotal:</strong> <span id="subtotal">Rs. 0.00</span></div>
        <div><strong>Total:</strong> <span id="total">Rs. 0.00</span></div>
        <button class="checkout-btn" onclick="window.location.href='checkout.jsp'">Proceed to Checkout</button>
    </div>
</div>

<script>
    updateCart(); // initialize totals
</script>

</body>
</html>
