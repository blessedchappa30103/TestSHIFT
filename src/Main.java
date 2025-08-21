import java.io.*;
import java.util.*;

public class Main {
    static String output = ".";
    static String prefix = "";
    static boolean append = false;
    static boolean shortStats = false;
    static boolean fullStats = false;

    static long intCount = 0, floatCount = 0, stringCount = 0;
    static long intMin = Long.MAX_VALUE, intMax = Long.MIN_VALUE, intTotal = 0;
    static double floatMin = Double.MAX_VALUE, floatMax = Double.MIN_VALUE, floatTotal = 0;
    static int minStrLen = Integer.MAX_VALUE, maxStrLen = 0;

    static HashSet<String> resetFile = new HashSet<>();

    public static void main(String[] args) {

        List<String> openFiles = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o") && i+1 < args.length) {
                output = args[i+1];
                i++;
            } else if (args[i].equals("-p") && i+1 < args.length) {
                prefix = args[i+1];
                i++;
            } else if (args[i].equals("-a")) {
                append = true;
            } else if (args[i].equals("-s")) {
                shortStats = true;
            } else if (args[i].equals("-f")) {
                fullStats = true;
            } else if (!args[i].startsWith("-")) {
                openFiles.add(args[i]);
            }
        }

        if (openFiles.isEmpty()) {
            System.out.println("Ошибка: Нет файлов для обработки!");
            return;
        }

        try {
            for (String file : openFiles) {
                readFile(file);
            }

            printStats();

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    static void readFile(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    readLine(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + filename);
        }
    }

    static void readLine(String line) {
        try {
            long num = Long.parseLong(line);
            saveFile("integers", line);
            IntStats(num);
            return;
        } catch (NumberFormatException e) {}

        try {
            double num = Double.parseDouble(line);
            saveFile("floats", line);
            FloatStats(num);
            return;
        } catch (NumberFormatException e) {}

        saveFile("strings", line);
        StringStats(line);
    }

    static void saveFile(String type, String data) {
        String filename = output + File.separator + prefix + type + ".txt";
        File file = new File(filename);

        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!append && !resetFile.contains(type)) {
                if (file.exists()) file.delete();
                resetFile.add(type);
            }

            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(data + "\n");
            }

        } catch (IOException e) {
            System.out.println("Ошибка записи в файл " + filename + ": " + e.getMessage());
        }
    }

    static void IntStats(long value) {
        intCount++;
        intTotal += value;
        if (value < intMin) intMin = value;
        if (value > intMax) intMax = value;
    }

    static void FloatStats(double value) {
        floatCount++;
        floatTotal += value;
        if (value < floatMin) floatMin = value;
        if (value > floatMax) floatMax = value;
    }

    static void StringStats(String value) {
        stringCount++;
        int len = value.length();
        if (len < minStrLen) minStrLen = len;
        if (len > maxStrLen) maxStrLen = len;
    }

    static void printStats() {
        if (!shortStats && !fullStats) return;

        if (intCount > 0) {
            System.out.println("\nЦелые числа: " + intCount);
            if (fullStats) {
                System.out.println("  Минимум: " + intMin);
                System.out.println("  Максимум: " + intMax);
                System.out.println("  Сумма: " + intTotal);
                System.out.printf("  Среднее: %.2f\n", (double)intTotal / intCount);
            }
        }

        if (floatCount > 0) {
            System.out.println("\nВещественные числа: " + floatCount);
            if (fullStats) {
                System.out.println("  Минимум: " + floatMin);
                System.out.println("  Максимум: " + floatMax);
                System.out.println("  Сумма: " + floatTotal);
                System.out.printf("  Среднее: %.2f\n", floatTotal / floatCount);
            }
        }

        if (stringCount > 0) {
            System.out.println("\nСтроки: " + stringCount);
            if (fullStats) {
                System.out.println("  Минимальная длина: " + minStrLen);
                System.out.println("  Максимальная длина: " + maxStrLen);
            }
        }
    }
}