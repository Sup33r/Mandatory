package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Ban;
import live.supeer.mandatory.Mandatory;
import live.supeer.metropolis.Metropolis;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

@CommandAlias("login")
public class CommandLogin extends BaseCommand {

    private static final long COOLDOWN_TIME = 5000;
    private static final Map<UUID, Long> lastUsage = new HashMap<>();

    @Default
    public void onLogin(Player player) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (lastUsage.containsKey(playerUUID) && (currentTime - lastUsage.get(playerUUID)) < COOLDOWN_TIME) {
            Metropolis.sendMessage(player, "messages.error.cooldown");
            return;
        }

        lastUsage.put(playerUUID, currentTime);

        if (!player.hasPermission("mandatory.command.login")) {
            player.sendMessage("messages.error.permissionDenied");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Mandatory.getInstance(), () -> {
            try {
                String uuid = player.getUniqueId().toString();
                String password = "@SZQPbqj1nBdn%";
                String urlParameters = "uuid=" + uuid + "&password=" + password;
                byte[] postData = urlParameters.getBytes();

                URL url = new URL("https://enserver.se/generate_link");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postData.length));

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(postData);
                }

                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                String responseLink = response.toString();
                Bukkit.getScheduler().runTask(Mandatory.getInstance(), () -> {
                    Mandatory.sendMessage(player, "messages.login.successful", "%link%", responseLink);
                });

            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getScheduler().runTask(Mandatory.getInstance(), () -> {
                    Mandatory.sendMessage(player, "messages.login.error");
                });
            }
        });
    }
}