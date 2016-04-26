/**
 * @author 吴俊
 * @创建日期 2016年4月20日
 * @版本 V 8.6
 */
package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;




/**
 * 工具类主要功能有：
 * 生成KNN、RKNN
 * 读入数据
 * 生成点距离，格式为Map<Set<Point>,double> 
 */
public class Until {
	
	/**
	 * 将数据文件中的每条记录转化成一个个DataPoint实例，最后生成一个List
	 * 文件第一列标记是否是noise,后面是数值和文本属性
	 * @param path：数据文件路径
	 * @return DataPoints：实体列表
	 * @throws IOException 
	 */
	public static List<DataPoint> getDataPoints(String path) throws IOException {
		List<DataPoint> dataPoints = new ArrayList<DataPoint>();
		System.out.println("开始取数数据集......");
		FileReader dataFile = new FileReader(new File(path));
		BufferedReader bufferedReader = new BufferedReader(dataFile);
		String tempString ="";
		while ((tempString = bufferedReader.readLine()) != null) {
			String[] items = tempString.trim().split(",");
			DataPoint dataPoint = new DataPoint();
			dataPoint.setNotNoise(Integer.parseInt(items[0]));
			double[] num_Dimension = new double[12] ;
			for (int i = 1; i < items.length-2; i++) 
				num_Dimension[i-1] = Double.parseDouble(items[i]);
			dataPoint.setNum_Dimension(num_Dimension);
			String[] str_Dimension = new String[2] ;
			for (int j = items.length-2; j < items.length; j++) 
				str_Dimension[j-items.length+2] = items[j];
			dataPoint.setStr_Dimension(str_Dimension);
			dataPoint.setDataPointID(dataPoints.size());
			dataPoints.add(dataPoint);
		}
		System.out.println("数据集读取完，总共生成 "+dataPoints.size()+ " 个对象");
		return dataPoints;
	}
	
	/**
	 * 读取数据，前面是数值型属性，最后一个是分类标签
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<DataPoint> getNumberDataPoints(String path) throws IOException {
		List<DataPoint> dataPoints = new ArrayList<DataPoint>();
		System.out.println("开始取数数据集......");
		FileReader dataFile = new FileReader(new File(path));
		BufferedReader bufferedReader = new BufferedReader(dataFile);
		String tempString ="";
		while ((tempString = bufferedReader.readLine()) != null) {
			String[] items = tempString.trim().split(",");
			DataPoint dataPoint = new DataPoint();
			double[] num_Dimension = new double[12] ;
			for (int i = 1; i < items.length-1; i++) 
				num_Dimension[i-1] = Double.parseDouble(items[i]);
			dataPoint.setNum_Dimension(num_Dimension);
			dataPoint.setDataPointID(dataPoints.size());
			dataPoints.add(dataPoint);
		}
		System.out.println("数据集读取完，总共生成 "+dataPoints.size()+ " 个对象");
		return dataPoints;
	}
	
	/**
	 * 将计算出的全部的欧氏距离放在内存中，便于后续迭代时重用，同时找出其K-近邻距离
	 * 自己与自己之间不用计算距离
	 * 使用Guava库中的Cache工具
	 * @return 全部点之间的欧氏距离 
	 */
	public static Cache<Set<DataPoint>, Double> getDistanceCache(List<DataPoint> dataPoints,int k) {
//		System.out.println("开始计算所有点之间的欧氏距离......");
		Cache<Set<DataPoint>, Double> distanceCache = CacheBuilder.newBuilder().maximumSize(Integer.MAX_VALUE).build();
		for (int i = 0; i < dataPoints.size(); i++) {
			DataPoint dataPoint1 = dataPoints.get(i);
			//用于存放该点与其他点之间距离的有序集合（降序）,用于寻找k近邻距离
			TreeSet<Double> sortedDistance = new TreeSet<Double>(new Comparator<Double>() {
				public int compare(Double o1, Double o2) {
					// TODO Auto-generated method stub
					double diff = o1 - o2;
					if (diff < 0 ) 
						return 1;
					if (diff >0) 
						return -1;
					return 0;
				}
			});
			//按照距离对近邻点进行升序排列，用于寻找K近邻点
			List<KNNnode> sortedDataPoint = new ArrayList<KNNnode>();
			for (int j = 0; j < dataPoints.size(); j++) {
				DataPoint dataPoint2 = dataPoints.get(j);
				if (i != j) {
					Set<DataPoint> dataPointSet = new HashSet<DataPoint>();
					dataPointSet.add(dataPoint1);
					dataPointSet.add(dataPoint2);
					double distance = 0.0;
					if (distanceCache.getIfPresent(dataPointSet) == null) {
//						distance = Until.getDistance(dataPoint1,dataPoint2);
						distance = Until.getNumberDistance(dataPoint1,dataPoint2);
						distanceCache.put(dataPointSet, distance);
					} else
						distance = distanceCache.getIfPresent(dataPointSet);
					if (!sortedDistance.contains(distance)) 
						sortedDistance.add(distance);
					if (sortedDistance.size() > k) {
						Iterator<Double> iterator= sortedDistance.iterator();
						iterator.next();
						iterator.remove();
					}
					sortedDataPoint.add(new KNNnode(dataPoint2, distance));
				}
			}
			double k_distance = sortedDistance.iterator().next();
			Collections.sort(sortedDataPoint,new Comparator<KNNnode>() {

				//升序
				public int compare(KNNnode o1, KNNnode o2) {
					// TODO Auto-generated method stub
					double diff = o1.getDistance() - o2.getDistance();
					if (diff < 0 ) 
						return -1;
					if (diff >0) 
						return 1;
					return 0;
				}
			} );
			dataPoint1.setK_distance(k_distance);
			List<DataPoint> K_NNList = new ArrayList<DataPoint>();
			Iterator<KNNnode> iterator = sortedDataPoint.iterator();
			while (iterator.hasNext()) {
				KNNnode nKnNnode = iterator.next();
				if (nKnNnode.getDistance()<=k_distance) {
					K_NNList.add(nKnNnode.getDataPoint());
				} else {
					break;
				}
			}
			dataPoint1.setkNN(K_NNList);
//			System.out.println(dataPoint1.getDataPointID()+" "+(k_distance-dataPoint1.getK_distance())+" "+sortedDataPoint.size()+" "+ dataPoint1.getkNN().size());
		}
//		System.out.println("共包含 "+distanceCache.size()+" 个欧氏距离");
		return distanceCache;
	}
	
	/***
	 * 计算两点之间的欧式距离
	 * 其中，对于非数值型变量，相同则距离为1，否则为0
	 * @param dataPoint
	 * @return distance
	 */
	public static double getDistance(DataPoint dataPoint1,DataPoint dataPoint2) {
		double distance = 0.0;
		double[] numDimension1 = dataPoint1.getNum_Dimension();
		String[] strDimension1 = dataPoint1.getStr_Dimension();
		double[] numDimension2 = dataPoint2.getNum_Dimension();
		String[] strDimension2 = dataPoint2.getStr_Dimension();
		if (numDimension1.length>0 && numDimension2.length>0) 
			for (int i = 0; i < numDimension1.length; i++) 
				distance += Math.pow(numDimension1[i]-numDimension2[i], 2);
		if (strDimension1.length>0 && strDimension2.length>0) {
			for (int i = 0; i < strDimension1.length; i++) {
				if (strDimension1[i].equals(strDimension2[i])) 
					distance += 0;
				else 
					distance += 1;
			}
		}
		return Math.sqrt(distance);
	}
	
	/***
	 * 计算两点之间的欧式距离
	 * @param dataPoint
	 * @return distance
	 */
	public static double getNumberDistance(DataPoint dataPoint1,DataPoint dataPoint2) {
		double distance = 0.0;
		double[] numDimension1 = dataPoint1.getNum_Dimension();
		String[] strDimension1 = dataPoint1.getStr_Dimension();
		double[] numDimension2 = dataPoint2.getNum_Dimension();
		String[] strDimension2 = dataPoint2.getStr_Dimension();
		if (numDimension1.length>0 && numDimension2.length>0) 
			for (int i = 0; i < numDimension1.length; i++) 
				distance += Math.pow(numDimension1[i]-numDimension2[i], 2);
		return Math.sqrt(distance);
	}
	
	
	/**
	 * 查找当前点的K_NN邻域
	 * @param dataPoints:数据点集合
	 * @param K：查找的近邻K值
	 * @param distanceCache：存放所有点之间的欧氏距离缓存
	 * @return
	 */
	public static List<DataPoint> getK_NNList(DataPoint currentDataPoint,List<DataPoint> dataPoints,Cache<Set<DataPoint>, Double> distanceCache) {
		double k_distance = currentDataPoint.getK_distance();
		List<KNNnode> K_NNList = new ArrayList<KNNnode>();
		for (int i = 0; i < dataPoints.size(); i++) {
			DataPoint dataPoint = dataPoints.get(i);
			if (currentDataPoint.getDataPointID() != dataPoint.getDataPointID()) {
				Set<DataPoint> points = new HashSet<DataPoint>();
				points.add(currentDataPoint);
				points.add(dataPoint);
				if (distanceCache.getIfPresent(points) <= k_distance) 
					K_NNList.add(new KNNnode(dataPoint,distanceCache.getIfPresent(points)));
			}
		}
		System.out.println(currentDataPoint.getDataPointID()+" "+new DecimalFormat("#.##").format(k_distance)+" "+K_NNList.size());
		List<DataPoint> list = new ArrayList<DataPoint>();
		Collections.sort(K_NNList,new Comparator<KNNnode>() {

			public int compare(KNNnode o1, KNNnode o2) {
				// TODO Auto-generated method stub
				double diff = o1.getDistance() - o2.getDistance();
				if (diff < 0 ) 
					return -1;
				if (diff >0) 
					return 1;
			return 0;
			}
		});
		for (KNNnode knNnode : K_NNList) {
//			System.out.print(knNnode.getDataPoint().getDataPointID()+" "+new DecimalFormat("#.##").format(knNnode.getDistance())+" ");
			list.add(knNnode.getDataPoint());
		}
//		System.out.println("\n");
		return list;
	}
	
	/**
	 * 查找当前点的反K_NN邻域
	 * 如果反K近邻邻域为空，则标记为Noise
	 */
	public static List<DataPoint> getRK_NNList(DataPoint currentDataPoint,List<DataPoint> dataPoints) {
		List<DataPoint> RK_NNList = new ArrayList<DataPoint>();
		for (int i = 0; i < dataPoints.size(); i++) {
			DataPoint dataPoint = dataPoints.get(i);
			if (dataPoint.getkNN().contains(currentDataPoint)) {
				RK_NNList.add(dataPoint);
			}
		}
		if (RK_NNList.size() == 0 ) 
			currentDataPoint.setClusterID(0);
		return RK_NNList;
	}
	
	/**
	 * 如果ISKNN为空，则直接返回ISKNN
	 * 如果ISKNN不为空，将KNN中的值逐个去RKNN里面匹配并将匹配到的加入到ISKNN
	 * 这样可保证ISKNN中的点是按照由近及远的顺序排列
	 * @return 样本点K-近邻和反K-近邻交集
	 */
	public static List<DataPoint>  getISKNN(DataPoint dataPoint) {
		List<DataPoint> iSKNN = new ArrayList<DataPoint>();
//		System.out.print(dataPoint.getDataPointID());
		if (dataPoint.getRKNN().size() > 0) {
			List<DataPoint> knn = dataPoint.getkNN();
			List<DataPoint> rknn = dataPoint.getRKNN();
			for ( int i = 0 ; i < rknn.size(); i++) {
				DataPoint dataPoint2 = rknn.get(i);
				if (knn.contains(dataPoint2)) {
//					System.out.print(" "+dataPoint2.getDataPointID());
					iSKNN.add(dataPoint2);
				}
			}
		}
//		System.out.print(" ["+iSKNN.size()+"]\n");
		return iSKNN;
	}
	
	
	public static void main(String[] args) throws IOException {
		/**
		 * java中的数据类型，可分为两类：
		 *1.基本数据类型，也称原始数据类型。byte,short,char,int,long,float,double,boolean
		 *他们之间的比较，应用双等号（==）,比较的是他们的值。 
		 *2.复合数据类型(类)
		 *当他们用（==）进行比较的时候，比较的是他们在内存中的存放地址，所以，除非是同一个new出来的对象，否则比较后结果为false。
		 *JAVA当中所有的类都是继承于Object这个基类的，在Object中的基类中定义了一个equals的方法，这个方法的初始行为是比较对象的内存地 址，
		 *但在一些类库当中这个方法被覆盖掉了，如String,Integer,Date在这些类当中equals有其自身的实现，而不再是比较类在堆内存中的存放地址了。   
		 *对于复合数据类型之间进行equals比较，在没有覆写equals方法的情况下，他们之间的比较还是基于他们在内存中的存放位置的地址值的，
		 *因为Object的equals方法也是用双等号（==）进行比较的，所以比较后的结果跟双等号（==）的结果相同。
		 */
		
		DataPoint dataPoint1 = new DataPoint();
		dataPoint1.setClusterID(5);
		
		DataPoint dataPoint2 = new DataPoint();
		dataPoint2.setClusterID(15);
		System.out.println(dataPoint1 == dataPoint2);
		System.out.println(dataPoint1.equals(dataPoint2));
		List<DataPoint> list = new ArrayList<DataPoint>();
		list.add(dataPoint1);

		for (DataPoint dataPoint3 : list) {
			for (DataPoint dataPoint4 : list) {
				System.out.println(dataPoint3 == dataPoint4);
				System.out.println(dataPoint3.equals(dataPoint4));
			}
		}
		

		String s1 = new String("wujun");
		String S2 = "wujun";
		System.out.println(s1.equals(S2));
		System.out.println(s1 == S2);
		
//		
		TreeSet<Double> sortDistance = new TreeSet<Double>(new Comparator<Double>() {
			// 按照降序排序
			public int compare(Double o1, Double o2) {
				// TODO Auto-generated method stub
				double diff = o1 - o2;
				if (diff > 0) {
					return -1;
				}
				if (diff < 0 ) {
					return 1;
				}
				return 0;
			}
		});
		sortDistance.add(1.0);
		sortDistance.add(3.0);
		sortDistance.add(2.0);
		sortDistance.add(1.0);
//		for (Double double1 : sortDistance) {
//			System.out.println(double1);
//		}
		System.out.println(sortDistance.toString());
		Iterator<Double> iterator = sortDistance.iterator();
		iterator.next();
		iterator.remove();
		System.out.println(sortDistance.toString());
		System.out.println(sortDistance.iterator().next());
//		String path = "testdata.txt";
//		Until.getDataPoints(path);
		
	}
}
