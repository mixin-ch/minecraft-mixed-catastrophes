package ch.mixin.helperClasses;

import org.bukkit.Location;

import java.util.Objects;

public class Coordinate2D {
    private double x;
    private double z;

    public Coordinate2D(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public Coordinate2D sum(int x, int z) {
        return new Coordinate2D(this.x + x, this.z + z);
    }

    public Coordinate3D to3D(double y) {
        return new Coordinate3D(x, y, z);
    }

    public static Coordinate2D convert(Location location) {
        return new Coordinate2D(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate2D that = (Coordinate2D) o;
        return x == that.x && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    public double getX() {
        return x;
    }

    public int getXRound() {
        return (int) Math.round(x);
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getZ() {
        return z;
    }

    public int getZRound() {
        return (int) Math.round(z);
    }

    public void setZ(int z) {
        this.z = z;
    }
}
