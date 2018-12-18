package main;

import java.util.HashMap;
import java.util.Map; 
import weka.KmeansArit;
import weka.dataFileout;
import weka.queryInfo;
import weka.sapartition;

public class choiceClassMain {
	
	public static void main(String args[]){
		int fengtongnum=8; //���÷ֶ��ٸ�Ͱ   
		int fentongid=7; //Ҫ�������ݴ���ķ�ͰΨһ��ʾ
		int clusternum=60; //Ҫ�����Ͱ�·ֶ��ٸ��۴�����
		double kL=1.0; //KL���ֵ ��Ҳ���Ǳ�׼ֵ���������ж����վ۴ؼ��������ֵ�Ƿ����Ҫ��
		String filename="data"+fengtongnum+"_"+fentongid+"_"+clusternum+"_"+kL+".txt";  //��Ͱ��Ӧ�������ļ�����
		String dirmd="D:\\�����ļ�\\qinting\\datafile\\";  //��������ļ���ַ
		String dirmdfile="d:/�����ļ�/qinting/datafile/"+filename; //��Ͱ��Ӧ�������ļ���ַ
		
		
		//��Ͱ
		Map map =new HashMap();
		int fentdatanum = queryInfo.getFengtongNum(map) ;  //��ѯ���ݿ���еķ�Ͱ����
		boolean fentonflang=false;
		if(fentdatanum!=fengtongnum){ //������ݿ��Ͱ���������õķ�Ͱ���ݲ�һ�£������·�Ͱ
			fentonflang = sapartition.classPart(fengtongnum); 
		}else{
			fentonflang=true;
		}
		
		//���������ļ�
		if(fentonflang){
			fentonflang=dataFileout.getfileData(fentongid,dirmd,filename);
		} else{
			fentonflang=false;
			System.out.println("�����ļ�����ʧ��!");
		}
		
		
		//����KLֵ
		if(fentonflang){
			KmeansArit.calculateKL(fentongid,clusternum,kL,dirmdfile,fengtongnum);
			System.out.println("KL�������!");
		}else{
			System.out.println("��KL����ֵʧ��!");
		}
		
	}

}
