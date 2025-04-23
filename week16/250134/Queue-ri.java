import java.util.*;

class Solution {
    int dy[] = {-1, 0, 0, 1};
    int dx[] = {0, -1, 1, 0};
    
    int n, m; // maze size
    
    int rdy, rdx; // red destination y, x
    int bdy, bdx; // blue destination y, x
    
    class State {
        int ry, rx;
        int by, bx;
        int[][] snapshot;
        int turn;
        
        State(int ry, int rx, int by, int bx, int[][] ss, int turn) {
            this.ry = ry;
            this.rx = rx;
            this.by = by;
            this.bx = bx;
            // deepcopy
            snapshot = new int[n][m];
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < m; ++j)
                    snapshot[i][j] = ss[i][j];
            this.turn = turn;
        }
        
//         void debug() {
//             System.out.println("red: " + ry + " " + rx + " / blue: " + by + " " + bx);
//             for (int[] line : snapshot) {
//                 for (int x : line)
//                     System.out.print(x + " ");
//                 System.out.println();
//             }
            
//             System.out.println("turn: " + turn);
//             System.out.println();
//         }
    }
    
    public boolean canMove(int y, int x, int myType, int[][] ss) {
        if (y < 0 || n <= y || x < 0 || m <= x) return false; // out of bound
        if (ss[y][x] == 5) return false; // wall or closed destination
        if (-myType == ss[y][x] || ss[y][x] == -3) return false; // visited
        return true;
    }
    
    public int solution(int[][] maze) {
        n = maze.length;
        m = maze[0].length;
        // bfs인데, 큐 원소가 하나의 퍼즐판 스냅샷임
        int ry = -1;
        int rx = -1;
        int by = -1;
        int bx = -1;
        for (int i=0; i<n; ++i) {
            for(int j=0; j<m; ++j) {
                switch (maze[i][j]) {
                    case 1: ry = i; rx = j; break;
                    case 2: by = i; bx = j; break;
                    case 3: rdy = i; rdx = j; break;
                    case 4: bdy = i; bdx = j; break;
                }
            }
        }
        
        // init queue
        Queue<State> q = new LinkedList<>();
        maze[ry][rx] = -1; // 시작점 빨강 빗금칠
        maze[by][bx] = -2; // 시작점 파랑 빗금칠
        State state = new State(ry, rx, by, bx, maze, 0);
        q.add(state);
        
        // bfs
        while (!q.isEmpty()) {
            State s = q.poll();
            //s.debug();
            if ((s.ry == rdy && s.rx == rdx) && (s.by == bdy && s.bx == bdx))
                return s.turn;
            
            // move red
            List<State> nxtStateList = new ArrayList<>();
            
            if (!(s.ry == rdy && s.rx == rdx)) { // if not arrived
                for (int i = 0; i < 4; ++i) {
                    int nry = s.ry + dy[i];
                    int nrx = s.rx + dx[i];
                    if (!canMove(nry, nrx, 1, s.snapshot)) continue; // 벽 or 이전 방문 지점이면 안됨
                    
                    // State ns = s; --> 얕은 복사라 ㄴㄴ
                    State ns = new State(s.ry, s.rx, s.by, s.bx, s.snapshot, s.turn+1);
                    
                    ns.ry = nry; // 빨강 좌표 갱신
                    ns.rx = nrx;
                    ns.snapshot[nry][nrx] -= 1; // 새로 가는 곳 빨강 빗금칠
                    if (nry == rdy && nrx == rdx) ns.snapshot[nry][nrx] = 5; // 도착지면 벽으로 처리
                    nxtStateList.add(ns);
                }
            }
            else { // red arrived
                State ns = new State(s.ry, s.rx, s.by, s.bx, s.snapshot, s.turn+1);
                nxtStateList.add(ns);
            }
            
            // move blue
            if (!(s.by == bdy && s.bx == bdx)) { // if not arrived
                for (State ns : nxtStateList) {
                    int nry = ns.ry;
                    int nrx = ns.rx;
                    for (int i = 0; i < 4; ++i) {
                        int nby = s.by + dy[i];
                        int nbx = s.bx + dx[i];
                        if (!canMove(nby, nbx, 2, s.snapshot)) continue; // 벽 or 이전 방문 지점이면 안됨
                        if (nry == nby && nrx == nbx) continue; // 같은 지점에 둘 다 오면 안됨
                        if (nry == s.by && nrx == s.bx && nby == s.ry && nbx == s.rx) continue; // 서로 크로스 이동 안됨

                        // 모든 제한 조건 통과하면
                        State nscp = new State(ns.ry, ns.rx, ns.by, ns.bx, ns.snapshot, ns.turn);
                        nscp.by = nby; // 파랑 좌표 갱신
                        nscp.bx = nbx;
                        nscp.snapshot[nby][nbx] -= 2; // 새로 가는 곳 파랑 빗금칠
                        if (nby == bdy && nbx == bdx) nscp.snapshot[nby][nbx] = 5; // 도착지면 벽으로 처리
                        
                        q.add(nscp);
                    }
                }
            }
            else { // blue arrived
                for (State ns : nxtStateList)
                    q.add(ns);
            }
            
        } // end of bfs
        
        return 0;
    }
}