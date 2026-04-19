package sokoban.util;

import java.io.Serializable;

public class SaveData implements Serializable {

    public int maxUnlockedLevel;

    public SaveData(int maxUnlockedLevel) {
        this.maxUnlockedLevel = maxUnlockedLevel;
    }
}