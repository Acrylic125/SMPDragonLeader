package com.acrylic.smpdl.captures;

import com.acrylic.smpdl.cache.CaptureCache;

public interface Capture<T> {

    CaptureCache getCaptureCache();

    boolean trackAction(T event);

    double getScoreAmount(T event);

    void setTracking(boolean tracking);

    boolean isTracking();

}
