package weka;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.util.ArrayList;
import java.util.List;
import java.util.Map; 
import bean.staticBean;
import db.jdbctools;

/**
 * 查询类
 * @author Administrator
 *
 */
public class queryInfo { 
	

	/**
	 * 根据条件，查询出需要按照聚类组装的字段数据
	 * @param map
	 * @return
	 */
	public static String getfileData(Map map){
		
	  jdbctools tool = new jdbctools(); 
	  Connection connection =tool.getConn();
	  PreparedStatement ps = null; 
	  ResultSet rs =null; 
		
		try{  
			  StringBuffer sqls=new StringBuffer();
			  
			  sqls.append(" select * from   adultinfo t where 1=1 ");
			  if(map!=null && !map.isEmpty()){
				  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
					  sqls.append(" and t.fentongid='"+map.get("fentongid")+"'");
				  }
                  if(map.get("age")!=null && !"".equals(map.get("age"))){
                	  sqls.append(" and t.age='"+map.get("age")+"'");
				  }
			  }
			  sqls.append(" order by  fentongid asc,age asc,workclassnum asc,relationshipnum asc,racenum asc,sexnum asc,nativecountrynum asc,educationnum asc");
			  
			  
			  ps =(PreparedStatement) connection.prepareStatement(sqls.toString()); 
			  rs =ps.executeQuery(); 
			  StringBuffer sbalue=new StringBuffer();
			  while(rs.next()){ 
				  String age=rs.getString("age");
				  String workclassnum=rs.getString("workclassnum");
				  String maritalstatusnum=rs.getString("maritalstatusnum");
				  String relationshipnum=rs.getString("relationshipnum");
				  String racenum=rs.getString("racenum");
				  String sexnum=rs.getString("sexnum");
				  String nativecountrynum=rs.getString("nativecountrynum");
				  String educationnum=rs.getString("educationnum");
				  
				  sbalue.append(age).append(" ").append(workclassnum).append(" ").append(maritalstatusnum).append(" ").append(relationshipnum)
				  .append(" ").append(racenum).append(" ").append(sexnum).append(" ").append(nativecountrynum).append(" ").append(educationnum).append("\r\n");
			  }
			  
			  //System.out.println(sbalue.toString());
			  
			  return sbalue.toString();
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			 try{
				 rs.close();
				 ps.close();
				 connection.close();
				 
			 }catch(Exception es){
				 es.printStackTrace();
				 
			 }
			
		 }
	}
	
	
	
	/**
	 * 根据天条件，查询出对应的唯一值ID，用来更新对应的排序顺序,知道每条数据属于分类的哪个聚簇
	 * @param map
	 * @return
	 */
public static List getfileDataId(Map map){
	   jdbctools tool = new jdbctools(); 
	   Connection connection =tool.getConn();
	   PreparedStatement ps = null; 
	   ResultSet rs =null; 
		
		try{ 
			  
			  StringBuffer sqls=new StringBuffer();
			  
			  sqls.append(" select * from   adultinfo t where 1=1 ");
			  if(map!=null && !map.isEmpty()){
				  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
					  sqls.append(" and t.fentongid='"+map.get("fentongid")+"'");
				  }
                  if(map.get("age")!=null && !"".equals(map.get("age"))){
                	  sqls.append(" and t.age='"+map.get("age")+"'");
				  }
			  }
			  sqls.append(" order by  fentongid asc,age asc,workclassnum asc,relationshipnum asc,racenum asc,sexnum asc,nativecountrynum asc,educationnum asc");
			  
			  
			  ps =(PreparedStatement) connection.prepareStatement(sqls.toString()); 
			  rs =ps.executeQuery(); 
			  List list = new ArrayList<String>();
			  while(rs.next()){ 
				  String id=rs.getString("id"); 
				  list.add(id);
				 
			  } 
			  return list;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			 try{
				 rs.close();
				 ps.close();
				 connection.close();
				 
			 }catch(Exception es){
				 es.printStackTrace();
				 
			 }
			
		 }
	}


/**
 * 获得p与q的统计值
 */
    public static List<staticBean> getStaticResult(Map map){ 
    	
    	  jdbctools tool = new jdbctools(); 
		  Connection connection =tool.getConn();
		  PreparedStatement ps = null; 
		  ResultSet rs =null; 
	    	
	    	try{ 
				  
				  StringBuffer sqls=new StringBuffer();
				  
				  sqls.append(" select  t.occupation, count(*) countsump,   ");
				  sqls.append(" count(*)/( select count(*)   from adultinfo ts where 1=1    ");
				  if(map!=null && !map.isEmpty()){
					  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
						  sqls.append(" and ts.fentongid='"+map.get("fentongid")+"'");
					  }
	                if(map.get("age")!=null && !"".equals(map.get("age"))){
	              	  sqls.append(" and ts.age='"+map.get("age")+"'");
					  }
	                if(map.get("cluster1")!=null && !"".equals(map.get("cluster1"))){
						  sqls.append(" and ts.cluster1='"+map.get("cluster1")+"'");
					  }
				  }
				  
				  sqls.append(" )  as  countnump,  ");
				  
				  sqls.append(" (select count(*) as dd   from adultinfo ts  where 1=1  ");
				  if(map!=null && !map.isEmpty()){
					  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
						  sqls.append(" and ts.fentongid='"+map.get("fentongid")+"'");
					  }
	                if(map.get("age")!=null && !"".equals(map.get("age"))){
	              	  sqls.append(" and ts.age=='"+map.get("age")+"'");
					  }
	                if(map.get("cluster1")!=null && !"".equals(map.get("cluster1"))){
						  sqls.append(" and ts.cluster1='"+map.get("cluster1")+"'");
					  }
				  }
				  sqls.append(" ) as countsumallp, ");
				  
				  sqls.append(" (select count(*) as dd   from adultinfo ts  where ts.occupation=t.occupation) as countsumq , ");
				  sqls.append(" (select count(*) as dd   from adultinfo ts ) countsumall, ");
				  sqls.append(" (select count(*) as dd   from adultinfo ts  where ts.occupation=t.occupation)/(select count(*) as dd   from adultinfo ts ) as countnumq ");
				  
				  
				  sqls.append(" from adultinfo t where 1=1 ");
				  
				  if(map!=null && !map.isEmpty()){
					  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
						  sqls.append(" and t.fentongid='"+map.get("fentongid")+"'");
					  }
	                if(map.get("age")!=null && !"".equals(map.get("age"))){
	              	  sqls.append(" and t.age='"+map.get("age")+"'");
					  }
	                if(map.get("cluster1")!=null && !"".equals(map.get("cluster1"))){
						  sqls.append(" and t.cluster1='"+map.get("cluster1")+"'");
					  }
				  }
				  sqls.append(" group by occupation ");
				  
				  
				  ps =(PreparedStatement) connection.prepareStatement(sqls.toString()); 
				  rs =ps.executeQuery(); 
				  List<staticBean> list = new ArrayList<staticBean>();
				  while(rs.next()){
					  staticBean bean= new staticBean();
					  bean.setOccupation(rs.getString("occupation")); 
					  bean.setCountsump(rs.getInt("countsump"));
					  bean.setCountnump(rs.getDouble("countnump"));
					  bean.setCountsumallp(rs.getInt("countsumallp"));
					  bean.setCountsumq(rs.getInt("countsumq"));
					  bean.setCountsumall(rs.getInt("countsumall"));
					  bean.setCountnumq(rs.getDouble("countnumq"));
					  list.add(bean);
					 
				  } 
				  
				  return list;
	    	
	    }catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			 try{
				 rs.close();
				 ps.close();
				 connection.close();
				 
			 }catch(Exception es){
				 es.printStackTrace();
				 
			 }
			
		 }
	
	  }
    
    
    /**
     * 某个分桶下所有的P与Q值
     * @param map
     * @return
     */
    public static List<staticBean> getAllStaticResult(Map map){  
    	
    	  jdbctools tool = new jdbctools(); 
		  Connection connection =tool.getConn();
		  PreparedStatement ps = null; 
		  ResultSet rs =null; 
		  
	    	try{ 
				  StringBuffer sqls=new StringBuffer();
				  
				  sqls.append(" select  t.occupation,cluster1, count(*) countsump,   ");
				  
				  sqls.append(" (select count(*) as dd   from adultinfo ts  where 1=1  and  ts.cluster1=t.cluster1 ");
				  if(map!=null && !map.isEmpty()){
					  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
						  sqls.append(" and ts.fentongid='"+map.get("fentongid")+"'");
					  }
	                if(map.get("age")!=null && !"".equals(map.get("age"))){
	              	  sqls.append(" and ts.age='"+map.get("age")+"'");
					  }
	                if(map.get("cluster1")!=null && !"".equals(map.get("cluster1"))){
						  sqls.append(" and ts.cluster1='"+map.get("cluster1")+"'");
					  }
				  }
				  sqls.append(" ) as countsumallp, ");
				  
				  sqls.append(" count(*)/( select count(*)   from adultinfo ts where 1=1 and  ts.cluster1=t.cluster1   ");
				  if(map!=null && !map.isEmpty()){
					  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
						  sqls.append(" and ts.fentongid='"+map.get("fentongid")+"'");
					  }
	                if(map.get("age")!=null && !"".equals(map.get("age"))){
	              	  sqls.append(" and ts.age=='"+map.get("age")+"'");
					  }
	                if(map.get("cluster1")!=null && !"".equals(map.get("cluster1"))){
						  sqls.append(" and ts.cluster1='"+map.get("cluster1")+"'");
					  }
				  }
				  sqls.append(" )  as  countnump,  ");
				  
				
				  sqls.append(" (select count(*) as dd   from adultinfo ts  where ts.occupation=t.occupation) as countsumq , ");
				  sqls.append(" (select count(*) as dd   from adultinfo ts ) countsumall, ");
				  sqls.append(" (select count(*) as dd   from adultinfo ts  where ts.occupation=t.occupation)/(select count(*) as dd   from adultinfo ts ) as countnumq ");
				  
				  
				  sqls.append(" from adultinfo t where 1=1   ");
				  
				  if(map!=null && !map.isEmpty()){
					  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
						  sqls.append(" and t.fentongid='"+map.get("fentongid")+"'");
					  }
	                if(map.get("age")!=null && !"".equals(map.get("age"))){
	              	  sqls.append(" and t.age=='"+map.get("age")+"'");
					  }
	                if(map.get("cluster1")!=null && !"".equals(map.get("cluster1"))){
						  sqls.append(" and t.cluster1='"+map.get("cluster1")+"'");
					  }
				  }
				  sqls.append(" group by occupation ,cluster1 ");
				  
				  
				  ps =(PreparedStatement) connection.prepareStatement(sqls.toString()); 
				  rs =ps.executeQuery(); 
				  List<staticBean> list = new ArrayList<staticBean>();
				  while(rs.next()){
					  staticBean bean= new staticBean();
					  bean.setOccupation(rs.getString("occupation")); 
					  bean.setCluster1(rs.getInt("cluster1"));
					  bean.setCountsump(rs.getInt("countsump"));
					  bean.setCountnump(rs.getDouble("countnump"));
					  bean.setCountsumallp(rs.getInt("countsumallp"));
					  bean.setCountsumq(rs.getInt("countsumq"));
					  bean.setCountsumall(rs.getInt("countsumall"));
					  bean.setCountnumq(rs.getDouble("countnumq"));
					  list.add(bean);
					 
				  } 
				  
				  return list;
	    	
	    }catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			 try{
				 rs.close();
				 ps.close();
				 connection.close();
				 
			 }catch(Exception es){
				 es.printStackTrace();
				 
			 } 
		 }  
  
    }
    
    
   /**
    * 保存分桶、聚簇、聚簇值、kL值信息
    * @param map
    * @return
    */
	public static boolean queryTagResult(Map map) throws Exception{
			  jdbctools tool = new jdbctools(); 
			  Connection connection =tool.getConn(); 
			  PreparedStatement pds=null;
			  ResultSet rs =null;
			  int tag=0;
			 
				  if(map!=null && !map.isEmpty()){
					  pds =  connection.prepareStatement(" select count(*) as resultnum from adult_tag where fengtongid=? and clusternum=? and clustertag=? and tagetvalue=? and klvalue=? and fentongnum=? ");
					  pds.setString(1, Integer.toString((int) map.get("fengtongid"))); 
					  pds.setString(2, Integer.toString((int) map.get("clusternum"))); 
					  pds.setString(3,  (String) map.get("clustertag")); 
					  pds.setString(4,  Double.toString((double) map.get("tagetvalue") )); 
					  pds.setString(5, Double.toString((double) map.get("kL") ));
					  pds.setString(6, Integer.toString((int) map.get("fentongnum")));
					  rs = pds.executeQuery();
					  if(rs!=null  ){ 
						  while(rs.next()){
							  if(rs.getInt("resultnum")>0){
								  tag=rs.getInt("resultnum");
								  break; 
							  } 
						}
					  }  
				  } 
			 
			  rs.close();
			  pds.close();
			  connection.close();  
			  if(tag>0){
				  return false;
			  }else{
				  return true;
			  }
		 
		}
	
	
	/**
     * 某个分桶下所有的P与Q值
     * @param map
     * @return
     */
    public static List<staticBean> getAllStaticResultByList(Map map,List listkey){   
    	
		  jdbctools tool = new jdbctools(); 
		  Connection connection =tool.getConn();
		  PreparedStatement ps = null; 
		  ResultSet rs =null;  
		    try{ 
					  StringBuffer sqls=new StringBuffer();
					  
					  sqls.append(" select  t.occupation, count(*) countsump,   ");
					  sqls.append(" count(*)/( select count(*)   from adultinfo ts where 1=1    ");
					  if(map!=null && !map.isEmpty()){
						  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
							  sqls.append(" and ts.fentongid='"+map.get("fentongid")+"'");
						  }
		                if(map.get("age")!=null && !"".equals(map.get("age"))){
		              	  sqls.append(" and ts.age=='"+map.get("age")+"'");
						  }
		                if(map.get("cluster1")!=null && !"".equals(map.get("cluster1"))){
							  sqls.append(" and ts.cluster1='"+map.get("cluster1")+"'");
						  }
					  }
					  
					  sqls.append(" )  as  countnump,  ");
					  
					  sqls.append(" (select count(*) as dd   from adultinfo ts  where 1=1  ");
					  if(map!=null && !map.isEmpty()){
						  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
							  sqls.append(" and ts.fentongid='"+map.get("fentongid")+"'");
						  }
		                if(map.get("age")!=null && !"".equals(map.get("age"))){
		              	  sqls.append(" and ts.age=='"+map.get("age")+"'");
						  }
		                 
					  }
					  
					  StringBuffer sqlkey= new StringBuffer();
					  if(listkey!=null && !listkey.isEmpty()){ 
						 /* sqlkey.append(" and  cluster1 in (");
						        String keyvalue="" ;
							    for(int key=0; key<listkey.size(); key++){  
							    	keyvalue =keyvalue+ "'"+listkey.get(key)+"',"; 
							    }
							    
							    keyvalue=keyvalue.substring(0, keyvalue.lastIndexOf(",")); 
							    System.out.println("keyvalue==>"+keyvalue);
					     sqlkey.append(keyvalue); 
					     sqlkey.append(" )"); */
						  sqlkey.append(" and (");
					     for(int c=0;c< Math.ceil((double)listkey.size()/1000);c++){
					    	 if(c==0){
					    		 sqlkey.append("    cluster1 in (");
					    	 }else{
					    		 sqlkey.append("  or  cluster1 in (");
					    	 }
					    	
					    	 String keyvalue="" ;
					    	 for(int key=c*1000;  key<(c+1)*1000 ;  key++){ 
					    		 if(key>listkey.size()){
					    			 break;
					    		 }else if(key < listkey.size()){
					    			 keyvalue =keyvalue+ "'"+listkey.get(key)+"',"; 
					    		 }
					    	 }
					    	 keyvalue=keyvalue.substring(0, keyvalue.lastIndexOf(",")); 
							 System.out.println("keyvalue==>"+keyvalue);
							 sqlkey.append(keyvalue); 
						     sqlkey.append(" ) "); 
					     } 
					     
					     sqlkey.append(" ) ") ;
					     
					  }
					  
					  sqls.append(sqlkey);
					  sqls.append(" ) as countsumallp, ");
					  
					  sqls.append(" (select count(*) as dd   from adultinfo ts  where ts.occupation=t.occupation) as countsumq , ");
					  sqls.append(" (select count(*) as dd   from adultinfo ts ) countsumall, ");
					  sqls.append(" (select count(*) as dd   from adultinfo ts  where ts.occupation=t.occupation)/(select count(*) as dd   from adultinfo ts ) as countnumq ");
					  
					  
					  sqls.append(" from adultinfo t where 1=1 ");
					  
					  if(map!=null && !map.isEmpty()){
						  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
							  sqls.append(" and t.fentongid='"+map.get("fentongid")+"'");
						  }
		                if(map.get("age")!=null && !"".equals(map.get("age"))){
		              	  sqls.append(" and t.age=='"+map.get("age")+"'");
						  } 
					  }
					  sqls.append(sqlkey);
					  sqls.append(" group by occupation ");
					  
					  
					  ps =(PreparedStatement) connection.prepareStatement(sqls.toString()); 
					  rs =ps.executeQuery(); 
					  List<staticBean> list = new ArrayList<staticBean>();
					  while(rs.next()){
						  staticBean bean= new staticBean();
						  bean.setOccupation(rs.getString("occupation")); 
						  bean.setCountsump(rs.getInt("countsump"));
						  bean.setCountnump(rs.getDouble("countnump"));
						  bean.setCountsumallp(rs.getInt("countsumallp"));
						  bean.setCountsumq(rs.getInt("countsumq"));
						  bean.setCountsumall(rs.getInt("countsumall"));
						  bean.setCountnumq(rs.getDouble("countnumq"));
						  list.add(bean);
						 
					  } 
					  
					  return list;
		    	
		    }catch(Exception e){
				e.printStackTrace();
				return null;
			}finally{
				 try{
					 rs.close();
					 ps.close();
					 connection.close();
					 
				 }catch(Exception es){
					 es.printStackTrace();
					 
				 } 
			}     
  
    }
    
    
    /**
     * 获得聚簇分组数量
     * @param map
     * @return
     */
    public static int getClusterNum(Map map){ 
    	
    	 jdbctools tool = new jdbctools(); 
		 Connection connection =tool.getConn();
		 PreparedStatement ps = null; 
		 ResultSet rs =null; 
		
		try{  
			  StringBuffer sqls=new StringBuffer();
			  
			  sqls.append(" select count(distinct cluster1) as clusternum  from adultinfo where cluster1 is not null ");
			  if(map!=null && !map.isEmpty()){
				  if(map.get("fentongid")!=null && !"".equals(map.get("fentongid"))){
					  sqls.append(" and fentongid='"+map.get("fentongid")+"'");
				  }
                  if(map.get("age")!=null && !"".equals(map.get("age"))){
                	  sqls.append(" and age=='"+map.get("age")+"'");
				  }
			  } 
			  
			  ps =(PreparedStatement) connection.prepareStatement(sqls.toString()); 
			  rs =ps.executeQuery();  
			  int clusternum=0;
			  while(rs.next()){ 
				  clusternum=rs.getInt("clusternum"); 
			  }  
			  
			  return clusternum;
			
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		} finally{
			 try{
				 rs.close();
				 ps.close();
				 connection.close();
				 
			 }catch(Exception es){
				 es.printStackTrace();
				 
			 } 
		}    
    	 
    }
    
    
    /**
     * 获得职业数量
     * @return
     */
    public static int getOccupationNum(){  
    	
   	     jdbctools tool = new jdbctools(); 
		 Connection connection =tool.getConn();
		 PreparedStatement ps = null; 
		 ResultSet rs =null; 
		
		try{  
			  StringBuffer sqls=new StringBuffer(); 
			  sqls.append(" select count(distinct occupation) occnum  from adultinfo  ");
			 
			  
			  ps =(PreparedStatement) connection.prepareStatement(sqls.toString()); 
			  rs =ps.executeQuery();  
			  int occnum=0;
			  while(rs.next()){ 
				  occnum=rs.getInt("occnum"); 
			  }  
			  
			  return occnum;
			
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		} finally{
			 try{
				 rs.close();
				 ps.close();
				 connection.close();
				 
			 }catch(Exception es){
				 es.printStackTrace();
				 
			 } 
		}     
    	
    }
    
    
    /**
     * 获得各个职业统计总数
     * @return
     */
    public static List<staticBean> getOccupationRes(){  
  	     jdbctools tool = new jdbctools(); 
		 Connection connection =tool.getConn();
		 PreparedStatement ps = null; 
		 ResultSet rs =null; 
		
		try{  
			  StringBuffer sqls=new StringBuffer(); 
			  sqls.append(" select occupation, count(occupation) as count from adultinfo group by occupation  ");
			 
			  
			  ps =(PreparedStatement) connection.prepareStatement(sqls.toString()); 
			  rs =ps.executeQuery(); 
			  List<staticBean> lis =new ArrayList<staticBean>();
			  while(rs.next()){
				  staticBean sbean=new staticBean();
				  sbean.setOccupation(rs.getString(1));
				  sbean.setCoutnum(rs.getInt(2));
				  lis.add(sbean)	 ;
				}
			  return lis; 
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		} finally{
			 try{
				 rs.close();
				 ps.close();
				 connection.close();
				 
			 }catch(Exception es){
				 es.printStackTrace();
				 
			 } 
		}     
    
    }
    
    
    /**
     * 获得分桶数量 
     * @param map
     * @return
     */
    public static int getFengtongNum(Map map){ 
    	
   	     jdbctools tool = new jdbctools(); 
		 Connection connection =tool.getConn();
		 PreparedStatement ps = null; 
		 ResultSet rs =null; 
		
		try{  
			  StringBuffer sqls=new StringBuffer();
			  
			  sqls.append(" select count(distinct fentongid) as fengtongnum from adultinfo "); 
			  ps =(PreparedStatement) connection.prepareStatement(sqls.toString()); 
			  rs =ps.executeQuery();  
			  int fengtongnum=-1;
			  while(rs.next()){ 
				  fengtongnum=rs.getInt("fengtongnum"); 
			  }  
			  
			  return fengtongnum;
			
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		} finally{
			 try{
				 rs.close();
				 ps.close();
				 connection.close();
				 
			 }catch(Exception es){
				 es.printStackTrace();
				 
			 } 
		}    
   	 
   }
    
}
