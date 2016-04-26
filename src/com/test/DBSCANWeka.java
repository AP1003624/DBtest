/**
 * @author 吴俊
 * @创建日期 2016年4月25日
 * @版本 V 8.6
 */
package com.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class DBSCANWeka {
	
	private double Eps;
	private int MinPts;
	private List<DataPoint> dataPoints;
	
	
	
	public void buildClusterer() throws Exception {
        int clusterID = 1;
        Iterator iterator = dataPoints.iterator();
        while (iterator.hasNext()) {
            DataPoint dataObject = (DataPoint) iterator.next();
            if (dataObject.getClusterID() == -1) {
                if (expandCluster(dataObject,clusterID)) {
                    clusterID++;
                }
            }
        }
    }

    /**
     * Assigns this dataObject to a cluster or remains it as NOISE
     * @param dataObject The DataObject that needs to be assigned
     * @return true, if the DataObject could be assigned, else false
     */
    private boolean expandCluster(DataPoint dataObject,int clusterID) {
        List seedList = epsilonRangeQuery(getEps(), dataObject);
        /** dataObject is NO coreObject */
        if (seedList.size() < getMinPts()) {
            dataObject.setClusterID(0);
            return false;
        }

        /** dataObject is coreObject */
        for (int i = 0; i < seedList.size(); i++) {
            DataPoint seedListDataObject = (DataPoint) seedList.get(i);
            /** label this seedListDataObject with the current clusterID, because it is in epsilon-range */
            seedListDataObject.setClusterID(clusterID);
            if (seedListDataObject.equals(dataObject)) {
                seedList.remove(i);
                i--;
            }
        }

        /** Iterate the seedList of the startDataObject */
        for (int j = 0; j < seedList.size(); j++) {
            DataPoint seedListDataObject = (DataPoint) seedList.get(j);
            List seedListDataObject_Neighbourhood = epsilonRangeQuery(getEps(), seedListDataObject);

            /** seedListDataObject is coreObject */
            if (seedListDataObject_Neighbourhood.size() >= getMinPts()) {
                for (int i = 0; i < seedListDataObject_Neighbourhood.size(); i++) {
                	DataPoint p = (DataPoint) seedListDataObject_Neighbourhood.get(i);
                    if (p.getClusterID() == -1 || p.getClusterID() == 0) {
                        if (p.getClusterID() == -1) {
                            seedList.add(p);
                        }
                        p.setClusterID(clusterID);
                    }
                }
            }
            seedList.remove(j);
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
    public List epsilonRangeQuery(double epsilon, DataPoint queryDataObject) {
        ArrayList epsilonRange_List = new ArrayList();
        Iterator iterator = dataPoints.iterator();
        while (iterator.hasNext()) {
        	DataPoint dataObject = (DataPoint) iterator.next();
            double distance = Until.getNumberDistance(queryDataObject,dataObject);
            if (distance < epsilon) {
                epsilonRange_List.add(dataObject);
            }
        }

        return epsilonRange_List;
    }


	public DBSCANWeka() {
		this.Eps = 0.9;
		this.MinPts = 6;
		
	}
	
	public DBSCANWeka(double Eps,int MinPts,List<DataPoint> dataPoints) {
		this.Eps = Eps;
		this.MinPts = MinPts;
		this.dataPoints = dataPoints;
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

	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}

	/**
	 * 
	 */


}
