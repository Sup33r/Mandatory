package xyz.supeer.mandatory.commands.admin;

import org.bukkit.entity.Player;

import java.io.IOException;

public abstract class SubCommand {


    public SubCommand() {

    }

    public abstract void onCommand(Player p, String[] args) throws IOException;

    public abstract String name();
    public abstract String info();
    public abstract String[] aliases();

}
/*
/admin
/admin disable
/admin enable
/admin perm
/admin reload
/admin set chatprefix
/admin set onlinegroup
/admin toggle chatprefix
/admin toggle extprefix
/admin set afk
/admin set spawn
/admin set firstspawn


 */