import java.util.*;

class Solution {
    List<Integer> ansList;
    int[][] _edges;
    int[] _info;
    boolean[] visited;
    
    public void dfs(int sheep, int wolf) {
        if (sheep > wolf) ansList.add(sheep);
        else return; // base case
        
        for (int[] e : _edges) {
            if (visited[e[0]] && !visited[e[1]]) {
                visited[e[1]] = true;
                if (_info[e[1]] == 0) dfs(sheep+1, wolf); // 가려는 곳이 양
                else dfs(sheep, wolf+1); // 가려는 곳이 늑대
                visited[e[1]] = false;
            }
        }
    }
    
    public int solution(int[] info, int[][] edges) {
        _edges = edges;
        _info = info;
        ansList = new ArrayList<>();
        visited = new boolean[info.length];
        Arrays.fill(visited, false);
        visited[0] = true;
        dfs(1, 0);
        
        return Collections.max(ansList);
    }
}