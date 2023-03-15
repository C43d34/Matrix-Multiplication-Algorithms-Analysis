import java.util.Arrays;

public class Main
{
		
	public static void main(String[] args) //test
	{
		long startTime;
		long endTime;
		int size = 2; //array size
		int iterations = 1000;
		double iters = (double)iterations;
		TwoMatrix t[] = new TwoMatrix[iterations];
		for (int i = 0; i < iterations; i++)
		{
			t[i] = new TwoMatrix(size);
		}

		// 10 unique instances of two matrices to be multiplied
		// (instantiated before testing as to not affect computation time)
		System.out.println("Using Classic Multiplication for size n = " + size);
		startTime = System.currentTimeMillis(); //initialize timer and begin computation
		
		for (int i = 0; i < iterations; i++)
		{
			t[i].computeClassicMult();
		}	
		
		endTime = System.currentTimeMillis();
	    System.out.println("Total Elapsed time " + (endTime - startTime));
	    System.out.println("Average time " + (endTime - startTime) / iters);

	    System.out.println("Using Naive Divide and Conquer for size n = " + size);
		startTime = System.currentTimeMillis(); //initialize timer and begin computation
		
		for (int i = 0; i < iterations; i++)
		{
			t[i].computeNaive();
		}	
		
		endTime = System.currentTimeMillis();
	    System.out.println("Total Elapsed time " + (endTime - startTime));
	    System.out.println("Average time " + (endTime - startTime) / iters);
	    
	    System.out.println("Using Strassen Divide and Conquer for size n = " + size);
		startTime = System.currentTimeMillis(); //initialize timer and begin computation
		
		for (int i = 0; i < iterations; i++)
		{
			t[i].computeStrassen();
		}	
		
		endTime = System.currentTimeMillis();
	    System.out.println("Total Elapsed time " + (endTime - startTime));
	    System.out.println("Average time " + (endTime - startTime) / iters);

	        
	    
	}
	
	
}


