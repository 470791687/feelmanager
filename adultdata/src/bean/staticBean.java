package bean;

public class staticBean {

	
	

	private String occupation;  //职业
	
	private int coutnum;

	private int cluster1; //所属聚簇 
	
	private int countsump; //桶中某个聚簇中每个职业的总数
	
	private int countsumallp; //桶中某个聚簇的总数
	
	private double countnump;  //聚簇职业总数除以聚簇总数得到的P值 
	
	private int countsumq;  //职业总数
	
	private int countsumall; //所有数据总数
	
	private double countnumq; //职业总数除以数据总数得到的q值
	
	private int index;    //要查找的下标索引
	
	private int nearindex;  //靠近要查找下标索引的值
	
	public int getCountsump() {
		return countsump;
	}

	public void setCountsump(int countsump) {
		this.countsump = countsump;
	}

	public int getCountsumallp() {
		return countsumallp;
	}
 
	public int getCluster1() {
		return cluster1;
	}

	public void setCluster1(int cluster1) {
		this.cluster1 = cluster1;
	}

	public void setCountsumallp(int countsumallp) {
		this.countsumallp = countsumallp;
	}

	public double getCountnump() {
		return countnump;
	}

	public void setCountnump(double countnump) {
		this.countnump = countnump;
	}

	public int getCountsumq() {
		return countsumq;
	}

	public void setCountsumq(int countsumq) {
		this.countsumq = countsumq;
	}

	public int getCountsumall() {
		return countsumall;
	}

	public void setCountsumall(int countsumall) {
		this.countsumall = countsumall;
	}

	public double getCountnumq() {
		return countnumq;
	}

	public void setCountnumq(double countnumq) {
		this.countnumq = countnumq;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public int getCoutnum() {
		return coutnum;
	}

	public void setCoutnum(int coutnum) {
		this.coutnum = coutnum;
	}
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getNearindex() {
		return nearindex;
	}

	public void setNearindex(int nearindex) {
		this.nearindex = nearindex;
	}
}
