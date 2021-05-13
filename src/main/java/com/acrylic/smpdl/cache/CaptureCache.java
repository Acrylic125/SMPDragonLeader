package com.acrylic.smpdl.cache;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiConsumer;

public class CaptureCache {

    private final Map<UUID, Double> scores;

    public CaptureCache() {
        this(new HashMap<>());
    }

    public CaptureCache(Map<UUID, Double> scores) {
        this.scores = scores;
    }

    public double getCaptureResult(Player player) {
        return scores.getOrDefault(player.getUniqueId(), 0.0);
    }

    public void iterateHighestScore(BiConsumer<UUID, Double> action) {
        iterateOrdered((o1, o2) -> o2.getValue().compareTo(o1.getValue()), action);
    }

    public void iterateOrdered(Comparator<Map.Entry<UUID, Double>> comparator, BiConsumer<UUID, Double> action) {
        final List<Map.Entry<UUID, Double>> toList =
                new LinkedList<>(scores.entrySet());
        toList.sort(comparator);
        for (Map.Entry<UUID, Double> entry : toList)
            action.accept(entry.getKey(), entry.getValue());
    }

    public LinkedHashMap<UUID, Double> toHighestScore(int maxPos) {
        return toOrdered(Map.Entry.comparingByValue(), maxPos);
    }

    public LinkedHashMap<UUID, Double> toOrdered(Comparator<Map.Entry<UUID, Double>> comparator, int maxSize) {
        final int max = scores.size();
        if (maxSize < 0 || maxSize > max)
            maxSize = max;
        final LinkedHashMap<UUID, Double> result = new LinkedHashMap<>(maxSize);
        iterateOrdered(comparator, result::put);
        return result;
    }

    public double getScore(UUID uuid) {
        return scores.getOrDefault(uuid, 0.0);
    }

    public void addScore(UUID uuid, double score) {
        Double cs = scores.getOrDefault(uuid, 0.0);
        cs += score;
        setScore(uuid, cs);
    }

    public void removeScore(UUID uuid, double score) {
        Double cs = scores.getOrDefault(uuid, 0.0);
        cs -= score;
        setScore(uuid, cs);
    }

    public void resetScore(UUID uuid) {
        setScore(uuid, 0);
    }

    public void setScore(UUID uuid, double score) {
        scores.put(uuid, score);
    }

    public void resetAllScores() {
        scores.clear();
    }
}
