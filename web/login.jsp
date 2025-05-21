<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS - Login</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(to right, #6a11cb, #2575fc);
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            background: white;
            width: 800px;
            height: 400px;
            border-radius: 15px;
            display: flex;
            box-shadow: 0 0 20px rgba(0,0,0,0.2);
            overflow: hidden;
        }

        .left {
            background: linear-gradient(to bottom right, #6a11cb, #2575fc);
            color: white;
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            padding: 40px;
        }

        .left h1 {
            margin-bottom: 20px;
        }

        .left p {
            text-align: center;
            font-size: 14px;
        }

        .right {
            flex: 1;
            padding: 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .right h2 {
            text-align: center;
            margin-bottom: 30px;
            color: #333;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }

        .options {
            display: flex;
            justify-content: space-between;
            font-size: 14px;
            margin-bottom: 20px;
        }

        .options a {
            color: #2575fc;
            text-decoration: none;
        }

        .btn {
            padding: 12px;
            width: 100%;
            background: linear-gradient(to right, #6a11cb, #2575fc);
            color: white;
            font-weight: bold;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
        }

        .signup-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
        }

        .signup-link a {
            color: #2575fc;
            text-decoration: none;
        }

        .error-msg {
            color: red;
            text-align: center;
            font-size: 14px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="left">
        <h1>Welcome to SYOS</h1>
        <p>Your one-stop shop for household essentials. Please login to continue shopping.</p>
    </div>

    <div class="right">
        <h2>User Login</h2>

        <% if (request.getParameter("error") != null) { %>
            <div class="error-msg">Invalid username or password!</div>
        <% } %>

        <form action="<%=request.getContextPath()%>/login" method="post">


            <div class="form-group">
                <input type="text" name="username" placeholder="Username" required />
            </div>

            <div class="form-group">
                <input type="password" name="password" placeholder="Password" required />
            </div>

            <div class="options">
                <label><input type="checkbox" /> Remember me</label>
                <a href="#">Forgot password?</a>
            </div>

            <button type="submit" class="btn">LOGIN</button>
        </form>

        <div class="signup-link">
            Don't have an account? <a href="createaccount.jsp">Sign up</a>
        </div>
    </div>
</div>

</body>
</html>