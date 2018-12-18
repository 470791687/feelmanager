package weka;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map; 
import bean.staticBean;
import weka.clusterers.SimpleKMeans;

public class KmeansAritResult { 
	
	/**
	 * 算出某个聚簇中的KL值,用统计出的p除以q
	 * @param mapcluser
	 * @return
	 */
	public static double getPqNearNumber(Map<String, Object> mapcluser){ 
		   List<staticBean> listbean = queryInfo.getStaticResult(mapcluser);
		   double mathlog = 0;
		   for(int i =0; i<listbean.size(); i++){ 
			   /*System.out.println(listbean.get(i).getOccupation()+" p==>"+listbean.get(i).getCountnump()+"  q==>"+listbean.get(i).getCountnumq()); 
			   System.out.println("math.log==>" +Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq())); */
			   mathlog=mathlog+Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq());
		   }  
		   System.out.println("endlog============>"+mathlog);
		return mathlog;
	}
	
	
	
	
	/**
	 * 算出平均值，并且得到离平均值最近的聚簇
	 * @param KM
	 */
	public static int getOneCenter(SimpleKMeans KM ){
		
		 //获得一列的值的和，用来算平均
		 Double cellone =0.0;
		 Map<Integer, Double> map = new LinkedHashMap<Integer, Double>();
		 int len = KM.getNumClusters();
		  if(KM!=null){ 
			   for(int i=0;i<KM.getNumClusters();i++){  
				   //System.out.println(":==>"+ KM.getClusterCentroids().instance(i).value(0));
				  // System.out.println("value:==>"+ KM.getClusterCentroids().instance(i).value(1));
				   for(int j=0;j<KM.getClusterCentroids().instance(i).numValues();j++){ 
					   Double d =  map.get(j);
					   if(d==null){
						   map.put(j,  KM.getClusterCentroids().instance(i).value(j));
					   }else{
						   map.put(j, d+KM.getClusterCentroids().instance(i).value(j));
					   }  
					   
				   } 
			   } 
		  }
		  
		  //算出平均值
		  List<Double> listavg = new ArrayList<Double>();
		  if(map.size()>0){
			   Iterator it = map.entrySet().iterator(); 
			    while (it.hasNext()) { 
				     Map.Entry entry = (Map.Entry) it.next();  
				     int key = (int) entry.getKey(); 
				     double value = (double) entry.getValue(); 
				     System.out.println("key=" + key + " value=" + value+ " avg=" + value/len); 
				     listavg.add(value/len);
			    } 
		  }  
		  
		  Map<Integer,Double> recmap= new LinkedHashMap<Integer,Double>();
		  //算出最接近的值
		  for(int i=0;i<KM.getNumClusters();i++){  
			   //System.out.println(":==>"+ KM.getClusterCentroids().instance(i).value(0));
			  // System.out.println("value:==>"+ KM.getClusterCentroids().instance(i).value(1));
			   for(int j=0;j<KM.getClusterCentroids().instance(i).numValues();j++){ 
				   Double rec=recmap.get(i);
				   if(rec==null){
					   recmap.put(i, (KM.getClusterCentroids().instance(i).value(j)-listavg.get(j))*(KM.getClusterCentroids().instance(i).value(j)-listavg.get(j)));
					  }else{
					   recmap.put(i, rec+(KM.getClusterCentroids().instance(i).value(j)-listavg.get(j))*(KM.getClusterCentroids().instance(i).value(j)-listavg.get(j)));
				   }
				   
			   }
			   
		  }
			 
		  //算出哪组数据离的最近
		  Double dvalue=null;
		  int keyvalue = -1;
		  if(recmap.size()>0){
			   Iterator it = recmap.entrySet().iterator(); 
			    while (it.hasNext()) { 
				     Map.Entry entry = (Map.Entry) it.next();  
				     int key = (int) entry.getKey(); 
				     double value = (double) entry.getValue(); 
				     //System.out.println("reckey=" + key + " recvalue=" + value);  
				     if(dvalue==null){
				    	 dvalue=value; 
			    		 keyvalue=key;
				     }else{
				    	 if(dvalue>value){
				    		 dvalue=value;  
				    		 keyvalue=key;
				    	 }
				     }
			    }  
		  } 
		  System.out.println("dvalue==>"+dvalue+"  keyvalue==>"+keyvalue); 
		  return keyvalue;  
	} 
	
	
	/**
	 * 获得KM当中与算出的中心值最近的值
	 * @param KM
	 * @param index
	 */
	public static void getNearCenter(SimpleKMeans KM ,int index){
		
		 Map recmap = new LinkedHashMap();
		 if(KM!=null){ 
			 for(int i = 0; i < KM.getNumClusters(); i++){  
					 for(int j =0 ;j < KM.getClusterCentroids().instance(i).numValues();j++){
						 //如果是自己本身就不要比较，找到跟自己本身最近的值就可以
						 if(i==index)  break; 
						 else{
							 Double rec = (Double) recmap.get(i);
							 if(rec==null){
								 recmap.put(i, (KM.getClusterCentroids().instance(index).value(j)-KM.getClusterCentroids().instance(i).value(j))*(KM.getClusterCentroids().instance(index).value(j)-KM.getClusterCentroids().instance(i).value(j)));
							 }else{
								 recmap.put(i, rec+(KM.getClusterCentroids().instance(index).value(j)-KM.getClusterCentroids().instance(i).value(j))*(KM.getClusterCentroids().instance(index).value(j)-KM.getClusterCentroids().instance(i).value(j)));
							 }
						 }
					 }
			 }
		 }
		 
		 
		 
		 
		  Double dvalue=null;
		  int keyvalue = -1;
		  if(recmap.size()>0){
			   Iterator it = recmap.entrySet().iterator(); 
			    while (it.hasNext()) { 
				     Map.Entry entry = (Map.Entry) it.next();  
				     int key = (int) entry.getKey(); 
				     double value = (double) entry.getValue(); 
				      System.out.println("reckey=" + key + " recvalue=" + value);  
				     if(dvalue==null){
				    	 dvalue=value; 
			    		 keyvalue=key;
				     }else{
				    	 if(dvalue>value){
				    		 dvalue=value;  
				    		 keyvalue=key;
				    	 }
				     }
			    }  
		  } 
		  System.out.println("dvalue==>"+dvalue+"  keyvalue==>"+keyvalue);  
		 
     }
	
	
	/**
	 * 
	 * @param KM
	 * @param index
	 * @param listkey
	 */
	public static int getNearCenterByListKey(SimpleKMeans KM ,int index,List listkey){
		
		 Map recmap = new LinkedHashMap();
		 if(KM!=null){ 
			 for(int i = 0; i < KM.getNumClusters(); i++){  
					 for(int j =0 ;j < KM.getClusterCentroids().instance(i).numValues();j++){
						 //如果是自己本身就不要比较，找到跟自己本身最近的值就可以
						 if(i==index){  
							  break;  
						 }else{
							 boolean nearflag=false;
							 for(int gv=0; gv < listkey.size(); gv++){ 
								   if(i==(int)listkey.get(gv)){
									   nearflag=true;  
								   }
							 }
							 
							 if(nearflag){
								 break;
							 }
							 
							 Double rec = (Double) recmap.get(i);
							 if(rec==null){
								 recmap.put(i, (KM.getClusterCentroids().instance(index).value(j)-KM.getClusterCentroids().instance(i).value(j))*(KM.getClusterCentroids().instance(index).value(j)-KM.getClusterCentroids().instance(i).value(j)));
							 }else{
								 recmap.put(i, rec+(KM.getClusterCentroids().instance(index).value(j)-KM.getClusterCentroids().instance(i).value(j))*(KM.getClusterCentroids().instance(index).value(j)-KM.getClusterCentroids().instance(i).value(j)));
							 }
						 }
					 }
			 }
		 }
		 
		 
		 
		 
		  Double dvalue=null;
		  int keyvalue = -1;
		  
		  if(recmap.size()>0){
			   Iterator it = recmap.entrySet().iterator(); 
			    while (it.hasNext()) { 
				     Map.Entry entry = (Map.Entry) it.next();  
				     int key = (int) entry.getKey(); 
				     double value = (double) entry.getValue(); 
				      System.out.println("reckey=" + key + " recvalue=" + value);  
				     if(dvalue==null){
				    	 dvalue=value; 
			    		 keyvalue=key;
				     }else{
				    	 if(dvalue>value){
				    		 dvalue=value;  
				    		 keyvalue=key;
				    	 }
				     }
			    }  
		  } 
		  System.out.println("dvalue==>"+dvalue+"  keyvalue==>"+keyvalue+" index==>"+index);  
		  
		  return keyvalue;
		 
    }
}
