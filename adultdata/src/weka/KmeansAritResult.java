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
	 * ���ĳ���۴��е�KLֵ,��ͳ�Ƴ���p����q
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
	 * ���ƽ��ֵ�����ҵõ���ƽ��ֵ����ľ۴�
	 * @param KM
	 */
	public static int getOneCenter(SimpleKMeans KM ){
		
		 //���һ�е�ֵ�ĺͣ�������ƽ��
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
		  
		  //���ƽ��ֵ
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
		  //�����ӽ���ֵ
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
			 
		  //�����������������
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
	 * ���KM���������������ֵ�����ֵ
	 * @param KM
	 * @param index
	 */
	public static void getNearCenter(SimpleKMeans KM ,int index){
		
		 Map recmap = new LinkedHashMap();
		 if(KM!=null){ 
			 for(int i = 0; i < KM.getNumClusters(); i++){  
					 for(int j =0 ;j < KM.getClusterCentroids().instance(i).numValues();j++){
						 //������Լ�����Ͳ�Ҫ�Ƚϣ��ҵ����Լ����������ֵ�Ϳ���
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
						 //������Լ�����Ͳ�Ҫ�Ƚϣ��ҵ����Լ����������ֵ�Ϳ���
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
