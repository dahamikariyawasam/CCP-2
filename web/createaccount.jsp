<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>SYOS - Sign Up</title>
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
            width: 850px;
            height: 460px;
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
            flex: 1.2;
            padding: 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .right h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #333;
        }

        .form-group {
            margin-bottom: 18px;
        }

        .form-group input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 8px;
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
            margin-top: 10px;
        }

        .login-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
        }

        .login-link a {
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
        <h1>Create Your SYOS Account</h1>
        <p>Join our customer portal to order groceries, manage your cart, and check out faster.</p>
    </div>

    <div class="right">
        <h2>Sign Up</h2>

        <% if (request.getParameter("error") != null) { %>
            <div class="error-msg">Signup failed. Please try again.</div>
        <% } %>

        <form action="register" method="post">

        <div class="form-group">
                <input type="text" name="name" placeholder="Full Name" required />
            </div>

            <div class="form-group">
                <input type="text" name="username" placeholder="Username" required />
            </div>

            <div class="form-group">
                <input type="password" name="password" placeholder="Password" required />
            </div>

            <div class="form-group">
                <input type="password" name="confirmPassword" placeholder="Confirm Password" required />
            </div>

            <button type="submit" class="btn">Create Account</button>
        </form>

        <div class="login-link">
            Already have an account? <a href="login.jsp">Login here</a>
        </div>
    </div>
</div>

</body>
</html>
