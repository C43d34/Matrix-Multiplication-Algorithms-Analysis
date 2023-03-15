import java.util.Random;
import java.math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

public class TwoMatrix { //Generate two random n x n matrix for later use
	private int col;
	private int row;
	private int realSize;
	private int modifiedSize = 0;
	int matrixA[][];
	int matrixB[][];
	int matrixC[][];
	Random rand = new Random();
	Date timer = new Date();
	
	
	TwoMatrix(int size) //specify matrix dimensions and create 2 matrices
	{
		col = size;
		row = size;
		realSize = size;
		
		double nExponent = Math.log(row)/Math.log(2);
		//if size a power of 2?
		if (nExponent % 1 != 0) //matrix dimensions not  == 2^n where n is an integer
		{
			int nxtPower2 = (int)((nExponent) + 1); //find next largest integer power of 2
			modifiedSize = (int)(Math.pow(2.0, nxtPower2));
			
			//instantiate matrices of new power of 2 size
			matrixA = new int[modifiedSize][modifiedSize]; 
			matrixB = new int[modifiedSize][modifiedSize];
		}
		else
		{
			//instantiate matrices of given power of 2 size
			matrixA = new int[col][row];
			matrixB = new int[col][row];
		}
		
		generateMatrix();
	}
	
	TwoMatrix() //default matrix dimensions (between 2-256) and create 2 matrices
	{
		int power = rand.nextInt(9) + 1; // generate random int 1-8
		int size = (int) Math.pow(2, power); //picks some matrix size 2-256 by default
		col = size;
		row = size;
		matrixA = new int[col][row];
		matrixB = new int[col][row];
		generateMatrix();
	}
	
	public void generateMatrix() //populate matrix A and B with random elements (some integer range for elements)
	{
	
		for (int i = 0; i < col; i++)
		{
			for (int j = 0; j < row; j++)
			{
				matrixA[i][j] = rand.nextInt(19) - 9; //random from 9 to -9
				matrixB[i][j] = rand.nextInt(19) - 9;
			}
		}
		
//		System.out.println(Arrays.deepToString(matrixA));
//		System.out.println(Arrays.deepToString(matrixB));
	}
	
	public int getSize()
	{
		return(this.realSize);
	}
	
	public void sanityMatrix() //populate matrix A and B with predetermined matrix for sanity check
	{
		//set default size 4x4
		col = 4;
		row = 4;
		realSize = 4;
		
		matrixA = new int[][] {
			{2,0,-1,6},
			{3,7,8,0},
			{-5,1,6,-2},
			{8,0,2,7}
		};
		
		matrixB = new int[][] {
			{0,1,6,3},
			{-2,8,7,1},
			{2,0,-1,0},
			{9,1,6,-2}
		};
		
		System.out.println("Created two default 4x4 matricies");
		System.out.println("MatrixA:\n" + Arrays.deepToString(matrixA));
		System.out.println("MatrixB:\n" + Arrays.deepToString(matrixB));

		//print out result of matrix multiplication using all 3 methods
		System.out.println("Classic Matrix Multiplication Result\n" + Arrays.deepToString(computeClassicMult()));
	
		System.out.println("Naive Divide and Conquer Result\n" + Arrays.deepToString(naiveDivideAndConquer(0, realSize-1, 0, realSize-1,
								  									0, realSize-1, 0, realSize-1)));
		
		System.out.println("Strassen Divide and Conquer Result\n" + Arrays.deepToString(strassenDAC(0, realSize-1, 0, realSize-1,
														   0, realSize-1, 0, realSize-1, matrixA, matrixB)));
	}
	
	public int[][] computeClassicMult() //multiple each column by row to calculate each element 
	{
		
		int[][] matrixC = new int[realSize][realSize];
		for (int i = 0; i < realSize; i++)
		{
			
			for (int j = 0; j < realSize; j++)
			{
				int temp = 0;
				for (int k = 0; k < realSize; k++)
				{
					temp += matrixA[i][k] * matrixB[k][j];
				}
				matrixC[i][j] = temp;
			}
		}
		return(matrixC);
		
	}
	
	public void computeNaive() //compute matrix multiplication of matrix size n using divide and conquer
	{
		if (modifiedSize != 0)//matrix dim not power of 2 so it was temporarily modified to be a power of 2
		{
			naiveDivideAndConquer(0, modifiedSize - 1, 0, modifiedSize - 1, //A
								  0, modifiedSize - 1, 0, modifiedSize - 1); //B
		}
		else //matrix dim = integer power of 2
		{
			naiveDivideAndConquer(0, realSize-1, 0, realSize-1, //A
								  0, realSize-1, 0, realSize-1); //B
		}
	    	
	}
	
	public int[][] naiveDivideAndConquer(int aRowStart, int aRowEnd, int aColStart, int aColEnd,
			int bRowStart, int bRowEnd, int bColStart, int bColEnd) //parameters of the quadrant(Q1,Q2,Q3,Q4) of the two matricies
	{
		//base case size = 2x2
		if (aRowEnd - aRowStart == 1)
		{
			int[][] temp = new int[2][2]; //calculate c11, c12, c21, c22
			temp[0][0] = (matrixA[aRowStart][aColStart] * matrixB[bRowStart][bColStart]) + 
					(matrixA[aRowStart][aColEnd] * matrixB[bRowEnd][bColStart]);
			temp[0][1] = (matrixA[aRowStart][aColStart] * matrixB[bRowStart][bColEnd]) + 
					(matrixA[aRowStart][aColEnd] * matrixB[bRowEnd][bColEnd]);
			temp[1][0] = (matrixA[aRowEnd][aColStart] * matrixB[bRowStart][bColStart]) + 
					(matrixA[aRowEnd][aColEnd] * matrixB[bRowEnd][bColStart]);
			temp[1][1] = (matrixA[aRowEnd][aColStart] * matrixB[bRowStart][bColEnd]) + 
					(matrixA[aRowEnd][aColEnd] * matrixB[bRowEnd][bColEnd]);
			
			return(temp);
	
		}
		else 
		{
			int curSize = aRowEnd - aRowStart + 1;//square matrices so size is consistent between row and column

			int[][] temp = new int[curSize][curSize];
		
			//C11
			int A11B11[][] = naiveDivideAndConquer(aRowStart,(aRowStart + aRowEnd)/2,aColStart,(aColStart + aColEnd)/2,
					bRowStart,(bRowStart+ bRowEnd)/2,bColStart,(bColStart + bColEnd)/2);// A11 B11
			int A12B21[][] = naiveDivideAndConquer(aRowStart,(aRowStart + aRowEnd)/2,((aColStart + aColEnd)/2) + 1,aColEnd,
					((bRowStart+ bRowEnd)/2) + 1,bRowEnd,bColStart,(bColStart + bColEnd)/2); // A12 B21
			for (int i = 0; i < curSize/2; i++) //add all the elements to the topLeft corner portion of the temp matrix C11
			{
				for (int j = 0; j < curSize/2; j++)
				{
					temp[i][j] = A11B11[i][j] + A12B21[i][j];
				}
			}
			//C12
			int A11B12[][] = naiveDivideAndConquer(aRowStart,(aRowStart + aRowEnd)/2,aColStart,(aColStart + aColEnd)/2,
					bRowStart,(bRowStart+ bRowEnd)/2,((bColStart + bColEnd)/2) + 1 ,bColEnd);// A11 B12
			int A12B22[][] = naiveDivideAndConquer(aRowStart,(aRowStart + aRowEnd)/2,((aColStart + aColEnd)/2) + 1,aColEnd,
					((bRowStart+ bRowEnd)/2) + 1,bRowEnd,((bColStart + bColEnd)/2) + 1,bColEnd); // A12 B22
			for (int i = 0; i < curSize/2; i++) //add all the elements to the topRight corner portion of the temp matrix C12
			{
				for (int j = 0; j < curSize/2; j++)
				{
					temp[i][j + curSize/2] = A11B12[i][j] + A12B22[i][j];
							
				}
			}
			//C21
			int A21B11[][] = naiveDivideAndConquer((aRowStart + aRowEnd)/2 + 1,aRowEnd,aColStart,(aColStart + aColEnd)/2,
					bRowStart,(bRowStart+ bRowEnd)/2,bColStart,(bColStart + bColEnd)/2); // A21 B11
			int A22B21[][] = naiveDivideAndConquer((aRowStart + aRowEnd)/2 + 1,aRowEnd,((aColStart + aColEnd)/2) + 1,aColEnd,
					((bRowStart+ bRowEnd)/2) + 1,bRowEnd,bColStart,(bColStart + bColEnd)/2); // A22 B21
			for (int i = 0; i < curSize/2; i++) //add all the elements to the bottomLeft corner portion of the temp matrix C21
			{
				for (int j = 0; j < curSize/2; j++)
				{
					temp[i + curSize/2][j] = A21B11[i][j] + A22B21[i][j];
							
				}
			}
			//C22
			int A21B12[][] = naiveDivideAndConquer(((aRowStart + aRowEnd)/2) + 1,aRowEnd,aColStart,(aColStart + aColEnd)/2,
					bRowStart,(bRowStart+ bRowEnd)/2,((bColStart + bColEnd)/2) + 1 ,bColEnd); // A21 B12
			int A22B22[][] = naiveDivideAndConquer((aRowStart + aRowEnd)/2 + 1,aRowEnd,((aColStart + aColEnd)/2) + 1,aColEnd,
					((bRowStart+ bRowEnd)/2) + 1,bRowEnd,((bColStart + bColEnd)/2) + 1,bColEnd); // A22 B22
			for (int i = 0; i < curSize/2; i++) //add all the elements to the bottomRight corner portion of the temp matrix C22
			{
				for (int j = 0; j < curSize/2; j++)
				{
					temp[i + curSize/2][j + curSize/2] = A21B12[i][j] + A22B22[i][j];
							
				}
			}
			return(temp);
		}
		
		
	}
	
	public void computeStrassen() //compute strassen matrixmult with size of matrix n
	{
		if (modifiedSize != 0)//matrix dim not power of 2 so it was temporarily modified to be a power of 2
		{
			strassenDAC(0, modifiedSize - 1, 0, modifiedSize - 1, //A
							 0, modifiedSize - 1, 0, modifiedSize - 1, matrixA, matrixB); //B
		}
		else //matrix dim = integer power of 2
		{
			strassenDAC(0, realSize-1, 0, realSize-1, //A
						 0, realSize-1, 0, realSize-1, matrixA, matrixB); //B));
		}
	}
	
	public int[][] strassenDAC(int aRowStart, int aRowEnd, int aColStart, int aColEnd,
			int bRowStart, int bRowEnd, int bColStart, int bColEnd, int auxMatrixA[][], int auxMatrixB[][]) //compute matrix using strassen method
	{
		//base case size = 2x2
		if (aRowEnd - aRowStart == 1)
		{
			int[][] temp = new int[2][2]; //calculate c11, c12, c21, c22
			temp[0][0] = (auxMatrixA[aRowStart][aColStart] * auxMatrixB[bRowStart][bColStart]) + 
					(auxMatrixA[aRowStart][aColEnd] * auxMatrixB[bRowEnd][bColStart]);
			temp[0][1] = (auxMatrixA[aRowStart][aColStart] * auxMatrixB[bRowStart][bColEnd]) + 
					(auxMatrixA[aRowStart][aColEnd] * auxMatrixB[bRowEnd][bColEnd]);
			temp[1][0] = (auxMatrixA[aRowEnd][aColStart] * auxMatrixB[bRowStart][bColStart]) + 
					(auxMatrixA[aRowEnd][aColEnd] * auxMatrixB[bRowEnd][bColStart]);
			temp[1][1] = (auxMatrixA[aRowEnd][aColStart] * auxMatrixB[bRowStart][bColEnd]) + 
					(auxMatrixA[aRowEnd][aColEnd] * auxMatrixB[bRowEnd][bColEnd]);


			return(temp);
	
		}
		else 
		{
			int curSize = aRowEnd - aRowStart + 1;//square matrices so size is consistent between row and column

			int[][] temp = new int[curSize][curSize];
			
			int[][] strassen1 = new int[curSize/2][curSize/2];	//(B12 - B22)
			for (int i = 0; i < curSize/2; i++ )
			{
				for (int j = 0; j < curSize/2; j++)
				{
					strassen1[i][j] = auxMatrixB[i + bRowStart][j + (1 + (bColStart + bColEnd)/2)] - auxMatrixB[i + (1 + (bRowStart + bRowEnd)/2)][j + (1 + (bColStart + bColEnd)/2)];
				}
			}
			int P1[][] = strassenDAC(aRowStart, (aRowStart + aRowEnd)/2, aColStart, (aColStart + aColEnd)/2, 0, (curSize/2) - 1, 0, (curSize/2) - 1, auxMatrixA, strassen1);
//////////////////////////////////////////////////////////////////////////////////			
			int[][] strassen2 = new int[curSize/2][curSize/2];	//(A11 + A12)
			for (int i = 0; i < curSize/2; i++ )
			{
				for (int j = 0; j < curSize/2; j++)
				{
					strassen2[i][j] = auxMatrixA[i + aRowStart][j + aColStart] + auxMatrixA[i + aRowStart][j + (1 + (aColStart + aColEnd)/2)];
				}
			}
			int P2[][] = strassenDAC( 0, (curSize/2) - 1, 0, (curSize/2) - 1, 1 + (bRowStart + bRowEnd)/2, bRowEnd, 1 + bColEnd/2, bColEnd, strassen2, auxMatrixB);
///////////////////////////////////////////////////////////////////////////////////	
			int[][] strassen3 = new int[curSize/2][curSize/2];	//(A21 + A22)
			for (int i = 0; i < curSize/2; i++ )
			{
				for (int j = 0; j < curSize/2; j++)
				{
					strassen3[i][j] = auxMatrixA[i + (1 + (aRowStart + aRowEnd)/2)][j + aColStart] + auxMatrixA[i + (1 + (aRowStart + aRowEnd)/2)][j + (1 + (aColStart + aColEnd)/2)];
				}
			}
			int P3[][] = strassenDAC( 0, (curSize/2) - 1, 0, (curSize/2) - 1, bRowStart, (bRowStart + bRowEnd)/2, bColStart, bColEnd/2, strassen3, auxMatrixB);
///////////////////////////////////////////////////////////////////////////////////		
			int[][] strassen4 = new int[curSize/2][curSize/2];	//(B21 - B11)
			for (int i = 0; i < curSize/2; i++ )
			{
				for (int j = 0; j < curSize/2; j++)
				{
					strassen4[i][j] = auxMatrixB[i + (1 + (bRowStart + bRowEnd)/2)][j + bColStart] - auxMatrixB[i + bRowStart][j + bColStart];
				}
			}
			int P4[][] = strassenDAC(1 + (aRowStart + aRowEnd)/2, aRowEnd, 1 + (aColStart + aColEnd)/2, aColEnd, 0, (curSize/2) - 1, 0, (curSize/2) - 1, auxMatrixA, strassen4);
///////////////////////////////////////////////////////////////////////////////////		
			int[][] strassen5 = new int[curSize/2][curSize/2];	//(A11 + A22)
			int[][] strassen6 = new int[curSize/2][curSize/2];	//(B11 + B22)
			for (int i = 0; i < curSize/2; i++ )
			{
				for (int j = 0; j < curSize/2; j++)
				{
					strassen5[i][j] = auxMatrixA[i + aRowStart][j + aColStart] + auxMatrixA[i + (1 + (aRowStart + aRowEnd)/2)][j + (1 + (aColStart + aColEnd)/2)];
					strassen6[i][j] = auxMatrixB[i + bRowStart][j + bColStart] + auxMatrixB[i + (1 + (bRowStart + bRowEnd)/2)][j + (1 + (bColStart + bColEnd)/2)];
				}
			}
			int P5[][] = strassenDAC( 0, (curSize/2) - 1, 0, (curSize/2) - 1, 0, (curSize/2) - 1, 0, (curSize/2) - 1, strassen5, strassen6);
///////////////////////////////////////////////////////////////////////////////////		
			int[][] strassen7 = new int[curSize/2][curSize/2];	//(A12 - A22)
			int[][] strassen8 = new int[curSize/2][curSize/2];	//(B21 + B22)
			for (int i = 0; i < curSize/2; i++ )
			{
				for (int j = 0; j < curSize/2; j++)
				{
					strassen7[i][j] = auxMatrixA[i + aRowStart][j + (1 + (aColStart + aColEnd)/2)] - auxMatrixA[i + (1 + (aRowStart + aRowEnd)/2)][j + (1 + aColEnd/2)];
					strassen8[i][j] = auxMatrixB[i + (1 + (bRowStart + bRowEnd)/2)][j + bColStart] + auxMatrixB[i + (1 + (bRowStart + bRowEnd)/2)][j + (1 + (bColStart + bColEnd)/2)];
				}
			}
			int P6[][] = strassenDAC( 0, (curSize/2) - 1, 0, (curSize/2) - 1, 0, (curSize/2) - 1, 0, (curSize/2) - 1, strassen7, strassen8);
///////////////////////////////////////////////////////////////////////////////////	
			int[][] strassen9 = new int[curSize/2][curSize/2];	//(A11 - A21)
			int[][] strassen10 = new int[curSize/2][curSize/2];	//(B11 + B12)
			for (int i = 0; i < curSize/2; i++ )
			{
				for (int j = 0; j < curSize/2; j++)
				{
					strassen9[i][j] = auxMatrixA[i + aRowStart][j + aColStart] - auxMatrixA[i + (1 + (aRowStart + aRowEnd)/2)][j + aColStart];
					strassen10[i][j] = auxMatrixB[i + bRowStart][j + bColStart] + auxMatrixB[i + bRowStart][j + (1 + (bColStart + bColEnd)/2)];
				}
			}
			int P7[][] = strassenDAC( 0, (curSize/2) - 1, 0, (curSize/2) - 1, 0, (curSize/2) - 1, 0, (curSize/2) - 1, strassen9, strassen10);
			
			
			//C11
			for (int i = 0; i < curSize/2; i++) //add all the elements to the topLeft corner portion of the temp matrix C11
			{
				for (int j = 0; j < curSize/2; j++)
				{
					temp[i][j] = -P2[i][j] + P4[i][j] + P5[i][j] + P6[i][j];
				}
			}
			
			//C12
			for (int i = 0; i < curSize/2; i++) //add all the elements to the topLeft corner portion of the temp matrix C11
			{
				for (int j = 0; j < curSize/2; j++)
				{
					temp[i][j + (curSize/2)] = P1[i][j] + P2[i][j];
				}
			}
			
			//C21
			for (int i = 0; i < curSize/2; i++) //add all the elements to the topLeft corner portion of the temp matrix C11
			{
				for (int j = 0; j < curSize/2; j++)
				{
					temp[i + (curSize/2)][j] = P3[i][j] + P4[i][j];
				}
			}
			
			//C22
			for (int i = 0; i < curSize/2; i++) //add all the elements to the topLeft corner portion of the temp matrix C11
			{
				for (int j = 0; j < curSize/2; j++)
				{
					temp[i + (curSize/2)][j + (curSize/2)] = P1[i][j] - P3[i][j] + P5[i][j] - P7[i][j];
				}
			}
			
			
//			System.out.println(Arrays.deepToString(temp));
			return(temp);
		}
	}
	
	
}
