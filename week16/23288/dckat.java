import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    // 입력 변수
    public static int n, m, k;
    public static int[][] board = new int[21][21];

    // 방향 정보
    public static int[] dx = {0, 1, 0, -1};
    public static int[] dy = {1, 0, -1, 0};
    public static int dir = 0;      // 초기방향: 동쪽

    // 기타 활용 변수 (주사위. 연속 영역 넘버링 등)
    public static int[] dice = {1, 6, 2, 5, 3, 4};
    public static int[][] idBoard = new int[21][21];
    public static List<Integer> idScore = new ArrayList<>();
    public static int answer = 0;

    // 입력
    public static void input() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        for (int i = 1; i <= n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= m; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }

    // 연속되는 영역을 찾아서 그룹 넘버링
    public static void setId() {
        int idCounter = 0;                                  // 연속된 영역의 그룹 넘버링 변수
        boolean[][] visited = new boolean[n+1][m+1];        // 방문 여부

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // 방문 체크
                if (!visited[i][j]) {
                    visited[i][j] = true;
                    idBoard[i][j] = idCounter;

                    // 연속된 영역의 넘버링을 위해 BFS 시작점 작업
                    Queue<int[]> q = new LinkedList<>();
                    q.add(new int[] {i, j});
                    int cnt = 1;
                    int num = board[i][j];

                    // 비어있을 때 까지 연속되어 있는 영역 탐색
                    while (!q.isEmpty()) {
                        int curX = q.peek()[0];
                        int curY = q.peek()[1];
                        q.poll();

                        for (int k = 0; k < 4; k++) {
                            int nx = curX + dx[k];
                            int ny = curY + dy[k];

                            // 인접한 곳에 방문하지 않은 같은 값을 찾은 경우
                            if (isBound(nx, ny) && !visited[nx][ny] && board[nx][ny] == num) {
                                q.add(new int[] {nx, ny});
                                idBoard[nx][ny] = idCounter;
                                visited[nx][ny] = true;
                                cnt++;
                            }
                        }
                    }

                    // 끝난 후 연속된 영역 갯수 저장 및 넘버링 번호 증가
                    idScore.add(cnt);
                    idCounter++;
                }
            }
        }

    }

    // 영역 확인
    public static boolean isBound(int x, int y) {
        if (1 <= x && x <= n && 1 <= y && y <= m) {
            return true;
        }
        return false;
    }

    // 점수 계산
    public static int getScore(int x, int y) {
        // 사전에 구현된 그룹의 번호를 구하여
        // 해당 그룹의 연속된 영역 갯수를 O(1)로 가져오기
        // k번의 BFS 수행 없이 사전에 한 번만 수행하면 되므로 효율성 증가
        int num = board[x][y];
        int id = idBoard[x][y];
        int cnt = idScore.get(id);

        return num * cnt;
    }

    // 이동 후 주사위 및 방향 업데이트
    public static void update(int nx, int ny) {
        int tmp = dice[0];

        // 주사위 면 업데이트
        switch (dir) {
            // 동쪽
            case 0:
                dice[0] = dice[5];
                dice[5] = dice[1];
                dice[1] = dice[4];
                dice[4] = tmp;
                break;
            // 남쪽
            case 1:
                dice[0] = dice[2];
                dice[2] = dice[1];
                dice[1] = dice[3];
                dice[3] = tmp;
                break;
            // 서쪽
            case 2:
                dice[0] = dice[4];
                dice[4] = dice[1];
                dice[1] = dice[5];
                dice[5] = tmp;
                break;
            // 북쪽
            case 3:
                dice[0] = dice[3];
                dice[3] = dice[1];
                dice[1] = dice[2];
                dice[2] = tmp;
                break;
        }

        int bottom = dice[1];
        int num = board[nx][ny];

        // 바닥면과 현재 보드의 번호를 비교 하여 문제 조건에 따라 방향 변경
        if (bottom > num) {
            dir = (dir + 1) % 4;
        }
        else if (bottom < num) {
            dir = (dir + 3) % 4;
        }
    }


    public static void solution() {
        // 시작점 셋팅
        int x = 1;
        int y = 1;

        // k번 만큼 이동.계산.업데이트를 계산하여 정답 구하기
        for (int i = 0; i < k; i++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            // 범위를 벗어나면 방향을 반대로
            if (!isBound(nx, ny)) {
                dir = (dir + 2) % 4;
                nx = x + dx[dir];
                ny = y + dy[dir];
            }
            answer += getScore(nx, ny);
            update(nx, ny);

            x = nx;
            y = ny;
        }
    }

    public static void main(String[] args) throws Exception {
        input();
        setId();
        solution();
        System.out.println(answer);
    }
}