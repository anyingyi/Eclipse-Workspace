import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class Pair<T>{
	private T first;
	private T second;

	public Pair(){
		first=null;
		second=null;
	}
	public Pair(T first,T second){
		this.first=first;
		this.second=second;
	}

	public T getFirst(){
		return first;
	}
	public T getSecond(){
		return second;
	}

	public void setFirst(T newValue){
		first=newValue;
	}
	public void setSecond(T newValue){
		second=newValue;
	}
}

interface me{
	static void display(){
		System.out.println("hello");

	}
}

public class Solution{


	public static void main(String[] args){
		System.out.println(Math.pow(8,0.5));
	}
}

class Arraylg{
	public static <T extends Comparable> Pair<T> minmax(T[] a){
		if (a == null || a.length == 0) return null;
		T min = a[0];
		T max = a[0];
		for (int i = 1; i < a.length; i++)
		{
			if(min.compareTo(a[i])>0) min=a[i];
			if(max.compareTo(a[i])<0) max=a[i];
		}
		return new Pair<>(min,max);
	}

	public static <T> T getMiddle(T... a){
		return a[a.length/2];
	}

	public static <T extends Comparable> T min(T[] a){
		if(a==null||a.length==0) return null;
		T smallest=a[0];
		for(int i=1;i<a.length;i++){
			if(smallest.compareTo(a[i])>0) smallest=a[i];
		}
		return smallest;
	}
}


