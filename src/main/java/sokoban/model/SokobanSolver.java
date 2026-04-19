package sokoban.model;

import java.util.*;

public class SokobanSolver {

    static class State {
        int px, py;
        Set<String> boxes;

        State(int px, int py, Set<String> b) {
            this.px = px;
            this.py = py;
            this.boxes = new HashSet<>(b);
        }

        String encode() {
            List<String> list = new ArrayList<>(boxes);
            Collections.sort(list);
            return px + "," + py + "|" + String.join(";", list);
        }
    }

    public static int solveSteps(char[][] map) {
        List<Direction> path = solvePath(map);
        return path == null ? -1 : path.size();
    }

    // ===== 新增：返回自动演示路径 =====
    public static List<Direction> solvePath(char[][] map) {

        int n = map.length, m = map[0].length;

        Set<String> boxes = new HashSet<>();
        Set<String> targets = new HashSet<>();
        int px = 0, py = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                char cell = map[i][j];

                if (cell == '3' || cell == '5') boxes.add(i + "," + j);
                if (cell == '2' || cell == '5' || cell == '6') targets.add(i + "," + j);
                if (cell == '4' || cell == '6') {
                    px = i;
                    py = j;
                }
            }
        }

        Queue<State> q = new LinkedList<>();
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Map<String, Direction> moveRecord = new HashMap<>();

        State start = new State(px, py, boxes);
        String startCode = start.encode();

        q.add(start);
        dist.put(startCode, 0);

        int[][] dir = {{1,0},{-1,0},{0,1},{0,-1}};
        Direction[] dirEnum = {
                Direction.DOWN,
                Direction.UP,
                Direction.RIGHT,
                Direction.LEFT
        };

        while (!q.isEmpty()) {

            State cur = q.poll();
            String curCode = cur.encode();
            int steps = dist.get(curCode);

            if (cur.boxes.equals(targets)) {
                return buildPath(curCode, parent, moveRecord);
            }

            for (int k = 0; k < 4; k++) {

                int[] d = dir[k];
                Direction moveDir = dirEnum[k];

                int nx = cur.px + d[0];
                int ny = cur.py + d[1];

                if (!inBounds(nx, ny, map)) continue;
                if (map[nx][ny] == '1') continue;

                String nextPos = nx + "," + ny;

                // ===== 推箱子 =====
                if (cur.boxes.contains(nextPos)) {

                    int bx = nx + d[0];
                    int by = ny + d[1];

                    if (!inBounds(bx, by, map)) continue;

                    String newBox = bx + "," + by;

                    if (map[bx][by] == '1' || cur.boxes.contains(newBox)) continue;

                    // 剪枝
                    if (isDeadCorner(bx, by, map, targets)) continue;
                    if (isWallDead(bx, by, map, targets)) continue;
                    if (isSquareDead(bx, by, map, cur.boxes, targets, nextPos, newBox)) continue;

                    Set<String> newBoxes = new HashSet<>(cur.boxes);
                    newBoxes.remove(nextPos);
                    newBoxes.add(newBox);

                    State next = new State(nx, ny, newBoxes);
                    String code = next.encode();

                    if (!dist.containsKey(code)) {
                        dist.put(code, steps + 1);
                        parent.put(code, curCode);
                        moveRecord.put(code, moveDir);
                        q.add(next);
                    }

                } else {
                    State next = new State(nx, ny, cur.boxes);
                    String code = next.encode();

                    if (!dist.containsKey(code)) {
                        dist.put(code, steps + 1);
                        parent.put(code, curCode);
                        moveRecord.put(code, moveDir);
                        q.add(next);
                    }
                }
            }
        }

        return null;
    }

    private static List<Direction> buildPath(String endCode,
                                             Map<String, String> parent,
                                             Map<String, Direction> moveRecord) {
        LinkedList<Direction> path = new LinkedList<>();
        String cur = endCode;

        while (parent.containsKey(cur)) {
            path.addFirst(moveRecord.get(cur));
            cur = parent.get(cur);
        }

        return path;
    }

    private static boolean inBounds(int x, int y, char[][] map) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }

    // ===== 1. 墙角死锁 =====
    private static boolean isDeadCorner(int x, int y, char[][] map, Set<String> targets) {

        if (targets.contains(x + "," + y)) return false;

        if (isWall(x - 1, y, map) && isWall(x, y - 1, map)) return true;
        if (isWall(x - 1, y, map) && isWall(x, y + 1, map)) return true;
        if (isWall(x + 1, y, map) && isWall(x, y - 1, map)) return true;
        if (isWall(x + 1, y, map) && isWall(x, y + 1, map)) return true;

        return false;
    }

    // ===== 2. 贴墙死锁 =====
    private static boolean isWallDead(int x, int y, char[][] map, Set<String> targets) {

        if (targets.contains(x + "," + y)) return false;

        if (isWall(x, y - 1, map) && isWall(x, y + 1, map)) return true;
        if (isWall(x - 1, y, map) && isWall(x + 1, y, map)) return true;

        return false;
    }

    private static boolean isWall(int x, int y, char[][] map) {
        if (!inBounds(x, y, map)) return true;
        return map[x][y] == '1';
    }

    // ===== 3. 2x2 方块死锁 =====
    private static boolean isSquareDead(int x, int y, char[][] map,
                                        Set<String> oldBoxes,
                                        Set<String> targets,
                                        String oldBoxPos,
                                        String newBoxPos) {

        if (targets.contains(x + "," + y)) return false;

        Set<String> boxes = new HashSet<>(oldBoxes);
        boxes.remove(oldBoxPos);
        boxes.add(newBoxPos);

        int[][] check = {
                {0,0},{0,-1},{-1,0},{-1,-1}
        };

        for (int[] d : check) {

            int r = x + d[0];
            int c = y + d[1];

            int cnt = 0;
            boolean hasTarget = false;

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {

                    int nr = r + i;
                    int nc = c + j;

                    if (!inBounds(nr, nc, map)) {
                        cnt++;
                        continue;
                    }

                    if (map[nr][nc] == '1' || boxes.contains(nr + "," + nc)) {
                        cnt++;
                    }

                    if (targets.contains(nr + "," + nc)) {
                        hasTarget = true;
                    }
                }
            }

            if (cnt == 4 && !hasTarget) return true;
        }

        return false;
    }
}