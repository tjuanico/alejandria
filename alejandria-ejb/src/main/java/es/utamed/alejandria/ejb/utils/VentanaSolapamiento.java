package es.utamed.alejandria.ejb.utils;

import java.util.ArrayDeque;
import java.util.Deque;

public class VentanaSolapamiento {
    private final int maxSize;
    private final Deque<String> window;

    private String lastWindow;
    private int lastNumberLines;

    public VentanaSolapamiento(int maxSize) {
        this.maxSize = maxSize;
        this.window = new ArrayDeque<>(maxSize);
        this.lastWindow = "";
        this.lastNumberLines = 0;

    }

    public void add(String linea) {
        if (window.size() == maxSize) {
            window.pollFirst();
        }
        window.addLast(linea);
    }

    public String getSolapamiento() {
        return String.join(" ", window);
    }

    public void swapWindow() {
        this.lastWindow = getSolapamiento();
        lastNumberLines = window.size();
    }

    public String getLastWindow() { return this.lastWindow; }
    public int getLastNumberLines() { return this.lastNumberLines; }
}
