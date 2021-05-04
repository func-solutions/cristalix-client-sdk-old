package ru.cristalix.bundler;

import lombok.Getter;
import lombok.Setter;
import org.gradle.jvm.tasks.Jar;

@Getter
@Setter
public class BundlerExtension {

	private String name = "Unnamed mod";
	private String mainClass;
	private String author = "Cristalix community";
	private String version = "1.0";
	private boolean obfuscate = false;
	private boolean clientMod = false;
	private Jar task;

}