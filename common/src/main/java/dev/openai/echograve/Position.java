package dev.openai.echograve;

public record Position(String dimension, double x, double y, double z) {
    public double distanceTo(Position o) { if (!dimension.equals(o.dimension)) return Double.MAX_VALUE; double dx=o.x-x, dy=o.y-y, dz=o.z-z; return Math.sqrt(dx*dx+dy*dy+dz*dz); }
    public String directionTo(Position o) { double dx=o.x-x, dz=o.z-z; if (Math.abs(dx)>Math.abs(dz)) return dx>=0 ? "east" : "west"; return dz>=0 ? "south" : "north"; }
}
