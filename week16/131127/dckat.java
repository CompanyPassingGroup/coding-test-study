import java.util.*;

class Solution {
    // 해당 날짜에 회원가입이 가능한지 판별
    public boolean check(HashMap<String, Integer> wants, HashMap<String, Integer> discounts) {
        for (String key: wants.keySet()) {
            // 물품의 수량이 하나라도 다를 경우 false 리턴
            if (wants.get(key) != discounts.get(key)) {
                return false;
            }
        }
        return true;
    }
    
    public int solution(String[] want, int[] number, String[] discount) {
        int answer = 0;
        HashMap<String, Integer> wants = new HashMap<>();           // 구매를 원하는 물품과 수량
        HashMap<String, Integer> discounts = new HashMap<>();       // 10일간 할인 상품과 전체 수
        
        // 구매를 원하는 수 저장
        for (int i = 0; i < number.length; i++) {
            wants.put(want[i], number[i]);
        }
        
        // 1일 ~ 10일부터 먼저 수행하고 확인
        // begin: 날짜가 하루 지날때 빠져야 할 물품 대상
        String begin = discount[0];
        for (int i = 0; i < 10; i++) {
            discounts.put(discount[i], discounts.getOrDefault(discount[i], 0) + 1);
        }
        
        if (check(wants, discounts)) {
            answer++;
        }
        
        // 2일째부터 하루씩 shift 하면서 할인 물품 정보 갱신
        // 갱신하면서 check 함수를 호출하여 회원가입 가능 여부 확인
        for (int i = 1; i < discount.length - 9; i++) {
            discounts.put(begin, discounts.get(begin) - 1);
            discounts.put(discount[i+9], discounts.getOrDefault(discount[i+9], 0) + 1);
            if (check(wants, discounts)) {
                answer++;
            }
            begin = discount[i];
        }
        
        return answer;
    }
}