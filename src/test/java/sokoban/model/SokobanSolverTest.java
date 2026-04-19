package sokoban.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SokobanSolverTest {

    @Test
    public void testSolve() {
        char[][] map = {
                {'1','1','1','1','1'},
                {'1','4','3','2','1'},
                {'1','1','1','1','1'}
        };

        int steps = SokobanSolver.solveSteps(map);

        assertNotEquals(-1, steps);
    }

    @Test
    public void testImpossibleMapReturnsMinusOne() {
        char[][] map = {
                {'1','1','1','1','1'},
                {'1','4','3','1','1'},
                {'1','1','1','2','1'},
                {'1','1','1','1','1'}
        };

        int steps = SokobanSolver.solveSteps(map);

        assertEquals(-1, steps);
    }

    @Test
    public void testAlreadySolvedMapDoesNotReturnMinusOne() {
        char[][] map = {
                {'1','1','1','1'},
                {'1','4','5','1'},
                {'1','1','1','1'}
        };

        int steps = SokobanSolver.solveSteps(map);

        assertNotEquals(-1, steps);
    }
}