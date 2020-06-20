
public class LongestPalindrome {
	public static String longestPalindrome(String s) {
		int len=s.length();
		int small=0,large=0;
		String s1;
		boolean[][] dp=new boolean[len][len];
		for(int le=1;le<=len;le++)
		{
			
			for(int i=0,j=i+le-1;j<len;i++,j++)
			{
				if(le==1)
					dp[i][j]=(s.charAt(j)==s.charAt(i));
				else if(le==2)
				{
					dp[i][j]=(s.charAt(j)==s.charAt(i));
				}
				else 
				{
					if(dp[i+1][j-1]==true&&s.charAt(j)==s.charAt(i))
					{
						dp[i][j]=true;
						
					}
					else
						dp[i][j]=false;
				}
				if(dp[i][j])
					if(le>large+1-small)
					{
						small=i;
						large=j;
					}
			}
		}
		s1=s.substring(small, large+1);
		return s1;
	}
	
	public static void main(String[] args) {
		String s="acbbcd";
		System.out.println(longestPalindrome(s));
	}
}
