package nekkkkate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static boolean isCorrect(String line) {
        if (line == null || line.trim().isEmpty()) {
            return false;
        }

        String[] parts = line.split(";", -1);

        for (String part : parts) {
            part = part.trim();

            if (!part.matches("\"[^\"]*\"")) {
                return false;
            }

            if (part.equals("\"\"")) {
                return false;
            }
        }

        return true;
    }

    public static void writeOutput(List<Set<String>> groups) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.txt"))) {
            writer.write("In total: " + groups.size());
            writer.newLine();
            writer.newLine();

            int groupNumber = 1;
            for (Set<String> group : groups) {
                writer.write("Group " + groupNumber++);
                writer.newLine();
                for (String row : group) {
                    writer.write(row);
                    writer.newLine();
                }
                writer.newLine();
            }

            System.out.println("Done! Congratulations!:))");

        } catch (IOException e) {
            System.err.println("error:(( " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        ArrayList<String[]> correctLines = new ArrayList<String[]>();
        Set<String> uniqueLines = new HashSet<>();
        try {
            System.out.println("~~~ Hello!!:) My name is Kate! I want to work with you:3 Let's start!! ~~~");
            File file = new File("lng.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                if (!isCorrect(line) || !uniqueLines.add(line)) {
                    continue;
                }
                String[] checkLine = line.split(";", -1);
                correctLines.add(checkLine);
            }

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GroupBuilder groupBuilder = new GroupBuilder(new ArrayList<>(uniqueLines));
        List<Set<String>> allGroups = groupBuilder.buildGroups();

        List<Set<String>> filteredGroups = allGroups.stream()
                .filter(group -> group.size() > 1)
                .sorted((g1, g2) -> Integer.compare(g2.size(), g1.size())) // по убыванию размера
                .toList();

        System.out.println("In total we have " + filteredGroups.size() + " groups");

        long endTime = System.currentTimeMillis();

        System.out.println("~~~ Time ~~~" + (endTime - startTime) + " ms");

        writeOutput(filteredGroups);

        System.out.println("~~~ Thank you:) I'll be happy to have some feedback! My tg: @nekkkkate ~~~");
    }
}