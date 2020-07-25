import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

//        long folderSize = calculateFolderSize("/home/puena/Documents/");
        long folderSize = calculateFolderSize("/sys/power/");
//        long folderSize = calculateFolderSize("/sys/power/wake_unlock");

        printHRSize(folderSize);

    }

    // Calculate folder size
    public static long calculateFolderSize(String path) {
        // Init
        Path folderPath = null;
        long folderSize = 0L;

        // Get path
        try {
            folderPath = Paths.get(path);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Не верный путь файла!");
        }

        // Path should be dir
        if (folderPath == null || ! Files.isDirectory(folderPath)) {
            System.out.println("По указанному пути нет папки.");
            return -1;
        }

        // Sum file sizes in folders
        try {
            folderSize = Files.walk(folderPath)
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Path " + p);
                            return 0L;
                        }
                    })
                    .reduce(0L, Long::sum);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Size folder in bytes
        return folderSize;
    }

    // Print human readable value of size
    static void printHRSize(Long sizeInBytes) {

        // Return if not size
        if (sizeInBytes < 0) return;

        if (sizeInBytes > GIGABYTE) {
            System.out.printf("Размер папки: %.3f Гб (%d)\n", (sizeInBytes / GIGABYTE), sizeInBytes);
        } else if (sizeInBytes > MEGABYTE) {
            System.out.printf("Размер папки: %.3f Мб (%d)\n", (sizeInBytes / MEGABYTE), sizeInBytes);
        } else if (sizeInBytes > KILOBYTE) {
            System.out.printf("Размер папки: %.3f Кб (%d)\n", (sizeInBytes / KILOBYTE), sizeInBytes);
        } else {
            System.out.println(sizeInBytes + " байт");
        }
    }

    private static final double  GIGABYTE = 1000 * 1000 * 1000;
    private static final double  MEGABYTE = 1000 * 1000;
    private static final double  KILOBYTE = 1000;

}
