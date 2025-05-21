<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Checkout</title>
</head>
<body>
    <h2>Checkout</h2>
    <form action="checkout" method="post">
        Total Price: <input type="text" name="totalPrice"><br><br>
        Cash Tendered: <input type="text" name="cash"><br><br>
        <input type="submit" value="Complete Payment">
    </form>
</body>
</html>
