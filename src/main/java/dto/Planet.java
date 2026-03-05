package dto;

public class Planet {

    private String name;
    private int discoveryYear;
    private String discoveryMethod;
    private double orbitalPeriod;
    private double radius;
    private double mass;
    private double distance;

    public Planet() {
    }

    public Planet(String name, int discoveryYear, String discoveryMethod, double orbitalPeriod, double radius, double mass, double distance) {
        this.name = name;
        this.discoveryYear = discoveryYear;
        this.discoveryMethod = discoveryMethod;
        this.orbitalPeriod = orbitalPeriod;
        this.radius = radius;
        this.mass = mass;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiscoveryYear() {
        return discoveryYear;
    }

    public void setDiscoveryYear(int discoveryYear) {
        this.discoveryYear = discoveryYear;
    }

    public String getDiscoveryMethod() {
        return discoveryMethod;
    }

    public void setDiscoveryMethod(String discoveryMethod) {
        this.discoveryMethod = discoveryMethod;
    }

    public double getOrbitalPeriod() {
        return orbitalPeriod;
    }

    public void setOrbitalPeriod(double orbitalPeriod) {
        this.orbitalPeriod = orbitalPeriod;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "name='" + name + '\'' +
                ", discoveryYear=" + discoveryYear +
                ", discoveryMethod='" + discoveryMethod + '\'' +
                ", orbitalPeriod=" + orbitalPeriod +
                ", radius=" + radius +
                ", mass=" + mass +
                ", distance=" + distance +
                '}';
    }
}