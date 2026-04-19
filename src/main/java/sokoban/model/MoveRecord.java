package sokoban.model;

public class MoveRecord {

    public int pr, pc; // 玩家原位置
    public int nr, nc; // 玩家新位置

    public int boxFromR, boxFromC;
    public int boxToR, boxToC;

    public boolean pushedBox;

    public MoveRecord(int pr, int pc, int nr, int nc) {
        this.pr = pr;
        this.pc = pc;
        this.nr = nr;
        this.nc = nc;
        this.pushedBox = false;
    }

    public void setBoxMove(int fr, int fc, int tr, int tc) {
        this.pushedBox = true;
        this.boxFromR = fr;
        this.boxFromC = fc;
        this.boxToR = tr;
        this.boxToC = tc;
    }
}