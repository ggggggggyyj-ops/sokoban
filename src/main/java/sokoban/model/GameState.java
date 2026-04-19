package sokoban.model;

import java.util.Stack;

public class GameState {

    private char[][] map;
    private char[][] originalMap;

    private int playerRow;
    private int playerCol;
    private int moves;

    private final Stack<char[][]> history = new Stack<>();

    // ===== 加载关卡 =====
    public void loadLevel(char[][] newMap) {
        originalMap = copy(newMap);
        map = copy(newMap);

        moves = 0;
        history.clear();

        findPlayer();
    }

    // ===== 重开 =====
    public void restart() {
        map = copy(originalMap);
        history.clear();
        moves = 0;
        findPlayer();
    }

    private void findPlayer() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == '4' || map[i][j] == '6') {
                    playerRow = i;
                    playerCol = j;
                    return;
                }
            }
        }
    }

    // ===== 移动 =====
    public boolean move(Direction d) {
        int nr = playerRow + d.dx;
        int nc = playerCol + d.dy;

        int nnr = nr + d.dx;
        int nnc = nc + d.dy;

        char next = map[nr][nc];

        // 墙
        if (next == '1') {
            return false;
        }

        // 先存档，便于撤回
        history.push(copy(map));

        // 推箱子
        if (next == '3' || next == '5') {
            char next2 = map[nnr][nnc];

            if (!(next2 == '0' || next2 == '2')) {
                history.pop();
                return false;
            }

            // 箱子前进
            map[nnr][nnc] = (next2 == '2') ? '5' : '3';

            // 箱子原位置恢复
            map[nr][nc] = (next == '5') ? '2' : '0';
        }
        // 普通行走
        else if (!(next == '0' || next == '2')) {
            history.pop();
            return false;
        }

        // 玩家离开原位置
        char cur = map[playerRow][playerCol];
        map[playerRow][playerCol] = (cur == '6') ? '2' : '0';

        // 玩家进入新位置
        map[nr][nc] = (map[nr][nc] == '2') ? '6' : '4';

        playerRow = nr;
        playerCol = nc;

        moves++;
        return true;
    }

    // ===== 撤回 =====
    public void undo() {
        if (!history.isEmpty()) {
            map = history.pop();
            findPlayer();
            moves = Math.max(0, moves - 1);
        }
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public char[][] getMap() {
        return map;
    }

    public int getMoves() {
        return moves;
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    private char[][] copy(char[][] src) {
        char[][] res = new char[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            res[i] = src[i].clone();
        }
        return res;
    }
}