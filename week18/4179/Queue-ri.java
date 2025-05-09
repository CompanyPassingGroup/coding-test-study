import java.io.*;
import java.util.*;

public class Main
{
    static int r, c;
    static int[][] miro;
    static boolean[][] fvisited;
    static boolean[][] jvisited;
    static Deque<int[]> fq = new ArrayDeque<>();
    static Deque<int[]> jq = new ArrayDeque<>();
    static int[] dy = {-1, 0, 0, 1};
    static int[] dx = {0, -1, 1, 0};
    
    public static int bfs() {
        // queue init was done in input()
        int time = 1;
        
		while (!jq.isEmpty()) { // until J evacuate
	        while (!fq.isEmpty() && fq.peek()[2] == time) {
    		    int[] fpos = fq.pop();
    		    for (int d = 0; d < 4; ++d) {
    		        int nfy = fpos[0] + dy[d];
    		        int nfx = fpos[1] + dx[d];
    		        if (nfy < 0 || r <= nfy || nfx < 0 || c <= nfx) continue; // out of bound
    		        if (fvisited[nfy][nfx] || miro[nfy][nfx] == 1) continue;
    		        fvisited[nfy][nfx] = true;
    		        fq.add(new int[]{nfy, nfx, time+1});
    		    }
	        }
		    
		    while (!jq.isEmpty() && jq.peek()[2] == time) {
    		    int[] jpos = jq.pop();
    		    for (int d = 0; d < 4; ++d) {
    		        int njy = jpos[0] + dy[d];
    		        int njx = jpos[1] + dx[d];
    		        if (njy < 0 || r <= njy || njx < 0 || c <= njx) return time; // evacuate
    		        if (fvisited[njy][njx] || jvisited[njy][njx] || miro[njy][njx] == 1) continue;
    		        jvisited[njy][njx] = true;
    		        jq.add(new int[]{njy, njx, time+1});
    		    }
		    }
		    ++time;
		}
		
		return -1;
    }
    
	public static void main(String[] args) throws IOException {
		input();
		int ans = bfs();
		System.out.println(ans == -1 ? "IMPOSSIBLE" : ans);
	}
	
	public static void input() throws IOException {
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    StringTokenizer st = new StringTokenizer(br.readLine());
	    r = Integer.parseInt(st.nextToken());
	    c = Integer.parseInt(st.nextToken());
	    miro = new int[r][c];
	    fvisited = new boolean[r][c];
	    jvisited = new boolean[r][c];
	    
	    for (int i = 0; i < r; ++i) {
	        char[] line = br.readLine().toCharArray();
	        for (int j = 0; j < c; ++j) {
	            miro[i][j] = line[j] == '#' ? 1 : 0;
	            if (line[j] == 'J') {
                    jq.add(new int[]{i, j, 1});
                    jvisited[i][j] = true;
	            }
	            else if (line[j] == 'F') {
	                fq.add(new int[]{i, j, 1});
	                fvisited[i][j] = true;
	            }
	        }
	    }
	}
}