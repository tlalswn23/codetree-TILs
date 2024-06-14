import java.util.*;
import java.io.*;

public class Main {
	
	static int K, M, total;
	static int[][] map, origin, answer;
	static PriorityQueue<int[]> queue;
	static boolean[][] visited;
	static Queue<Integer> number, copyNum;
	static int[][] direction = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	static int t;
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st = new StringTokenizer(br.readLine());
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		map = new int[5][5];
		answer = new int[5][5];
		origin = new int[5][5];
		number = new LinkedList<>();
		queue = new PriorityQueue<>((o1, o2) -> o1[1] == o2[1] ? o2[0] - o1[0] : o1[1] - o2[1]);
		copyNum = new LinkedList<>();
		
		for(int i = 0; i < 5; i++) { // 맵 세팅 
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < 5; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				origin[i][j] = map[i][j];
			}
		}
		
		st = new StringTokenizer(br.readLine());
		while(st.hasMoreTokens()) {
			copyNum.add(Integer.parseInt(st.nextToken()));
		}
		
		for(int k = 0; k < K; k++) { // k번 만큼 반복
			total = 0;
			number.clear();
			number.addAll(copyNum);
			queue.clear();
			copyMap(origin, map);
			
			t = 3;
			
			// 1. 탐사 진행
			exploration();

			if(queue.isEmpty()) {
				break;
			}
			
			// 2. 연쇄 유물 획득 
			while(queue.size() > 2) {	
				total += queue.size();
				fullRelics(); // 유물 채우기 
				queue.clear();
				queue.addAll(countRelics());
				copyMap(answer, map);
			}
			bw.write(Integer.toString(total)+" ");
		}
		
		bw.close();
	}
	
	// 1. 탐사 진행 함수 
	static void exploration() {
		
		// 3x3 모든 경우의 수 확인 
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < 3; k++) { // 90 -> 180 -> 270
					rotation(i, j); // 배열 돌리기
					
					// 유물 수 구하기(BFS)
					Queue<int[]> cnt = countRelics();
					if(queue.size() < cnt.size() || (queue.size() == cnt.size() && t > k)) {
						t = k;
						queue.clear();
						queue.addAll(cnt);
						copyMap(answer, map);
					}
				}
				
				copyMap(map, origin);
			}
		}
	}
	
	// 유물 채우기 
	static void fullRelics() {
		copyMap(map, answer);
		
		while(!queue.isEmpty()) {
			int[] p = queue.poll();
			int n = number.poll();
	
			map[p[0]][p[1]] = n;
		}		
	}
	
	static void rotation(int x, int y) {
		int[][] temp = new int[3][3];
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				temp[i][j] = map[x+i][y+j];
			}
		}
		
		// 돌리기 
		map[x][y+2] = temp[0][0];
		map[x+1][y+2] = temp[0][1];
		map[x+2][y+2] = temp[0][2];

		map[x][y+1] = temp[1][0];
		map[x+2][y+1] = temp[1][2];
		
		map[x][y] = temp[2][0];
		map[x+1][y] = temp[2][1];
		map[x+2][y] = temp[2][2];
	}
	
	// 유물 수 구하는 함수 
	static Queue<int[]> countRelics(){
		visited = new boolean[5][5];
		Queue<int[]> result = new LinkedList<>();
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				
				if(visited[i][j]) {
					continue;
				}
				
				visited[i][j] = true;
				
				// 방문안한 번호이면 해당 번호랑 같은 유물 찾기 
				Queue<int[]> pos = bfs(i, j);
				if(pos.isEmpty() || pos.size() < 3) { // 이어진 조각이 3개 이상이면 유물 발굴
					continue;
				}
				
				result.addAll(pos);
			}
		}
		
		return result;
	}
	
	static Queue<int[]> bfs(int x, int y) {
		Queue<int[]> result = new LinkedList<>();
		Queue<int[]> pos = new LinkedList<>();
		
		int num = map[x][y]; // 찾아야할 유물 번호 
		result.add(new int[] {x, y});
		pos.add(new int[] {x, y});
		visited[x][y] = true;
		
		while(!pos.isEmpty()) {
			int[] current = pos.poll();
			
			for(int d = 0; d < 4; d++) {
				int nx = current[0] + direction[d][0];
				int ny = current[1] + direction[d][1];
				
				if(nx < 0 || nx > 4 || ny < 0 || ny > 4) {
					continue;
				}
				
				if(visited[nx][ny]) {
					continue;
				}
				
				if(map[nx][ny] != num) {
					continue;
				}
				
				pos.add(new int[] {nx, ny});
				result.add(new int[] {nx, ny});
				visited[nx][ny] = true;
			}
		}
		
		return result;
	}
	
	static void printMap() {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				System.out.print(map[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	static void copyMap(int[][] a, int[][] b) {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				a[i][j] = b[i][j];
			}
		}
	}
}