import java.util.*;

class Solution {  
    public int solution(String[] want, int[] number, String[] discount) {
        Map<String, Integer> wantMap = new HashMap<>();
        Map<String, Integer> availMap = new HashMap<>();
        int satisfied = 0; // if satisfied == want.length then ans+=1
        int ldx = 0;
        int rdx = 9; // sum(number) is 10
        int ans = 0;
        
        // init wantMap
        for (int i = 0; i < want.length; ++i) {
            String item = want[i];
            int val = number[i];
            wantMap.put(item, val);
        }
        
        // init availMap
        for (int i = ldx; i < rdx+1; ++i) {
            String dItem = discount[i];
            availMap.merge(dItem, 1, Integer::sum);
            if (availMap.get(dItem) == wantMap.get(dItem)) {
                satisfied += 1;
            }
        }
        if (satisfied == want.length)
            ans += 1;
        
        // slide: window size = 10
        while (rdx < discount.length-1) {
            String lItem = discount[ldx];
            String rItem = discount[rdx+1];
                
            // calculate map & counter
            availMap.merge(lItem, -1, Integer::sum);
            if (availMap.get(lItem) == wantMap.getOrDefault(lItem, 0) - 1)
                satisfied -= 1;
            ++ldx;
            availMap.merge(rItem, 1, Integer::sum);
            if (availMap.get(rItem) == wantMap.getOrDefault(rItem, 0))
                satisfied += 1;
            ++rdx;
            
            if (satisfied == want.length)
                ans += 1;
        }
        
        return ans;
    }
}