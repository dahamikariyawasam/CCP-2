<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Shopping Cart - SYOS</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 40px;
        }

        h1 {
            text-align: center;
            margin-bottom: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
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

        img {
            width: 60px;
            height: 60px;
            border-radius: 6px;
        }

        .total-section {
            display: flex;
            justify-content: flex-end;
            margin-top: 20px;
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
    </style>
</head>
<body>

<h1>Shopping Cart</h1>

<table>
    <thead>
        <tr>
            <th>Product</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Subtotal</th>
        </tr>
    </thead>
    <tbody>
        <!-- Row 1 -->
        <tr>
            <td>
                <img src="images/rice.jpg" alt="Rice"><br>
                Red Kekulu Rice
            </td>
            <td>Rs. 217.00</td>
            <td><input type="number" value="1" min="1" style="width: 60px;"></td>
            <td>Rs. 217.00</td>
        </tr>

        <!-- Row 2 -->
        <tr>
            <td>
                <img src="images/onions.jpg" alt="Onions"><br>
                Big Onions
            </td>
            <td>Rs. 150.00</td>
            <td><input type="number" value="1" min="1" style="width: 60px;"></td>
            <td>Rs. 150.00</td>
        </tr>
    </tbody>
</table>

<div class="total-section">
    <div class="total-box">
        <div><strong>Subtotal:</strong> Rs. 367.00</div>
        <div><strong>Total:</strong> Rs. 367.00</div>
        <button class="checkout-btn" onclick="window.location.href='checkout.jsp'">Proceed to Checkout</button>
    </div>
</div>

</body>
</html>
