package xyz.supeer.mandatory.sql;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.bukkit.Bukkit;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.supeer.mandatory.Main;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;



public class MySQL {

    private static final Main plugin = Main.getInstance();
    public static Connection con;

    static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public MySQL(Main plugin) throws SQLException {
    }

    // connect
    public static void connect() {
        if (!isConnected()) {
            try {
                String host = plugin.getCustomConfig().getString("sql.host");
                String port = plugin.getCustomConfig().getString("sql.port");
                String database = plugin.getCustomConfig().getString("sql.database");
                String username = plugin.getCustomConfig().getString("sql.username");
                String password = plugin.getCustomConfig().getString("sql.password");
                if (host == null || port == null || database == null || username == null || password == null) {
                    console.sendMessage("§7[Mandatory] §cNågon (eller några) variablar för databas-anslutningen är inte satta i internals.yml. Se över detta.");
                    return;
                }
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                console.sendMessage("§7[Mandatory] §aMySQL Anslutningen har upprättats!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // disconnect
    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                console.sendMessage("§cMySQL Anslutningen har stängts!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // isConnected
    public static boolean isConnected() {
        return (con != null);
    }

    // getConnection
    public static Connection getConnection() {
        return con;
    }

}
