import java.util.*;

class Solution {
    // 다음으로 방문 가능한 노드들 탐색
    public Set<Integer> getNext(int node, Set<Integer> nodes, List<List<Integer>> adj) {
        Set<Integer> next = new HashSet<>(nodes);
        next.remove(node);      // 현재 노드는 삭제
        
        // 연결된 자식노드를 추가
        for (int i = 0; i < adj.get(node).size(); i++) {
            next.add(adj.get(node).get(i));
        }
        return next;
    }
    
    public int go(int sheep, int wolf, Set<Integer> nodes, int[] info, List<List<Integer>> adj) {
        int maxSheep = sheep;
        
        for (int node: nodes) {
            // 현재까지 모은 양과 늑대
            int nextSheep = sheep;
            int nextWolf = wolf;
            
            // 양
            if (info[node] == 0) {
                nextSheep++;
            } // 늑대
            else {
                nextWolf++;
            }
            
            // 양이 모두 잡아먹힐 수 있는 경우
            if (nextWolf >= nextSheep) {
                continue;
            }
            
            // 다음으로 방문 가능한 노드 탐색
            Set<Integer> next = getNext(node, nodes, adj);
            
            int tmp = go(nextSheep, nextWolf, next, info, adj);
            if (tmp > maxSheep) {
                maxSheep = tmp;
            }
        }
        return maxSheep;
    }
    
    public int solution(int[] info, int[][] edges) {
        int answer = 0;
        int n = info.length;
        
        // 인접 리스트 초기화
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        // 부모노드에 연결된 자식노드 정보 설정
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            adj.get(u).add(v);
        }
        
        // 방문 가능한 노드 (0번부터)
        Set<Integer> nodes = new HashSet<>();
        nodes.add(0);
    
        answer = go(0, 0, nodes, info, adj);
        
        return answer;
    }
}
