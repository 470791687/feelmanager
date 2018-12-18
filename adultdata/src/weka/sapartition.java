package weka; 
import bean.staticBean; 
import java.util.ArrayList;
import java.util.List; 
 
public class sapartition {
	static int tagflag=0;
	public static void main(String[] args) {

    	try {   
    		int n=8; //设置分多少个桶
			int occnum = queryInfo.getOccupationNum(); 
			List<staticBean> rs  = queryInfo.getOccupationRes();
			int total=0;
			int m=0; 
			int a1[]=new int[occnum];
			String occ[]=new String[occnum];
			double a[]=new double[occnum];
			
			for(int i=0;i<rs.size();i++){
				a1[m]=rs.get(i).getCoutnum();
				occ[m]=rs.get(i).getOccupation(); 
				total=total+a1[m];
				m++;
			} 
			 
			//对不同的SA计算敏感程度a[i];a1[i]是每种SA取值的记录数
			for(int i=0;i<occnum;i++){
				a[i]=Math.log(total/a1[i]);
     		    System.out.println(a[i]); 
			} 
		 
			SaveValue.delete();
			fentong(a1,a,n,occ,total); 
			
	
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	/**
	 * 进行分桶数据
	 * @param fengnum
	 * @return
	 */
	public static boolean classPart(int fengnum){ 
		try {   
    		int n=fengnum; //设置分多少个桶
			int occnum = queryInfo.getOccupationNum(); 
			List<staticBean> rs  = queryInfo.getOccupationRes();
			int total=0;
			int m=0; 
			int a1[]=new int[occnum];
			String occ[]=new String[occnum];
			double a[]=new double[occnum];
			
			for(int i=0;i<rs.size();i++){
				a1[m]=rs.get(i).getCoutnum();
				occ[m]=rs.get(i).getOccupation(); 
				total=total+a1[m];
				m++;
			}  
			//对不同的SA计算敏感程度a[i];a1[i]是每种SA取值的记录数
			for(int i=0;i<occnum;i++){
				a[i]=Math.log(total/a1[i]);
     		    System.out.println(a[i]);
     		   
			}  
			SaveValue.delete();
			fentong(a1,a,n,occ,total);  
	
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		return true;
	}

	//计算个SA敏感程度彼此之间的距离，二维数组形式实现
	public static void fentong(int p[],double a[],int n,String occ[],int total){
		double a1[][]=new double[a.length][a.length];
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a.length;j++){ 
				a1[i][j]=Math.abs(a[i]-a[j]); 
			}
		}
		
		double flag=a1[0][1];
		int flag1=0,flag2=0;
		//找出最小距离及其数组下标i,j
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a.length;j++){ 
				//判断标记值是不是大于数组中的值，大于说明这个值更小，更符合要求。并且下标值不能相等，因为相等就是自己
				if(flag>a1[i][j]&&i!=j){
					flag=a1[i][j];
					flag1=i;
					flag2=j;
				}
			}
		}
		System.out.println("flag1==>"+flag1 +"flag2==>"+flag2);

		//合并敏感程度差距最小的属性，形成新数组q[i].q[i]表示记录数
		int q[]=new int[p.length-1];
		String aqq[]=new String[occ.length-1];
		int j=0;
		
		for(int i=0;i<p.length-1;i++){
			//System.out.println("p.length==>"+p.length);
			if(j==p.length){
				break;
			} else if(j!=flag1&&j!=flag2){ 
				q[i]=p[j];
				aqq[i]=occ[j];
				j++;
			} else{ 
				j++; 
				if(j==p.length) {
					break;
				}else{
					if(j==flag1||j==flag2){
						j++; 
						if(j==p.length) {
							break;
						}else if(j==flag1){
							j++;
							if(j==p.length) {
								break;
							}
						}else if(j==flag2){
							j++;
							if(j==p.length) {
								break;
							}
						}else{
							q[i]=p[j];
							aqq[i]=occ[j];
							j++;
						}
					 }else{
						q[i]=p[j];
						aqq[i]=occ[j];
						j++;
					 } 
				}
			}
		}
		
		//将敏感程度差距最小的属性p[flag1]、p[flag2]合并，放至新数组的最后。 
		q[a.length-2]=p[flag1]+p[flag2];
		aqq[occ.length-2]=occ[flag1]+","+occ[flag2];
		//定义数组b[]，重新计算敏感程度
		double b[]=new double[q.length]; 
		
		for(int i=0;i<b.length;i++){
			b[i]=Math.log(total/q[i]);
		}
 
		tagflag++;
		System.out.println("start==============="+tagflag+"=====================start"); 
		System.out.println("p[flag1===>"+p[flag1]);
		System.out.println("p[flag2===>"+p[flag2]);
		System.out.println("两个数的和==>"+(p[flag1]+p[flag2]));
		System.out.println("log==>"+(Math.log(45222/(p[flag1]+p[flag2])))); 
		System.out.println(occ[flag1]+","+occ[flag2]);
		System.out.println("end==============="+tagflag+"=====================end"); 
	    System.out.println("数组长度为："+a.length+"。合并的两条记录为："+flag1+","+flag2);
	
		int value_add=p[flag1]+p[flag2];
		double value_log=Math.log(45222/(p[flag1]+p[flag2]));
		String sql="insert into detial_value(value1,value2,value_add,value_log,occu_name1,occu_name2) "
		 		  +" values('"+p[flag1]+"','"+p[flag2]+"','"+value_add+"','"+value_log+"','"+occ[flag1]+"','"+occ[flag2]+"')";
		SaveValue.save(sql); 
 		
	    System.out.println("q.length==>"+q.length);
		if(q.length>n){
			fentong(q,b, n,aqq,total);
		}
		
		valueAllUpdate(q,n,aqq);
	}
	

	
	
	//记录组合的分组，并且更新adultinfo对应的分桶ID
	public static void valueAllUpdate(int q[],int len,String aqq[]){ 
		if(q.length==len){
			List<staticBean> list = new ArrayList<staticBean>();
			for(int i=0;i<q.length;i++){
				System.out.println("最终数组值["+i+"]==>"+q[i]);
				String sql="insert into array_value(value_detail,valuegroup_name) "
				 		+ "values('"+q[i]+"','"+aqq[i]+"')"; 
				  SaveValue.save(sql);
				  
				 String names[]= aqq[i].split(",");
				 for(int k = 0; k < names.length; k++){
					 staticBean sbn = new staticBean();
					 sbn.setIndex(i+1);
					 sbn.setOccupation(names[k]);
					 list.add(sbn);
					 System.out.println("names[k]==>"+(i+1)+"==>"+names[k]);
				 } 
			}  
			
			dataChangeInfo.updateFentongByOccuption(list);
			System.out.println("分桶成功，继续下一步！");
		}
	} 
	
	
	
}
