import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    // CCTV 유형별 감시 가능한 방향 조합
    private static final int[][][] CCTV_DIRS = {
            {},                                 // 인덱스 편의를 위해 0번은 사용 안 함
            { {0}, {1}, {2}, {3} },             // 타입1
            { {0,2}, {1,3} },                   // 타입2
            { {0,1}, {1,2}, {2,3}, {3,0} },     // 타입3
            { {0,1,2}, {1,2,3}, {2,3,0}, {3,0,1} }, // 타입4
            { {0,1,2,3} }                       // 타입5
    };

    // CCTV 정보
    private static class CCTV {
        int x, y, type;
        int[] dirs;  // 이 CCTV가 현재 선택한 방향 조합

        CCTV(int x, int y, int type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    private static int n, m;
    private static int[][] board = new int[8][8];
    private static List<CCTV> cctvList = new ArrayList<>();
    private static int answer = Integer.MAX_VALUE;

    private static final int[] dx = { 0, 1, 0, -1 };
    private static final int[] dy = { 1, 0, -1, 0 };

    private static void input() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < m; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());

                // CCTV
                if (1 <= board[i][j] && board[i][j] <= 5) {
                    cctvList.add(new CCTV(i, j, board[i][j]));
                }
            }
        }
    }

    // 설정된 방향에 따라 사각지대 갯수 계산
    private static void simulateAndCount() {
        // 보드 복사
        int[][] copy = new int[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, m);
        }

        // 모든 CCTV의 감시 표시
        for (CCTV c : cctvList) {
            for (int d : c.dirs) {
                mark(copy, c.x, c.y, d);
            }
        }

        // 사각지대(0) 개수 세기
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (copy[i][j] == 0) cnt++;
            }
        }
        answer = Math.min(answer, cnt);
    }

    // 감시받는 영역 확인
    private static void mark(int[][] b, int x, int y, int d) {
        int nx = x + dx[d], ny = y + dy[d];
        while (0 <= nx && nx < n && 0 <= ny && ny < m && b[nx][ny] != 6) {
            if (b[nx][ny] == 0) {
                b[nx][ny] = -1;
            }
            nx += dx[d];
            ny += dy[d];
        }
    }

    // 모든 CCTV에 대해 방향 조합을 하나씩 선택하며 재귀 탐색
    private static void solution(int idx) {
        if (idx == cctvList.size()) {
            simulateAndCount();
            return;
        }

        CCTV c = cctvList.get(idx);

        // 각 CCTV 나오는 방향들의 조합 선택
        for (int[] dirs : CCTV_DIRS[c.type]) {
            c.dirs = dirs;
            solution(idx + 1);
        }
    }

    public static void main(String[] args) throws Exception {
        input();
        solution(0);
        System.out.println(answer);
    }
}