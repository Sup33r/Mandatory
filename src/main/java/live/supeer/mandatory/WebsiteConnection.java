package live.supeer.mandatory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class WebsiteConnection {

    public static void startServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8765), 0);
            server.createContext("/ticketAnswered", new TicketAnsweredHandler());
            server.createContext("/newTicket", new newTicketHandler());
            server.createContext("/modzoneNewTicket", new modzoneNewTicketHandler());
            server.createContext("/checkBan", new CheckBanHandler());
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class TicketAnsweredHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String requestBody = sb.toString();
                JSONObject json = Mandatory.parseJSON(requestBody);
                if (json == null) {
                    exchange.sendResponseHeaders(400, -1);
                    return;
                }
                String playerUUID = (String) json.get("playerUUID");
                String link = (String) json.get("link");
                int ticketID = Integer.parseInt((String) json.get("ticketID"));
                Player player = Mandatory.getInstance().getServer().getPlayer(UUID.fromString(playerUUID));
                if (player != null && player.isOnline()) {
                    Mandatory.sendMessage(player, "messages.ticket.answered", "%id%", Integer.toString(ticketID), "%link%", link);
                }

                String response = "Message sent";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class CheckBanHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String[] queryParts = query.split("=");
                if (queryParts.length == 2 && "uuid".equals(queryParts[0])) {
                    String uuid = queryParts[1];

                    // Check if the player is banned using ApiedAPI
                    MPlayer targetPlayer = ApiedAPI.getPlayer(UUID.fromString(uuid));
                    boolean isBanned = targetPlayer != null && targetPlayer.isBanned();

                    // Build response
                    String response = isBanned ? "yes" : "no";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(400, -1); // Bad Request if query is invalid
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method not allowed if not GET
            }
        }
    }

    static class newTicketHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String requestBody = sb.toString();
                JSONObject json = Mandatory.parseJSON(requestBody);
                if (json == null) {
                    exchange.sendResponseHeaders(400, -1);
                    return;
                }
                String playerUUID = (String) json.get("playerUUID");
                String link = (String) json.get("link");
                Player player = Mandatory.getInstance().getServer().getPlayer(UUID.fromString(playerUUID));
                if (player != null && player.isOnline()) {
                    Mandatory.sendMessage(player, "messages.ticket.new", "%link%", link);
                }

                String response = "Message sent";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class modzoneNewTicketHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String requestBody = sb.toString();
                JSONObject json = Mandatory.parseJSON(requestBody);
                if (json == null) {
                    exchange.sendResponseHeaders(400, -1);
                    return;
                }
                String playerUUID = (String) json.get("playerUUID");
                String link = (String) json.get("link");
                MandatoryChat.modChatAnnouncement(Mandatory.getRawMessage("messages.ticket.modzoneNew", "%player%", ApiedAPI.getPlayer(UUID.fromString(playerUUID)).getName(), "%link%", link));
                String response = "Message sent";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

}