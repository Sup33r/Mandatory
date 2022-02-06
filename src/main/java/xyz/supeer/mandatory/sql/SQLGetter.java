package xyz.supeer.mandatory.sql;

import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGetter {



    public void createTable() {
        PreparedStatement ps;
        try {
            ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playereco " + "(NAME VARCHAR(100),UUID VARCHAR(100),BALANCE INT(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM playereco WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            results.next();
            if (!exists(uuid)) {
                PreparedStatement ps2 = MySQL.getConnection().prepareStatement("INSERT IGNORE INFO playereco" + " (NAME,UUID) VALUES (?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.executeUpdate();

                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM playereco WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addBalance(UUID uuid, int balance) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE playereco SET BALANCE=? WHERE UUID=?");
            ps.setInt(1, (getBalance(uuid) + balance));
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getBalance(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT BALANCE FROM playereco WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int balance = 0;
            if (rs.next()) {
                balance = rs.getInt("BALANCE");
                return balance;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void createLoveTable() {
        PreparedStatement ps;
        try {
            ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerloves " + "(NAME VARCHAR(100),UUID VARCHAR(100),SENDERNAME VARCHAR(100),SENDERUUID VARCHAR(100),LOVES INT(100),MESSAGE TEXT(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createLovePlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM playerloves WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            results.next();
            if (!loveExists(uuid)) {
                PreparedStatement ps2 = MySQL.getConnection().prepareStatement("INSERT IGNORE INFO playerloves" + " (NAME,UUID) VALUES (?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.executeUpdate();

                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean loveExists(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM playerloves WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addLoves(UUID uuid, int loves) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE playerloves SET LOVES=? WHERE UUID=?");
            ps.setInt(1, (getLoves(uuid) + loves));
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getLoves(UUID uuid) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT LOVES FROM playerloves WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int loves = 0;
            if (rs.next()) {
                loves = rs.getInt("LOVES");
                return loves;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
