package dto;

import java.util.ArrayList;
import java.util.List;

public class PlanetAggregate {

    private final List<Double> orbitalPeriods = new ArrayList<>();
    private final List<Double> radii = new ArrayList<>();
    private final List<Double> masses = new ArrayList<>();
    private final List<Integer> discoveryYears = new ArrayList<>();
    private final List<String> discoveryMethods = new ArrayList<>();
    private final List<Double> distances = new ArrayList<>();

    public List<Double> getOrbitalPeriods() {
        return orbitalPeriods;
    }

    public List<Double> getRadii() {
        return radii;
    }

    public List<Double> getMasses() {
        return masses;
    }

    public List<Integer> getDiscoveryYears() {
        return discoveryYears;
    }

    public List<String> getDiscoveryMethods() {
        return discoveryMethods;
    }

    public List<Double> getDistances() {
        return distances;
    }

    public int getSampleCount() {
        int orbitalSamples = orbitalPeriods.size();
        int radiusSamples = radii.size();
        int massSamples = masses.size();
        int yearSamples = discoveryYears.size();
        int methodSamples = discoveryMethods.size();
        int distanceSamples = distances.size();

        int maxA = Math.max(orbitalSamples, radiusSamples);
        int maxB = Math.max(massSamples, yearSamples);
        int maxC = Math.max(methodSamples, distanceSamples);
        return Math.max(maxA, Math.max(maxB, maxC));
    }
}