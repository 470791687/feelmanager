package weka;  
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map; 

public class dataFileout {
	
	public static void main(String args[]){
		
		Map map = new HashMap();
		map.put("fentongid", "7");
		String masterData=queryInfo.getfileData(map);
		Map mastermap=new   HashMap();
		mastermap.put("masterData", masterData);
		
		//System.out.println(dataFileout.getAssemData(mastermap));
		
		String filename="data"+map.get("fentongid")+".txt";
		
		String dirmd="D:\\�����ļ�\\qinting\\datafile\\";
		
		System.out.println(filename);
		
		try{
			File file = new File(dirmd+filename);
			if(!file.exists()){
				File dir = new File(file.getParent());
				dir.mkdirs(); 
			}
			file.createNewFile(); 
			
			FileOutputStream outStream = new FileOutputStream(file);	//�ļ���������ڽ�����д���ļ�
			outStream.write(dataFileout.getAssemData(mastermap).toString().getBytes());
			outStream.close();	//�ر��ļ������
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * ���������ļ��������������
	 * @param fengtongid
	 * @param fileUrl
	 * @param fileName
	 * @return
	 */
	public static boolean getfileData(int fengtongid,String fileUrl,String fileName){ 
		
		Map map = new HashMap();
		map.put("fentongid", fengtongid);
		String masterData=queryInfo.getfileData(map);
		Map mastermap=new   HashMap();
		mastermap.put("masterData", masterData);
		
		//System.out.println(dataFileout.getAssemData(mastermap));
		
		String filename=fileName;
		
		System.out.println(filename);
		
		try{
			File file = new File(fileUrl+filename);
			if(!file.exists()){
				File dir = new File(file.getParent());
				dir.mkdirs(); 
			}
			file.createNewFile(); 
			
			FileOutputStream outStream = new FileOutputStream(file);	//�ļ���������ڽ�����д���ļ�
			outStream.write(dataFileout.getAssemData(mastermap).toString().getBytes());
			outStream.close();	//�ر��ļ������
			System.out.println("�����ļ����ɳɹ����������һ����");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		} 
	
	} 
	
	
	//�����Ҫ���ļ���ʽ����Ϊ�����㷨����Ҫ�˸�ʽ
	public static String getAssemData(Map map){
		
		StringBuffer sheadata=new StringBuffer();
		sheadata.append("@RELATION data ").append("\r\n")
		.append("\r\n")
		.append("@ATTRIBUTE one REAL ").append("\r\n")
		.append("@ATTRIBUTE two REAL ").append("\r\n")
		.append("@ATTRIBUTE three REAL ").append("\r\n") 
		.append("@ATTRIBUTE four REAL ").append("\r\n")
		.append("@ATTRIBUTE five REAL ").append("\r\n")
		.append("@ATTRIBUTE six REAL ").append("\r\n")
		.append("@ATTRIBUTE seven REAL ").append("\r\n")
		.append("@ATTRIBUTE eight REAL ").append("\r\n")
		.append("\r\n");
		;
		if(map!=null && !map.isEmpty()){
			sheadata.append("@DATA ").append("\r\n");
			sheadata.append(map.get("masterData"));
		}
		return sheadata.toString();
	} 
	
	
}
