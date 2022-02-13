package xyz.supeer.mandatory.sql;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import xyz.supeer.mandatory.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static xyz.supeer.mandatory.Main.plugin;

public class MySQL {


    public static String host = "localhost";
    public static String port = "3306";
    public static String database = "Mandatory";
    public static String username = "super";
    public static String password = "Dx8cALg7m00";

    public static Connection con;

    static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public MySQL() throws SQLException {
    }

    // connect
    public static void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                console.sendMessage("§aMySQL Anslutningen har upprättats!");
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
        return (con == null ? false : true);
    }

    // getConnection
    public static Connection getConnection() {
        return con;
    }

}
