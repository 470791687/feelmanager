package main;

import java.util.HashMap;
import java.util.Map; 
import weka.KmeansArit;
import weka.dataFileout;
import weka.queryInfo;
import weka.sapartition;

public class choiceClassMain {
	
	public static void main(String args[]){
		int fengtongnum=8; //设置分多少个桶   
		int fentongid=7; //要进行数据处理的分桶唯一标示
		int clusternum=60; //要处理分桶下分多少个聚簇数量
		double kL=1.0; //KL标记值 ，也就是标准值，用来做判断最终聚簇计算出来的值是否符合要求
		String filename="data"+fengtongnum+"_"+fentongid+"_"+clusternum+"_"+kL+".txt";  //分桶对应的数据文件名称
		String dirmd="D:\\个人文件\\qinting\\datafile\\";  //设置输出文件地址
		String dirmdfile="d:/个人文件/qinting/datafile/"+filename; //分桶对应的数据文件地址
		
		
		//分桶
		Map map =new HashMap();
		int fentdatanum = queryInfo.getFengtongNum(map) ;  //查询数据库库中的分桶数量
		boolean fentonflang=false;
		if(fentdatanum!=fengtongnum){ //如果数据库分桶数据与设置的分桶数据不一致，就重新分桶
			fentonflang = sapartition.classPart(fengtongnum); 
		}else{
			fentonflang=true;
		}
		
		//生成数据文件
		if(fentonflang){
			fentonflang=dataFileout.getfileData(fentongid,dirmd,filename);
		} else{
			fentonflang=false;
			System.out.println("生成文件数据失败!");
		}
		
		
		//计算KL值
		if(fentonflang){
			KmeansArit.calculateKL(fentongid,clusternum,kL,dirmdfile,fengtongnum);
			System.out.println("KL计算完成!");
		}else{
			System.out.println("算KL最终值失败!");
		}
		
	}

}
