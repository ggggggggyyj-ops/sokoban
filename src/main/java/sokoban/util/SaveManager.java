package sokoban.util;

import java.io.*;

public class SaveManager {

    private static final String FILE = "save.dat";

    public static void save(int maxLevel) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE))) {

            oos.writeObject(new SaveData(maxLevel));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE))) {

            SaveData data = (SaveData) ois.readObject();
            return data.maxUnlockedLevel;

        } catch (Exception e) {
            return 1; // 默认只解锁第一关
        }
    }
}