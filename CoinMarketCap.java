import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CoinMarketCap {
    static class AppException extends RuntimeException {
        AppException(String msg) {
            super(msg);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Invalid number of arguments. Usage:");
            System.out.println("  java com.mentics.coin.client.CoinMarketCap COINID");
            System.out.println("  java com.mentics.coin.client.CoinMarketCap COINID json  # for json formatted output");
            System.out.println("for example:");
            System.out.println("  java com.mentics.coin.client.CoinMarketCap bitcoin");
            System.out.println("or to show a list of valid COINIDs:");
            System.out.println("  java com.mentics.coin.client.CoinMarketCap list");
            System.exit(1);
        }

        if ("list".equals(args[0])) {
            showList();
        } else {
            String currencyId = args[0];
            try {
                showCurrency(currencyId, args.length > 1 ? args[1] : "");
            } catch (AppException e) {
                System.out.println("Error for [" + currencyId + "]: " + e.getMessage());
            }
        }
    }

    public static void showCurrency(String currencyId, String format) throws IOException {
        String allData = getContent(new URL("https://api.coinmarketcap.com/v1/ticker/" + currencyId + "/"));
        String price = getField(allData, "price_usd");
        String marketCap = getField(allData, "market_cap_usd");
        if ("json".equals(format)) {
            System.out.println("{ \"id\": \"" + currencyId + "\",\n" + "  \"price\": \"" + price + "\",\n"
                    + "  \"market-cap\": \"" + marketCap + "\" }");
        } else {
            System.out.println("Price: " + price + "\nMarket cap: " + marketCap);
        }
    }

    public static void showList() throws IOException {
        List<String> result = new ArrayList<>();
        String allData = getContent(new URL("https://api.coinmarketcap.com/v1/ticker/"));
        Pattern regex = Pattern.compile("\"id\"\\s*\\:\\s*\"(.+?)\"");
        Matcher m = regex.matcher(allData);
        while (m.find()) {
            result.add(m.group(1));
        }
        System.out.println(String.join(",", result));
    }

    public static String getContent(URL url) throws IOException {
        try {
            URLConnection conn = url.openConnection();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (FileNotFoundException e) {
            throw new AppException("Currency not found");
        }
    }

    private static String getField(String allData, String fieldName) {
        Matcher m = Pattern.compile("\"" + fieldName + "\"\\s*\\:\\s*\"(.+?)\"").matcher(allData);
        if (!m.find()) {
            if (allData.contains("id not found")) {
                throw new AppException("Currency not recognized");
            } else {
                throw new AppException("Invalid response from CoinMarketCap API:\n" + allData);
            }
        }
        return m.group(1);
    }
}
