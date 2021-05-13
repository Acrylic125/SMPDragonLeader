package com.acrylic.smpdl.impl;

import com.acrylic.smpdl.captures.Capture;
import com.acrylic.smpdl.displays.ActionBarCaptureDisplay;
import com.acrylic.smpdl.displays.CaptureDisplay;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Captures implements Runnable {

    private final Map<Class<?>, Map<String, Capture<?>>> captureMap = new HashMap<>();
    private CaptureDisplay captureDisplay;

    public CaptureDisplay getCaptureDisplay() {
        return captureDisplay;
    }

    public void setCaptureDisplay(CaptureDisplay captureDisplay) {
        this.captureDisplay = captureDisplay;
    }

    public <T> Map<String, Capture<?>> getOrCreateTypeMap(Class<T> captureType) {
        Map<String, Capture<?>> captureMap = this.captureMap.get(captureType);
        if (captureMap == null)
            captureMap = new HashMap<>();
        this.captureMap.put(captureType, captureMap);
        return captureMap;
    }

    public <T> void addCapture(Class<T> captureType, String captureName, Capture<T> capture) {
        Map<String, Capture<?>> captureMap = getOrCreateTypeMap(captureType);
        captureMap.put(captureName, capture);
    }

    public <T> void removeCapture(Class<T> captureType, String captureName) {
        Map<String, Capture<?>> captureMap = getOrCreateTypeMap(captureType);
        captureMap.remove(captureName);
    }

    public <T> void iterateCaptures(Class<T> captureType, BiConsumer<String, Capture<?>> action) {
        Map<String, Capture<?>> captureMap = getOrCreateTypeMap(captureType);
        captureMap.forEach(action);
    }

    public void iterateAll(Consumer<Class<?>> actionClass, BiConsumer<String, Capture<?>> action) {
        captureMap.forEach((aClass, stringCaptureMap) -> {
            actionClass.accept(aClass);
            stringCaptureMap.forEach(action);
        });
    }


    @Override
    public void run() {
        if (captureDisplay != null)
            captureDisplay.run();
    }
}
