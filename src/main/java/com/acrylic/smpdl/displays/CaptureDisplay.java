package com.acrylic.smpdl.displays;

import com.acrylic.smpdl.captures.Capture;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface CaptureDisplay {

    Capture<?> getCapturing();

    void displayTo(Player player);

    default void displayToAll() {
        for (Player player : Bukkit.getOnlinePlayers())
            displayTo(player);
    }

    void run();

}
