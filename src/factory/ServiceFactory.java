package factory;

import util.SingletonDatabase;
import service.ProductService;
import service.AuthService;
import service.InventoryService;
import service.BillService;
import service.UserService;
import service.SalesService;
import service.StockService;


public class ServiceFactory {
    private static ProductService productService;
    private static AuthService authService;
    private static InventoryService inventoryService;
    private static BillService billService;
    private static UserService userService;
    private static SalesService salesService;
    private static StockService stockService;

    private static final SingletonDatabase database = SingletonDatabase.getInstance();

    public static ProductService getProductService() {
        if (productService == null) {
            productService = new ProductService(database);
        }
        return productService;
    }

    public static AuthService getAuthService() {
        if (authService == null) {
            authService = new AuthService(); // ✅ No argument needed
        }
        return authService;
    }

    public static InventoryService getInventoryService() {
        if (inventoryService == null) {
            inventoryService = new InventoryService(database);
        }
        return inventoryService;
    }

    public static BillService getBillService() {
        if (billService == null) {
            billService = new BillService(database);
        }
        return billService;
    }

    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserService(database);
        }
        return userService;
    }

    public static SalesService getSalesService() {
        if (salesService == null) {
            salesService = new SalesService(database);
        }
        return salesService;
    }

    public static StockService getStockService() {
        if (stockService == null) {
            stockService = new StockService(database);
        }
        return stockService;
    }
}
