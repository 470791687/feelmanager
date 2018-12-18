package weka;

import java.sql.Connection;
import java.sql.PreparedStatement;
 

import db.jdbctools;

public class SaveValue {

	 public static void save(String sql)  { 
		 jdbctools tool = new jdbctools();
		 Connection connection = tool.getConn();
		 PreparedStatement ps = null;
		 try {  
				int rs;  
				ps = (PreparedStatement)connection.prepareStatement(sql);
				rs = ps.executeUpdate(sql); 
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 try{
				 if(connection!=null){
					 connection.close();
				 }
				 if(ps!=null){
					 ps.close();
				 }
			 }catch(Exception es){
				 
			 }
		 }
		 
	 }
	 
	 public static int delete(){ 
		 jdbctools tool = new jdbctools();
		 Connection connection = tool.getConn();
		 PreparedStatement ps = null;
		 try {  
				int rs=0; 
				String sql = "delete from array_value";
				ps = (PreparedStatement)connection.prepareStatement(sql);
				rs = ps.executeUpdate(sql); 
				String sqld = "delete from detial_value";
				ps = (PreparedStatement)connection.prepareStatement(sqld);
				rs = ps.executeUpdate(sqld);
				return rs;
				
		 }catch(Exception e){
			 e.printStackTrace();
			 return 0;
		 }finally{
			 try{
				 if(connection!=null){
					 connection.close();
				 }
				 if(ps!=null){
					 ps.close();
				 }
			 }catch(Exception es){
				 
			 }
		 }
	 }
}
