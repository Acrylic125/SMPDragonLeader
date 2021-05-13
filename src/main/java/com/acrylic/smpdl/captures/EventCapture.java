package com.acrylic.smpdl.captures;

import com.acrylic.smpdl.cache.CaptureCache;
import org.bukkit.event.Event;

import java.util.function.Predicate;

public class EventCapture<T extends Event>
        implements Capture<T> {

    private final CaptureCache captureCache = new CaptureCache();
    private final Predicate<T> condition;
    private final ScoreSource<T> score;
    private boolean tracking = true;

    public EventCapture(Predicate<T> condition, ScoreSource<T> score) {
        this.condition = condition;
        this.score = score;
    }

    @Override
    public boolean trackAction(T event) {
        return condition.test(event);
    }

    @Override
    public double getScoreAmount(T event) {
        return score.getScore(event);
    }

    @Override
    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    @Override
    public boolean isTracking() {
        return tracking;
    }

    @Override
    public CaptureCache getCaptureCache() {
        return captureCache;
    }
}
