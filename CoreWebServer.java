import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CoreWebServer {

   
    static String[][] inventory = {
        // --- STUDENT (20k to 1.2L) ---
        {"Asus", "Chromebook CX1", "Student", "20000", "Intel Celeron N4020", "4GB", "128GB eMMC", "11.6-inch HD"},
        {"Acer", "Extensa 15", "Student", "22000", "Intel Celeron N4500", "8GB", "256GB SSD", "15.6-inch HD"},
        {"Lenovo", "IdeaPad 1", "Student", "25000", "AMD Athlon Silver", "8GB", "256GB SSD", "14-inch HD"},
        {"HP", "15s Basic", "Student", "28000", "Intel Core i3-1115G4", "8GB", "256GB SSD", "15.6-inch FHD"},
        {"Dell", "Inspiron 3511", "Student", "32000", "Intel Core i3-1115G4", "8GB", "512GB SSD", "15.6-inch FHD"},
        {"Asus", "Vivobook 15", "Student", "35000", "Intel Core i3-1215U", "8GB", "512GB SSD", "15.6-inch FHD"},
        {"Lenovo", "IdeaPad Slim 3", "Student", "38000", "AMD Ryzen 3 7320U", "8GB", "512GB SSD", "15.6-inch FHD"},
        {"Acer", "Aspire 5", "Student", "40000", "Intel Core i5-1135G7", "8GB", "512GB SSD", "14-inch FHD"},
        {"HP", "14s Advanced", "Student", "42000", "AMD Ryzen 5 5500U", "8GB", "512GB SSD", "14-inch FHD"},
        {"Samsung", "Galaxy Book Go", "Student", "45000", "Snapdragon 7c", "4GB", "128GB eMMC", "14-inch FHD"},
        {"Dell", "Inspiron 14", "Student", "48000", "Intel Core i5-1235U", "8GB", "512GB SSD", "14-inch FHD"},
        {"Asus", "Vivobook 16X", "Student", "52000", "AMD Ryzen 5 5600H", "8GB", "512GB SSD", "16-inch WUXGA"},
        {"Lenovo", "IdeaPad Flex 5", "Student", "58000", "Intel Core i5-1235U", "16GB", "512GB SSD", "14-inch Touch"},
        {"HP", "Pavilion 14", "Student", "62000", "Intel Core i5-1240P", "16GB", "512GB SSD", "14-inch FHD"},
        {"Acer", "Swift 3", "Student", "65000", "Intel Core i5-1240P", "16GB", "512GB SSD", "14-inch QHD"},
        {"Samsung", "Galaxy Book 2", "Student", "68000", "Intel Core i5-1235U", "8GB", "512GB SSD", "15.6-inch FHD"},
        {"Dell", "Inspiron 16", "Student", "72000", "Intel Core i5-1335U", "16GB", "512GB SSD", "16-inch FHD+"},
        {"Lenovo", "Yoga 6", "Student", "78000", "AMD Ryzen 7 7730U", "16GB", "512GB SSD", "13.3-inch Touch"},
        {"Apple", "MacBook Air M1", "Student", "85000", "Apple M1", "8GB", "256GB SSD", "13.3-inch Retina"},
        {"Asus", "Zenbook 14X", "Student", "90000", "Intel Core i5-13500H", "16GB", "512GB SSD", "14-inch OLED"},
        {"HP", "Envy x360", "Student", "95000", "Intel Core i5-1335U", "16GB", "512GB SSD", "15.6-inch Touch"},
        {"Apple", "MacBook Air M2", "Student", "105000", "Apple M2", "8GB", "256GB SSD", "13.6-inch Liquid Retina"},
        {"Dell", "XPS 13", "Student", "115000", "Intel Core i5-1230U", "16GB", "512GB SSD", "13.4-inch FHD+"},
        {"Samsung", "Galaxy Book 3", "Student", "118000", "Intel Core i7-1355U", "16GB", "512GB SSD", "15.6-inch AMOLED"},
        {"Apple", "MacBook Air M3", "Student", "125000", "Apple M3", "16GB", "512GB SSD", "15.3-inch Liquid Retina"},

        // --- BUSINESS (30k to 3.5L) ---
        {"Lenovo", "ThinkBook 15", "Business", "35000", "Intel Core i3-1115G4", "8GB", "256GB SSD", "15.6-inch FHD"},
        {"HP", "ProBook 440 G8", "Business", "42000", "Intel Core i5-1135G7", "8GB", "512GB SSD", "14-inch FHD"},
        {"Dell", "Vostro 3420", "Business", "46000", "Intel Core i5-1235U", "8GB", "512GB SSD", "14-inch FHD"},
        {"Asus", "ExpertBook B1", "Business", "49000", "Intel Core i5-1235U", "8GB", "512GB SSD", "14-inch FHD"},
        {"Acer", "TravelMate P2", "Business", "53000", "Intel Core i5-1240P", "16GB", "512GB SSD", "14-inch FHD"},
        {"Lenovo", "ThinkPad E14", "Business", "58000", "AMD Ryzen 5 7530U", "16GB", "512GB SSD", "14-inch FHD"},
        {"HP", "ProBook 450 G9", "Business", "65000", "Intel Core i5-1235U", "16GB", "512GB SSD", "15.6-inch FHD"},
        {"Dell", "Latitude 3540", "Business", "72000", "Intel Core i5-1335U", "16GB", "512GB SSD", "15.6-inch FHD"},
        {"Microsoft", "Surface Laptop Go 2", "Business", "75000", "Intel Core i5-1135G7", "8GB", "256GB SSD", "12.4-inch PixelSense"},
        {"Lenovo", "ThinkPad L14", "Business", "82000", "Intel Core i5-1335U", "16GB", "512GB SSD", "14-inch FHD"},
        {"HP", "EliteBook 640 G9", "Business", "88000", "Intel Core i7-1255U", "16GB", "512GB SSD", "14-inch FHD"},
        {"Asus", "ExpertBook B5", "Business", "95000", "Intel Core i7-1260P", "16GB", "1TB SSD", "14-inch OLED"},
        {"Dell", "Latitude 5440", "Business", "105000", "Intel Core i7-1355U", "16GB", "512GB SSD", "14-inch FHD"},
        {"Microsoft", "Surface Laptop 5", "Business", "112000", "Intel Core i5-1235U", "16GB", "512GB SSD", "13.5-inch PixelSense"},
        {"Lenovo", "ThinkPad T14", "Business", "120000", "Intel Core i7-1355U", "16GB", "1TB SSD", "14-inch WUXGA"},
        {"HP", "EliteBook 840 G10", "Business", "135000", "Intel Core i7-1355U", "16GB", "1TB SSD", "14-inch WUXGA"},
        {"Apple", "MacBook Pro 14", "Business", "155000", "Apple M3", "8GB", "512GB SSD", "14.2-inch Liquid Retina"},
        {"Dell", "XPS 14", "Business", "165000", "Intel Core Ultra 7", "16GB", "1TB SSD", "14-inch OLED"},
        {"Microsoft", "Surface Laptop 6", "Business", "175000", "Intel Core Ultra 7", "16GB", "1TB SSD", "15-inch PixelSense"},
        {"Lenovo", "ThinkPad X1 Carbon", "Business", "190000", "Intel Core i7-1365U", "32GB", "1TB SSD", "14-inch OLED"},
        {"HP", "Dragonfly G4", "Business", "210000", "Intel Core i7-1365U", "32GB", "1TB SSD", "13.5-inch WUXGA+"},
        {"Apple", "MacBook Pro 14", "Business", "240000", "Apple M3 Pro", "18GB", "1TB SSD", "14.2-inch Liquid Retina"},
        {"Dell", "Latitude 9440", "Business", "260000", "Intel Core i7-1365U", "32GB", "1TB SSD", "14-inch QHD+ Touch"},
        {"Lenovo", "ThinkPad X1 Yoga", "Business", "280000", "Intel Core i7-1365U", "32GB", "2TB SSD", "14-inch OLED Touch"},
        {"Apple", "MacBook Pro 16", "Business", "350000", "Apple M3 Max", "36GB", "1TB SSD", "16.2-inch Liquid Retina XDR"},

        // --- GAMING (50k to 6L) ---
        {"MSI", "GF63 Thin", "Gaming", "52000", "Intel Core i5-11400H", "8GB", "512GB SSD", "GTX 1650 4GB"},
        {"Acer", "Nitro 5", "Gaming", "58000", "AMD Ryzen 5 5600H", "8GB", "512GB SSD", "RTX 3050 4GB"},
        {"Lenovo", "IdeaPad Gaming 3", "Gaming", "62000", "Intel Core i5-12450H", "8GB", "512GB SSD", "RTX 3050 4GB"},
        {"Asus", "TUF Gaming F15", "Gaming", "68000", "Intel Core i5-11400H", "16GB", "512GB SSD", "RTX 3050 Ti"},
        {"HP", "Victus 15", "Gaming", "74000", "AMD Ryzen 7 5800H", "16GB", "512GB SSD", "RTX 3050 Ti"},
        {"MSI", "Cyborg 15", "Gaming", "80000", "Intel Core i7-12650H", "16GB", "512GB SSD", "RTX 4050 6GB"},
        {"Lenovo", "LOQ 15", "Gaming", "85000", "Intel Core i5-13420H", "16GB", "512GB SSD", "RTX 4050 6GB"},
        {"Acer", "Nitro V 15", "Gaming", "90000", "Intel Core i5-13420H", "16GB", "1TB SSD", "RTX 4050 6GB"},
        {"Dell", "G15", "Gaming", "95000", "Intel Core i7-12700H", "16GB", "512GB SSD", "RTX 3060 6GB"},
        {"Asus", "TUF Gaming A15", "Gaming", "105000", "AMD Ryzen 7 7735HS", "16GB", "1TB SSD", "RTX 4060 8GB"},
        {"HP", "Omen 16", "Gaming", "115000", "AMD Ryzen 7 7840HS", "16GB", "1TB SSD", "RTX 4060 8GB"},
        {"Lenovo", "Legion Slim 5", "Gaming", "125000", "AMD Ryzen 7 7840HS", "16GB", "1TB SSD", "RTX 4060 8GB"},
        {"Acer", "Predator Helios Neo", "Gaming", "135000", "Intel Core i7-13700HX", "16GB", "1TB SSD", "RTX 4060 8GB"},
        {"MSI", "Katana 15", "Gaming", "145000", "Intel Core i7-13620H", "16GB", "1TB SSD", "RTX 4070 8GB"},
        {"Asus", "ROG Zephyrus G14", "Gaming", "160000", "AMD Ryzen 9 7940HS", "16GB", "1TB SSD", "RTX 4060 8GB"},
        {"Dell", "Alienware m16", "Gaming", "180000", "Intel Core i7-13700HX", "16GB", "1TB SSD", "RTX 4070 8GB"},
        {"Lenovo", "Legion Pro 5i", "Gaming", "195000", "Intel Core i9-13900HX", "32GB", "1TB SSD", "RTX 4070 8GB"},
        {"Asus", "ROG Strix G16", "Gaming", "215000", "Intel Core i9-13980HX", "16GB", "1TB SSD", "RTX 4070 8GB"},
        {"Razer", "Blade 14", "Gaming", "240000", "AMD Ryzen 9 7940HS", "16GB", "1TB SSD", "RTX 4070 8GB"},
        {"MSI", "Vector GP68", "Gaming", "265000", "Intel Core i9-13980HX", "32GB", "1TB SSD", "RTX 4080 12GB"},
        {"Acer", "Predator Helios 16", "Gaming", "290000", "Intel Core i9-13900HX", "32GB", "2TB SSD", "RTX 4080 12GB"},
        {"Lenovo", "Legion Pro 7i", "Gaming", "320000", "Intel Core i9-13900HX", "32GB", "2TB SSD", "RTX 4080 12GB"},
        {"Dell", "Alienware x16", "Gaming", "360000", "Intel Core i9-13900HK", "32GB", "2TB SSD", "RTX 4080 12GB"},
        {"Razer", "Blade 16", "Gaming", "450000", "Intel Core i9-13950HX", "32GB", "2TB SSD", "RTX 4090 16GB"},
        {"MSI", "Titan GT77 HX", "Gaming", "580000", "Intel Core i9-13980HX", "64GB", "4TB SSD", "RTX 4090 16GB"},

        // --- CREATOR / WORKSTATION (60k to 9.5L) ---
        {"Asus", "Vivobook Pro 15", "Creator", "60000", "AMD Ryzen 5 5600H", "16GB", "512GB SSD", "GTX 1650 4GB"},
        {"Lenovo", "IdeaPad Pro 5", "Creator", "78000", "Intel Core i5-13500H", "16GB", "512GB SSD", "2.5K Display"},
        {"HP", "Envy 16", "Creator", "95000", "Intel Core i7-13700H", "16GB", "512GB SSD", "Intel Arc A370M"},
        {"Acer", "Swift X 14", "Creator", "110000", "Intel Core i7-13700H", "16GB", "1TB SSD", "RTX 3050 6GB"},
        {"Asus", "Zenbook Pro 15", "Creator", "125000", "AMD Ryzen 7 6800H", "16GB", "1TB SSD", "RTX 3050 Ti OLED"},
        {"Dell", "Inspiron 16 Plus", "Creator", "135000", "Intel Core i7-13700H", "16GB", "1TB SSD", "RTX 4050 6GB"},
        {"Lenovo", "Yoga Pro 7", "Creator", "148000", "Intel Core i7-13700H", "16GB", "1TB SSD", "RTX 4050 6GB"},
        {"Apple", "MacBook Pro 14", "Creator", "169000", "Apple M3", "16GB", "512GB SSD", "14.2 Liquid Retina XDR"},
        {"MSI", "Creator M16", "Creator", "185000", "Intel Core i7-13700H", "32GB", "1TB SSD", "RTX 4060 8GB"},
        {"Asus", "ProArt Studiobook 16", "Creator", "205000", "Intel Core i7-13700H", "32GB", "1TB SSD", "RTX 4060 8GB OLED"},
        {"Apple", "MacBook Pro 16", "Creator", "249000", "Apple M3 Pro", "18GB", "512GB SSD", "16.2 Liquid Retina XDR"},
        {"Dell", "XPS 15", "Creator", "265000", "Intel Core i9-13900H", "32GB", "1TB SSD", "RTX 4070 8GB OLED"},
        {"HP", "ZBook Studio G10", "Creator", "285000", "Intel Core i7-13700H", "32GB", "1TB SSD", "RTX 2000 Ada"},
        {"Lenovo", "ThinkPad P1 Gen 6", "Creator", "310000", "Intel Core i7-13700H", "32GB", "1TB SSD", "RTX 3000 Ada"},
        {"Microsoft", "Surface Laptop Studio 2", "Creator", "340000", "Intel Core i7-13700H", "64GB", "2TB SSD", "RTX 4060 8GB"},
        {"Dell", "XPS 17", "Creator", "365000", "Intel Core i9-13900H", "64GB", "2TB SSD", "RTX 4080 12GB"},
        {"Apple", "MacBook Pro 16", "Creator", "399000", "Apple M3 Max", "48GB", "1TB SSD", "16.2 Liquid Retina XDR"},
        {"Asus", "Zenbook Pro 16X", "Creator", "420000", "Intel Core i9-13905H", "32GB", "2TB SSD", "RTX 4080 12GB"},
        {"HP", "ZBook Fury 16 G10", "Creator", "460000", "Intel Core i9-13950HX", "64GB", "2TB SSD", "RTX 4000 Ada"},
        {"Lenovo", "ThinkPad P16 Gen 2", "Creator", "520000", "Intel Core i9-13980HX", "64GB", "2TB SSD", "RTX 4000 Ada"},
        {"Dell", "Precision 7780", "Creator", "600000", "Intel Core i9-13950HX", "64GB", "4TB SSD", "RTX 5000 Ada"},
        {"Apple", "MacBook Pro 16 Maxed", "Creator", "720000", "Apple M3 Max 40-core", "128GB", "8TB SSD", "16.2 Liquid Retina XDR"},
        {"HP", "ZBook Fury 16 Max", "Creator", "800000", "Intel Core i9-13950HX", "128GB", "4TB SSD", "RTX 5000 Ada"},
        {"Lenovo", "ThinkPad P16 Ultimate", "Creator", "880000", "Intel Core i9-13980HX", "128GB", "8TB SSD", "RTX 5000 Ada"},
        {"Dell", "Alienware m18 Workstation", "Creator", "950000", "Intel Core i9-13980HX", "64GB", "8TB SSD", "RTX 4090 16GB"}
    };

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange t) throws IOException {
                byte[] response = Files.readAllBytes(Paths.get("index.html"));
                t.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                t.sendResponseHeaders(200, response.length);
                OutputStream os = t.getResponseBody();
                os.write(response);
                os.close();
            }
        });

        server.createContext("/recommend", new HttpHandler() {
            @Override
            public void handle(HttpExchange t) throws IOException {
                InputStream is = t.getRequestBody();
                String rawBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                String query = URLDecoder.decode(rawBody, StandardCharsets.UTF_8.toString());
                
                String[] params = query.split("&");
                double userBudget = 0;
                String userCategory = "All";
                String userBrand = "";
                String sortOrder = "low";

                for (String param : params) {
                    String[] pair = param.split("=");
                    if (pair.length > 1) {
                        if (pair[0].equals("budget")) userBudget = Double.parseDouble(pair[1]);
                        if (pair[0].equals("category")) userCategory = pair[1];
                        if (pair[0].equals("brand")) userBrand = pair[1].toLowerCase().trim();
                        if (pair[0].equals("sort")) sortOrder = pair[1];
                    }
                }

                List<String[]> filteredList = new ArrayList<>();
                double totalPrice = 0, minPrice = Double.MAX_VALUE, maxPrice = 0;
                int bestValueIndex = -1;
                double bestRatio = 0;

                for (int i = 0; i < inventory.length; i++) {
                    double price = Double.parseDouble(inventory[i][3]);
                    String brand = inventory[i][0].toLowerCase();
                    String category = inventory[i][2];

                    if (price <= userBudget && (userCategory.equals("All") || category.equalsIgnoreCase(userCategory)) && (userBrand.isEmpty() || brand.contains(userBrand))) {
                        filteredList.add(inventory[i]);
                        totalPrice += price;
                        if (price < minPrice) minPrice = price;
                        if (price > maxPrice) maxPrice = price;

                        int ram = Integer.parseInt(inventory[i][5].replace("GB", ""));
                        double ratio = ram / price;
                        if (ratio > bestRatio) {
                            bestRatio = ratio;
                            bestValueIndex = filteredList.size() - 1;
                        }
                    }
                }

                if (sortOrder.equals("low")) {
                    filteredList.sort((a, b) -> Double.compare(Double.parseDouble(a[3]), Double.parseDouble(b[3])));
                } else {
                    filteredList.sort((a, b) -> Double.compare(Double.parseDouble(b[3]), Double.parseDouble(a[3])));
                }

                StringBuilder html = new StringBuilder();
                html.append("<html><head><style>");
                html.append("body { font-family:'-apple-system', BlinkMacSystemFont, 'Segoe UI', sans-serif; background-color:#f5f7fa; color:#1d1d1f; text-align:center; padding:40px; margin:0;}");
                
                html.append(".stats-bar { background:#ffffff; padding:20px; border-radius:16px; margin-bottom:30px; box-shadow:0 4px 15px rgba(0,0,0,0.05); display:inline-block; border:1px solid #e2e8f0;}");
                html.append(".stat-item { display:inline-block; padding:0 20px; border-right:1px solid #e2e8f0; }");
                html.append(".stat-item:last-child { border:none; }");
                
                html.append(".card { background:#ffffff; padding:25px; margin:15px; border-radius:20px; display:inline-block; text-align:left; width: 300px; box-shadow: 0 10px 25px rgba(0,0,0,0.08); border:1px solid #e2e8f0; vertical-align: top; transition: transform 0.2s;}");
                html.append(".card:hover { transform: translateY(-5px); box-shadow: 0 15px 35px rgba(0,0,0,0.12);}");
                
                html.append(".tag { background:#f0f7ff; color:#0071e3; padding:6px 12px; border-radius:20px; font-size:12px; font-weight:700;}");
                html.append(".best-value { background:#fff5f5; color:#e53e3e; padding:6px 12px; border-radius:20px; font-size:12px; font-weight:700; margin-left: 5px;}");
                html.append(".specs-box { background:#fbfbfd; padding:15px; border-radius:12px; margin-bottom: 15px; border:1px solid #f0f0f0;}");
                
                html.append(".btn { background:#0071e3; color:#ffffff; font-weight:600; padding:14px; width:100%; border:none; border-radius:12px; cursor:pointer; margin-top:15px; font-size:15px; transition: background 0.2s;}");
                html.append(".btn:hover { background:#0077ed; }");
                html.append(".btn-outline { background:#ffffff; color:#0071e3; border:2px solid #0071e3; margin-top:10px; }");
                html.append(".btn-outline:hover { background:#f0f7ff; }");
                
                html.append(".modal { display:none; position:fixed; z-index:100; left:0; top:0; width:100%; height:100%; background-color:rgba(255,255,255,0.8); backdrop-filter: blur(10px); }");
                html.append(".modal-content { background-color:#ffffff; margin:5% auto; padding:40px; border-radius:24px; width:500px; text-align:left; box-shadow: 0 30px 60px rgba(0,0,0,0.2); border: 1px solid #e2e8f0; }");
                html.append(".close { color:#86868b; float:right; font-size:28px; font-weight:bold; cursor:pointer; transition: color 0.2s; }");
                html.append(".close:hover { color:#1d1d1f; }");
                html.append(".config-section { background:#fbfbfd; padding:15px; border-radius:12px; margin-bottom:15px; border:1px solid #f0f0f0;}");
                
                // CSS For The Printable E-Commerce Invoice
                html.append(".invoice-box { display:none; position:absolute; z-index:200; left:0; top:0; width:100%; min-height:100%; background:#f5f7fa; padding:40px; box-sizing:border-box;}");
                html.append(".invoice-card { background:#ffffff; max-width:700px; margin:0 auto; padding:50px; border-radius:16px; box-shadow:0 15px 40px rgba(0,0,0,0.1); border-top: 8px solid #0071e3; text-align:left;}");
                html.append(".invoice-header { display:flex; justify-content:space-between; border-bottom: 2px solid #e2e8f0; padding-bottom: 20px; margin-bottom: 30px; }");
                html.append(".invoice-table { width:100%; border-collapse:collapse; margin-bottom: 30px; }");
                html.append(".invoice-table th { text-align:left; padding:12px; border-bottom:2px solid #e2e8f0; color:#86868b; }");
                html.append(".invoice-table td { padding:15px 12px; border-bottom:1px solid #f0f0f0; color:#1d1d1f; font-weight:600; }");
                html.append(".invoice-totals { width:50%; margin-left:auto; border-top:2px solid #e2e8f0; padding-top:20px;}");
                html.append(".total-row { display:flex; justify-content:space-between; margin-bottom:10px; color:#515154; font-size:16px;}");
                html.append(".grand-total { display:flex; justify-content:space-between; margin-top:15px; color:#0071e3; font-size:22px; font-weight:800; border-top: 2px dashed #0071e3; padding-top:15px;}");
                
                // Magic CSS that hides the website background when the user clicks Print
                html.append("@media print { body * { visibility: hidden; } .invoice-card, .invoice-card * { visibility: visible; } .invoice-card { position: absolute; left: 0; top: 0; width:100%; box-shadow:none; } .no-print { display: none !important; } }");

                html.append("</style></head><body>");

                html.append("<h2 style='font-size:32px; margin-bottom:10px; color:#1d1d1f;'>Inventory Results</h2>");
                
                if (filteredList.isEmpty()) {
                    html.append("<p style='color:#e53e3e; font-size: 18px;'>No laptops match. Try adjusting your filters or increasing your budget to view the premium ranges.</p>");
                } else {
                    double avgPrice = totalPrice / filteredList.size();
                    html.append("<div class='stats-bar'>");
                    html.append("<div class='stat-item'><span style='color:#86868b; font-size:12px;'>FOUND</span><br><b style='color:#1d1d1f;'>").append(filteredList.size()).append(" Laptops</b></div>");
                    html.append("<div class='stat-item'><span style='color:#86868b; font-size:12px;'>AVERAGE PRICE</span><br><b style='color:#1d1d1f;'>Rs. ").append(Math.round(avgPrice)).append("</b></div>");
                    html.append("<div class='stat-item'><span style='color:#86868b; font-size:12px;'>LOWEST</span><br><b style='color:#0071e3;'>Rs. ").append(Math.round(minPrice)).append("</b></div>");
                    html.append("</div><br>");

                    for (int i = 0; i < filteredList.size(); i++) {
                        String[] laptop = filteredList.get(i);
                        double price = Double.parseDouble(laptop[3]);
                        String fullName = laptop[0] + " " + laptop[1];
                        
                        int stockLeft = Math.abs(fullName.hashCode()) % 6 + 1; 

                        html.append("<div class='card'>");
                        html.append("<span class='tag'>").append(laptop[2]).append("</span>");
                        
                        if (i == bestValueIndex) {
                            html.append("<span class='best-value'>👑 Best Value</span>");
                        }
                        
                        html.append("<h3 style='margin:15px 0 10px 0; color:#1d1d1f;'>").append(fullName).append("</h3>");
                        
                        html.append("<div class='specs-box'>");
                        html.append("<p style='margin:4px 0; font-size:13px; color:#515154;'>⚙️ <b>CPU:</b> ").append(laptop[4]).append("</p>");
                        html.append("<p style='margin:4px 0; font-size:13px; color:#515154;'>🧠 <b>RAM:</b> ").append(laptop[5]).append("</p>");
                        html.append("<p style='margin:4px 0; font-size:13px; color:#515154;'>💾 <b>Storage:</b> ").append(laptop[6]).append("</p>");
                        html.append("<p style='margin:4px 0; font-size:13px; color:#515154;'>🖥️ <b>Display:</b> ").append(laptop[7]).append("</p>");
                        html.append("</div>");
                        
                        html.append("<div style='display:flex; justify-content:space-between; align-items:center;'>");
                        html.append("<p style='margin: 0; font-size: 22px; font-weight:700; color:#1d1d1f;'>Rs. ").append(price).append("</p>");
                        html.append("<p style='margin: 0; font-size: 12px; color:#e53e3e; font-weight:bold;'>Only ").append(stockLeft).append(" left</p>");
                        html.append("</div>");
                        
                        html.append("<button class='btn' onclick='openConfigurator(\"").append(fullName).append("\", ").append(price).append(")'>Configure & Order</button>");
                        html.append("</div>");
                    }
                }

                html.append("<br><br><a href='/' style='color:#0071e3; text-decoration:none; font-weight:bold;'>⬅ Back to Search</a>");

                // --- BRIGHT MODAL POPUP ---
                html.append("<div id='configModal' class='modal'>");
                html.append("<div class='modal-content'>");
                html.append("<span class='close' onclick='closeConfigurator()'>&times;</span>");
                html.append("<h2 id='modalTitle' style='margin-top:0; color:#1d1d1f;'>Configure System</h2>");
                html.append("<p id='modalBasePrice' style='color:#86868b; border-bottom: 1px solid #e2e8f0; padding-bottom: 15px;'>Base Price: Rs. 0</p>");
                
                html.append("<div class='config-section'>");
                html.append("<h4 style='margin:0 0 10px 0; color:#1d1d1f;'>🧠 Memory (RAM)</h4>");
                // Added data-label so the invoice knows exactly what the user bought
                html.append("<label style='display:block; margin:8px 0; color:#1d1d1f;'><input type='radio' name='ram' value='0' data-label='Base RAM' checked onchange='calculateTotal()'> Keep Base RAM (Included)</label>");
                html.append("<label style='display:block; margin:8px 0; color:#1d1d1f;'><input type='radio' name='ram' value='8000' data-label='+16GB Memory Upgrade' onchange='calculateTotal()'> Upgrade to +16GB (+ Rs. 8,000)</label>");
                html.append("<label style='display:block; margin:8px 0; color:#1d1d1f;'><input type='radio' name='ram' value='16000' data-label='+32GB Memory Upgrade' onchange='calculateTotal()'> Upgrade to +32GB (+ Rs. 16,000)</label>");
                html.append("</div>");

                html.append("<div class='config-section'>");
                html.append("<h4 style='margin:0 0 10px 0; color:#1d1d1f;'>💾 Storage (SSD)</h4>");
                html.append("<label style='display:block; margin:8px 0; color:#1d1d1f;'><input type='radio' name='storage' value='0' data-label='Base SSD' checked onchange='calculateTotal()'> Keep Base Storage (Included)</label>");
                html.append("<label style='display:block; margin:8px 0; color:#1d1d1f;'><input type='radio' name='storage' value='8000' data-label='1TB SSD Upgrade' onchange='calculateTotal()'> Upgrade to 1TB SSD (+ Rs. 8,000)</label>");
                html.append("<label style='display:block; margin:8px 0; color:#1d1d1f;'><input type='radio' name='storage' value='18000' data-label='2TB NVMe SSD Upgrade' onchange='calculateTotal()'> Upgrade to 2TB SSD (+ Rs. 18,000)</label>");
                html.append("</div>");

                html.append("<h2 id='modalFinalPrice' style='color:#0071e3; margin-top:20px;'>Final Price: Rs. 0</h2>");
                
                html.append("<button class='btn' onclick='generateInvoice()'>🧾 Generate Official Bill</button>");
                html.append("</div></div>");

                // --- THE NEW BILL / INVOICE HTML ---
                html.append("<div id='invoiceView' class='invoice-box'>");
                html.append("<div class='invoice-card'>");
                
                html.append("<div class='invoice-header'>");
                html.append("<div><h1 style='color:#1d1d1f; margin:0;'>TechFinder Pro</h1><p style='color:#86868b; margin:5px 0 0 0;'>Official Tax Invoice</p></div>");
                html.append("<div style='text-align:right;'><b style='color:#1d1d1f;'>Order ID: <span id='invOrder'></span></b><br><span style='color:#86868b;'>Date: <span id='invDate'></span></span></div>");
                html.append("</div>");

                html.append("<table class='invoice-table'>");
                html.append("<tr><th>Description</th><th style='text-align:right;'>Amount (INR)</th></tr>");
                html.append("<tr><td><b><span id='invItemName'></span></b><br><span style='color:#86868b; font-size:13px;'>Base Configuration</span></td><td style='text-align:right;'>Rs. <span id='invBasePrice'></span></td></tr>");
                html.append("<tr><td><span id='invRamName'></span></td><td style='text-align:right;'>Rs. <span id='invRamPrice'></span></td></tr>");
                html.append("<tr><td><span id='invStorageName'></span></td><td style='text-align:right;'>Rs. <span id='invStoragePrice'></span></td></tr>");
                html.append("</table>");

                html.append("<div class='invoice-totals'>");
                html.append("<div class='total-row'><span>Subtotal:</span> <span>Rs. <span id='invSubtotal'></span></span></div>");
                html.append("<div class='total-row'><span>Estimated GST (18%):</span> <span>Rs. <span id='invTax'></span></span></div>");
                html.append("<div class='grand-total'><span>GRAND TOTAL:</span> <span>Rs. <span id='invGrandTotal'></span></span></div>");
                html.append("</div>");

                html.append("<div class='no-print' style='margin-top:40px; text-align:center;'>");
                html.append("<button class='btn' style='width:auto; padding: 12px 30px; margin-right:15px;' onclick='window.print()'>🖨️ Print / Save as PDF</button>");
                html.append("<button class='btn btn-outline' style='width:auto; padding: 12px 30px;' onclick='closeInvoice()'>Close</button>");
                html.append("</div>");

                html.append("</div></div>");

                // --- JAVASCRIPT LOGIC ---
                html.append("<script>");
                html.append("let currentBasePrice = 0;");
                html.append("function openConfigurator(laptopName, basePrice) {");
                html.append("  currentBasePrice = basePrice;");
                html.append("  document.getElementById('modalTitle').innerText = laptopName;");
                html.append("  document.getElementById('modalBasePrice').innerText = 'Base Price: Rs. ' + basePrice.toLocaleString();");
                html.append("  document.getElementById('configModal').style.display = 'block';");
                html.append("  calculateTotal();");
                html.append("}");
                html.append("function closeConfigurator() { document.getElementById('configModal').style.display = 'none'; }");
                
                html.append("function calculateTotal() {");
                html.append("  let total = currentBasePrice;");
                html.append("  document.querySelectorAll('input[type=\"radio\"]:checked').forEach(radio => { total += parseInt(radio.value); });");
                html.append("  document.getElementById('modalFinalPrice').innerText = 'Final Price: Rs. ' + total.toLocaleString();");
                html.append("}");

                // INVOICE GENERATOR LOGIC
                html.append("function generateInvoice() {");
                html.append("  document.getElementById('configModal').style.display = 'none';");
                html.append("  document.getElementById('invoiceView').style.display = 'block';");
                
                html.append("  let orderId = 'ORD-' + Math.floor(Math.random() * 90000 + 10000) + '-IN';");
                html.append("  document.getElementById('invOrder').innerText = orderId;");
                html.append("  document.getElementById('invDate').innerText = new Date().toLocaleDateString();");
                
                html.append("  let laptopName = document.getElementById('modalTitle').innerText;");
                html.append("  let ramRadio = document.querySelector('input[name=\"ram\"]:checked');");
                html.append("  let storageRadio = document.querySelector('input[name=\"storage\"]:checked');");
                
                html.append("  let ramPrice = parseInt(ramRadio.value);");
                html.append("  let storagePrice = parseInt(storageRadio.value);");
                html.append("  let subtotal = currentBasePrice + ramPrice + storagePrice;");
                html.append("  let tax = Math.round(subtotal * 0.18);");
                html.append("  let grandTotal = subtotal + tax;");
                
                html.append("  document.getElementById('invItemName').innerText = laptopName;");
                html.append("  document.getElementById('invBasePrice').innerText = currentBasePrice.toLocaleString();");
                html.append("  document.getElementById('invRamName').innerText = ramRadio.getAttribute('data-label');");
                html.append("  document.getElementById('invRamPrice').innerText = ramPrice.toLocaleString();");
                html.append("  document.getElementById('invStorageName').innerText = storageRadio.getAttribute('data-label');");
                html.append("  document.getElementById('invStoragePrice').innerText = storagePrice.toLocaleString();");
                
                html.append("  document.getElementById('invSubtotal').innerText = subtotal.toLocaleString();");
                html.append("  document.getElementById('invTax').innerText = tax.toLocaleString();");
                html.append("  document.getElementById('invGrandTotal').innerText = grandTotal.toLocaleString();");
                html.append("}");
                
                html.append("function closeInvoice() { document.getElementById('invoiceView').style.display = 'none'; }");
                
                html.append("</script>");

                html.append("</body></html>");

                byte[] response = html.toString().getBytes(StandardCharsets.UTF_8);
                t.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                t.sendResponseHeaders(200, response.length);
                OutputStream os = t.getResponseBody();
                os.write(response);
                os.close();
            }
        });

        server.start();
        System.out.println("✅ ULTIMATE SYSTEM RUNNING (100 Laptops + Invoice Generator)! Open Google Chrome -> http://localhost:8080");
    }
}