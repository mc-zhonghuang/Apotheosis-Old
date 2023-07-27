package com.alan.clients.protocol.hyt.germmod.util;

public enum GuiState {
    CLICK(0),
    HOVERED(2),
    COVERED(3);

    private final int id;

    public int getId() {
        return this.id;
    }

    GuiState(int stats) {
        this.id = stats;
    }
}
