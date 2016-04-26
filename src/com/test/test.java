/**
 * @author 吴俊
 * @创建日期 2016年4月21日
 * @版本 V 8.6
 */
package com.test;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.google.common.cache.Cache;





public class test {
	
	
	public static void run_IS_DBSCAN(String path,int K) throws Exception{
		long time = System.currentTimeMillis();
//		List<DataPoint> dataPoints = Until.getDataPoints(path);
		List<DataPoint> dataPoints = Until.getNumberDataPoints(path);
		Cache<Set<DataPoint>, Double> distanceCache = Until.getDistanceCache(dataPoints, K);
//		
//		for (int i = 0; i < dataPoints.size(); i++) {
//			DataPoint dataPoint = dataPoints.get(i);
//			dataPoint.setkNN(Until.getK_NNList(dataPoint,dataPoints, distanceCache));
//		}
		for (int j = 0; j < dataPoints.size(); j++) {
			DataPoint dataPoint = dataPoints.get(j);
			dataPoint.setRKNN(Until.getRK_NNList(dataPoint,dataPoints));
			dataPoint.setISKNN(Until.getISKNN(dataPoint));
		}
//		//按照ISKNN集合大小降序排列
//		Collections.sort(dataPoints, new Comparator<DataPoint>() {
//
//			public int compare(DataPoint o1, DataPoint o2) {
//				// TODO Auto-generated method stub
//				int diff = o1.getISKNN().size()-o2.getISKNN().size();
//				if (diff > 0) {
//					return -1;
//				}
//				if (diff < 0) {
//					return 1;
//				} 
//				return 0;
//			}
//		});
		IS_DBSCAN is_DBSCAN = new IS_DBSCAN(K);
		is_DBSCAN.run_IS_DBSCAN_Cluster(dataPoints);
		for (int m = 0; m < dataPoints.size(); m++) {
			DataPoint dataPoint = dataPoints.get(m);
			dataPoint.to_String();
		}
		System.out.println(System.currentTimeMillis() - time);
	}
	
	/** 参考K-近邻半径识别核心点 */
	public static void run_K_DBSCAN(String path,int K) throws Exception{
		long time = System.currentTimeMillis();
//		List<DataPoint> dataPoints = Until.getDataPoints(path);
		List<DataPoint> dataPoints = Until.getNumberDataPoints(path);
		Cache<Set<DataPoint>, Double> distanceCache = Until.getDistanceCache(dataPoints, K);
//		
//		for (int i = 0; i < dataPoints.size(); i++) {
//			DataPoint dataPoint = dataPoints.get(i);
//			dataPoint.setkNN(Until.getK_NNList(dataPoint,dataPoints, distanceCache));
//		}
		for (int j = 0; j < dataPoints.size(); j++) {
			DataPoint dataPoint = dataPoints.get(j);
			dataPoint.setRKNN(Until.getRK_NNList(dataPoint,dataPoints));
			dataPoint.setISKNN(Until.getISKNN(dataPoint));
		}
//		//按照ISKNN集合大小降序排列
//		Collections.sort(dataPoints, new Comparator<DataPoint>() {
//
//			public int compare(DataPoint o1, DataPoint o2) {
//				// TODO Auto-generated method stub
//				int diff = o1.getISKNN().size()-o2.getISKNN().size();
//				if (diff > 0) {
//					return -1;
//				}
//				if (diff < 0) {
//					return 1;
//				} 
//				return 0;
//			}
//		});
		IS_DBSCAN is_DBSCAN = new IS_DBSCAN(K);
		is_DBSCAN.run_IS_DBSCAN_Cluster(dataPoints);
		for (int m = 0; m < dataPoints.size(); m++) {
			DataPoint dataPoint = dataPoints.get(m);
			dataPoint.to_String();
		}
		System.out.println(System.currentTimeMillis() - time);
	}
	
	public static void run_Weka_DBSCAN(String path,double Eps,int MinPts) throws Exception{
		long time = System.currentTimeMillis();
		//读入数据，封装成DataPoint实例
//		List<DataPoint> dataPoints = Until.getDataPoints(path);
		List<DataPoint> dataPoints = Until.getNumberDataPoints(path);
		// 聚类算法主程序
		DBSCANWeka dbscanWeka = new DBSCANWeka(Eps,MinPts,dataPoints);
		dbscanWeka.buildClusterer();
		//打印每个点聚类结果
		for (int j = 0; j < dataPoints.size(); j++) {
			DataPoint dataPoint = dataPoints.get(j);
			dataPoint.to_String();
		}
		System.out.println("一共耗时： "+1.0*(System.currentTimeMillis() - time)/1000+"s");
	}
	
	
	public static void runBaseDBSCAN(String path,double Eps,int MinPts) throws Exception{
		// TODO Auto-generated method stub
		long time = System.currentTimeMillis();
		//读入数据，封装成DataPoint实例
//		List<DataPoint> dataPoints = Until.getDataPoints(path);
		List<DataPoint> dataPoints = Until.getNumberDataPoints(path);
		// 聚类算法主程序
		BaseDBSCAN baseDBSCAN = new BaseDBSCAN(Eps,MinPts);
		baseDBSCAN.runCluster(dataPoints);
		//打印每个点聚类结果
		for (int j = 0; j < dataPoints.size(); j++) {
			DataPoint dataPoint = dataPoints.get(j);
			dataPoint.to_String();
		}
		System.out.println("一共耗时： "+1.0*(System.currentTimeMillis() - time)/1000+"s");
	}
	
	
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

				
		/** IS_DBSCAN */
		int k = 6; 
		String path = "C:\\Users\\wujun\\Desktop\\dataSet\\iris.csv";
//		String path = "C:\\Users\\wujun\\Desktop\\dataSet\\wine.csv";
//		String path = "C:\\Users\\wujun\\Desktop\\dataSet\\seeds.csv";
//		String path = "C:\\Users\\wujun\\Desktop\\dataSet\\wdbc.csv";
//		String path = "C:\\Users\\wujun\\Desktop\\dataSet\\ionosphere.csv";
//		run_IS_DBSCAN(path, k);
		
		/** Weka中的DBSCAN程序 */
		double Eps = 0.9;
		int MinPts = 6; 
//		run_Weka_DBSCAN(path, Eps, MinPts);
		
		/**最基本的DBSCAN程序，根据最先提出的原始外文实现的 */
		runBaseDBSCAN(path, Eps, MinPts);
		
	}

}
