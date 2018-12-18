package db;

import java.util.List;

public class Patient {
	private String id;
	private List<String> diagSeq;

	public Patient() {
		super();
	}

	public Patient(String id) {
		super();
		this.id = id;
	}

	public Patient(String id, List<String> diagSeq) {
		super();
		this.id = id;
		this.diagSeq = diagSeq;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getDiagSeq() {
		return diagSeq;
	}

	public void setDiagSeq(List<String> diagSeq) {
		this.diagSeq = diagSeq;
	}

	@Override
	public String toString() {
		return "Patient [id=" + id + ", diagSeq=" + diagSeq + "]";
	}

}
