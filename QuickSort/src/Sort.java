
public class Sort {
	private static void exch(int[] a, int i, int j) {
		int temp=a[i];
		a[i]=a[j];
		a[j]=temp;
	}
	
	private static int partition(int[] a, int lo, int hi) {
		int v=a[lo];
		while(lo<hi)
		{
			while(lo<hi&&(v<=a[hi]))hi--;
			a[lo]=a[hi];
			while(lo<hi&&(a[lo]<=v))lo++;
			a[hi]=a[lo];
		}
		a[lo]=v;
		return lo;
	}
	
	public static void sort(int[] a,int lo,int hi)
	{
		if(hi>lo)
		{
			int j=partition(a,lo,hi);
			sort(a,lo,j-1);
			sort(a,j+1,hi);
		}
	}
	
	public static void main(String args[])
	{
		int[] a= {3,5,1,7,2};
		Sort.sort(a, 0, a.length-1);
		for(int i:a)
			System.out.println(i);
	}	
	
}
