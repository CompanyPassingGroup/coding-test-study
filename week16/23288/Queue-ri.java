import java.util.*;
import java.io.*;

public class Main
{
    static int n, m, k;
    static int[][] table;
    static int[][] score;
    static int[] dy = {0, 1, 0, -1}; // R B L T
    static int[] dx = {1, 0, -1, 0};
    static boolean[][] visited;
    static List<int[]> visitedList;
    
    static int[] dice = {1, 2, 3, 4, 5, 6};
    static int y, x, d; // dice info: r = y, c = x
    
    public static void roll(int ny, int nx) {
        // 1. change dice position
        y = ny;
        x = nx;
        
        // 2. change dice map
        switch (d) {
            // dice = {윗면, 옆위, 옆오, 옆왼, 옆아래, 밑면}
            case 0: dice = new int[]{dice[3], dice[1], dice[0], dice[5], dice[4], dice[2]}; break; // R: 1, 4 인덱스 그대로
            case 1: dice = new int[]{dice[1], dice[5], dice[2], dice[3], dice[0], dice[4]}; break; // B: 2, 3 인덱스 그대로
            case 2: dice = new int[]{dice[2], dice[1], dice[5], dice[0], dice[4], dice[3]}; break; // L: 1, 4 인덱스 그대로
            case 3: dice = new int[]{dice[4], dice[0], dice[2], dice[3], dice[5], dice[1]}; break; // T: 2, 3 인덱스 그대로
        }
        
        // 3. change dice direction
        if (dice[5] > table[y][x]) d = (d+1) % 4; // clockwise
        else if (dice[5] < table[y][x]) d = (d+3) % 4; // reverse clockwise
    }
    
    public static int count(int y, int x) {
        visited[y][x] = true;
        visitedList.add(new int[]{y, x});
        
        int cnt = 1;
        for (int i = 0; i < 4; ++i) {
            int ny = y + dy[i];
            int nx = x + dx[i];
            if (ny < 0 || n <= ny || nx < 0 || m <= nx) continue; // out of bound
            if (visited[ny][nx]) continue; // visited
            if (table[ny][nx] != table[y][x]) continue; // not same number
            
            cnt += count(ny, nx);
        }
        
        return cnt;
    }
    
    public static void precalc() {
        score = new int[n][m];
        visited = new boolean[n][m];
        for (boolean[] line : visited) Arrays.fill(line, false);
        
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (!visited[i][j]) {
                    visitedList = new ArrayList<>();
                    int val = count(i, j);
                    for (int[] pos : visitedList) score[pos[0]][pos[1]] = table[i][j] * val;
                }
            }
        }
    }
    
	public static void main(String[] args) throws IOException {
		input();
		precalc();
		
		int ans = 0;
		for (int i = 0; i < k; ++i) {
		    int ny = y + dy[d];
		    int nx = x + dx[d];
		    
		    // 못가는 곳이면 방향 반대로
		    if (ny < 0 || n <= ny || nx < 0 || m <= nx) {
		        d = (d+2) % 4;
		        ny = y + dy[d];
		        nx = x + dx[d];
		    }
		    
		    roll(ny, nx); // 여기부터 y = ny, x = nx
		    ans += score[ny][nx];
		}
		
		System.out.println(ans);
	}
	
	public static void input() throws IOException {
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		table = new int[n][m];
		for (int i = 0; i < n; ++i) {
		    st = new StringTokenizer(br.readLine());
		    for (int j = 0; j < m; ++j) {
		        table[i][j] = Integer.parseInt(st.nextToken());
		    }
		}
		
		y = 0;
		x = 0;
		d = 0;
	}
}