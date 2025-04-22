import java.util.*;

class Solution {
    
    int answer = Integer.MAX_VALUE;
    int dx[] = {-1, 1, 0, 0};
    int dy[] = {0, 0, -1, 1};
    int[][] map;
    boolean[][] rVisited, bVisited;
    
    public boolean isBound(int x, int y) {
        if (0 <= x && x < map.length && 0 <= y && y < map[0].length) {
            return true;
        }
        return false;
    }
    
    public boolean canMove(int x, int y, String mode) {
        // 범위 내에 있는지와 벽인지 확인
        if (isBound(x, y) && map[x][y] != 5) {
            // 빨간 수레. 파란 수레 방문 여부 체크
            if (mode.equals("RED") && !rVisited[x][y]) {
                return true;
            }
            else if (mode.equals("BLUE") && !bVisited[x][y]) {
                return true;
            }
        }
        return false;
    }
    
    public void move(int rx, int ry, int bx, int by, int cnt) {
        // 이미 최소횟수를 넘은 경우
        if (cnt >= answer) {
            return;
        }
        
        // 수레 모두 도착점에 도달
        if (map[rx][ry] == 3 && map[bx][by] == 4) {
            answer = Math.min(answer, cnt);
            return;
        }
        
        ArrayList<int[]> r_list = new ArrayList<>();
        ArrayList<int[]> b_list = new ArrayList<>();
        
        // 빨간 수레 이동가능 케이스
        if (map[rx][ry] != 3) {
            for (int k = 0; k < 4; k++) {
                int nx = rx + dx[k];
                int ny = ry + dy[k];
                
                if (canMove(nx, ny, "RED")) {
                    r_list.add(new int[] {nx, ny});
                }
            }
        }
        // 이미 도착점
        else {
            r_list.add(new int[] {rx, ry});
        }
        
        // 파란 수레 이동가능 케이스
        if (map[bx][by] != 4) {
            for (int k = 0; k < 4; k++) {
                int nx = bx + dx[k];
                int ny = by + dy[k];
                
                if (canMove(nx, ny, "BLUE")) {
                    b_list.add(new int[] {nx, ny});
                }
            }
        }
        // 이미 도착점
        else {
            b_list.add(new int[] {bx, by});
        }
        
        // 이동 가능한 케이스들로 브루트 포스 (재귀 방식)
        for (int i = 0; i < r_list.size(); i++) {
            int[] r_next = r_list.get(i);
            for (int j = 0; j < b_list.size(); j++) {
                int[] b_next = b_list.get(j);
                
                // 같은 칸으로 이동 불가
                if (r_next[0] == b_next[0] && r_next[1] == b_next[1]) {
                    continue;
                }
                // 서로 자리 바꾸기 불가
                if (rx == b_next[0] && ry == b_next[1] && bx == r_next[0] && by == r_next[1]) {
                    continue;
                }
                
                rVisited[r_next[0]][r_next[1]] = true;
                bVisited[b_next[0]][b_next[1]] = true;
                move(r_next[0], r_next[1], b_next[0], b_next[1], cnt + 1);
                rVisited[r_next[0]][r_next[1]] = false;
                bVisited[b_next[0]][b_next[1]] = false;
            }
        }
    }
     
    public int solution(int[][] maze) {
        int n = maze.length;
        int m = maze[0].length;
        int rx = -1;
        int ry = -1;
        int bx = -1;
        int by = -1;
        
        map = new int[n][m];
        rVisited = new boolean[n][m];
        bVisited = new boolean[n][m];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                map[i][j] = maze[i][j];
                
                // Red 시작
                if (maze[i][j] == 1) {
                    rx = i;
                    ry = j;
                    rVisited[i][j] = true;
                }
                
                // Blue 시작
                if (maze[i][j] == 2) {
                    bx = i;
                    by = j;
                    bVisited[i][j] = true;
                }
            }
        }
        move(rx, ry, bx, by, 0);
        
        return answer == Integer.MAX_VALUE ? 0 : answer;
    }
    
}