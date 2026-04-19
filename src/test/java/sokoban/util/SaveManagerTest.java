package sokoban.util;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class SaveManagerTest {

    @Test
    public void testSaveLoad() {
        SaveManager.save(5);

        int loaded = SaveManager.load();

        assertEquals(5, loaded);
    }

    @Test
    public void testSaveOverwrite() {
        SaveManager.save(3);
        SaveManager.save(7);

        int loaded = SaveManager.load();

        assertEquals(7, loaded);
    }

    @Test
    public void testLoadAfterDeletingSaveFile() {
        File file = new File("save.dat");
        if (file.exists()) {
            file.delete();
        }

        int loaded = SaveManager.load();

        // 你的项目如果默认不是 1，这里把 1 改成你项目真实默认值
        assertEquals(1, loaded);
    }
}