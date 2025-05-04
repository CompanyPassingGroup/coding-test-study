import java.util.*;
import java.io.*;

public class Main
{
    static int n, m;
    static int[][] table;
    static List<int[]> cctvList;
    static int[] dy = new int[]{0, 1, 0, -1}; // R B L T
    static int[] dx = new int[]{1, 0, -1, 0};
    static int ans = 999;
    
    public static void mark(int y, int x, int d) { // fill -1 for reachable areas
        d %= 4;
        int ny = y;
        int nx = x;
        while (true) {
            ny += dy[d];
            nx += dx[d];
            if (ny < 0 || n <= ny || nx < 0 || m <= nx) return; // out of bound
            if (table[ny][nx] == 6) return; // wall
            if (table[ny][nx] != 0) continue; // cctv
            table[ny][nx] = -1;
        }
    }
    
    public static void dfs(int idx) {
        // base case
        if (idx >= cctvList.size()) {
            int cnt = 0;
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < m; ++j)
                    if (table[i][j] == 0) ++cnt;
            
            ans = cnt < ans ? cnt : ans;
            return;
        }
        
        // 1. pre-set value
        int[][] tableCpy = new int[n][m];
        int y = cctvList.get(idx)[0];
        int x = cctvList.get(idx)[1];
        
        // 2. mark areas
        for (int d = 0; d < 4; ++d) { // rotate clockwise: 0 -> R
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < m; ++j)
                    tableCpy[i][j] = table[i][j]; // deepcopy
            
            switch(table[y][x]) {
                case 1: mark(y, x, d); break;
                case 2: mark(y, x, d); mark(y, x, d+2); break;
                case 3: mark(y, x, d); mark(y, x, d+3); break;
                case 4: mark(y, x, d); mark(y, x, d+2); mark(y, x, d+3); break;
                case 5: mark(y, x, d); mark(y, x, d+1); mark(y, x, d+2); mark(y, x, d+3); break;
            }
            
            // 3. recursion
            dfs(idx+1);
            
            // 4. post-set value
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < m; ++j)
                    table[i][j] = tableCpy[i][j]; // deepcopy
        }
                
        return;
    }
    
	public static void main(String[] args) throws IOException {
		input();
		dfs(0);
		System.out.println(ans);
	}
	
	public static void input() throws IOException {
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    StringTokenizer st = new StringTokenizer(br.readLine());
	    n = Integer.parseInt(st.nextToken());
	    m = Integer.parseInt(st.nextToken());
	    table = new int[n][m]; // init
	    cctvList = new ArrayList<>(); // init
	    
	    for (int i = 0; i < n; ++i) {
	        st = new StringTokenizer(br.readLine());
	        for (int j = 0; j < m; ++j) {
	            int value = Integer.parseInt(st.nextToken());
	            if (0 < value && value < 6) cctvList.add(new int[]{i, j});
	            table[i][j] = value;
	        }
	    }
	}
}