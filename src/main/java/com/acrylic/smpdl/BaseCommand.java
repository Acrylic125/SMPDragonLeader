package com.acrylic.smpdl;

import com.acrylic.smpdl.captures.Capture;
import com.acrylic.smpdl.captures.EventCapture;
import com.acrylic.smpdl.captures.ScoreSource;
import com.acrylic.smpdl.displays.ActionBarCaptureDisplay;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class BaseCommand implements CommandExecutor {

    private final Predicate<EntityDamageByEntityEvent> condition = event -> event.getEntityType() == EntityType.ZOGLIN;
    private final ScoreSource<EntityDamageByEntityEvent> scoreSource = EntityDamageEvent::getDamage;
    private final String[] helpArray = Utils.colorize(new String[] {
            "&e&lSMP Dragon Leaderboard",
            "/sdl display <capture name> &7Displays capture.",
            "/sdl removedisplay &7Removes capture.",
            "/sdl start <capture name> &7Start tracking.",
            "/sdl stop <capture name> &7Stops tracking.",
            "/sdl create <capture name> &7Create capture.",
            "/sdl delete <capture name> &7Delete capture.",
            "/sdl reset <capture name> &7Reset capture.",
            "/sdl broadcast <capture name> &7Broadcasts the results.",
            "/sdl captures &7List all the captures."
    });

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int argLength = args.length;
        if (argLength > 0) {
            String arg = args[0].toUpperCase(Locale.ROOT);
            switch (arg) {
                case "CREATE":
                    if (argLength >= 2)
                        create(sender, args[1]);
                    else
                        sendHelp(sender);
                    break;
                case "TERMINATE":
                    if (argLength >= 2)
                        terminate(sender, args[1]);
                    else
                        sendHelp(sender);
                    break;
                case "START":
                    if (argLength >= 2)
                        start(sender, args[1]);
                    else
                        sendHelp(sender);
                    break;
                case "STOP":
                    if (argLength >= 2)
                        stop(sender, args[1]);
                    else
                        sendHelp(sender);
                    break;
                case "RESET":
                    if (argLength >= 2)
                        reset(sender, args[1]);
                    else
                        sendHelp(sender);
                    break;
                case "BROADCAST":
                    if (argLength >= 2)
                        broadcast(sender, args[1]);
                    else
                        sendHelp(sender);
                    break;
                case "CAPTURES":
                    captures(sender);
                    break;
                case "DISPLAY":
                    if (argLength >= 2)
                        display(sender, args[1]);
                    else
                        sendHelp(sender);
                    break;
                case "REMOVEDISPLAY":
                    removeDisplay(sender);
                    break;
                default:
                    sendHelp(sender);
            }
        } else {
            sendHelp(sender);
        }
        return true;
    }

    private void create(CommandSender sender, String capture) {
        sender.sendMessage(Utils.colorize("&aCreate capture, &f" + capture + "&a."));
        SMPDL.getSmpDl().getCaptures().addCapture(EntityDamageByEntityEvent.class, capture, new EventCapture<>(condition, scoreSource));
    }

    private void terminate(CommandSender sender, String capture) {
        sender.sendMessage(Utils.colorize("&cTerminate capture, &f" + capture + "&c."));
        SMPDL.getSmpDl().getCaptures().removeCapture(EntityDamageByEntityEvent.class, capture);
    }

    private void display(CommandSender sender, String capture) {
        Capture<?> captureObj = SMPDL.getSmpDl()
                .getCaptures().getOrCreateTypeMap(EntityDamageByEntityEvent.class)
                .get(capture);
        if (captureObj == null) {
            sender.sendMessage(Utils.colorize("&cNo such capture named &c&n" + capture + "&r&c."));
            return;
        }
        SMPDL.getSmpDl().getCaptures().setCaptureDisplay(new ActionBarCaptureDisplay(captureObj));
        sender.sendMessage(Utils.colorize("&eSet display to capture, &f" + capture + "&c."));
    }

    private void removeDisplay(CommandSender sender) {
        SMPDL.getSmpDl().getCaptures().setCaptureDisplay(null);
        sender.sendMessage(Utils.colorize("&eRemoved display capture"));
    }

    private void reset(CommandSender sender, String capture) {
        Capture<?> captureObj = SMPDL.getSmpDl()
                .getCaptures().getOrCreateTypeMap(EntityDamageByEntityEvent.class)
                .get(capture);
        if (captureObj == null) {
            sender.sendMessage(Utils.colorize("&cNo such capture named &c&n" + capture + "&r&c."));
            return;
        }
        captureObj.getCaptureCache().resetAllScores();
    }

    private void toggleTracking(CommandSender sender, String capture, boolean b) {
        Capture<?> captureObj = SMPDL.getSmpDl()
                .getCaptures().getOrCreateTypeMap(EntityDamageByEntityEvent.class)
                .get(capture);
        if (captureObj == null) {
            sender.sendMessage(Utils.colorize("&cNo such capture named &c&n" + capture + "&r&c."));
            return;
        }
        captureObj.setTracking(b);
        sender.sendMessage(Utils.colorize("&aStarted tracking capture, &f" + capture + "&a."));
    }

    private void start(CommandSender sender, String capture) {
        toggleTracking(sender, capture, true);
    }

    private void stop(CommandSender sender, String capture) {
        toggleTracking(sender, capture, false);
    }

    private void captures(CommandSender sender) {
        sender.sendMessage(Utils.colorize("&6&lCaptures:"));
        SMPDL.getSmpDl().getCaptures().iterateAll((classType) -> sender.sendMessage(Utils.colorize("  &f" + classType)),
                (s, capture) -> sender.sendMessage(Utils.colorize("    &7" + s)));
    }

    private void broadcast(CommandSender sender, String capture) {
        Capture<?> captureObj = SMPDL.getSmpDl()
                                    .getCaptures().getOrCreateTypeMap(EntityDamageByEntityEvent.class)
                                    .get(capture);
        if (captureObj == null) {
            sender.sendMessage(Utils.colorize("&cNo such capture named &c&n" + capture + "&r&c."));
            return;
        }
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(Utils.colorize("&5&lDragon Leaderboard"));
        Bukkit.broadcastMessage("");
        AtomicInteger index = new AtomicInteger(0);
        captureObj
                 .getCaptureCache()
                 .iterateHighestScore((uuid, aDouble) -> {
                     Player player = Bukkit.getPlayer(uuid);
                     int position = index.addAndGet(1);
                     char posColor = 'c';
                     if (position == 1) {
                         posColor = 'a';
                     } else if (position == 2) {
                         posColor = 'e';
                     } else if (position == 3) {
                         posColor = '6';
                     }
                     if (player != null)
                         Bukkit.broadcastMessage(Utils.colorize("&" + posColor + "&l" + position + ". &r&f" + player.getName() + " &r&c&l" + Utils.round2dp(aDouble)));
                     else
                         Bukkit.broadcastMessage(Utils.colorize("&" + posColor + "&l" + position + ". &r&6&lLOGGED OUT &r&c&l" + Utils.round2dp(aDouble)));
                 });
        Bukkit.broadcastMessage("");
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(helpArray);
    }
}
