
public enum EEncodingType {
	/*Solution is represented as an array with n+1 elements from 0 to n where n is a flag.*/
	/*Those elements before element n are placed in the first row, and the others are placed in the second row*/
    ORDER, 
    /*Solution is represented as an array with n elements and an integer variable in range [1, n-2].*/
	/*Those elements before index indicated by the integer variable are placed in the first row, and the others are placed in the second row*/
    ORDER_INT,
    /*Solution is represented by two arrays each with n elements.*/
	/*The first array is a permutation of facilities (p) which represents the order in which facilities are placed.*/
    /*The second array is a bit array (row) where each element is 0 or 1. */
    /*row[i]==0 means the corresponding facility (p[i]) in the first array is placed in first row.*/
    ORDER_BIT, 
    TWO_LIST
}
