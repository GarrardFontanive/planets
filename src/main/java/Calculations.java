import java.io.BufferedReader;
import java.io.FileReader;

public class Calculations {

    private double averageOrbitalPeriod;
    private double averageRadius;
    private double averageDistance;

    public double getAverageOrbitalPeriod() {
        return averageOrbitalPeriod;
    }

    public void setAverageOrbitalPeriod(double averageOrbitalPeriod) {
        this.averageOrbitalPeriod = averageOrbitalPeriod;
    }

    public double getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(double averageDistance) {
        this.averageDistance = averageDistance;
    }

    public double getAverageRadius() {
        return averageRadius;
    }

    public void setAverageRadius(double averageRadius) {
        this.averageRadius = averageRadius;
    }

    private double calculateDistance(Planet planet1, Planet planet2){
        return 0;
    }
}
