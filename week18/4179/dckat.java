import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    private static int r, c;
    private static char[][] board;      // 격자 정보
    private static int[][] fire;        // 불의 확산 정보
    private static int[][] jihoon;      // 지훈이의 이동 정보

    // 불과 지훈이의 BFS 기반 이동정보 queue
    private static Queue<int[]> fQueue = new LinkedList<>();
    private static Queue<int[]> jQueue = new LinkedList<>();

    private static int[] dx = {-1, 1, 0, 0};
    private static int[] dy = {0, 0, -1, 1};

    private static void input() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());

        board = new char[r][c];
        fire = new int[r][c];
        jihoon = new int[r][c];

        // 불의 확산과 지훈이의 위치정보들을 -1로 초기화
        for(int i = 0; i < r; i++) {
            Arrays.fill(fire[i], -1);
            Arrays.fill(jihoon[i], -1);
        }

        for (int i = 0; i < r; i++) {
            char[] arr = br.readLine().toCharArray();
            for (int j = 0; j < c; j++) {
                board[i][j] = arr[j];

                // 지훈이의 위치
                if (board[i][j] == 'J') {
                    jihoon[i][j] = 0;
                    jQueue.add(new int[]{i, j});
                }
                // 불의 위치
                else if (board[i][j] == 'F') {
                    fire[i][j] = 0;
                    fQueue.add(new int[]{i, j});
                }
            }
        }
    }

    // 범위 확인
    private static boolean isBound(int x, int y) {
        if (0 <= x && x < r && 0 <= y && y < c) {
            return true;
        }
        return false;
    }

    // 다음으로 불의 확산이 가능한지 확인
    private static boolean canFire(int nx, int ny) {
        if (isBound(nx, ny) && board[nx][ny] != '#' && fire[nx][ny] == -1) {
            return true;
        }
        return false;
    }

    // 지훈이의 이동이 가능한지 확인
    private static boolean canMove(int nx, int ny, int nTime) {
        // 경계 및 방문하지 않았는지 확인
        if (isBound(nx, ny) && board[nx][ny] != '#' && jihoon[nx][ny] == -1) {
            // 불이 확산되지 않은 지역이거나 불이 확산되지 전에 이동하는 경우
            if (fire[nx][ny] == -1 || fire[nx][ny] > nTime) {
                return true;
            }
        }
        return false;
    }

    // 가장자리에 도착한 경우
    private static boolean isEdge(int x, int y) {
        if (x == 0 || x == r-1 || y == 0 || y == c-1) {
            return true;
        }
        return false;
    }

    private static int solution() {
        // 불의 확산 처리
        while(!fQueue.isEmpty()) {
            int[] cur = fQueue.poll();
            int x = cur[0];
            int y = cur[1];
            int time = fire[x][y];

            // 상하좌우 인접한 칸 처리
            for (int k = 0; k < 4; k++) {
                int nx = cur[0] + dx[k];
                int ny = cur[1] + dy[k];
                int nTime = time + 1;

                // 다음칸이 확산 가능한 경우
                if (canFire(nx, ny)) {
                    fQueue.add(new int[]{nx, ny});
                    fire[nx][ny] = nTime;
                }
            }
        }

        // 지훈이의 이동
        while(!jQueue.isEmpty()) {
            int[] cur = jQueue.poll();

            int x = cur[0];
            int y = cur[1];
            int time = jihoon[x][y];

            // 가장 자리에 도착한 경우
            if (isEdge(x, y)) {
                return jihoon[x][y] + 1;
            }

            // 상하좌우 인접한 칸으로 이동 확인
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];
                int nTime = time + 1;

                // 지훈이의 이동
                if (canMove(nx, ny, nTime)) {
                    jQueue.add(new int[]{nx, ny});
                    jihoon[nx][ny] = nTime;
                }
            }
        }

        // 탈출이 불가능하면 -1 반환
        return -1;
    }

    public static void main(String[] args) throws Exception {
        input();
        int answer = solution();

        if (answer != -1) {
            System.out.println(answer);
        }
        else {
            System.out.println("IMPOSSIBLE");
        }
    }
}