import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    private static int answer = 0;
    private static char[][] board = new char[5][5];
    private static boolean selected[][] = new boolean[5][5];

    private static int[] dx = {-1, 1, 0, 0};
    private static int[] dy = {0, 0, -1, 1};

    private static void input() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i < 5; i++) {
            char[] arr = br.readLine().toCharArray();
            for (int j = 0; j < 5; j++) {
                board[i][j] = arr[j];
            }
        }
    }

    // 범위 확인
    private static boolean isBound(int x, int y) {
        if (0 <= x && x < 5 && 0 <= y && y < 5) {
            return true;
        }
        return false;
    }

    // 다음으로 이동가능한지 확인
    private static boolean canMove(int nx, int ny) {
        if (isBound(nx, ny)) {
            // 다음으로 이동할 곳이 선택된 후보인 경우
            if (selected[nx][ny]) {
                return true;
            }
        }
        return false;
    }

    private static boolean bfs(int x, int y) {
        Queue<int[]> q = new LinkedList<>();
        boolean[][] visited = new boolean[5][5];

        q.add(new int[]{x, y});
        visited[x][y] = true;

        int cnt = 1;
        while(!q.isEmpty()) {
            int[] cur = q.poll();

            // 상하좌우 인접 탐색
            for (int k = 0; k < 4; k++) {
                int nx = cur[0] + dx[k];
                int ny = cur[1] + dy[k];

                // 다음으로 이동가능하고 방문하지 않은 경우 큐에 추가
                if (canMove(nx, ny) && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    q.add(new int[]{nx, ny});
                    cnt++;
                }
            }
        }
        return cnt == 7;
    }

    private static void solution(int idx, int sCnt, int yCnt, int total) {
        // 임도연파가 4명 이상인 경우 수행하지 않고 0을 반환하여 종료
        if (yCnt >= 4) {
            return;
        }

        // 7명을 채운 경우
        if (total == 7) {
            // 7명이 모두 인접해 있는지 확인
            int curIdx = idx - 1;
            if (bfs(curIdx/5, curIdx%5)) {
                answer++;
            }
            return;
        }

        // 모든 조합을 탐색
        for (int i = idx; i < 25; i++) {
            int x = i/5;
            int y = i%5;
            selected[x][y] = true;

            // 이다솜파가 선택된 경우
            if (board[x][y] == 'S') {
                solution(i+1, sCnt+1, yCnt, total+1);
            }
            // 임도연파가 선택된 경우
            else {
                solution(i+1, sCnt, yCnt+1, total+1);
            }
            selected[x][y] = false;
        }
    }

    public static void main(String[] args) throws Exception {
        input();
        solution(0,0, 0, 0);
        System.out.println(answer);
    }
}