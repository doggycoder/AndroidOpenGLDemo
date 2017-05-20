package edu.wuwang.opengl.vr;

public class MyMathUtil{
	static double a[][];
	//通过doolittle分解解n元一次线性方程组的工具方法
	static double[] doolittle(double a[][]){
		MyMathUtil.a=a;
		int rowNum = a.length ;//获得未知数的个数
		int xnum = a[0].length-rowNum;// 所求解的组数（一）
		
		double AugMatrix[][]=new double[10][20];//拓展的增广矩阵
		
		readData(a,rowNum,xnum,AugMatrix);
		
		for(int i=1;i<=rowNum;i++)
		{
			prepareChoose(i,rowNum,AugMatrix);
			choose(i,rowNum,xnum,AugMatrix);
			resolve(i,rowNum,xnum,AugMatrix);
		}

		findX(rowNum,xnum,AugMatrix);
		
		double[] result=new double[rowNum];
		for(int i=0;i<rowNum;i++)
		{
			result[i]=AugMatrix[i+1][rowNum+1];
		}

		return result;
	}
	
	static void readData(double a[][],int rowNum,int xnum,double AugMatrix[][])
	{//增广矩阵的拓展
		for(int i=0;i<=rowNum;i++)
		{
			AugMatrix[i][0]=0;
		}
		for(int i=0;i<=rowNum+xnum;i++)
		{
			AugMatrix[0][i]=0;
		}
		for(int i=1;i<=rowNum;i++)
			for(int j=1;j<=rowNum+xnum;j++)
				AugMatrix[i][j]=a[i-1][j-1];
	}
	
	static void prepareChoose(int times,int rowNum,double AugMatrix[][])
	{//计算准备选主元
		for(int i=times;i<=rowNum;i++)
		{
			for(int j=times-1;j>=1;j--)
			{
				AugMatrix[i][times]=AugMatrix[i][times]-AugMatrix[i][j]*AugMatrix[j][times];
			}
		}
	}
	static void choose(int times,int rowNum,int xnum,double AugMatrix[][])
	{//选主元
		int line=times;
		for(int i=times+1;i<=rowNum;i++)//选最大行
		{
			if(AugMatrix[i][times]*AugMatrix[i][times]>AugMatrix[line][times]*AugMatrix[line][times])
				line=i;
		}
		if(AugMatrix[line][times]==0)//最大数等于零
		{
			System.out.println("doolittle fail !!!");
			
		}
		if(line!=times)//交换
		{
			double temp;
			for(int i=1;i<=rowNum+xnum;i++)
			{
				temp=AugMatrix[times][i];
				AugMatrix[times][i]=AugMatrix[line][i];
				AugMatrix[line][i]=temp;
			}
		}
	}
	static void resolve(int times,int rowNum,int xnum,double AugMatrix[][])
	{//分解
		for(int i=times+1;i<=rowNum;i++)
		{
			AugMatrix[i][times]=AugMatrix[i][times]/AugMatrix[times][times];
		}
		for(int i=times+1;i<=rowNum+xnum;i++)
		{
			for(int j=times-1;j>=1;j--)
			{
				AugMatrix[times][i]=AugMatrix[times][i]-AugMatrix[times][j]*AugMatrix[j][i];
			}
		}
	}
	static void findX(int rowNum,int xnum,double AugMatrix[][])
	{//求解
		for(int k=1;k<=xnum;k++)
		{
			AugMatrix[rowNum][rowNum+k]=AugMatrix[rowNum][rowNum+k]/AugMatrix[rowNum][rowNum];
			for(int i=rowNum-1;i>=1;i--)
			{
				for(int j=rowNum;j>i;j--)
				{
					AugMatrix[i][rowNum+k]=AugMatrix[i][rowNum+k]-AugMatrix[i][j]*AugMatrix[j][rowNum+k];
				}
				AugMatrix[i][rowNum+k]=AugMatrix[i][rowNum+k]/AugMatrix[i][i];
			}
		}
	}
}
