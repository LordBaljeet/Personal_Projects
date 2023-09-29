package me.lordbaljeet.projectchess.module;

import java.util.Objects;

public class Player {

    private PlayerColor color;

    private int timeLeft;

    private boolean hasPlayed;

    public Player(PlayerColor color) {
        this.color = color;
        this.timeLeft = 600;
        this.hasPlayed = false;
    }

    public boolean hasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public PlayerColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return color + " Player";
    }

    public boolean timeElapsed() {
        return timeLeft == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return color == player.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}