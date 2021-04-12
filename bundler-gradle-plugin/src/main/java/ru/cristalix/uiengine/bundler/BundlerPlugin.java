package ru.cristalix.uiengine.bundler;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.tasks.Jar;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proguard.gradle.ProGuardTask;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class BundlerPlugin implements Plugin<Project> {

	private final Logger logger = LoggerFactory.getLogger(BundlerPlugin.class);

	@Override
	public void apply(@NotNull Project project) {

		BundlerExtension extension = project.getExtensions().create("bundler", BundlerExtension.class);

		project.afterEvaluate(p -> {
			try {

				File modProperties = new File(project.getBuildDir(), "mod.properties");
				FileUtils.write(modProperties,
						"main=" + extension.getMainClass() + "\n" +
								"author=" + extension.getAuthor() + "\n" +
								"name=" + extension.getName() + "\n" +
								"version=" + extension.getVersion()
							   );



				ProGuardTask proGuardTask = (ProGuardTask) project.task(Collections.singletonMap("type", ProGuardTask.class), "bundle");

				proGuardTask.target("1.6");
				proGuardTask.printmapping(new File(project.getBuildDir(), "mapping.txt"));
//				proGuardTask.libraryjars("<java.home>/lib/rt.jar");

				String sunBootClassPath = System.getProperty("sun.boot.class.path");
				if (sunBootClassPath != null) try {
					proGuardTask.libraryjars(new File(Arrays.stream(sunBootClassPath.split(File.pathSeparator))
							.filter(n -> n.endsWith("rt.jar")).findFirst().get()));
				} catch (Exception exception) {
					logger.warn("Unable to retrieve path to rt.jar! Are you running JDK 9+?");
				}

				proGuardTask.useuniqueclassmembernames();
				proGuardTask.dontusemixedcaseclassnames();
				String packageName = extension.getName().replaceAll("[^A-Za-z]", "_");
				proGuardTask.flattenpackagehierarchy(packageName);
				proGuardTask.repackageclasses(packageName);
				proGuardTask.keepattributes("Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod");
				proGuardTask.keepparameternames();
				proGuardTask.adaptresourcefilecontents("**.properties,META-INF/MANIFEST.MF");
				proGuardTask.dontpreverify();

				if (!extension.isObfuscate()) proGuardTask.dontobfuscate();

				proGuardTask.keep(Collections.singletonMap("allowobfuscation", true), "class " + extension.getMainClass());
				proGuardTask.keepclassmembers("enum  * {\n" +
						"    public static **[] values();\n" +
						"    public static ** valueOf(java.lang.String);\n" +
						"}");

				proGuardTask.assumenosideeffects("public class java.lang.Thread {\n" +
						"    public static void dumpStack();\n" +
						"}");

				proGuardTask.assumenosideeffects("class kotlin.jvm.internal.Intrinsics {\n" +
						"    <methods>;\n" +
						"}");

				Jar jar = (Jar) project.getTasks().getByName("jar");

				jar.getArchiveAppendix().set("raw");

				jar.exclude("**.kotlin_metadata");
				jar.exclude("**.kotlin_builtins");

				jar.from(modProperties);

				proGuardTask.dependsOn(jar);

				File inJarFile = jar.getArchiveFile().get().getAsFile();

				File outJarFile = new File(inJarFile.getParentFile(), inJarFile.getName().replace("-raw", "-bundle"));

				proGuardTask.injars(inJarFile);
				proGuardTask.outjars(outJarFile);

				proGuardTask.libraryjars(project.getConfigurations().getByName("compileOnlyDependenciesMetadata"));

				proGuardTask.setGroup("build");

				project.getTasks().getByName("assemble").dependsOn(proGuardTask);

//				Jar bundleTask = (Jar) project.task(Collections.singletonMap("type", Jar.class), "bundle");
//				bundleTask.dependsOn(proGuardTask);
//				bundleTask.from(project.zipTree(outJarFile).matching(
//						matcher -> matcher.exclude(it -> {
//							String path = it.getRelativePath().getPathString();
//							return path.endsWith(".kotlin_metadata") ||
//									path.endsWith(".kotlin_builtins") ||
//									path.startsWith("META-INF/");
//						})));
//
//				bundleTask.getArchiveAppendix().set("bundle");
//				bundleTask.setGroup("build");
			} catch (Exception ex) {
				throw new RuntimeException(ex);

			}
		});

	}

}
