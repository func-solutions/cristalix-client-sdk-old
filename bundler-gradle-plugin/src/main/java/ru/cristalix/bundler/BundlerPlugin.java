package ru.cristalix.bundler;

import lombok.SneakyThrows;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.tasks.Jar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proguard.ParseException;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class BundlerPlugin implements Plugin<Project> {

    private final Logger logger = LoggerFactory.getLogger(BundlerPlugin.class);

    @Override
    @SneakyThrows
    public void apply(Project project) {

        BundlerTask proGuardTask = (BundlerTask) project.task(Collections.singletonMap("type", BundlerTask.class), "bundle");
        GenerateModProperitesTask properitesTask = (GenerateModProperitesTask) project.task(Collections.singletonMap("type", GenerateModProperitesTask.class), "generateModProperties");
        proGuardTask.setProperitesTask(properitesTask);

        Jar jar = proGuardTask.getSourceJarTask() != null ? proGuardTask.getSourceJarTask() : (Jar) project.getTasks().getByName("jar");
        jar.dependsOn(properitesTask);
        jar.from(properitesTask.getPropertiesFile());

        proGuardTask.target("1.6");

        proGuardTask.printmapping(new File(project.getBuildDir(), "mapping.txt"));

        String sunBootClassPath = System.getProperty("sun.boot.class.path");
        if (sunBootClassPath != null) try {
            proGuardTask.libraryjars(new File(Arrays.stream(sunBootClassPath.split(File.pathSeparator))
                    .filter(n -> n.endsWith("rt.jar")).findFirst().get()));
        } catch (Exception exception) {
            logger.warn("Unable to retrieve path to rt.jar! Are you running JDK 9+?");
        }

        proGuardTask.useuniqueclassmembernames();
        proGuardTask.dontusemixedcaseclassnames();
        String packageName = properitesTask.getModName().replaceAll("[^A-Za-z]", "_");
        proGuardTask.flattenpackagehierarchy(packageName);
        proGuardTask.repackageclasses(packageName);
        proGuardTask.keepattributes("Signature,SourceFile,LineNumberTable,*Annotation*");
//                proGuardTask.keepparameternames();
        proGuardTask.adaptresourcefilecontents("**.properties,META-INF/MANIFEST.MF");
        proGuardTask.dontpreverify();
        proGuardTask.renamesourcefileattribute("SourceFile");

        if (!proGuardTask.isObfuscate()) proGuardTask.dontobfuscate();

        proGuardTask.keepclassmembers("enum  * {\n" +
                "    public static **[] values();\n" +
                "    public static ** valueOf(java.lang.String);\n" +
                "}");

        proGuardTask.getActions().add(0, (taskIgnored) -> {
            try {
                proGuardTask.keep(Collections.singletonMap("allowobfuscation", true), "class " + proGuardTask.getMainClass());
                proGuardTask.keep(Collections.singletonMap("allowobfuscation", true), "class " + proGuardTask.getMainClass() + "{\n" +
                        "    public void load(dev.xdark.clientapi.ClientApi);\n" +
                        "    public void unload();\n" +
                        "}");

                proGuardTask.libraryjars(project.getConfigurations().getByName("compileClasspath").minus(project.getConfigurations().getByName("runtimeClasspath")));

            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        });

        proGuardTask.assumevalues("class ru.cristalix.clientapi.JavaMod {" +
                "public static boolean isClientMod() return " + proGuardTask.isClientMod() + ";" +
                "}");

        proGuardTask.assumenosideeffects("class ru.cristalix.clientapi.JavaMod {" +
                "public static boolean isClientMod();" +
                "}");

        proGuardTask.assumenosideeffects("public class java.lang.Thread {\n" +
                "    public static void dumpStack();\n" +
                "}");

        proGuardTask.assumenosideeffects("class kotlin.jvm.internal.Intrinsics {\n" +
                "    <methods>;\n" +
                "}");
//
        proGuardTask.assumenosideeffects("class kotlin.jvm.internal.Reflection {\n" +
                "    <methods>;\n" +
                "}");

        jar.getArchiveAppendix().set("raw");

        File inJarFile = jar.getArchiveFile().get().getAsFile();
        File outJarFile = new File(inJarFile.getParentFile(), inJarFile.getName().replace("-raw", "-bundle"));

        proGuardTask.injars(inJarFile);
        proGuardTask.outjars(outJarFile);
        proGuardTask.getInputs().file(inJarFile);
        proGuardTask.getOutputs().file(outJarFile);

        jar.exclude("**/*.kotlin_metadata");
        jar.exclude("**/*.kotlin_builtins");

        proGuardTask.dependsOn(jar);
        proGuardTask.setGroup("build");

        project.getTasks().getByName("assemble").dependsOn(proGuardTask);

    }

}
