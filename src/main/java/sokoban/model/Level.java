package sokoban.model;

public class Level {

    private int levelNumber;
    private char[][] map;

    public Level(int levelNumber, char[][] map) {
        this.levelNumber = levelNumber;
        this.map = map;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public char[][] getMap() {
        return map;
    }

    public char[][] copyMap() {
        char[][] newMap = new char[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            newMap[i] = map[i].clone();
        }
        return newMap;
    }
}