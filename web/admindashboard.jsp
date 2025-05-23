<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="service.ProductService, util.SingletonDatabase" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.PreparedStatement, java.sql.ResultSet" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - SYOS</title>
    <style>
        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background: #f9f9f9;
        }

        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            width: 220px;
            height: 100vh;
            background: linear-gradient(180deg, #006600, #33cc33);
            color: white;
            padding-top: 30px;
        }

        .sidebar h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        .sidebar a {
            display: block;
            padding: 15px 30px;
            color: white;
            text-decoration: none;
            font-weight: bold;
        }

        .sidebar a:hover {
            background-color: rgba(255, 255, 255, 0.1);
        }

        .main {
            margin-left: 220px;
            padding: 40px;
        }

        .main h1 {
            margin-bottom: 30px;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 30px;
            margin-bottom: 50px;
        }

        .card {
            background: linear-gradient(to right, #66bb6a, #43a047);
            color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.2);
            text-align: center;
            cursor: pointer;
            transition: transform 0.2s ease;
        }

        .card:hover {
            transform: scale(1.05);
        }

        .card i {
            font-size: 30px;
            margin-bottom: 10px;
        }

        .card-title {
            font-size: 18px;
            font-weight: bold;
        }

        .stock-section {
            margin-top: 40px;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 8px #ccc;
        }

        .stock-section h2 {
            color: #004d00;
            margin-bottom: 20px;
        }

        .search-bar {
            margin-bottom: 20px;
            display: flex;
            justify-content: flex-end;
        }

        .search-bar input {
            padding: 10px;
            width: 300px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            text-align: center;
        }

        th, td {
            padding: 12px;
            border: 1px solid #ddd;
        }

        th {
            background-color: #004d00;
            color: white;
        }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>SYOS Admin</h2>
    <a href="admindashboard.jsp">Dashboard</a>
    <a href="addshelf.jsp">Add to Product Shelf</a>
    <a href="warehouse.jsp">Add to Warehouse</a>
    <a href="totalsalesreport.jsp">Sales Report</a>
    <a href="totalbillreport.jsp">Bill Report</a>
    <a href="reshelvingreport.jsp">Reshelving Report</a>
    <a href="reorderreport.jsp">Reorder Report</a>
    <hr>
    <a href="login.jsp" style="color: red;">🚪 Logout</a>

</div>

<div class="main">
    <h1>Admin Dashboard</h1>

    <div class="grid">
        <div class="card" onclick="location.href='addshelf.jsp'">
            <i class="fas fa-box"></i>
            <div class="card-title">Add to Product Shelf</div>
        </div>

        <div class="card" onclick="location.href='warehouse.jsp'">
            <i class="fas fa-warehouse"></i>
            <div class="card-title">Add to Warehouse</div>
        </div>

        <div class="card" onclick="location.href='totalsalesreport.jsp'">
            <i class="fas fa-chart-line"></i>
            <div class="card-title">Total Sales Report</div>
        </div>

        <div class="card" onclick="location.href='totalbillreport.jsp'">
            <i class="fas fa-receipt"></i>
            <div class="card-title">Total Bill Report</div>
        </div>

        <div class="card" onclick="location.href='reshelvingreport.jsp'">
            <i class="fas fa-sync-alt"></i>
            <div class="card-title">Reshelving Report</div>
        </div>

        <div class="card" onclick="location.href='reorderreport.jsp'">
            <i class="fas fa-exclamation-triangle"></i>
            <div class="card-title">Reorder Report</div>
        </div>
    </div>

    <div class="stock-section">
        <h2>📦 Stock Report</h2>

        <div class="search-bar">
            <input type="text" id="searchInput" placeholder="Search product name...">
        </div>

        <table id="stockTable">
            <thead>
            <tr>
                <th>Product ID</th>
                <th>Name</th>
                <th>Price (Rs.)</th>
                <th>Discount (%)</th>
                <th>Stock Quantity</th>
            </tr>
            </thead>
            <tbody>
            <%
                ProductService productService = new ProductService(SingletonDatabase.getInstance());
                List<Map<String, Object>> products = productService.getAllProducts();

                for (Map<String, Object> product : products) {
                    int id = (Integer) product.get("id");
                    String name = (String) product.get("name");
                    double price = (Double) product.get("price");
                    double discount = (Double) product.get("discount");

                    // You may add stock_quantity in getAllProducts() if needed
                    String qtyQuery = "SELECT stock_quantity FROM products WHERE id = ?";
                    PreparedStatement stmt = SingletonDatabase.getInstance().getConnection().prepareStatement(qtyQuery);
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    int quantity = rs.next() ? rs.getInt("stock_quantity") : 0;
            %>
            <tr>
                <td><%= id %></td>
                <td><%= name %></td>
                <td>Rs. <%= price %></td>
                <td><%= discount %></td>
                <td><%= quantity %></td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<!-- Font Awesome -->
<script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>

<!-- Filter Script -->
<script>
    const searchInput = document.getElementById("searchInput");
    searchInput.addEventListener("keyup", function () {
        const filter = searchInput.value.toLowerCase();
        const rows = document.querySelectorAll("#stockTable tbody tr");

        rows.forEach(row => {
            const name = row.cells[1].textContent.toLowerCase();
            row.style.display = name.includes(filter) ? "" : "none";
        });
    });
</script>

</body>
</html>
