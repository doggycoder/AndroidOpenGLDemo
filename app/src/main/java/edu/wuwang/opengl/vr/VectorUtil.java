package edu.wuwang.opengl.vr;

import java.util.ArrayList;

//计算三角形法向量的工具类
public class VectorUtil {
	//向量规格化的方法
	public static float[] normalizeVector(float [] vec){
		float mod=module(vec);
		return new float[]{vec[0]/mod, vec[1]/mod, vec[2]/mod};//返回规格化后的向量
	}
	//求向量的模的方法
	public static float module(float [] vec){
		return (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
	}
	//两个向量叉乘的方法
	public static float[] crossTwoVectors(float[] a, float[] b)
	{
		float x=a[1]*b[2]-a[2]*b[1];
		float y=a[2]*b[0]-a[0]*b[2];
		float z=a[0]*b[1]-a[1]*b[0];
		return new float[]{x, y, z};//返回叉乘结果
	}
	//两个向量点乘的方法
	public static float dotTwoVectors(float[] a, float[] b)
	{
		return a[0]*b[0]+a[1]*b[1]+a[2]*b[2];//返回点乘结果
	}

	//根据原纹理坐标和索引，计算卷绕后的纹理的方法
	public static float[] cullTexCoor(
			ArrayList<Float> alST,//原纹理坐标列表（未卷绕）
			ArrayList<Integer> alTexIndex//组织成面的纹理坐标的索引值列表（按逆时针卷绕）
			)
	{
		float[] textures=new float[alTexIndex.size()*2];
		//生成顶点的数组
		int stCount=0;
		for(int i:alTexIndex){
			textures[stCount++]=alST.get(2*i);
			textures[stCount++]=alST.get(2*i+1);
		}
		return textures;
	}
	public static float[] cullVertex(
			ArrayList<Float> alv,//原顶点列表（未卷绕）
			ArrayList<Integer> alFaceIndex//组织成面的顶点的索引值列表（按逆时针卷绕）
			)
	{
		float[] vertices=new float[alFaceIndex.size()*3];
		//生成顶点的数组
		int vCount=0;
		for(int i:alFaceIndex){
			vertices[vCount++]=alv.get(3*i);
			vertices[vCount++]=alv.get(3*i+1);
			vertices[vCount++]=alv.get(3*i+2);
		}
		return vertices;
	}
	//计算圆弧的n等分点坐标的方法
	public static float[] devideBall(
			float r, //球的半径
			float[] start, //指向圆弧起点的向量
			float[] end, //指向圆弧终点的向量
			int n, //圆弧分的份数
			int i //求第i份在圆弧上的坐标（i为0和n时分别代表起点和终点坐标）
			)
	{
		/* 
		 * 先求出所求向量的规格化向量，再乘以半径r即可
		 * s0*x+s1*y+s2*z=cos(angle1)//根据所求向量和起点向量夹角为angle1---1式
		 * e0*x+e1*y+e2*z=cos(angle2)//根据所求向量和终点向量夹角为angle2---2式
		 * x*x+y*y+z*z=1//所球向量的规格化向量模为1---3式
		 * x*n0+y*n1+z*n2=0//所球向量与法向量垂直---4式
		 * 算法为：将1、2两式用换元法得出x=a1+b1*z，y=a2+b2*z的形式，
		 * 将其代入4式求出z，再求出x、y，最后将向量(x,y,z)乘以r即为所求坐标。
		 * 1式和2式是将3式代入得到的，因此已经用上了。
		 * 由于叉乘的结果做了分母，因此起点、终点、球心三点不能共线
		 * 注意结果是将劣弧等分
		 */
		//先将指向起点和终点的向量规格化
		float[] s=VectorUtil.normalizeVector(start);
		float[] e=VectorUtil.normalizeVector(end);
		if(n==0){//如果n为零，返回起点坐标
			return new float[]{s[0]*r, s[1]*r, s[2]*r};
		}
		//求两个向量的夹角
		double angrad=Math.acos(VectorUtil.dotTwoVectors(s, e));//起点终点向量夹角
		double angrad1=angrad*i/n;//所球向量和起点向量的夹角
		double angrad2=angrad-angrad1;//所球向量和终点向量的夹角
		//求法向量normal
		float[] normal=VectorUtil.crossTwoVectors(s, e);
		//用doolittle分解算法解n元一次线性方程组
		double matrix[][]={//增广矩阵
				{s[0],s[1],s[2],Math.cos(angrad1)},
				{e[0],e[1],e[2],Math.cos(angrad2)},
				{normal[0],normal[1],normal[2],0}  
		};
		double result[]=MyMathUtil.doolittle(matrix);//解
		//求规格化向量xyz的值
		float x=(float) result[0];
		float y=(float) result[1];
		float z=(float) result[2];
		//返回圆弧的n等分点坐标
		return new float[]{x*r, y*r, z*r};
	}
	//计算线段的n等分点坐标的方法
	public static float[] devideLine(
			float[] start, //线段起点坐标
			float[] end, //线段终点坐标
			int n, //线段分的份数
			int i //求第i份在线段上的坐标（i为0和n时分别代表起点和终点坐标）
			)
	{
		if(n==0){//如果n为零，返回起点坐标
			return start;
		}
		//求起点到终点的向量
		float[] ab=new float[]{end[0]-start[0], end[1]-start[1], end[2]-start[2]};
		//求向量比例
		float vecRatio=i/(float)n;
		//求起点到所求点的向量
		float[] ac=new float[]{ab[0]*vecRatio, ab[1]*vecRatio, ab[2]*vecRatio};
		//所求坐标
		float x=start[0]+ac[0];
		float y=start[1]+ac[1];
		float z=start[2]+ac[2];
		//返回线段的n等分点坐标
		return new float[]{x, y, z};
	}
}
