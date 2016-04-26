/**
 * @author 吴俊
 * @创建日期 2016年4月19日
 * @版本 V 8.6
 */
package com.test;

import java.util.List;

public class IS_DBSCAN {
	
	/** K-近邻参数，也可以代表MinPts */
	int K;

	/** 默认构造函数 */
	public IS_DBSCAN() {
		// TODO Auto-generated constructor stub
		this.K = 6;
	}
	
	/** 构造函数 */
	public IS_DBSCAN (int K) {
		this.K = K;
	}
	

	/**
	 * IS-DBSCAN算法主函数
	 * */
	public void run_IS_DBSCAN_Cluster(List<DataPoint> dataPoints) {
		int clusterID = 1;
		System.out.println("开始寻找核心点......");
		for (DataPoint dataPoint : dataPoints) 
			if (dataPoint.getClusterID() == -1) 
				if (ExpandCoreCluster(dataPoint, clusterID)) 
					clusterID++;
		System.out.println("核心点寻找完成......");
		System.out.println("开始对非核心点进行聚类......");
		for (DataPoint dataPoint : dataPoints) {
			if (dataPoint.getClusterID() == -1) {
				List<DataPoint> dataPointISKNN = dataPoint.getISKNN();
				int i = 0;
				for (int j=0;j<dataPointISKNN.size(); j++) {
					DataPoint dataPoint1 = dataPointISKNN.get(j);
					if (dataPoint1.getClusterID() == 0) 
						i++;
				}
				if (i == dataPointISKNN.size() || dataPointISKNN.size() == 0) {
					dataPoint.setClusterID(0);
					continue;
				} else {
					//如果ISKNN邻域点P都没有簇标签，则递归检测P的ISKNN标签
					for (int m = 0; m < dataPointISKNN.size(); m++) {
						DataPoint dataPoint2 = dataPointISKNN.get(m);
						int nearestClusterID = dataPoint2.getClusterID();
						if (nearestClusterID != -1) {
							dataPoint.setClusterID(nearestClusterID);
							break;
						} else {
							dataPointISKNN.addAll(dataPoint2.getISKNN());
						}
						dataPointISKNN.remove(m);
						m--;
					}
				}
			}
		} 
		System.out.println("非核心点进行聚类完成......");
	}
	

	/**
	 * 核心点扩展
	 * @param dataPoint:当前样本点
	 * @param clusterID：当前最大簇编号
	 * @param dataPoints：样本点集合
	 * @return 是否是核心点，是则进行ISKNN领域核心点扩展，否则不执行任何操作
	 */
	public boolean ExpandCoreCluster(DataPoint dataPoint,int clusterID) {
		List<DataPoint> seedList = dataPoint.getISKNN();
		double e = (2*this.K)/3;
		if (seedList.size() > e) {
			dataPoint.setClusterID(clusterID);
//			System.out.println(dataPoint.getDataPointID() +" "+dataPoint.getClusterID());
		}	
		else 
			return false;
		for (int i = 0; i < seedList.size(); i++) {
			DataPoint dataPoint1 = seedList.get(i);
			List<DataPoint> seedList1 = dataPoint1.getISKNN();
			if (seedList1.size() > e) {
				dataPoint1.setClusterID(clusterID);
//				System.out.println(dataPoint1.getDataPointID()+" "+dataPoint1.getClusterID());
				for (int j = 0; j < seedList1.size(); j++) {
					DataPoint dataPoint2 = seedList1.get(j);
					int dataPoint2ClusterID = dataPoint2.getClusterID();
					if (dataPoint2ClusterID == -1 && !seedList.contains(dataPoint2)) 
						seedList.add(dataPoint2);
				}
			}
		}
		return true;
	}
	
	public int getK() {
		return K;
	}
	public void setK(int k) {
		K = k;
	}

}
