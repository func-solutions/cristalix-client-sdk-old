package ru.cristalix.uiengine.bundler;

public class BundlerExtension {

	private String name = "Unnamed mod";
	private String mainClass;
	private String author = "Cristalix community";
	private String version = "1.0";
	private boolean obfuscate = false;

	public void setName(String name) {
		this.name = name;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public String getMainClass() {
		return mainClass;
	}

	public String getAuthor() {
		return author;
	}

	public String getVersion() {
		return version;
	}

	public boolean isObfuscate() {
		return obfuscate;
	}

	public void setObfuscate(boolean obfuscate) {
		this.obfuscate = obfuscate;
	}
}
