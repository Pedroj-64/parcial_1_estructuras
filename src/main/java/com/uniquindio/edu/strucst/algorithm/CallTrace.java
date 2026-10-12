package com.uniquindio.edu.strucst.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class CallTrace {

    private static final CallTrace DISABLED = new CallTrace(false);

    private final boolean enabled;
    private final List<String> lines = new ArrayList<>();
    private int depth;
    private int maxDepth;

    private CallTrace(boolean enabled) {
        this.enabled = enabled;
    }

    
    public static CallTrace enabled() {
        return new CallTrace(true);
    }

    
    public static CallTrace disabled() {
        return DISABLED;
    }

    
    public void enter(String call) {
        if (!enabled) {
            return;
        }
        depth++;
        maxDepth = Math.max(maxDepth, depth);
        lines.add(indent() + "→ " + call + "   [marcos en pila: " + depth + "]");
    }

    
    public void exit(String call, Object result) {
        if (!enabled) {
            return;
        }
        lines.add(indent() + "← " + call + " retorna " + result);
        depth--;
    }

    
    public void note(String note) {
        if (enabled) {
            lines.add(indent() + "  · " + note);
        }
    }

    
    public List<String> lines() {
        return Collections.unmodifiableList(lines);
    }

    
    public int maxDepth() {
        return maxDepth;
    }

    private String indent() {
        return "  ".repeat(Math.max(0, depth - 1));
    }

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), lines);
    }
}
