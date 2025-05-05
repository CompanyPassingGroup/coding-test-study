public class Solution {
    
    // key를 시계방향 90° 회전
    private void rotate(int[][] key) {
        int m = key.length;
        int[][] temp = new int[m][m];
        
        // 90도 회전 코드
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                temp[i][j] = key[m - j - 1][i];
            }
        }
        
        // 원본 덮어쓰기
        for (int i = 0; i < m; i++) {
            System.arraycopy(temp[i], 0, key[i], 0, m);
        }
    }

    // 한 번의 배치(i,j)에서 4방향 회전 검사
    private boolean check(int[][] board, int[][] key, int offsetX, int offsetY, int n) {
        int m = key.length;
        
        // 네 번 회전
        for (int rot = 0; rot < 4; rot++) {
            
            // key를 board에 맞춰 더하기
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < m; y++) {
                    board[offsetX + x][offsetY + y] += key[x][y];
                }
            }
            
            // 2) N×N lock 영역이 모두 1인지 검사
            boolean ok = true;
            for (int x = m - 1; x < m - 1 + n && ok; x++) {
                for (int y = m - 1; y < m - 1 + n; y++) {
                    if (board[x][y] != 1) {
                        ok = false;
                        break;
                    }
                }
            }
            
            // 맞으면 true 리턴으로 종료
            if (ok) return true;
            
            // 기존으로 복원작업 수행
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < m; y++) {
                    board[offsetX + x][offsetY + y] -= key[x][y];
                }
            }
            
            // 4) key를 시계방향으로 90도 회전
            rotate(key);
        }
        return false;
    }

    public boolean solution(int[][] key, int[][] lock) {
        int m = key.length;
        int n = lock.length;
        
        // board 크기 (회전을 고려하여 size 늘림)
        int B = n + 2 * (m - 1);
        
        // board 초기화 & 중앙에 lock 복사
        int[][] boardTemplate = new int[B][B];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardTemplate[m - 1 + i][m - 1 + j] = lock[i][j];
            }
        }

        // 가능한 모든 배치에 대해 복사한 후 확인
        for (int i = 0; i <= B - m; i++) {
            for (int j = 0; j <= B - m; j++) {
                int[][] board = new int[B][B];
                for (int x = 0; x < B; x++) {
                    System.arraycopy(boardTemplate[x], 0, board[x], 0, B);
                }
                
                int[][] k = new int[m][m];
                for (int x = 0; x < m; x++) {
                    System.arraycopy(key[x], 0, k[x], 0, m);
                }
                
                // 자물쇠의 모든 곳이 채워지는 경우 true 리턴
                if (check(board, k, i, j, n)) {
                    return true;
                }
            }
        }
        return false;
    }
}