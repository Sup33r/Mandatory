package xyz.supeer.mandatory.sql;

import org.bukkit.entity.Player;

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
                PreparedStatement ps2 = MySQL.getConnection().prepareStatement("INSERT IGNORE INTO playereco" + " (NAME,UUID) VALUES (?,?)");
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

    public static void addBalance(UUID uuid, int balance) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE playereco SET BALANCE=? WHERE UUID=?");
            ps.setInt(1, (getBalance(uuid) + balance));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeBalance(UUID uuid, int balance) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE playereco SET BALANCE=? WHERE UUID=?");
            ps.setInt(1, (getBalance(uuid) - balance));
            ps.setString(2, uuid.toString());
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

    public static void createLoveTable() {
        PreparedStatement ps;
        try {
            ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerloves " + "(NAME VARCHAR(100),UUID VARCHAR(100),SENDERNAME VARCHAR(100),SENDERUUID VARCHAR(100),LOVES INT(100),MESSAGE TEXT(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createLovePlayer(Player player, Player sender, String message) {
        try {
            UUID uuid = player.getUniqueId();
            UUID senderuuid = sender.getUniqueId();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM playerloves WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            results.next();
            if (!loveExists(uuid)) {
                PreparedStatement ps2 = MySQL.getConnection().prepareStatement("INSERT IGNORE INTO playerloves" + " (NAME,UUID,SENDERNAME,SENDERUUID,MESSAGE) VALUES (?,?,?,?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, uuid.toString());
                ps2.setString(3, sender.getName());
                ps2.setString(4, senderuuid.toString());
                ps2.setString(5, message);
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

    public static void addLoves(UUID uuid, int loves, UUID senderuuid, String message) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE playerloves SET LOVES=?, MESSAGE=?, SENDERUUID=? WHERE UUID=?");
            ps.setInt(1, (getLoves(uuid) + loves));
            ps.setString(2, message);
            ps.setString(3, senderuuid.toString());
            ps.setString(4, uuid.toString());
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

    public static void createGoTable() {
        PreparedStatement ps;
        try {
            ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS golocations " + "(WORLD VARCHAR(100),NAME VARCHAR(100),XLOCATION DOUBLE,YLOCATION DOUBLE,ZLOCATION DOUBLE,PITCHLOCATION FLOAT,YAWLOCATION FLOAT,OWNER VARCHAR(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean goExists(String name) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM golocations WHERE NAME=?");
            ps.setString(1, name);

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

    public static void addGo(String world, String name, Double x, Double y, Double z, Float pitch, Float yaw, UUID uuid) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT IGNORE INTO golocations" + " (WORLD,NAME,XLOCATION,YLOCATION,ZLOCATION,PITCHLOCATION,YAWLOCATION,OWNER) VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, world);
            ps.setString(2, name);
            ps.setDouble(3, x);
            ps.setDouble(4, y);
            ps.setDouble(5, z);
            ps.setFloat(6, pitch);
            ps.setFloat(7, yaw);
            ps.setString(8, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeOwner(String name, UUID to) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE golocations SET OWNER=? WHERE NAME=?");
            ps.setString(1, to.toString());
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getOwner(String name) {
        String owner = null;
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT OWNER FROM golocations WHERE NAME=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            owner = "";
            if (rs.next()) {
                owner = rs.getString("OWNER");
                return owner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner;
    }

    public static Double getXLocation(String name) {
        Double x = null;
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT XLOCATION FROM golocations WHERE NAME=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                x = rs.getDouble("XLOCATION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return x;
    }

    public static Double getYLocation(String name) {
        Double y = null;
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT YLOCATION FROM golocations WHERE NAME=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                y = rs.getDouble("YLOCATION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return y;
    }

    public static Double getZLocation(String name) {
        Double z = null;
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT ZLOCATION FROM golocations WHERE NAME=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                z = rs.getDouble("ZLOCATION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return z;
    }

    public static Float getPitchLocation(String name) {
        Float pitch = null;
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT PITCHLOCATION FROM golocations WHERE NAME=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                pitch = rs.getFloat("PITCHLOCATION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return pitch;
    }

    public static Float getYawLocation(String name) {
        Float yaw = null;
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT YAWLOCATION FROM golocations WHERE NAME=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                yaw = rs.getFloat("YAWLOCATION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return yaw;
    }

    public static String getWorld(String name) {
        String world = null;
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT WORLD FROM golocations WHERE NAME=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                world = rs.getString("WORLD");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return world;
    }

    public static String deleteGo(String name) {
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM golocations WHERE NAME = ?");
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }
}
