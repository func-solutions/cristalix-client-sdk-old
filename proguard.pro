
-target 1.6
-printmapping mapping.txt
-libraryjars  <java.home>/lib/rt.jar
-useuniqueclassmembernames
-dontusemixedcaseclassnames
-flattenpackagehierarchy 'hello'
-repackageclasses 'hello'
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-keepparameternames
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF
-dontpreverify

-keep,allowobfuscation class ru.cristalix.uiengine.example.ExampleMod
-keep,allowobfuscation class ru.cristalix.uiengine.UIEngine

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
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
