import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    // 연산정보 저장 클래스
    private static class Term {
        int num;        // 숫자
        int op;         // 연산자 (-1:계산완료, 0:피연산자. 1:+, 2:-, 3:*)

        Term(int num, int op) {
            this.num = num;
            this.op = op;
        }
    }

    private static int n;
    private static String s = null;

    private static void input() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        s = br.readLine();
    }

    private static int solution(int n, String s) {
        // 전체 연산정보를 저장할 리스트
        List<Term> v = new ArrayList<>();

        // 파싱하여 연산정보를 리스트에 추가
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);

            // 피연산자
            if (Character.isDigit(c)) {
                v.add(new Term(c - '0', 0));
            } // +
            else if (c == '+') {
                v.add(new Term(0, 1));
            } // -
            else if (c == '-') {
                v.add(new Term(0, 2));
            } // *
            else if (c == '*') {
                v.add(new Term(0, 3));
            }
        }

        int m = (n - 1) / 2;
        int answer = Integer.MIN_VALUE;

        // 비트마스킹을 이용하여 연산의 최댓값을 가져옴
        for (int i = 0; i < (1 << m); i++) {
            boolean ok = true;

            // 연속으로 괄호를 추가하는 경우인지 확인
            for (int j = 0; j < m - 1; j++) {
                if ((i & (1 << j)) != 0 && (i & (1 << (j + 1))) != 0) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                continue;
            }

            // 괄호계산을 위해 저장한 리스트
            List<Term> temp = new ArrayList<>();
            for (Term t: v) {
                temp.add(new Term(t.num, t.op));
            }

            for (int j = 0; j < m; j++) {
                // 괄호를 친 부분을 먼저 연산 [ex: a+b ==> (a+b) / 0 으로 변환]
                if ((i & (1 << j)) != 0) {
                    int k = 2 * j + 1; // 연산자 인덱스

                    // 더하기
                    if (temp.get(k).op == 1) {
                        temp.get(k-1).num += temp.get(k+1).num;
                    } // 빼기
                    else if (temp.get(k).op == 2) {
                        temp.get(k-1).num -= temp.get(k+1).num;
                    } // 곱하기
                    else if (temp.get(k).op == 3){
                        temp.get(k-1).num *= temp.get(k+1).num;
                    }
                    temp.get(k).op = -1;
                    temp.get(k+1).num = 0;
                }
            }

            // 곱하기 우선 처리
            List<Term> multi = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                // 피연산자
                if (j % 2 == 0) {
                    multi.add(temp.get(j));
                }
                else {
                    // 괄호로 연산처리가 된 경우
                    if (temp.get(j).op == -1) {
                        j++;
                    }
                    else {
                        // 곱하기 연산
                        if (temp.get(j).op == 3) {
                            // 최근 숫자와 다음 숫자를 곱하여 값 갱신
                            int num = multi.get(multi.size()-1).num * temp.get(j+1).num;
                            multi.remove(multi.size()-1);
                            multi.add(new Term(num, 0));
                            j++;
                        }
                        // 나머지 연산
                        else {
                            multi.add(temp.get(j));
                        }
                    }
                }
            }

            // 나머지 연산 처리
            int m2 = (multi.size() - 1) / 2;
            int tmp = multi.get(0).num;
            for (int j = 0; j < m2; j++) {
                int k = 2*j + 1;

                Term oper  = multi.get(k);
                Term right = multi.get(k + 1);

                if (oper.op == 1) {
                    tmp += right.num;
                }
                else if (oper.op == 2) {
                    tmp -= right.num;
                }
                else if (oper.op == 3) {
                    tmp *= right.num;
                }
            }
            answer = Math.max(answer, tmp);
        }
        return answer;
    }

    public static void main(String[] args) throws Exception {
        input();
        int answer = solution(n, s);
        System.out.println(answer);
    }
}