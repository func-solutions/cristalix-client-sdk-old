package ru.cristalix.bundler;

public interface ModProperties {

    String getModName();

    String getMainClass();

    String getAuthor();

    String getModVersion();

    void setModName(String modName);

    void setMainClass(String mainClass);

    void setAuthor(String author);

    void setModVersion(String modVersion);

}
