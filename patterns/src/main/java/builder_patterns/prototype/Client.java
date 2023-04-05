package builder_patterns.prototype;

import java.util.Objects;

public class Client {
    public static void main(String[] args) {
        DataFile dataFile = new DataFile("data.txt", new int[][]{{1, 2, 3}, {4, 5, 6}});
        DataFile dataFileCopy = dataFile.clone();

        System.out.println(dataFileCopy.equals(dataFile));
        System.out.println(dataFileCopy == dataFile);
        System.out.println(dataFileCopy.getName().equals(dataFile.getName()));
        System.out.println(dataFileCopy.getX() == dataFile.getX());
    }
}
