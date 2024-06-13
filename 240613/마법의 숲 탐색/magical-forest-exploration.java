import java.util.*;
import java.io.*;

public class Main {
	static int R, C, K, total;
	static int[][] map, exit;
	static int[][] direction = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	static int[][] check = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, 0}};
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		map = new int[R+1][C+1];
		exit = new int[R+1][C+1];
		
		for(int i = 1; i < K+1; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			
//			System.out.println(0+" "+r+" "+d);
			moveGolem(0, r, d, i); // 골렘 이동 함수 
		}
		
		bw.write(Integer.toString(total));
		bw.close();
		
	}
	
	
	static void moveGolem(int r, int c, int d, int n) {
		
		// 1. 밑으로 내려가기 
		if(canGo(r+1, c, d, n)) {
			moveGolem(r+1, c, d, n);
			return;
		}
		
		// 2. 왼쪽으로 내려가기 
		if(canGo(r+1, c-1, d, n)) {
			if(d == 0) {
				d = 4;
			}
			
			moveGolem(r+1, c-1, d-1, n);
			return;
		}
		
		// 3. 오른쪽으로 내려가기 
		if(canGo(r+1, c+1, d, n)) {
			if(d == 3) {
				d = -1;
			}
			moveGolem(r+1, c+1, d+1, n);
			return;
		}
		
		// 4. 더 이상 이동할 수 없으면 
		if(!checkRange(r, c)) { // 숲을 벗어나는 경우 
			initMap();
		}else { // 숲을 벗어나지 않으면 맵에 정착 
			for(int i = 0; i < 4; i++) {
				int nx = r + direction[i][0];
				int ny = c + direction[i][1];
				map[nx][ny] = n; // 맵에 표시 
				map[r][c] = n;
			}
			
			exit[r + direction[d][0]][c + direction[d][1]] = 1; // 출구 표시 
			
			// 골렘 층수 계산 
			int row = findGolem(r, c, n);
			total += row; // 골렘 위치 찾는 함수 
			
//			System.out.println(row);
//			printMap(map);
//			printMap(exit);
		}
	}
	
	static boolean checkRange(int r, int c) {
		for(int d = 0; d < 4; d++) {
			int nr = r + direction[d][0];
			int nc = c + direction[d][1];
			
			if(nr < 1 || nr > R || nc < 1 || nc > C) {
				return false;
			}
		}
		
		return true;
	}
	
	static boolean canGo(int r, int c, int d, int n) {
		for(int i = 0; i < 7; i++) {
			int nr = r + check[i][0];
			int nc = c + check[i][1];
			
			if(nr < 0 || nr > R || nc < 1 || nc > C) {
				return false;
			}
			
			if(map[nr][nc] != 0) {
				return false;
			}
		}
		
		return true;
	}
	
	// 골렘의 위치 찾기 - BFS
	static int findGolem(int r, int c, int n) {
		boolean[][] visited = new boolean[R+1][C+1];
		Queue<int[]> queue = new LinkedList<>();
		int row = r;
		queue.add(new int[] {r, c});
		visited[r][c] = true;
		
		while(!queue.isEmpty()) {
			int[] current = queue.poll();
			
			for(int d = 0; d < 4; d++) {
				int nr = current[0] + direction[d][0];
				int nc = current[1] + direction[d][1];
				
				if(nr < 1 || nr > R || nc < 1 || nc > C) {
					continue;
				}
				
				if(visited[nr][nc]) { // 방문했으며 못감 
					continue;
				}
				
				if(map[nr][nc] == 0) { // 벽이면 못감 
					continue;
				}
				
				if((map[nr][nc] != map[current[0]][current[1]]) && exit[current[0]][current[1]] == 0) { // 다른 골렘으로 이동 but, 출구가 아니면 못감 
					continue;
				}
				
				queue.add(new int[] {nr, nc});
				visited[nr][nc] = true;
				row = Math.max(row, nr);
			}
		}
		
		return row;
	}
	
	static void initMap() {
		for(int i = 0; i < R+1; i++) {
			for(int j = 0; j < C+1; j++) {
				map[i][j] = 0;
				exit[i][j] = 0;
			}
		}
	}
	
	static void printMap(int[][] arr) {
		for(int i = 0; i <= R; i++) {
			for(int j = 1; j <= C; j++) {
				System.out.print(arr[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	static void printExit(boolean[][] arr) {
		for(int i = 0; i <= R; i++) {
			for(int j = 1; j <= C; j++) {
				System.out.print(arr[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
}