import java.io.*;
import java.util.*;

public class Main
{
    static int[][] table = new int[5][5];
    static boolean[] visited = new boolean[25];
    static int[] selected = new int[7];
    static int[] dy = {-1, 0, 0, 1};
    static int[] dx = {0, -1, 1, 0};
    
    public static int dfs(int idx, int depth) {
        if (depth == 7) return bfs() ? 1 : 0;
        
        int res = 0;
        for (int i = idx+1; i < 25; ++i) {
            if (!visited[i]) {
                visited[i] = true;
                selected[depth] = i;
                res += dfs(i, depth+1);
                selected[depth] = -1;
                visited[i] = false;
            }
        }
        
        return res;
    }
    
    public static boolean bfs() {
        Deque<int[]> q = new ArrayDeque<>();
        boolean[] joined = new boolean[7]; // visited for 'selected'
        
        int yimCnt = 0;
        int somCnt = 0;
        
        int y = selected[0] / 5;
        int x = selected[0] % 5;
        q.add(new int[]{y, x});
        joined[0] = true;
        if (table[y][x] == 0) ++yimCnt; else ++somCnt;
        
        while (!q.isEmpty()) {
            int[] pos = q.pop();
            for (int d = 0; d < 4; ++d) {
                int ny = pos[0] + dy[d];
                int nx = pos[1] + dx[d];
                
                if (ny < 0 || 4 < ny || nx < 0 || 4 < nx) continue; // out of bound
                
                int sdx = 0; // find index of adj value in selected: 0으로 두면 어차피 밑에서 continue 처리됨
                for (int s = 0; s < 7; ++s)
                    if (selected[s] == ny*5+nx) sdx = s;
                    
                if (joined[sdx]) continue; // target value has already joined
                joined[sdx] = true; // else join
                
                q.add(new int[]{ny, nx});
                
                if (table[ny][nx] == 0) ++yimCnt; else ++somCnt;
                if (3 < yimCnt) return false; // 생존 불가
            }
        }
        
        return (yimCnt + somCnt == 7) ? true : false;
    }
    
	public static void main (String[] args) throws IOException {
	    input();
	    
	    int ans = 0;
	    for (int i = 0; i < 19; ++i) { // 0 ~ 25-7+1
	        visited[i] = true;
	        selected[0] = i;
	        ans += dfs(i, 1);
	        selected[0] = -1;
	        visited[i] = false;
	    }
	    
	    System.out.println(ans);
	}
	
	public static void input() throws IOException {
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    
	    for (int i = 0; i < 5; ++i) {
	        char[] line = br.readLine().toCharArray();
	        for (int j = 0; j < 5; ++j) {
	            table[i][j] = line[j] == 'S' ? 1 : 0;
	        }
	    }
	}
}