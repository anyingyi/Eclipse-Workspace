import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class PsoOperators {
	//��Ⱥ��ʼ���ٶ�
	
		public  static List<ArrayList<SO>> initListV() {
			int r;
			int rA;
			int rB;
			Problem problem = Problem.get();
		    int length = problem.getFacNum()+1;
			List<ArrayList<SO>> ListV = new ArrayList<>();
			
			for(int i = 0;i < Simulations.popSize;i++) {
				ArrayList<SO> list = new ArrayList<SO>();
				r = rand.nextInt(problem.getFacNum()+1);//rand.nextInt(2*combination(2,length));
				for(int j=0;j<r;j++) {
					rA = rand.nextInt(length);
					rB = rand.nextInt(length);
					while (rA == rB) {
						rB = rand.nextInt(length);
					}
					SO s = new SO(rA,rB);
					list.add(s);
				}
				ListV.add(list);
			}
			return ListV;
		}

		//һ���������������ڱ���arr��ı���
		public static int[] add(int[] arr, ArrayList<SO> list) {
			int temp = -1;
			SO s;
			for(int i = 0;i<list.size();i++) {
				s = list.get(i);
				temp = arr[s.getX()];
				arr[s.getX()] = arr[s.getY()];
				arr[s.getY()] = temp;
			}
			return arr;
		}
		//����������Ļ�����������
		public static ArrayList<SO> minus(int[] a,int[] b){
			int[] temp = b.clone();
			int index;
			SO s;
			ArrayList<SO> list = new ArrayList<SO>();
			for(int i = 0;i < a.length;i++) {
				if(a[i] != temp[i]) {
					index = findNum(temp,a[i]);
					changeIndex(temp,i,index);
					s = new SO(i,index);
					list.add(s);
				}
			}
			return list;
		}
		
		public static int findNum(int[] arr, int num) {
			int index = -1;
			for(int i = 0;i < arr.length;i++) {
				if(arr[i] == num) {
					index = i;
					break;
				}
			}
			return index;
		}
		public static void changeIndex(int[] arr, int index1,int index2) {
			int temp = arr[index1];
			arr[index1] = arr[index2];
			arr[index2] = temp;
		}
		
		private static Random rand = new Random();
}
