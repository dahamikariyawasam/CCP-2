<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="service.ProductService" %>
<%@ page import="util.SingletonDatabase" %>
<%@ page import="java.util.*" %>

<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Customer Home - SYOS</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #004d00;
            color: white;
            padding: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 20px;
        }

        .user-info {
            margin-left: 20px;
        }

        .logout-btn {
            background: transparent;
            color: white;
            padding: 8px 14px;
            text-decoration: none;
            border: 2px solid white;
            border-radius: 5px;
            font-weight: bold;
            margin-right: 20px;
        }

        .logout-btn:hover {
            background-color: white;
            color: #004d00;
        }

        nav {
            background-color: #e0e0e0;
            padding: 12px 0;
            display: flex;
            justify-content: center;
            gap: 40px;
        }

        nav a {
            text-decoration: none;
            color: #004d00;
            font-weight: bold;
            font-size: 16px;
            padding: 8px 20px;
            border-radius: 6px;
        }

        nav a:hover {
            background-color: #004d00;
            color: white;
        }

        .banner {
            width: 100%;
            height: auto;
            display: block;
        }

        .content {
            padding: 40px;
        }

        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }

        .items {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 30px;
        }

        .card {
            background: white;
            width: 250px;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            text-align: center;
            transition: 0.3s;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 20px rgba(0,0,0,0.15);
        }

        .card h3 {
            margin-top: 10px;
        }

        .price {
            font-size: 18px;
            color: #2e7d32;
            font-weight: bold;
        }

        .discount {
            color: #d32f2f;
            font-size: 14px;
            margin-top: 5px;
        }

        .card button {
            background-color: #004d00;
            color: white;
            padding: 10px 14px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 15px;
        }

        .card button:hover {
            background-color: #006600;
        }
    </style>

    <%
        String cartMsg = (String) session.getAttribute("cartMsg");
        if (cartMsg != null) {
            session.removeAttribute("cartMsg");
        }
    %>

    <script>
        window.onload = function () {
            <% if (cartMsg != null) { %>
            const popup = document.createElement('div');
            popup.textContent = "<%= cartMsg %>";
            popup.style.position = 'fixed';
            popup.style.top = '20px';
            popup.style.right = '20px';
            popup.style.backgroundColor = '#4CAF50';
            popup.style.color = 'white';
            popup.style.padding = '12px 20px';
            popup.style.borderRadius = '8px';
            popup.style.boxShadow = '0 2px 8px rgba(0,0,0,0.3)';
            popup.style.zIndex = '9999';
            popup.style.fontWeight = 'bold';
            document.body.appendChild(popup);

            setTimeout(() => {
                popup.remove();
            }, 3000);
            <% } %>
        };
    </script>

</head>
<body>

<header>
    <div class="user-info">Welcome, <%= username %> 👋</div>
    <a href="login.jsp" class="logout-btn">Logout</a>
</header>

<nav>
    <a href="home.jsp">Home</a>
    <a href="itemcart.jsp">Cart</a>
    <a href="checkout.jsp">Checkout</a>
</nav>

<img src="images/Synex.png" alt="SYOS Banner" class="banner"/>

<div class="content">
    <h2>What would you like?</h2>

    <div class="items">
        <%
            ProductService productService = new ProductService(SingletonDatabase.getInstance());
            List<Map<String, Object>> products = productService.getAllProducts();

            for (Map<String, Object> product : products) {
                String name = (String) product.get("name");
                double price = (Double) product.get("price");
                double discount = (Double) product.get("discount");
        %>
        <div class="card">
            <img src="images/default.jpg" alt="Product Image" width="180" height="130"><br>
            <h3><%= name %></h3>
            <div class="price">Rs. <%= price %></div>
            <div class="discount">
                <%= discount > 0 ? "Discount: " + discount + "%" : "No Discount" %>
            </div>
            <form action="add-to-cart" method="post">
                <input type="hidden" name="name" value="<%= name %>"/>
                <input type="hidden" name="price" value="<%= price %>"/>
                <input type="hidden" name="discount" value="<%= discount %>"/>
                <button type="submit">Add to Cart</button>
            </form>
        </div>
        <% } %>
    </div>
</div>

</body>
</html>
