package ru.cristalix.bundler;

import lombok.Getter;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

@Setter
@Getter
public class GenerateModProperitesTask extends DefaultTask implements ModProperties {

    @Input
    private String modName = "Unnamed mod";

    @Input
    private String mainClass;

    @Input
    private String author = "Cristalix community";

    @Input
    private String modVersion = "1.0";

    @OutputFile
    private File propertiesFile = new File(getProject().getBuildDir(), "mod.properties");;

    @TaskAction
    public void generateModProperties() {
        try {
            File buildDir = getProject().getBuildDir();
            if (!buildDir.isDirectory()) {
                buildDir.mkdirs();
            }

            Files.write(propertiesFile.toPath(), Collections.singletonList(
                    "main=" + getMainClass() + "\n" +
                            "author=" + getAuthor() + "\n" +
                            "name=" + getModName() + "\n" +
                            "version=" + getModVersion()
            ), StandardCharsets.UTF_8);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
