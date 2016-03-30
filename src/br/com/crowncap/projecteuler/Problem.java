package br.com.crowncap.projecteuler;

public class Problem {
	private int id;
	private String title;
	private String info;
	private String text;

	public Problem() {

	}

	public Problem(int id, String title, String info, String text) {
		this.id = id;
		this.title = title;
		this.info = info;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
