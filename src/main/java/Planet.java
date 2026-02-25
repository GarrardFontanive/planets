public class Planet {

    private String name;
    private int discoveryYear;
    private double orbitalPeriod;
    private double planetRadiusEarth;

    Planet(String name, int discoveryYear, double orbitalPeriod, double planetRadiusEarth){
        setName(name);
        setDiscoveryYear(discoveryYear);
        setOrbitalPeriod(orbitalPeriod);
        setPlanetRadiusEarth(planetRadiusEarth);
    }

    Planet(String name, int discoveryYear){
        setName(name);
        setDiscoveryYear(discoveryYear);
    }

    Planet(){}

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

    public double getOrbitalPeriod() {
        return orbitalPeriod;
    }

    public void setOrbitalPeriod(double orbitalPeriod) {
        this.orbitalPeriod = orbitalPeriod;
    }

    public double getPlanetRadiusEarth() {
        return planetRadiusEarth;
    }

    public void setPlanetRadiusEarth(double planetRadiusEarth) {
        this.planetRadiusEarth = planetRadiusEarth;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Planet{");
        sb.append("name='").append(name).append('\'');
        sb.append(", discoveryYear=").append(discoveryYear);
        sb.append(", orbitalPeriod=").append(orbitalPeriod);
        sb.append(", planetRadiusEarth=").append(planetRadiusEarth);
        sb.append('}');
        return sb.toString();
    }


}
