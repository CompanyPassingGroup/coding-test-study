class Solution {
    int n, m;
    int zeros; // number of zeros in lock
    int[][] key, lock;
    int[][] key90, key180, key270;
    int[][][] keyArr;

    public boolean solution(int[][] key, int[][] lock) {
        this.n = lock.length;
        this.m = key.length;
        this.key = key;
        this.lock = lock;
        initKey();
        initZeros();

        // set offset
        for (int yOfs = -m + 1; yOfs < n; ++yOfs) {
            for (int xOfs = -m + 1; xOfs < n; ++xOfs) {
                boolean[] kFail = new boolean[4];
                int[] cnt = new int[4];

                kfor: // offset done -> match key
                for (int k = 0; k < 4; ++k) {
                    for (int ky = 0; ky < m; ++ky) {
                        for (int kx = 0; kx < m; ++kx) {
                            if (kFail[k]) continue;

                            int ly = ky + yOfs;
                            int lx = kx + xOfs;
                            if (ly < 0 || ly >= n || lx < 0 || lx >= n) continue;

                            int kVal = keyArr[k][ky][kx];
                            int lVal = lock[ly][lx];

                            if ((kVal ^ lVal) == 0) { // collide or can't solve all
                                kFail[k] = true;
                                continue kfor;
                            } else if (lVal == 0) { // match: 1 solved
                                ++cnt[k];
                            }
                        }
                    }
                } // end of key match

                for (int i = 0; i < cnt.length; ++i) {
                    if (cnt[i] == zeros && !kFail[i]) return true;
                }
            }
        }

        return false;
    }

    public void initKey() {
        key90 = new int[m][m];
        key180 = new int[m][m];
        key270 = new int[m][m];
        keyArr = new int[][][]{key, key90, key180, key270};
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < m; ++j) {
                key90[j][m - i - 1] = key[i][j];
                key180[m - i - 1][m - j - 1] = key[i][j];
                key270[m - j - 1][i] = key[i][j];
            }
        }
    }

    public void initZeros() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (lock[i][j] == 0) ++zeros;
            }
        }
    }
}