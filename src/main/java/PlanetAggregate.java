import java.util.ArrayList;
import java.util.List;

public class PlanetAggregate {

    private final List<Double> orbitalPeriods = new ArrayList<>();
    private final List<Double> radii = new ArrayList<>();
    private final List<Double> masses = new ArrayList<>();

    public List<Double> getOrbitalPeriods() {
        return orbitalPeriods;
    }

    public List<Double> getRadii() {
        return radii;
    }

    public List<Double> getMasses() {
        return masses;
    }

    public int getSampleCount() {
        int orbitalSamples = orbitalPeriods.size();
        int radiusSamples = radii.size();
        int massSamples = masses.size();
        return Math.max(orbitalSamples, Math.max(radiusSamples, massSamples));
    }
}