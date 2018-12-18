package weka;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import bean.staticBean;
import db.jdbctools;
import weka.core.Instances;

public class dataChangeInfo {

	
	/**
	 * 根据排序顺序,更新每条数据对应的聚簇,.新对应的排序顺序,知道每条数据属于分类的哪个聚簇
	 * @param listdata
	 * @param a
	 * @param ins
	 */
	public static void updateClusterData(List listdata,int a[],Instances ins){ 
		 jdbctools tool = new jdbctools(); 
		 Connection connection =tool.getConn();
		 Statement  ps = null;  
		 try{ 
			  ps =   connection.createStatement();
			  connection.setAutoCommit(false); 
			  if(a!=null ){
				  for(int i=0;i<a.length;i++){
					 // System.out.println("update adultinfo set cluster1='"+a[i]+"' where id='"+listdata.get(i).toString()+"'  ");
					  ps.addBatch("update adultinfo set cluster1='"+a[i]+"' where id='"+listdata.get(i).toString()+"'  "); 
					  //System.out.println("k==>"+k+ "  a[i]==>"+a[i] + "   listdata==>"+listdata.get(i).toString()+"  ins==>"+ins.instance(i));
				  }
				  int b[]=  ps.executeBatch();
				  System.out.println("更新成功"+b.length);
				  connection.commit();
				  System.out.println("提交成功"+b.length);
				  connection.setAutoCommit(true);
			  }

		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 try{
				 ps.close();
				 connection.close();
			 }catch(Exception es){
				 es.printStackTrace();
			 }
			
		 }
		 
	}
	
	
	/**
	 * 更新聚簇为空
	 * @param listdata
	 * @param a
	 * @param ins
	 */
	public static boolean updateClusterByNull( ){ 
		 jdbctools tool = new jdbctools(); 
		 Connection connection =tool.getConn();
		 PreparedStatement  ps = null;  
		 try{ 
			  ps =   connection.prepareStatement("update adultinfo set cluster1=null" );
			  int clusternum=ps.executeUpdate() ;
			  if(clusternum>0)  return true;  
			  else return false; 
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 return false;
		 }finally{
			 try{
				 ps.close();
				 connection.close();
				 return true; 
			 }catch(Exception es){
				 es.printStackTrace();
				 return false;
			 }
			
		 }
		 
	}
	
	/**
	 * 保存记录聚簇中心值算出的KL记录信息
	 * @param map
	 * @return
	 */
	public static boolean insertTaginfo(Map map) throws Exception{ 
		  jdbctools tool = new jdbctools(); 
		  Connection connection =tool.getConn(); 
		  PreparedStatement pds=null;
		  boolean tag=false;
	 
			  if(map!=null && !map.isEmpty()){
				  pds =  connection.prepareStatement(" insert into adult_tag(fengtongid, clusternum,clustertag,tagetvalue,klvalue,fentongnum) values(?,?,?,?,?,?)");
				  pds.setString(1, Integer.toString((int) map.get("fengtongid"))); 
				  pds.setString(2, Integer.toString((int) map.get("clusternum"))); 
				  pds.setString(3,   (String) map.get("clustertag") ); 
				  pds.setString(4,  Double.toString((double) map.get("tagetvalue") )); 
				  pds.setString(5, Double.toString((double) map.get("kL") ));
				  pds.setString(6, Integer.toString((int) map.get("fentongnum")));
				  int flag = pds.executeUpdate();
				  if(flag>0){
					  tag = true;
				  }else{
					  tag = false;
				  }
			  }else{ 
				  tag = false;
			  }
			  
			  pds.close();
			  connection.close();
			  return tag;
	}
	
	
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public static boolean updateFentongByOccuption(List<staticBean> list){ 
		  jdbctools tool = new jdbctools(); 
		  Connection connection =tool.getConn(); 
		  PreparedStatement pds=null;
		  Statement  ps = null;  
		  try{
			  pds= connection.prepareStatement("update adultinfo set fentongid=null "); 
			  int upnum= pds.executeUpdate(); 
			  ps =   connection.createStatement();
			  if(upnum>0){
				 
				  connection.setAutoCommit(false); 
				  if(list!=null ){
					  for(int i=0;i<list.size();i++){ 
						  ps.addBatch("update adultinfo set fentongid='"+list.get(i).getIndex()+"' where occupation='"+list.get(i).getOccupation()+"'  ");  
					  }
					  int b[]=  ps.executeBatch();
					  System.out.println("更新成功"+b.length);
					  connection.commit();
					  System.out.println("提交成功"+b.length);
					  connection.setAutoCommit(true);
				  }
			  } 
			  return true;
		  }catch(Exception e){
			  e.printStackTrace();
			  return false;
		  } finally{
				 try{
					 pds.close();
					 ps.close();
					 connection.close();
					 return true; 
				 }catch(Exception es){
					 es.printStackTrace();
					 return false;
				 }
				
			 } 
	}
	
	
	
	
}
