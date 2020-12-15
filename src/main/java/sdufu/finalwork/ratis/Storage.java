package sdufu.finalwork.ratis;

public enum Storage {
	DIR(System.getProperty("user.dir") + "/bd/sdufu/finalwork/tmp");

	public String value;

	Storage(String value) {
		this.value = value;
	}
}
