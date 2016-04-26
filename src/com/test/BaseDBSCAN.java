/**
 * @author 吴俊
 * @创建日期 2016年4月26日
 * @版本 V 8.6
 */
package com.test;

import java.util.ArrayList;
import java.util.List;


public class BaseDBSCAN {

	final static int Noise = 0;
	final static int Unclassifed = -1;
	private double Eps;
	private int MinPts;
	
	
	
	public void runCluster(List<DataPoint> dataPoints) {
		int clusterID = 1;
		for (int i = 0; i < dataPoints.size(); i++) {
			DataPoint dataPoint = dataPoints.get(i);
			if (dataPoint.getClusterID() == Unclassifed) {
				if (ExpandCluster(dataPoints,dataPoint,clusterID)) {
					clusterID++;
				}
			}
		}
	} 
	
	
	
	/**
	 * @param dataPoints
	 * @param dataPoint
	 * @param clusterID
	 * @param eps
	 * @param minPts
	 * @return
	 */
	private boolean ExpandCluster(List<DataPoint> dataPoints, DataPoint dataPoint, int clusterID) {
		// TODO Auto-generated method stub
		List<DataPoint> seeds = regionQuery(dataPoints,dataPoint);
		if (seeds.size()<getMinPts()) {
			dataPoint.setClusterID(Noise);
			return false;
		} 
		for (int i = 0; i<seeds.size(); i++) {
			DataPoint dataPoint2 = seeds.get(i);
			dataPoint2.setClusterID(clusterID);
			if (dataPoint.equals(dataPoint2)) {
				seeds.remove(i);
				i--;
			}
		}
		for (int j = 0; j<seeds.size(); j++) {
			DataPoint currentP = seeds.get(j);
			List<DataPoint> result = regionQuery(dataPoints, currentP);
			if (result.size()>getMinPts()) {
				for (int i = 0; i<result.size(); i++) {
					DataPoint resultP = result.get(i);
					if (resultP.getClusterID() == Unclassifed || resultP.getClusterID() == Noise) {
						if (resultP.getClusterID() == Unclassifed) {
							seeds.add(resultP);
						}
						resultP.setClusterID(clusterID);
					}
				}
			}
			seeds.remove(j);
			j--;
		}	
		return true;
	}



	/**
	 * @param dataPoints
	 * @param dataPoint
	 * @param eps
	 * @return dataPoint的Eps邻域点列表
	 */
	private List<DataPoint> regionQuery(List<DataPoint> dataPoints, DataPoint dataPoint) {
		// TODO Auto-generated method stub
		List<DataPoint> regionQueryList = new ArrayList<DataPoint>();
		for (DataPoint dataPoint2 : dataPoints) {
			double distance = Until.getNumberDistance(dataPoint, dataPoint2);
			if (distance < getEps()) {
				regionQueryList.add(dataPoint2);
			}
		}
		return regionQueryList;
	}




	public BaseDBSCAN()  {
		this.Eps = 0.9;
		this.MinPts = 6;
		
	}
	
	public BaseDBSCAN(double Eps,int MinPts) {
		this.Eps = Eps;
		this.MinPts = MinPts;
	}

	public double getEps() {
		return Eps;
	}

	public int getMinPts() {
		return MinPts;
	}

	public void setEps(double eps) {
		Eps = eps;
	}

	public void setMinPts(int minPts) {
		MinPts = minPts;
	}

}

