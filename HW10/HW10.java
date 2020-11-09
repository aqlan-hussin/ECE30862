import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
import java.lang.*;

class MatrixObject {                                         
    int n;
    int [][] matrix;
    
    MatrixObject(int q){
    	n = q;
    }
    
    public void fill () throws IllegalArgumentException{
    	int i, x;
    	matrix = new int[n][n];
    	Random rn = new Random();
    	
    	if (n % 2 != 0) {
        	throw new IllegalArgumentException("N is " + n  + " but it must be even");
        }

    	
    	for (i = 0; i < n; i++) {
    		for (x = 0; x < n; x++) {
    			
    			matrix[i][x] = rn.nextInt(10);
    		}
    	}
    }
    
    public void ZeroFill () throws IllegalArgumentException{
    	int i, x;
    	matrix = new int[n][n];
    	
    	for (i = 0; i < n; i++) {
    		for (x = 0; x < n; x++) {
    			matrix[i][x] = 0;
    		}
    	}
    }
    
    public int GetValue (int x, int y) {
    	return matrix[y][x];
    }
    
    public void SetValue (int x, int y, int value) {
    	matrix[y][x] = value;
    }
    

    public void print () {
    	int y, x;
    	
    	for (y = 0; y < n; y++) {
    		for (x = 0; x < n; x++) {
    			System.out.print(matrix[y][x] + " ");
    		}
    		System.out.println();
    	}
    	System.out.println();
    }
    
    public void multiply (MatrixObject m1, MatrixObject m2, int xmin, int xmax, int ymin, int ymax) {
    
    	for (int i = xmin; i < xmax; i++) { 
            for (int j = ymin; j < ymax; j++) { 
                for (int k = 0; k < n; k++) { 
                	//System.out.println(j + "," + i + " = " + this.GetValue(i,j) + " for result   " + k + "," + j + " = " + m1.GetValue(k,j) + " for m1   " + i + "," + k + " = " + m2.GetValue(i,k) + " for m2");
                	this.SetValue(i, j, this.GetValue(i, j) + (m1.GetValue(k,j) * m2.GetValue(i, k)));
                }
            }
        }
  	
    }
    
    public long multiply (MatrixObject m1, MatrixObject m2) {
    	long curr = System.currentTimeMillis();
    	for (int i = 0; i < n; i++) { 
            for (int j = 0; j < n; j++) { 
                for (int k = 0; k < n; k++) { 
                	//System.out.println(j + "," + i + " = " + this.GetValue(i,j) + " for result   " + k + "," + j + " = " + m1.GetValue(k,j) + " for m1   " + i + "," + k + " = " + m2.GetValue(i,k) + " for m2");
                	this.SetValue(i, j, this.GetValue(i, j) + (m1.GetValue(k,j) * m2.GetValue(i, k)));
                }
            }
        }
  	return System.currentTimeMillis() - curr;
    }
    

}


class MultiplyThread extends Thread {
	
	MatrixObject m1, m2, result;
	int xmin, xmax, ymin, ymax;
	
	public void run() {
		//System.out.println("Calling multiply with " + xmin + ", " + xmax + ", " + ymin + ", " + ymax);
		
		
		result.multiply(m1, m2, xmin, xmax, ymin, ymax);
	}

	public MultiplyThread(MatrixObject result, MatrixObject m1, MatrixObject m2, int xmin, int xmax, int ymin, int ymax) {
		this.m1 = m1;
		this.m2 = m2;
		this.result = result;
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		
		
		
	}
}


class MatrixMultiply {  
    public static void main( String[] args ) {           
        boolean loop = true;
        long curr;
        
        while (loop) {
          	
	    	
	        try {
	        	Scanner in = new Scanner(System.in);
	            System.out.println("Input integer n, size of array");
	            int q = in.nextInt();
	        	MatrixObject m1 = new MatrixObject(q);  
	        	m1.fill();
	        	MatrixObject m2 = new MatrixObject(q);  
	        	m2.fill();
	        	//m1.print();
	        	//m2.print();
	        	
	        	MatrixObject Result = new MatrixObject(q);
	        	Result.ZeroFill();
	        	
	        	System.out.println("Time " + Result.multiply(m1, m2) + " ms for single thread");
	        	//Result.print();
	        	
	        	
	        	curr = System.currentTimeMillis();
	        	Result.ZeroFill();
	        	MultiplyThread t1 = new MultiplyThread(Result, m1, m2, 0, q, 0, q/2);
	        	MultiplyThread t2 = new MultiplyThread(Result, m1, m2, 0, q, q/2, q);
	        	t1.run();
	        	t2.run();
	        	//Result.print();
	        	System.out.println("Time " + (System.currentTimeMillis() - curr) + " ms for two threads");
	        	
	        	
	        	curr = System.currentTimeMillis();
	        	Result.ZeroFill();
	        	MultiplyThread ta = new MultiplyThread(Result, m1, m2, 0, q/2, 0, q/2);
	        	MultiplyThread tb = new MultiplyThread(Result, m1, m2, q/2, q, 0, q/2);
	        	MultiplyThread tc = new MultiplyThread(Result, m1, m2, 0, q/2, q/2, q);
	        	MultiplyThread td = new MultiplyThread(Result, m1, m2, q/2, q, q/2, q);
	        	ta.run();
	        	tb.run();
	        	tc.run();
	        	td.run();
	        	//Result.print();
	        	System.out.println("Time " + (System.currentTimeMillis() - curr) + " ms for four threads");
	        	
	        	loop = false;
	        }
	        catch (IllegalArgumentException e) {
	        	System.out.println(e.getLocalizedMessage());
	        	
	        }
	        
	        
        }
    }
} 