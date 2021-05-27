package ch.mixin.mixedCatastrophes.helperClasses;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;
import java.util.Random;

public class Coordinate3D {
    private double x;
    private double y;
    private double z;

    public Coordinate3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate3D clone() {
        return new Coordinate3D(x, y, z);
    }

    public static Coordinate3D random() {
        Random random = new Random();
        return new Coordinate3D(random.nextDouble(), random.nextDouble(), random.nextDouble()).normalize();
    }

    public Coordinate2D to2D() {
        return new Coordinate2D(getXRound(), getZRound());
    }

    public static Coordinate3D toCoordinate(Location location) {
        return new Coordinate3D(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }

    public Coordinate3D sum(Coordinate3D c3d) {
        return sum(c3d.getX(), c3d.getY(), c3d.getZ());
    }

    public Coordinate3D sum(double x, double y, double z) {
        return new Coordinate3D(this.x + x, this.y + y, this.z + z);
    }

    public Coordinate3D difference(Coordinate3D c3d) {
        return sum(-c3d.getX(), -c3d.getY(), -c3d.getZ());
    }

    public Coordinate3D multiply(double factor) {
        return new Coordinate3D(x * factor, y * factor, z * factor);
    }

    public Coordinate3D divide(double divisor) {
        return new Coordinate3D(x / divisor, y / divisor, z / divisor);
    }

    public double distance(Coordinate3D c3d) {
        return difference(c3d).length();
    }

    public double length() {
        return Math.pow(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2), 0.5);
    }

    public Coordinate3D normalize() {
        return divide(length());
    }

    public Coordinate3D rotateY90Clockwise() {
        double newX = -z;
        double newZ = x;

        return new Coordinate3D(newX, y, newZ);
    }

    public Coordinate3D rotateY90Clockwise(int times) {
        Coordinate3D coordinate3D = clone();

        for (int i = 0; i < times; i++) {
            coordinate3D = coordinate3D.rotateY90Clockwise();
        }

        return coordinate3D;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate3D that = (Coordinate3D) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public double getX() {
        return x;
    }

    public int getXRound() {
        return (int) Math.round(x);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public int getYRound() {
        return (int) Math.round(y);
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public int getZRound() {
        return (int) Math.round(z);
    }

    public void setZ(double z) {
        this.z = z;
    }
}
