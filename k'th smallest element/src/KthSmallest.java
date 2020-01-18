
public class KthSmallest {
	private static void exch(int[] a, int i, int j) {
		int temp=a[i];
		a[i]=a[j];
		a[j]=temp;
	}
	
	private static int partition(int[] a, int lo, int hi) {
		int i=lo;
		int v=a[lo];
		while(lo<=hi)
		{
			while(lo<=hi&&(a[lo]<=v))lo++;
			while(lo<=hi&&(v<=a[hi]))hi--;
			if(hi<lo) break;
			exch(a,lo,hi);
		}
		exch(a,i,hi);
		return hi;
	}
	
	public static int sort(int[] a,int lo,int hi,int k)
	{
		if(lo<=k&&k<=hi)
		{
			int j=partition(a,lo,hi);
			if(k==j)
				return a[k];
			else if(k<j)
				return sort(a,lo,j-1,k);
			else
				return sort(a,j+1,hi,k);
		}
		return 0;
		
	}
	
	public static void main(String args[])
	{
		int k=4;
		k=k-1;
		int[] a= {3,5,1,7,2};
		int value=KthSmallest.sort(a, 0, a.length-1,k);
		System.out.println(value);
	}	
	
}
