package com.test;
import java.util.List;


/**
 * @author 吴俊
 * @创建日期 2016年4月26日
 * @版本 V 8.6
 */

public class DataPoint {
	
	/** 样本点名 */
	private int dataPointID;
	/** 样本点的数值属性 */
	private double[] num_Dimension;
	/** 样本点的非数值属性 */
	private String[] str_Dimension;
	/** 样本点状态（-1：uncluterID【默认值】，0：noise） */
	private int clusterID;
	/** 样本点K-近邻点ID集合*/
	private List<DataPoint> kNN;
	/** 样本点反K-近邻点ID集合 */
	private List<DataPoint> RKNN;
	/** 样本点反K-近邻点ID集合 */
	private List<DataPoint> ISKNN;
	/** 样本点是否是离群点，用于检测聚类是否正确（0：noise,1:not_noise）  */
	private int notNoise;
	/** K_近邻距离 */
	private double k_distance;
	
	/** 默认构造函数 */
	public DataPoint() {
		// TODO Auto-generated constructor stub
		this.clusterID = -1;
	}
	
	/** 构造函数 */
	public DataPoint(int dataPointID,double[] num_Dimension,String[] str_Dimension,int notNoise) {
		// TODO Auto-generated constructor stub
		this.clusterID = -1;
		this.dataPointID = dataPointID;
		this.num_Dimension = num_Dimension;
		this.str_Dimension = str_Dimension;
		this.notNoise = notNoise;
	}
			

	/** 打印：[ID:dataPointID]isNoise ——> clusterID */
	public void to_String() {
		System.out.println("[ID：" + this.dataPointID + "] "+"   ————>   " + this.getClusterID());
	}
	
	public int getDataPointID() {
		return dataPointID;
	}
	public void setDataPointID(int dataPointID) {
		this.dataPointID = dataPointID;
	}
	public double[] getNum_Dimension() {
		return num_Dimension;
	}
	public void setNum_Dimension(double[] num_Dimension) {
		this.num_Dimension = num_Dimension;
	}
	public String[] getStr_Dimension() {
		return str_Dimension;
	}
	public void setStr_Dimension(String[] str_Dimension) {
		this.str_Dimension = str_Dimension;
	}
	public int getClusterID() {
		return clusterID;
	}
	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}

	public List<DataPoint> getkNN() {
		return kNN;
	}
	public List<DataPoint> getRKNN() {
		return RKNN;
	}
	public void setkNN(List<DataPoint> kNN) {
		this.kNN = kNN;
	}
	public void setRKNN(List<DataPoint> rKNN) {
		RKNN = rKNN;
	}
	public List<DataPoint> getISKNN() {
		return ISKNN;
	}

	

	public void setISKNN(List<DataPoint> iSKNN) {
		this.ISKNN = iSKNN;
	}

	public int getNotNoise() {
		return notNoise;
	}

	public void setNotNoise(int notNoise) {
		this.notNoise = notNoise;
	}

	public double getK_distance() {
		return k_distance;
	}

	public void setK_distance(double k_distance) {
		this.k_distance = k_distance;
	}

}
