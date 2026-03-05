import java.io.FileWriter;
import java.util.List;

public class Percistencia {

    public void salvarJSON(List<Planet> planetas) throws Exception {
        try (FileWriter writer = new FileWriter("planetas.json")) {
            writer.write("[\n");
            for (int i = 0; i < planetas.size(); i++) {
                Planet planeta = planetas.get(i);
                writer.write("  {\n");
                writer.write("    \"nome\": \"" + planeta.getName() + "\",\n");
                writer.write("    \"orbitalPeriod\": " + planeta.getOrbitalPeriod() + ",\n");
                writer.write("    \"radiusEarth\": " + planeta.getRadius() + ",\n");
                writer.write("    \"massEarth\": " + planeta.getMass() + "\n");
                writer.write("  }");
                if (i < planetas.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("]");
        }
    }

    public void salvarXML(List<Planet> planetas) throws Exception {
        try (FileWriter writer = new FileWriter("planetas.xml")) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<planetas>\n");
            for (Planet planeta : planetas) {
                writer.write("  <planeta>\n");
                writer.write("    <nome>" + planeta.getName() + "</nome>\n");
                writer.write("    <orbitalPeriod>" + planeta.getOrbitalPeriod() + "</orbitalPeriod>\n");
                writer.write("    <radiusEarth>" + planeta.getRadius() + "</radiusEarth>\n");
                writer.write("    <massEarth>" + planeta.getMass() + "</massEarth>\n");
                writer.write("  </planeta>\n");
            }
            writer.write("</planetas>");
        }
    }
}