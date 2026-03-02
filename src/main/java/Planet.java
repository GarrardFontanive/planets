public class Planet {

    //select+pl_name,disc_year,discoverymethod,pl_orbper,pl_rade,pl_masse,sy_dist" +
    private String name;
    private int discoveryYear;
    private String discoveryMethod;
    private double orbitalPeriod;
    private double radius;
    private double mass;
    private double distance;

    Planet(){}

    Planet(String name, int discoveryYear, String discoveryMethod, double orbitalPeriod, double radius, double distance){
        setName(name);
        setDiscoveryYear(discoveryYear);
        setDiscoveryMethod(discoveryMethod);
        setOrbitalPeriod(orbitalPeriod);
        setRadius(radius);
        setMass(distance);
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
        final StringBuilder sb = new StringBuilder("Planet{");
        sb.append("name='").append(name).append('\'');
        sb.append(", discoveryYear=").append(discoveryYear);
        sb.append(", discoveryMethod='").append(discoveryMethod).append('\'');
        sb.append(", orbitalPeriod=").append(orbitalPeriod);
        sb.append(", radius=").append(radius);
        sb.append(", mass=").append(mass);
        sb.append(", distance=").append(distance);
        sb.append('}');
        return sb.toString();
    }

}
