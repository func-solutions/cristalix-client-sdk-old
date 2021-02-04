-injars usage-example\build\libs\usage-example.jar
-outjars usage-example\build\libs\usage-example-shrinked.jar

-libraryjars 'C:\Program Files\Java\jre1.8.0_161\lib\rt.jar'
-libraryjars 'C:\projects\cristalix\cristalix-client-api-java\build\libs\clientapi-1.0.1.jar'
-libraryjars build\libs\lwjgl-2.9.2.jar
-libraryjars build\libs\lwjgl-platform-2.9.2-natives-windows.jar
-libraryjars build\libs\lwjgl_util-2.9.2.jar
-libraryjars build\libs\lwjgl_util_applet-2.9.3.jar

-target 1.6
-printmapping mapping.txt
-useuniqueclassmembernames
-dontusemixedcaseclassnames
-flattenpackagehierarchy ''
-repackageclasses ''
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-keepparameternames
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF
-dontpreverify



-keep class ru.cristalix.uiengine.example.ExampleMod

# Keep - Applications. Keep all application classes, along with their 'main' methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Also keep - Serialization code. Keep all fields and methods that are used for
# serialization.
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep - Native method names. Keep all native class/method names.
-keepclasseswithmembers,includedescriptorclasses,allowshrinking class * {
    native <methods>;
}

# Remove debugging - Thread_dumpStack calls. Remove all invocations of
# Thread.dumpStack().
-assumenosideeffects public class java.lang.Thread {
    public static void dumpStack();
}

-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    <methods>;
}
