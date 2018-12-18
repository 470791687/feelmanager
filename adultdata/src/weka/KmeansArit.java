package weka;

import java.io.File; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator; 
import java.util.List;
import java.util.Map; 
import bean.staticBean; 
import weka.classifiers.functions.pace.DiscreteFunction;
import weka.clusterers.SimpleKMeans; 
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class KmeansArit {
	

	public static void main(String[] args) {

		Instances ins = null; 
		SimpleKMeans KM = null;
		DiscreteFunction disFun = null;  
		
		int fentongid=7; //分桶标示
		int clusternum=13; //聚簇数量
		double kL=3.0; //KL标记值 
		String filename="data"+fentongid+".txt";
		String dirmd="d:/个人文件/qinting/datafile/"+filename;
        int fentongnum=9;
		try { 
			//读取需要聚类的数据
			File file = new File(dirmd);
			ArffLoader loader = new ArffLoader();
			loader.setFile(file);
			ins = loader.getDataSet();

			KM = new SimpleKMeans();
			KM.setNumClusters(clusternum);

			//聚类数据
			KM.buildClusterer(ins);
			// System.out.println(KM.getSeed());
			// System.out.println(KM.getOptions());
			int a[] = new int[ins.numInstances()];
			System.out.println("ins.numInstances()==>"+ins.numInstances());
			 
			for (int i = 0; i < ins.numInstances(); i++) {
				a[i] = KM.clusterInstance(ins.instance(i));
				//System.out.println("ins.instance(i)==>"+i+"  content:"+ins.instance(i)+" value:"+a[i]);
                //System.out.println("a[i]"+"_"+i+"_"+a[i]);
			}
			 
	 	   System.out.println(KM.toString());
		   System.out.println("KM.getNumClusters()===>"+KM.getNumClusters()+"   KM.getClusterCentroids().numInstances()=>"+  KM.getClusterCentroids().instance(4).numValues());	 
		   System.out.println("KM.getOptions()[0]===>"+KM.getClusterCentroids().instance(0).value(0)+"  KM.getClusterCentroids().instance(0)==>"+KM.getClusterCentroids().instance(0).numValues());	
		   System.out.println("ins.numInstances()==>"+ins.numInstances());
		   System.out.println("a[i]"+a[0]+" ins.instance(i)"+ins.instance(0));  
		    
		   
		   //更新对应的聚簇 ，查询出对应的唯一值ID，用来更新对应的排序顺序,知道每条数据属于分类的哪个聚簇 
		 
		    Map map = new HashMap();
			map.put("fentongid", fentongid);
			//判断聚簇是否重新划分，如果重新划分需要更改cluster值
			if(queryInfo.getClusterNum(map)!=-1 && queryInfo.getClusterNum(map)!=clusternum){
				System.out.println("有更改的聚簇");
				if(dataChangeInfo.updateClusterByNull()){
					System.out.println("更新聚簇成功");
					List listdata=queryInfo.getfileDataId(map); 
					dataChangeInfo.updateClusterData(listdata, a,ins); 
				}
				
			} 
		   
		   
		   //得到中心索引，并且算出对应的P与Q值
		   int tagindex = KmeansAritResult.getOneCenter(KM);   //得到分桶聚簇中心
		   Map<String, Object> mapcluser = new HashMap<String, Object>();
		   mapcluser.put("fentongid", fentongid);
		   mapcluser.put("cluster1", tagindex);   
		   double mathlog =KmeansAritResult.getPqNearNumber(mapcluser);  //算出聚簇中心的KL
		   
		   Map insertmap=new HashMap();  //用来保存分桶聚簇最终kl值，存入数据库
		   insertmap.put("fengtongid", fentongid); //分桶ID
 		   insertmap.put("clusternum", clusternum);  //分桶当中分了多少个聚簇
 		   insertmap.put("clustertag", Integer.toString((int)tagindex)); //具体某个聚簇
 		   insertmap.put("tagetvalue", mathlog); //分桶当中某个聚簇算出中心值的kl
 		   insertmap.put("kL", kL); //预设的KL值
 		  insertmap.put("fentongnum", fentongnum);// 这批数据分桶的数量
 		  
 		 
		   Map mapvalue= new HashMap(); //存放某个分桶当中所有聚簇算出的KL值
		   //当聚簇中心算出的Kl比预设的KL大时，说明不符合要求，将不满足要求的聚簇组合重新算KL
		   if(mathlog>kL){
			   // KmeansArit.getNearCenter(KM, tagindex);
			   mapcluser.clear();
			   mapcluser.put("fentongid", fentongid);
			   //查出某个分桶下所有聚簇的P与Q值，并且算出这个分桶下所有聚簇的kL值
			   List<staticBean> listbean = queryInfo.getAllStaticResult(mapcluser);  
			   
			   for(int i =0; i<listbean.size(); i++){
				   staticBean sbean = new staticBean();
				   /* System.out.println(listbean.get(i).getOccupation()+": "+listbean.get(i).getCluster1()+":  p==>"+listbean.get(i).getCountnump()+"  q==>"+listbean.get(i).getCountnumq()); 
				   System.out.println("math.log==>" +Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));*/
				   int cluster1=listbean.get(i).getCluster1();
				   Double dbcluseter=  (Double) mapvalue.get(cluster1);
				   if(dbcluseter==null){
					   mapvalue.put(cluster1, Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));
					   /* System.out.println("P值==>"+listbean.get(i).getCountnump()+"q值==>"+listbean.get(i).getCountnumq());
					   System.out.println("math=>>"+Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));*/
				   }else{
					   mapvalue.put(cluster1, dbcluseter+Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));
				   }  
			   }  
			   
			   //将算出来的KL值再次比较一次，符合要求的保存，不符合要求的组合重新算KL值
			   Iterator  it = mapvalue.entrySet().iterator();
			   List listkey = new ArrayList();
			   boolean flagkl=true; //用来判断这个分组中的聚簇是不是全部不满足
			   while(it.hasNext()){ 
				   Map.Entry entry = (Map.Entry) it.next();   
				   double entryvalue=(double) entry.getValue();
				   if(entryvalue>kL){
					   System.out.println("entry.getKey()==>"+entry.getKey()+" entry.getValue()=>"+entry.getValue());   
					   listkey.add(entry.getKey());  
					   
				   }else{
					   flagkl=false;
					   insertmap.put("clustertag", Integer.toString((int) entry.getKey()));
					   insertmap.put("tagetvalue", entry.getValue());
					   if(queryInfo.queryTagResult(insertmap)){
						   dataChangeInfo.insertTaginfo(insertmap);
					   } 
				   }  
			   }  
			  
		       //判断是否有复合要求的数据，没有需要重新分聚簇 
			   if(flagkl){
				   System.out.println("分桶为"+fentongid+"中，聚簇为"+clusternum+",KL预设值为"+kL+"没有找到任何匹配的聚簇!需要重新划分聚簇!");
				   System.exit(0);
			   } 
			   
			   //将不满足组合重新算KL值
			   Map mapgroup = new HashMap();
			   double groupmathlog = 0;
			   if(listkey!=null && !listkey.isEmpty()){
				   List<staticBean> listkeybean = queryInfo.getAllStaticResultByList(mapcluser, listkey);  
				   for(int i =0; i<listkeybean.size(); i++){ 
					   System.out.println("groupmathlog  value:==>"+listkeybean.get(i).getOccupation()+" p==>"+listkeybean.get(i).getCountnump()+"  q==>"+listkeybean.get(i).getCountnumq()); 
					   System.out.println("groupmathlog.log==>" +Math.log(listkeybean.get(i).getCountnump()/listkeybean.get(i).getCountnumq()));  
					   groupmathlog=groupmathlog+Math.log(listkeybean.get(i).getCountnump()/listkeybean.get(i).getCountnumq());
				   } 
			   }
			   
			   System.out.println("groupmathlog end:==>"+groupmathlog);
	
			   
			   //判断不符合要求的聚簇重新组合算出来的kl值是否满足要求，满足就保存，不满足重新组合
			   if(groupmathlog>kL){  
				   List<staticBean> nearlist = new ArrayList<staticBean>();
				   for(int gv=0; gv < listkey.size(); gv++){
					   System.out.println("listkey.get(gv)==>"+listkey.get(gv));
					   staticBean nearkybean =new staticBean();
					   //找出离不满足要求最近的满足聚簇
					   int nearkey= KmeansAritResult.getNearCenterByListKey(KM, (int)listkey.get(gv), listkey);
					   nearkybean.setNearindex(nearkey);
					   nearkybean.setIndex((int)listkey.get(gv));
					   
					   System.out.println("nearkey==>"+nearkey+"gv==>"+listkey.get(gv));
					   nearlist.add(nearkybean); 
				   }
				   
				   
				   //不满足于满足两两组合算出KL值,判断是否满足要求
				   if(nearlist!=null && !nearlist.isEmpty()){ 
					   for(int nl = 0;nl < nearlist.size(); nl++){
						   List listnearkey=new ArrayList();
						   listnearkey.add(0, nearlist.get(nl).getIndex());
						   listnearkey.add(1, nearlist.get(nl).getNearindex());
						   List<staticBean> listkeynearbeans = queryInfo.getAllStaticResultByList(mapcluser, listnearkey); 
						   double groupnearmathlog = 0;
						   for(int i =0; i<listkeynearbeans.size(); i++){ 
							   System.out.println("groupnearmathlog  value:==>"+listkeynearbeans.get(i).getOccupation()+" p==>"+listkeynearbeans.get(i).getCountnump()+"  q==>"+listkeynearbeans.get(i).getCountnumq()); 
							   System.out.println("groupnearmathlog.log==>" +Math.log(listkeynearbeans.get(i).getCountnump()/listkeynearbeans.get(i).getCountnumq()));  
							   groupnearmathlog=groupnearmathlog+Math.log(listkeynearbeans.get(i).getCountnump()/listkeynearbeans.get(i).getCountnumq());
						   } 
						   System.out.println("groupnearmathlog end:==>"+groupnearmathlog);
						   
						   //不满足要求与满足要求的合并算出的KL,可以就保存，不满足就跳出
						   if(groupnearmathlog>kL){
							   
						   }else{
							   String groupkeyvalue="" ;
							   if(listnearkey!=null && !listnearkey.isEmpty()){   
								    for(int key=0; key<listnearkey.size(); key++){  
								    	groupkeyvalue =groupkeyvalue+listnearkey.get(key)+","; 
								    }
								    groupkeyvalue=groupkeyvalue.substring(0, groupkeyvalue.lastIndexOf(","));  
							   } 
							   insertmap.put("clustertag", groupkeyvalue);
							   insertmap.put("tagetvalue",groupnearmathlog);
							   if(queryInfo.queryTagResult(insertmap)){
								   dataChangeInfo.insertTaginfo(insertmap);
							   }  
						   }  
					   }  
				   }  
				   
			   }else{  
				   //不满足的全部组合算出最终的KL值，满足就保存
				   String keyvalue="" ;
				   if(listkey!=null && !listkey.isEmpty()){   
					    for(int key=0; key<listkey.size(); key++){  
					    	keyvalue =keyvalue+listkey.get(key)+","; 
					    }
					    keyvalue=keyvalue.substring(0, keyvalue.lastIndexOf(","));  
				   } 
				   insertmap.put("clustertag", keyvalue);
				   insertmap.put("tagetvalue",groupmathlog);
				   if(queryInfo.queryTagResult(insertmap)){
					   dataChangeInfo.insertTaginfo(insertmap);
				   } 
			   } 
			    
		   }else{ //当聚簇中心算出的Kl小于预设的KL，说米满足要求，保存满足要求的信息
			    //保留某个分桶当中中心对象的KL值,因为这个值小于设定的KL，判断如果数据库不存在就存储
			   if(queryInfo.queryTagResult(insertmap)){
				   dataChangeInfo.insertTaginfo(insertmap);
			   }  
		   } 
		   
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	
	/**
	 * 计算最终的KL，找到符合要求的聚簇
	 * @param fentid
	 * @param clusnum
	 * @param kLnum
	 * @param fileDataUrl
	 * @param fengtongnum
	 * @return
	 */
	public static boolean  calculateKL(int fentid,int clusnum,double kLnum,String fileDataUrl,int fengtongnum){
		Instances ins = null; 
		SimpleKMeans KM = null;
		DiscreteFunction disFun = null; 
	   
		
		int fentongid=fentid; //分桶唯一标示,具体的某个分桶
		int clusternum=clusnum; //聚簇数量
		double kL=kLnum; //KL标记值 
		int fentongnum=fengtongnum; //这批数据分桶的数量

		try { 
			//读取需要聚类的数据
			File file = new File(fileDataUrl);
			ArffLoader loader = new ArffLoader();
			loader.setFile(file);
			ins = loader.getDataSet();

			KM = new SimpleKMeans();
			KM.setNumClusters(clusternum);

			//聚类数据
			KM.buildClusterer(ins);
			// System.out.println(KM.getSeed());
			// System.out.println(KM.getOptions());
			int a[] = new int[ins.numInstances()];
			System.out.println("ins.numInstances()==>"+ins.numInstances());
			 
			for (int i = 0; i < ins.numInstances(); i++) {
				a[i] = KM.clusterInstance(ins.instance(i));
				//System.out.println("ins.instance(i)==>"+i+"  content:"+ins.instance(i)+" value:"+a[i]);
                //System.out.println("a[i]"+"_"+i+"_"+a[i]);
			}
			 
	 	   System.out.println(KM.toString());
		   System.out.println("KM.getNumClusters()===>"+KM.getNumClusters()+"   KM.getClusterCentroids().numInstances()=>"+  KM.getClusterCentroids().instance(4).numValues());	 
		   System.out.println("KM.getOptions()[0]===>"+KM.getClusterCentroids().instance(0).value(0)+"  KM.getClusterCentroids().instance(0)==>"+KM.getClusterCentroids().instance(0).numValues());	
		   System.out.println("ins.numInstances()==>"+ins.numInstances());
		   System.out.println("a[i]"+a[0]+" ins.instance(i)"+ins.instance(0));  
		    
		   
		   //更新对应的聚簇 ，查询出对应的唯一值ID，用来更新对应的排序顺序,知道每条数据属于分类的哪个聚簇 
		 
		    Map map = new HashMap();
			map.put("fentongid", fentongid);
			//判断聚簇是否重新划分，如果重新划分需要更改cluster值
			if(queryInfo.getClusterNum(map)!=-1 && queryInfo.getClusterNum(map)!=clusternum){
				System.out.println("有更改的聚簇");
				if(dataChangeInfo.updateClusterByNull()){
					System.out.println("更新聚簇成功");
					List listdata=queryInfo.getfileDataId(map); 
					dataChangeInfo.updateClusterData(listdata, a,ins); 
				}
				
			} 
		   
		   
		   //得到中心索引，并且算出对应的P与Q值
		   int tagindex = KmeansAritResult.getOneCenter(KM);   //得到分桶聚簇中心
		   Map<String, Object> mapcluser = new HashMap<String, Object>();
		   mapcluser.put("fentongid", fentongid);
		   mapcluser.put("cluster1", tagindex);   
		   double mathlog =KmeansAritResult.getPqNearNumber(mapcluser);  //算出聚簇中心的KL
		   
		   Map insertmap=new HashMap();  //用来保存分桶聚簇最终kl值，存入数据库
		   insertmap.put("fengtongid", fentongid); //分桶ID
 		   insertmap.put("clusternum", clusternum);  //分桶当中分了多少个聚簇
 		   insertmap.put("clustertag", Integer.toString((int)tagindex)); //具体某个聚簇
 		   insertmap.put("tagetvalue", mathlog); //分桶当中某个聚簇算出中心值的kl
 		   insertmap.put("kL", kL); //预设的KL值
 		   insertmap.put("fentongnum", fentongnum);// 这批数据分桶的数量
 		   
 		   
		   Map mapvalue= new HashMap(); //存放某个分桶当中所有聚簇算出的KL值
		   //当聚簇中心算出的Kl比预设的KL大时，说明不符合要求，将不满足要求的聚簇组合重新算KL
		   if(mathlog>kL){
			   // KmeansArit.getNearCenter(KM, tagindex);
			   mapcluser.clear();
			   mapcluser.put("fentongid", fentongid);
			   //查出某个分桶下所有聚簇的P与Q值，并且算出这个分桶下所有聚簇的kL值
			   List<staticBean> listbean = queryInfo.getAllStaticResult(mapcluser);  
			   
			   for(int i =0; i<listbean.size(); i++){
				   staticBean sbean = new staticBean();
				   /* System.out.println(listbean.get(i).getOccupation()+": "+listbean.get(i).getCluster1()+":  p==>"+listbean.get(i).getCountnump()+"  q==>"+listbean.get(i).getCountnumq()); 
				   System.out.println("math.log==>" +Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));*/
				   int cluster1=listbean.get(i).getCluster1();
				   Double dbcluseter=  (Double) mapvalue.get(cluster1);
				   if(dbcluseter==null){
					   mapvalue.put(cluster1, Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));
					   /* System.out.println("P值==>"+listbean.get(i).getCountnump()+"q值==>"+listbean.get(i).getCountnumq());
					   System.out.println("math=>>"+Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));*/
				   }else{
					   mapvalue.put(cluster1, dbcluseter+Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));
				   }  
			   }  
			   
			   //将算出来的KL值再次比较一次，符合要求的保存，不符合要求的组合重新算KL值
			   Iterator  it = mapvalue.entrySet().iterator();
			   List listkey = new ArrayList();
			   boolean flagkl=true; //用来判断这个分组中的聚簇是不是全部不满足
			   while(it.hasNext()){ 
				   Map.Entry entry = (Map.Entry) it.next();   
				   double entryvalue=(double) entry.getValue();
				   if(entryvalue>kL){
					   System.out.println("entry.getKey()==>"+entry.getKey()+" entry.getValue()=>"+entry.getValue());   
					   listkey.add(entry.getKey());  
					   
				   }else{
					   flagkl=false;
					   insertmap.put("clustertag", Integer.toString((int) entry.getKey()));
					   insertmap.put("tagetvalue", entry.getValue());
					   if(queryInfo.queryTagResult(insertmap)){
						   dataChangeInfo.insertTaginfo(insertmap);
					   } 
				   }  
			   }  
			   
			   if(flagkl){
				   System.out.println("分桶为"+fentongid+"中，聚簇为"+clusternum+",KL预设值为"+kL+"没有找到任何匹配的聚簇!需要重新划分聚簇!");
				   System.exit(0);
			   } 
			   
			   //将不满足组合重新算KL值
			   Map mapgroup = new HashMap();
			   double groupmathlog = 0;//不满足条件组合之后算出的Kl值
			   if(listkey!=null && !listkey.isEmpty()){
				   List<staticBean> listkeybean = queryInfo.getAllStaticResultByList(mapcluser, listkey);  
				   for(int i =0; i<listkeybean.size(); i++){ 
					   System.out.println("groupmathlog  value:==>"+listkeybean.get(i).getOccupation()+" p==>"+listkeybean.get(i).getCountnump()+"  q==>"+listkeybean.get(i).getCountnumq()); 
					   System.out.println("groupmathlog.log==>" +Math.log(listkeybean.get(i).getCountnump()/listkeybean.get(i).getCountnumq()));  
					   groupmathlog=groupmathlog+Math.log(listkeybean.get(i).getCountnump()/listkeybean.get(i).getCountnumq());
				   } 
			   }
			   
			   System.out.println("groupmathlog end:==>"+groupmathlog);
	
			   
			   //判断不符合要求的聚簇重新组合算出来的kl值是否满足要求，满足就保存，不满足重新组合
			   if(groupmathlog>kL){  
				   List<staticBean> nearlist = new ArrayList<staticBean>();
				   for(int gv=0; gv < listkey.size(); gv++){
					   System.out.println("listkey.get(gv)==>"+listkey.get(gv));
					   staticBean nearkybean =new staticBean();
					   //找出离不满足要求最近的满足聚簇
					   int nearkey= KmeansAritResult.getNearCenterByListKey(KM, (int)listkey.get(gv), listkey);
					   nearkybean.setNearindex(nearkey);
					   nearkybean.setIndex((int)listkey.get(gv));
					   
					   System.out.println("nearkey==>"+nearkey+"gv==>"+listkey.get(gv));
					   nearlist.add(nearkybean); 
				   }
				   
				   
				   //不满足于满足两两组合算出KL值,判断是否满足要求
				   if(nearlist!=null && !nearlist.isEmpty()){ 
					   for(int nl = 0;nl < nearlist.size(); nl++){
						   List listnearkey=new ArrayList();
						   listnearkey.add(0, nearlist.get(nl).getIndex());
						   listnearkey.add(1, nearlist.get(nl).getNearindex());
						   List<staticBean> listkeynearbeans = queryInfo.getAllStaticResultByList(mapcluser, listnearkey); 
						   double groupnearmathlog = 0;
						   for(int i =0; i<listkeynearbeans.size(); i++){ 
							   System.out.println("groupnearmathlog  value:==>"+listkeynearbeans.get(i).getOccupation()+" p==>"+listkeynearbeans.get(i).getCountnump()+"  q==>"+listkeynearbeans.get(i).getCountnumq()); 
							   System.out.println("groupnearmathlog.log==>" +Math.log(listkeynearbeans.get(i).getCountnump()/listkeynearbeans.get(i).getCountnumq()));  
							   groupnearmathlog=groupnearmathlog+Math.log(listkeynearbeans.get(i).getCountnump()/listkeynearbeans.get(i).getCountnumq());
						   } 
						   System.out.println("groupnearmathlog end:==>"+groupnearmathlog);
						   
						   //不满足要求与满足要求的合并算出的KL,可以就保存，不满足就跳出
						   if(groupnearmathlog>kL){
							   
						   }else{
							   String groupkeyvalue="" ;
							   if(listnearkey!=null && !listnearkey.isEmpty()){   
								    for(int key=0; key<listnearkey.size(); key++){  
								    	groupkeyvalue =groupkeyvalue+listnearkey.get(key)+","; 
								    }
								    groupkeyvalue=groupkeyvalue.substring(0, groupkeyvalue.lastIndexOf(","));  
							   } 
							   insertmap.put("clustertag", groupkeyvalue);
							   insertmap.put("tagetvalue",groupnearmathlog);
							   try{  
								   if(queryInfo.queryTagResult(insertmap)){
									   try{
										   dataChangeInfo.insertTaginfo(insertmap);
										   System.out.println("分桶标示为:"+fentongid+",聚簇数量为:"+clusternum+",标准KL值为:"+kL+",有符合要求的数据，具体请看表'adult_tag'中的数据！");
									   }catch(Exception e){
										   System.out.println("出现异常，请检查代码");
										   e.printStackTrace();
									   } 
								   } else{
									   System.out.println("分桶标示为:"+fentongid+",聚簇数量为:"+clusternum+",标准KL值为:"+kL+",已有符合要求的数据保存在'adult_tag'表中！");
								   }
							   }catch(Exception e){
								   System.out.println("计算失败，出现异常，请检查代码");
								   e.printStackTrace();
							   }
						   }  
					   }  
				   }  
				   
			   }else{  
				   //不满足的全部组合算出最终的KL值，满足就保存
				   String keyvalue="" ;
				   if(listkey!=null && !listkey.isEmpty()){   
					    for(int key=0; key<listkey.size(); key++){  
					    	keyvalue =keyvalue+listkey.get(key)+","; 
					    }
					    keyvalue=keyvalue.substring(0, keyvalue.lastIndexOf(","));  
				   } 
				   insertmap.put("clustertag", keyvalue);
				   insertmap.put("tagetvalue",groupmathlog);
				   
				   try{  
					   if(queryInfo.queryTagResult(insertmap)){
						   try{
							   dataChangeInfo.insertTaginfo(insertmap);
							   System.out.println("分桶标示为:"+fentongid+",聚簇数量为:"+clusternum+",标准KL值为:"+kL+",有符合要求的数据，具体请看表'adult_tag'中的数据！");
						   }catch(Exception e){
							   System.out.println("出现异常，请检查代码");
							   e.printStackTrace();
						   } 
					   } else{
						   System.out.println("分桶标示为:"+fentongid+",聚簇数量为:"+clusternum+",标准KL值为:"+kL+",已有符合要求的数据保存在'adult_tag'表中！");
					   }
				   }catch(Exception e){
					   System.out.println("计算失败，出现异常，请检查代码");
					   e.printStackTrace();
				   }
				   
			   } 
			    
		   }else{ //当聚簇中心算出的Kl小于预设的KL，说米满足要求，保存满足要求的信息
			    //保留某个分桶当中中心对象的KL值,因为这个值小于设定的KL，判断如果数据库不存在就存储
			   try{  
				   if(queryInfo.queryTagResult(insertmap)){
					   try{
						   dataChangeInfo.insertTaginfo(insertmap);
						   System.out.println("分桶标示为:"+fentongid+",聚簇数量为:"+clusternum+",标准KL值为:"+kL+",有符合要求的数据，具体请看表'adult_tag'中的数据！");
					   }catch(Exception e){
						   System.out.println("出现异常，请检查代码");
						   e.printStackTrace();
					   } 
				   } else{
					   System.out.println("分桶标示为:"+fentongid+",聚簇数量为:"+clusternum+",标准KL值为:"+kL+",已有符合要求的数据保存在'adult_tag'表中！");
				   }
			   }catch(Exception e){
				   System.out.println("计算失败，出现异常，请检查代码");
				   e.printStackTrace();
			   }
		   } 
		   return true;
		   
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} 

}
