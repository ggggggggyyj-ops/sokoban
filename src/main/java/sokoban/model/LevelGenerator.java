package sokoban.model;

import java.util.*;

public class LevelGenerator {

    private static final Map<Integer, char[][]> CACHE = new HashMap<>();

    public static char[][] generateLevel(int level) {
        if (CACHE.containsKey(level)) {
            return copy(CACHE.get(level));
        }

        int attempt = 0;
        while (true) {
            Random rand = new Random(20260418L + level * 10007L + attempt * 1000003L);
            char[][] map = randomGenerate(level, rand);

            int steps = SokobanSolver.solveSteps(map);

            if (steps >= getMinSteps(level)) {
                CACHE.put(level, copy(map));
                return copy(map);
            }

            attempt++;
        }
    }

    private static char[][] randomGenerate(int level, Random rand) {
        int size = Math.min(6 + level / 5, 10);
        int boxCount = getBoxCount(level);
        char[][] map = new char[size][size];

        buildWall(map);
        addWalls(map, level, rand);

        List<int[]> free = getFreeCells(map);
        Collections.shuffle(free, rand);

        // 放目标
        for (int i = 0; i < boxCount && i < free.size(); i++) {
            int[] p = free.get(i);
            map[p[0]][p[1]] = '2';
        }

        // 放箱子
        int placed = 0;
        int index = boxCount;
        while (placed < boxCount && index < free.size()) {
            int[] p = free.get(index++);
            if (map[p[0]][p[1]] == '0') {
                map[p[0]][p[1]] = '3';
                placed++;
            }
        }

        // 放玩家
        for (int i = index; i < free.size(); i++) {
            int[] p = free.get(i);
            if (map[p[0]][p[1]] == '0') {
                map[p[0]][p[1]] = '4';
                break;
            }
        }

        return map;
    }

    private static int getBoxCount(int level) {
        if (level <= 5) return 1;
        if (level <= 15) return 2;
        if (level <= 30) return 3;
        return 4;
    }

    private static int getMinSteps(int level) {
        return 5 + level * 2;
    }

    private static void buildWall(char[][] map) {
        int n = map.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                map[i][j] = (i == 0 || j == 0 || i == n - 1 || j == n - 1) ? '1' : '0';
            }
        }
    }

    private static List<int[]> getFreeCells(char[][] map) {
        List<int[]> list = new ArrayList<>();
        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map.length - 1; j++) {
                if (map[i][j] == '0') {
                    list.add(new int[]{i, j});
                }
            }
        }
        return list;
    }

    private static void addWalls(char[][] map, int level, Random rand) {
        int count = level;
        for (int i = 0; i < count; i++) {
            int r = rand.nextInt(map.length - 2) + 1;
            int c = rand.nextInt(map.length - 2) + 1;
            if (map[r][c] == '0') {
                map[r][c] = '1';
            }
        }
    }

    private static char[][] copy(char[][] src) {
        char[][] res = new char[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            res[i] = src[i].clone();
        }
        return res;
    }
}