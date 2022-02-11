package ru.cristalix.bundler;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.gradle.api.tasks.Input;
import org.gradle.jvm.tasks.Jar;
import proguard.gradle.ProGuardTask;

@Setter
@Getter
public class BundlerTask extends ProGuardTask {

    @Input
    private boolean obfuscate = false;

    @Input
    private boolean clientMod = false;

    private Jar sourceJarTask;

    @Delegate(types={ModProperties.class})
    private GenerateModProperitesTask properitesTask;



}
