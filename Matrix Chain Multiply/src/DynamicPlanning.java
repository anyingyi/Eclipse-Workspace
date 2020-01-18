
public class DynamicPlanning {
	static int IMC(int p[],int n)
	{
		int[][] m=new int[n][n],s=new int[n][n];
		for(int i=1;i<n;i++)
			for(int j=1;j<n;j++)
			{
				m[i][j]=0;
				s[i][j]=0;
			}
		for(int le=2;le<n;le++)
		{
			for(int i=1;i<n-le+1;i++)
			{
				int j=i+le-1;
				m[i][j]=m[i+1][j]+p[i-1]*p[i]*p[j];
				for(int k=i+1;k<=j-1;k++)
				{
					int t=m[i][k]+m[k+1][j]+p[i-1]*p[k]*p[j];
					if(t<m[i][j])
					{
						m[i][j]=t;
						s[i][j]=k;
					}
				}
			}
		}
		return m[1][n-1];
	}
	
	public static void main(String args[])
	{
		int arr[]= {1,2,3,4};
		int size=arr.length;
		System.out.println(IMC(arr,size));
	}
}
