package ch.mixin.mixedCatastrophes.helperClasses;

import org.bukkit.Location;

import java.util.Objects;
import java.util.Random;

public class Coordinate2D {
    private double x;
    private double z;

    public Coordinate2D(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public static Coordinate2D random() {
        Random random = new Random();
        return new Coordinate2D(random.nextDouble() - 0.5, random.nextDouble() - 0.5).normalize();
    }

    public Coordinate2D sum(int x, int z) {
        return new Coordinate2D(this.x + x, this.z + z);
    }

    public Coordinate2D sum(double x, double z) {
        return new Coordinate2D(this.x + x, this.z + z);
    }

    public Coordinate2D difference(Coordinate2D c2d) {
        return sum(-c2d.getX(), -c2d.getZ());
    }

    public Coordinate2D multiply(double factor) {
        return new Coordinate2D(x * factor, z * factor);
    }

    public Coordinate2D divide(double divisor) {
        return new Coordinate2D(x / divisor, z / divisor);
    }

    public double distance(Coordinate2D c2d) {
        return difference(c2d).length();
    }

    public double length() {
        return Math.pow(x * x + z * z, 0.5);
    }

    public Coordinate2D normalize() {
        return divide(length());
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
