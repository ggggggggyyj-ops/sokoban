package sokoban.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    @Test
    public void testMoveUpSuccessfully() {
        GameState state = new GameState();

        char[][] map = {
                {'1','1','1','1'},
                {'1','0','0','1'},
                {'1','4','2','1'},
                {'1','1','1','1'}
        };

        state.loadLevel(map);

        boolean moved = state.move(Direction.UP);

        assertTrue(moved);
    }

    @Test
    public void testCannotMoveIntoWall() {
        GameState state = new GameState();

        char[][] map = {
                {'1','1','1','1'},
                {'1','1','0','1'},
                {'1','4','2','1'},
                {'1','1','1','1'}
        };

        state.loadLevel(map);

        boolean moved = state.move(Direction.UP);

        assertFalse(moved);
    }

    @Test
    public void testPushBoxSuccessfully() {
        GameState state = new GameState();

        char[][] map = {
                {'1','1','1','1','1'},
                {'1','0','0','0','1'},
                {'1','4','3','2','1'},
                {'1','1','1','1','1'}
        };

        state.loadLevel(map);

        boolean moved = state.move(Direction.RIGHT);

        assertTrue(moved);
    }

    @Test
    public void testCannotPushBoxIntoWall() {
        GameState state = new GameState();

        char[][] map = {
                {'1','1','1','1','1'},
                {'1','0','0','0','1'},
                {'1','4','3','1','1'},
                {'1','1','1','1','1'}
        };

        state.loadLevel(map);

        boolean moved = state.move(Direction.RIGHT);

        assertFalse(moved);
    }

    @Test
    public void testUndoAfterMove() {
        GameState state = new GameState();

        char[][] map = {
                {'1','1','1','1'},
                {'1','0','0','1'},
                {'1','4','2','1'},
                {'1','1','1','1'}
        };

        state.loadLevel(map);
        boolean moved = state.move(Direction.UP);

        assertTrue(moved);

        // 只要项目里有 undo()，这里就能验证撤回不会报错
        assertDoesNotThrow(state::undo);
    }

    @Test
    public void testRestartAfterMove() {
        GameState state = new GameState();

        char[][] map = {
                {'1','1','1','1'},
                {'1','0','0','1'},
                {'1','4','2','1'},
                {'1','1','1','1'}
        };

        state.loadLevel(map);
        boolean moved = state.move(Direction.UP);

        assertTrue(moved);

        // 只要项目里有 restart()，这里就能验证重开不会报错
        assertDoesNotThrow(state::restart);
    }
}