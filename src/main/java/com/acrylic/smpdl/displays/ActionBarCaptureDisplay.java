package com.acrylic.smpdl.displays;

import com.acrylic.smpdl.Utils;
import com.acrylic.smpdl.captures.Capture;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionBarCaptureDisplay implements CaptureDisplay {

    private final Capture<?> capture;
    private String displayAs;

    public ActionBarCaptureDisplay(Capture<?> capture, String displayAs) {
        this.capture = capture;
        this.displayAs = displayAs;
    }

    public ActionBarCaptureDisplay(Capture<?> capture) {
        this(capture, "&d&lYou Dealt: &r&c&l%player_score%");
    }

    /**
     *
     * @param displayAs Display as.
     *                  Use
     *                  %player_score% to display the current player's score.
     */
    public void setDisplayAs(String displayAs) {
        this.displayAs = displayAs;
    }

    public String getDisplayAs() {
        return displayAs;
    }

    @Override
    public Capture<?> getCapturing() {
        return capture;
    }

    @Override
    public void displayTo(Player player) {
        String out = displayAs.replaceAll("%player_score%", Utils.round2dp(capture.getCaptureCache().getCaptureResult(player)) + "");
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.colorize(out)));
    }

    @Override
    public void run() {
        displayToAll();
    }
}
