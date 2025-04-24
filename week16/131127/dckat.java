import java.util.*;

class Solution {
    
    public int solution(String[] want, int[] number, String[] discount) {
        int answer = 0;
        int matchCnt = 0;       // 구매 물품과 할인 물품의 일치 여부 확인 변수
        HashMap<String, Integer> wants = new HashMap<>();
        HashMap<String, Integer> discounts = new HashMap<>();
        
        // 구매희망 물품 갯수 저장
        for (int i = 0; i < number.length; i++) {
            wants.put(want[i], number[i]);
        }
        
        // 첫날 부터 10일까지 할인 물품의 갯수 확인 (이 부분만 따로 빼서 해당 작업이 n번이 아닌 1번만 수행되도록 함)
        String begin = discount[0];
        for (int i = 0; i < 10; i++) {
            discounts.put(discount[i], discounts.getOrDefault(discount[i], 0) + 1);
            
            if (discounts.get(discount[i]) == wants.get(discount[i])) {
                matchCnt++;
            }
        }
        
        if (matchCnt == wants.size()) {
            answer++;
        }
        
        for (int i = 1; i < discount.length - 9; i++) {
            // 이동하면서 할인에서 빠지는 물품의 갯수의 match 여부 비교 (빠지기 전 상태로 확인) 
            if (discounts.get(begin) == wants.get(begin)) {
                matchCnt--;
            }
            discounts.put(begin, discounts.get(begin) - 1);
            
            // 추가되는 할인 물품의 추가 후 match 여부 비교
            discounts.put(discount[i+9], discounts.getOrDefault(discount[i+9], 0) + 1);
            if (discounts.get(discount[i+9]) == wants.get(discount[i+9])) {
                matchCnt++;
            }
            
            // 구매 물품들과 할인 물품의 갯수가 일치
            if (matchCnt == wants.size()) {
                answer++;
            }
            
            begin = discount[i];
        }
        
        return answer;
    }
}