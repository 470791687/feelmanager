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
		
		int fentongid=7; //��Ͱ��ʾ
		int clusternum=13; //�۴�����
		double kL=3.0; //KL���ֵ 
		String filename="data"+fentongid+".txt";
		String dirmd="d:/�����ļ�/qinting/datafile/"+filename;
        int fentongnum=9;
		try { 
			//��ȡ��Ҫ���������
			File file = new File(dirmd);
			ArffLoader loader = new ArffLoader();
			loader.setFile(file);
			ins = loader.getDataSet();

			KM = new SimpleKMeans();
			KM.setNumClusters(clusternum);

			//��������
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
		    
		   
		   //���¶�Ӧ�ľ۴� ����ѯ����Ӧ��ΨһֵID���������¶�Ӧ������˳��,֪��ÿ���������ڷ�����ĸ��۴� 
		 
		    Map map = new HashMap();
			map.put("fentongid", fentongid);
			//�жϾ۴��Ƿ����»��֣�������»�����Ҫ����clusterֵ
			if(queryInfo.getClusterNum(map)!=-1 && queryInfo.getClusterNum(map)!=clusternum){
				System.out.println("�и��ĵľ۴�");
				if(dataChangeInfo.updateClusterByNull()){
					System.out.println("���¾۴سɹ�");
					List listdata=queryInfo.getfileDataId(map); 
					dataChangeInfo.updateClusterData(listdata, a,ins); 
				}
				
			} 
		   
		   
		   //�õ��������������������Ӧ��P��Qֵ
		   int tagindex = KmeansAritResult.getOneCenter(KM);   //�õ���Ͱ�۴�����
		   Map<String, Object> mapcluser = new HashMap<String, Object>();
		   mapcluser.put("fentongid", fentongid);
		   mapcluser.put("cluster1", tagindex);   
		   double mathlog =KmeansAritResult.getPqNearNumber(mapcluser);  //����۴����ĵ�KL
		   
		   Map insertmap=new HashMap();  //���������Ͱ�۴�����klֵ���������ݿ�
		   insertmap.put("fengtongid", fentongid); //��ͰID
 		   insertmap.put("clusternum", clusternum);  //��Ͱ���з��˶��ٸ��۴�
 		   insertmap.put("clustertag", Integer.toString((int)tagindex)); //����ĳ���۴�
 		   insertmap.put("tagetvalue", mathlog); //��Ͱ����ĳ���۴��������ֵ��kl
 		   insertmap.put("kL", kL); //Ԥ���KLֵ
 		  insertmap.put("fentongnum", fentongnum);// �������ݷ�Ͱ������
 		  
 		 
		   Map mapvalue= new HashMap(); //���ĳ����Ͱ�������о۴������KLֵ
		   //���۴����������Kl��Ԥ���KL��ʱ��˵��������Ҫ�󣬽�������Ҫ��ľ۴����������KL
		   if(mathlog>kL){
			   // KmeansArit.getNearCenter(KM, tagindex);
			   mapcluser.clear();
			   mapcluser.put("fentongid", fentongid);
			   //���ĳ����Ͱ�����о۴ص�P��Qֵ��������������Ͱ�����о۴ص�kLֵ
			   List<staticBean> listbean = queryInfo.getAllStaticResult(mapcluser);  
			   
			   for(int i =0; i<listbean.size(); i++){
				   staticBean sbean = new staticBean();
				   /* System.out.println(listbean.get(i).getOccupation()+": "+listbean.get(i).getCluster1()+":  p==>"+listbean.get(i).getCountnump()+"  q==>"+listbean.get(i).getCountnumq()); 
				   System.out.println("math.log==>" +Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));*/
				   int cluster1=listbean.get(i).getCluster1();
				   Double dbcluseter=  (Double) mapvalue.get(cluster1);
				   if(dbcluseter==null){
					   mapvalue.put(cluster1, Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));
					   /* System.out.println("Pֵ==>"+listbean.get(i).getCountnump()+"qֵ==>"+listbean.get(i).getCountnumq());
					   System.out.println("math=>>"+Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));*/
				   }else{
					   mapvalue.put(cluster1, dbcluseter+Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));
				   }  
			   }  
			   
			   //���������KLֵ�ٴαȽ�һ�Σ�����Ҫ��ı��棬������Ҫ������������KLֵ
			   Iterator  it = mapvalue.entrySet().iterator();
			   List listkey = new ArrayList();
			   boolean flagkl=true; //�����ж���������еľ۴��ǲ���ȫ��������
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
			  
		       //�ж��Ƿ��и���Ҫ������ݣ�û����Ҫ���·־۴� 
			   if(flagkl){
				   System.out.println("��ͰΪ"+fentongid+"�У��۴�Ϊ"+clusternum+",KLԤ��ֵΪ"+kL+"û���ҵ��κ�ƥ��ľ۴�!��Ҫ���»��־۴�!");
				   System.exit(0);
			   } 
			   
			   //�����������������KLֵ
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
	
			   
			   //�жϲ�����Ҫ��ľ۴���������������klֵ�Ƿ�����Ҫ������ͱ��棬�������������
			   if(groupmathlog>kL){  
				   List<staticBean> nearlist = new ArrayList<staticBean>();
				   for(int gv=0; gv < listkey.size(); gv++){
					   System.out.println("listkey.get(gv)==>"+listkey.get(gv));
					   staticBean nearkybean =new staticBean();
					   //�ҳ��벻����Ҫ�����������۴�
					   int nearkey= KmeansAritResult.getNearCenterByListKey(KM, (int)listkey.get(gv), listkey);
					   nearkybean.setNearindex(nearkey);
					   nearkybean.setIndex((int)listkey.get(gv));
					   
					   System.out.println("nearkey==>"+nearkey+"gv==>"+listkey.get(gv));
					   nearlist.add(nearkybean); 
				   }
				   
				   
				   //����������������������KLֵ,�ж��Ƿ�����Ҫ��
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
						   
						   //������Ҫ��������Ҫ��ĺϲ������KL,���Ծͱ��棬�����������
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
				   //�������ȫ�����������յ�KLֵ������ͱ���
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
			    
		   }else{ //���۴����������KlС��Ԥ���KL��˵������Ҫ�󣬱�������Ҫ�����Ϣ
			    //����ĳ����Ͱ�������Ķ����KLֵ,��Ϊ���ֵС���趨��KL���ж�������ݿⲻ���ھʹ洢
			   if(queryInfo.queryTagResult(insertmap)){
				   dataChangeInfo.insertTaginfo(insertmap);
			   }  
		   } 
		   
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	
	/**
	 * �������յ�KL���ҵ�����Ҫ��ľ۴�
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
	   
		
		int fentongid=fentid; //��ͰΨһ��ʾ,�����ĳ����Ͱ
		int clusternum=clusnum; //�۴�����
		double kL=kLnum; //KL���ֵ 
		int fentongnum=fengtongnum; //�������ݷ�Ͱ������

		try { 
			//��ȡ��Ҫ���������
			File file = new File(fileDataUrl);
			ArffLoader loader = new ArffLoader();
			loader.setFile(file);
			ins = loader.getDataSet();

			KM = new SimpleKMeans();
			KM.setNumClusters(clusternum);

			//��������
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
		    
		   
		   //���¶�Ӧ�ľ۴� ����ѯ����Ӧ��ΨһֵID���������¶�Ӧ������˳��,֪��ÿ���������ڷ�����ĸ��۴� 
		 
		    Map map = new HashMap();
			map.put("fentongid", fentongid);
			//�жϾ۴��Ƿ����»��֣�������»�����Ҫ����clusterֵ
			if(queryInfo.getClusterNum(map)!=-1 && queryInfo.getClusterNum(map)!=clusternum){
				System.out.println("�и��ĵľ۴�");
				if(dataChangeInfo.updateClusterByNull()){
					System.out.println("���¾۴سɹ�");
					List listdata=queryInfo.getfileDataId(map); 
					dataChangeInfo.updateClusterData(listdata, a,ins); 
				}
				
			} 
		   
		   
		   //�õ��������������������Ӧ��P��Qֵ
		   int tagindex = KmeansAritResult.getOneCenter(KM);   //�õ���Ͱ�۴�����
		   Map<String, Object> mapcluser = new HashMap<String, Object>();
		   mapcluser.put("fentongid", fentongid);
		   mapcluser.put("cluster1", tagindex);   
		   double mathlog =KmeansAritResult.getPqNearNumber(mapcluser);  //����۴����ĵ�KL
		   
		   Map insertmap=new HashMap();  //���������Ͱ�۴�����klֵ���������ݿ�
		   insertmap.put("fengtongid", fentongid); //��ͰID
 		   insertmap.put("clusternum", clusternum);  //��Ͱ���з��˶��ٸ��۴�
 		   insertmap.put("clustertag", Integer.toString((int)tagindex)); //����ĳ���۴�
 		   insertmap.put("tagetvalue", mathlog); //��Ͱ����ĳ���۴��������ֵ��kl
 		   insertmap.put("kL", kL); //Ԥ���KLֵ
 		   insertmap.put("fentongnum", fentongnum);// �������ݷ�Ͱ������
 		   
 		   
		   Map mapvalue= new HashMap(); //���ĳ����Ͱ�������о۴������KLֵ
		   //���۴����������Kl��Ԥ���KL��ʱ��˵��������Ҫ�󣬽�������Ҫ��ľ۴����������KL
		   if(mathlog>kL){
			   // KmeansArit.getNearCenter(KM, tagindex);
			   mapcluser.clear();
			   mapcluser.put("fentongid", fentongid);
			   //���ĳ����Ͱ�����о۴ص�P��Qֵ��������������Ͱ�����о۴ص�kLֵ
			   List<staticBean> listbean = queryInfo.getAllStaticResult(mapcluser);  
			   
			   for(int i =0; i<listbean.size(); i++){
				   staticBean sbean = new staticBean();
				   /* System.out.println(listbean.get(i).getOccupation()+": "+listbean.get(i).getCluster1()+":  p==>"+listbean.get(i).getCountnump()+"  q==>"+listbean.get(i).getCountnumq()); 
				   System.out.println("math.log==>" +Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));*/
				   int cluster1=listbean.get(i).getCluster1();
				   Double dbcluseter=  (Double) mapvalue.get(cluster1);
				   if(dbcluseter==null){
					   mapvalue.put(cluster1, Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));
					   /* System.out.println("Pֵ==>"+listbean.get(i).getCountnump()+"qֵ==>"+listbean.get(i).getCountnumq());
					   System.out.println("math=>>"+Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));*/
				   }else{
					   mapvalue.put(cluster1, dbcluseter+Math.log(listbean.get(i).getCountnump()/listbean.get(i).getCountnumq()));
				   }  
			   }  
			   
			   //���������KLֵ�ٴαȽ�һ�Σ�����Ҫ��ı��棬������Ҫ������������KLֵ
			   Iterator  it = mapvalue.entrySet().iterator();
			   List listkey = new ArrayList();
			   boolean flagkl=true; //�����ж���������еľ۴��ǲ���ȫ��������
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
				   System.out.println("��ͰΪ"+fentongid+"�У��۴�Ϊ"+clusternum+",KLԤ��ֵΪ"+kL+"û���ҵ��κ�ƥ��ľ۴�!��Ҫ���»��־۴�!");
				   System.exit(0);
			   } 
			   
			   //�����������������KLֵ
			   Map mapgroup = new HashMap();
			   double groupmathlog = 0;//�������������֮�������Klֵ
			   if(listkey!=null && !listkey.isEmpty()){
				   List<staticBean> listkeybean = queryInfo.getAllStaticResultByList(mapcluser, listkey);  
				   for(int i =0; i<listkeybean.size(); i++){ 
					   System.out.println("groupmathlog  value:==>"+listkeybean.get(i).getOccupation()+" p==>"+listkeybean.get(i).getCountnump()+"  q==>"+listkeybean.get(i).getCountnumq()); 
					   System.out.println("groupmathlog.log==>" +Math.log(listkeybean.get(i).getCountnump()/listkeybean.get(i).getCountnumq()));  
					   groupmathlog=groupmathlog+Math.log(listkeybean.get(i).getCountnump()/listkeybean.get(i).getCountnumq());
				   } 
			   }
			   
			   System.out.println("groupmathlog end:==>"+groupmathlog);
	
			   
			   //�жϲ�����Ҫ��ľ۴���������������klֵ�Ƿ�����Ҫ������ͱ��棬�������������
			   if(groupmathlog>kL){  
				   List<staticBean> nearlist = new ArrayList<staticBean>();
				   for(int gv=0; gv < listkey.size(); gv++){
					   System.out.println("listkey.get(gv)==>"+listkey.get(gv));
					   staticBean nearkybean =new staticBean();
					   //�ҳ��벻����Ҫ�����������۴�
					   int nearkey= KmeansAritResult.getNearCenterByListKey(KM, (int)listkey.get(gv), listkey);
					   nearkybean.setNearindex(nearkey);
					   nearkybean.setIndex((int)listkey.get(gv));
					   
					   System.out.println("nearkey==>"+nearkey+"gv==>"+listkey.get(gv));
					   nearlist.add(nearkybean); 
				   }
				   
				   
				   //����������������������KLֵ,�ж��Ƿ�����Ҫ��
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
						   
						   //������Ҫ��������Ҫ��ĺϲ������KL,���Ծͱ��棬�����������
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
										   System.out.println("��Ͱ��ʾΪ:"+fentongid+",�۴�����Ϊ:"+clusternum+",��׼KLֵΪ:"+kL+",�з���Ҫ������ݣ������뿴��'adult_tag'�е����ݣ�");
									   }catch(Exception e){
										   System.out.println("�����쳣���������");
										   e.printStackTrace();
									   } 
								   } else{
									   System.out.println("��Ͱ��ʾΪ:"+fentongid+",�۴�����Ϊ:"+clusternum+",��׼KLֵΪ:"+kL+",���з���Ҫ������ݱ�����'adult_tag'���У�");
								   }
							   }catch(Exception e){
								   System.out.println("����ʧ�ܣ������쳣���������");
								   e.printStackTrace();
							   }
						   }  
					   }  
				   }  
				   
			   }else{  
				   //�������ȫ�����������յ�KLֵ������ͱ���
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
							   System.out.println("��Ͱ��ʾΪ:"+fentongid+",�۴�����Ϊ:"+clusternum+",��׼KLֵΪ:"+kL+",�з���Ҫ������ݣ������뿴��'adult_tag'�е����ݣ�");
						   }catch(Exception e){
							   System.out.println("�����쳣���������");
							   e.printStackTrace();
						   } 
					   } else{
						   System.out.println("��Ͱ��ʾΪ:"+fentongid+",�۴�����Ϊ:"+clusternum+",��׼KLֵΪ:"+kL+",���з���Ҫ������ݱ�����'adult_tag'���У�");
					   }
				   }catch(Exception e){
					   System.out.println("����ʧ�ܣ������쳣���������");
					   e.printStackTrace();
				   }
				   
			   } 
			    
		   }else{ //���۴����������KlС��Ԥ���KL��˵������Ҫ�󣬱�������Ҫ�����Ϣ
			    //����ĳ����Ͱ�������Ķ����KLֵ,��Ϊ���ֵС���趨��KL���ж�������ݿⲻ���ھʹ洢
			   try{  
				   if(queryInfo.queryTagResult(insertmap)){
					   try{
						   dataChangeInfo.insertTaginfo(insertmap);
						   System.out.println("��Ͱ��ʾΪ:"+fentongid+",�۴�����Ϊ:"+clusternum+",��׼KLֵΪ:"+kL+",�з���Ҫ������ݣ������뿴��'adult_tag'�е����ݣ�");
					   }catch(Exception e){
						   System.out.println("�����쳣���������");
						   e.printStackTrace();
					   } 
				   } else{
					   System.out.println("��Ͱ��ʾΪ:"+fentongid+",�۴�����Ϊ:"+clusternum+",��׼KLֵΪ:"+kL+",���з���Ҫ������ݱ�����'adult_tag'���У�");
				   }
			   }catch(Exception e){
				   System.out.println("����ʧ�ܣ������쳣���������");
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
