/**
 * @author 吴俊
 * @创建日期 2016年4月24日
 * @版本 V 8.6
 */
package com.test;

/**
 * 近邻节点
 */
public class KNNnode {

	private DataPoint dataPoint;
	private double distance;
	
	public KNNnode() {
		
	}
	
	public KNNnode(DataPoint dataPoint, double distance) {
		this.dataPoint = dataPoint;
		this.distance = distance;
	}
	
	public DataPoint getDataPoint() {
		return dataPoint;
	}
	public double getDistance() {
		return distance;
	}
	public void setDataPoint(DataPoint dataPoint) {
		this.dataPoint = dataPoint;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
}
