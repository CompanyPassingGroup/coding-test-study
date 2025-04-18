import sys

N, M = map(int, sys.stdin.readline().split())

arr = [list(map(int, sys.stdin.readline().split())) for _ in range(N)]
dp = [[0] * (N + 1) for _ in range(N + 1)]

# 나머지 원소 초기화
for i in range(1, N + 1):
    for j in range(1, N + 1):
        dp[i][j] = dp[i - 1][j] + dp[i][j - 1] - dp[i - 1][j - 1] + arr[i - 1][j - 1]

# M회동안 주어진 좌표에 따라 부분합을 구함
for _ in range(M):
    x1, y1, x2, y2 = map(int, sys.stdin.readline().split())

    answer = dp[x2][y2] - dp[x1 - 1][y2] - dp[x2][y1 - 1] + dp[x1 - 1][y1 - 1]
    print(answer)