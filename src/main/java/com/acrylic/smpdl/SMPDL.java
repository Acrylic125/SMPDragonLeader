package com.acrylic.smpdl;

import com.acrylic.smpdl.captures.Capture;
import com.acrylic.smpdl.captures.EventCapture;
import com.acrylic.smpdl.impl.Captures;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class SMPDL
        extends JavaPlugin
        implements Listener {

    private static SMPDL SMP_DL;
    private final Captures captures = new Captures();

    public static SMPDL getSmpDl() {
        return SMP_DL;
    }

    public Captures getCaptures() {
        return captures;
    }

    @Override
    public void onEnable() {
        SMP_DL = this;
        // Plugin startup logic
        System.out.println("Loading SDL.");
        Objects.requireNonNull(getCommand("sdl")).setExecutor(new BaseCommand());
        Bukkit.getPluginManager().registerEvents(this, this);
        new BukkitRunnable() {
            @Override
            public void run() {
                captures.run();
            }
        }.runTaskTimer(this, 1, 1);
        System.out.println("Loaded SDL.");
    }

    @EventHandler
    public void listen(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Player) {
            Player playerDamager = (Player) damager;
            captures.iterateCaptures(EntityDamageByEntityEvent.class, (s, capture) -> {
                if (capture instanceof EventCapture && capture.isTracking()) {
                    Capture<EntityDamageByEntityEvent> eventCapture = (Capture<EntityDamageByEntityEvent>) capture;
                    if (eventCapture.trackAction(event))
                        eventCapture.getCaptureCache().addScore(playerDamager.getUniqueId(), eventCapture.getScoreAmount(event));
                }
            });
        }
    }
}
