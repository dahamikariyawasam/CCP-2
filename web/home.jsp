<%@ page contentType="text/html;charset=UTF-8" %>
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
            width: 240px;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px #ccc;
            text-align: center;
        }

        .card h3 {
            margin-top: 0;
        }

        .price {
            font-size: 18px;
            color: green;
        }

        .discount {
            color: red;
        }

        .card button {
            background-color: #004d00;
            color: white;
            padding: 8px 14px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 10px;
        }

        .card button:hover {
            background-color: #006600;
        }
    </style>
</head>
<body>

<header>Welcome to SYOS Customer Portal</header>

<nav>
    <a href="login.jsp">Login</a>
    <a href="createaccount.jsp">Sign Up</a>
    <a href="itemcart.jsp">Cart</a>
    <a href="checkout.jsp">Checkout</a>
</nav>

<div class="content">
    <h2>What would you like?</h2>

    <div class="items">
        <!-- Product 1 -->
        <div class="card">
            <img src="images/apple.jpg" alt="Apples" width="180" height="130"><br>
            <h3>Red Kekulu Rice</h3>
            <div class="price">Rs. 217.00</div>
            <div class="discount">Discount: 10%</div>
            <button>Add to Cart</button>
        </div>

        <!-- Product 2 -->
        <div class="card">
            <img src="images/pineapple.jpg" alt="Pineapples" width="180" height="130"><br>
            <h3>Big Onions</h3>
            <div class="price">Rs. 150.00</div>
            <div class="discount">Discount: 5%</div>
            <button>Add to Cart</button>
        </div>

        <!-- Product 3 -->
        <div class="card">
            <img src="images/nuts.jpg" alt="Cashew" width="180" height="130"><br>
            <h3>Highland Yoghurt</h3>
            <div class="price">Rs. 70.00</div>
            <div class="discount">No Discount</div>
            <button>Add to Cart</button>
        </div>
    </div>
</div>

</body>
</html>