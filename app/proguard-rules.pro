#
#-optimizationpasses 5
#
##保护注解
#-keepattributes *Annotation*
#
## 混淆时不会产生形形色色的类名
#-dontusemixedcaseclassnames
#
## 指定不去忽略非公共的库类
#-dontskipnonpubliclibraryclasses
#
## 不预校验
## -dontpreverify
#
## 预校验
#-dontoptimize
#
## 这1句是屏蔽警告
##-ignorewarnings
#-verbose
#
## 优化
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
## 这1句是导入第三方的类库，防止混淆时候读取包内容出错
##-libraryjars libs/MobTools-2017.0321.1624.jar
##-libraryjars libs/MobCommons-2017.0321.1624.jar
##-libraryjars libs/Android_Map3D_SDK_V5.0.0_20170311.jar
##-libraryjars libs/AMap_Search_V5.0.0_20170309.jar
##-libraryjars libs/AMap_Location_V3.4.0_20170427.jar
##-libraryjars libs/SMSSDK-2.1.4.aar
#
## 去掉警告
#-dontwarn
#-dontskipnonpubliclibraryclassmembers
#
## 不进行混淆保持原样
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Fragment
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService
#
## 过滤R文件的混淆：
#-keep class **.R$* {*;}
#
## 过滤第三方包的混淆：其中packagename为第三方包的包名
#-keep class com.jiechic.library.** {*;}
#-keep class com.squareup.okhttp3.** {*;}
#-keep class com.google.code.gson.** {*;}
#-keep class com.facebook.fresco.** {*;}
#-keep class de.greenrobot.** {*;}
#-keep class com.cjj.materialrefeshlayout.** {*;}
#-keep class de.hdodenhof.** {*;}
#-keep class com.github.d-max.** {*;}
#
## 所有方法不进行混淆
#-keep public abstract interface com.huawei.android.airsharing.listener{
#public protected <methods>;
#}
#
#-keep public abstract interface com.huawei.android.airsharing.api{
#public protected <methods>;
#}
#
## 对该方法不进行混淆
## -keep public class com.asqw.android{
## public void Start(java.lang.String);
## }
#
## 保护指定的类和类的成员的名称，如果所有指定的类成员出席(在压缩步骤之后)
##-keepclasseswithmembernames class * {
##    native <methods>;
##}
#
## 保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在
##-keepclasseswithmembernames class * {
##    public <init>(android.content.Context, android.util.AttributeSet);
##}
#
#-keepclasseswithmembernames class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
## 保护指定类的成员，如果此类受到保护他们会保护的更好
#-keepclassmembers class * extends android.app.Activity {
#public void *(android.view.View);
#}
#
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
## 保护指定的类文件和类的成员
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
##-libraryjars libs/android-support-v4.jar
##-dontwarn android.support.v4.**{*;}
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#
##不混淆第三方包中的指定内容
#-keep class android-support-v4.**{*;}
#-keep public class * extends android.support.v4.**
#-keep class android.view.**{*;}
#
## 保留Serializable序列化的类不被混淆
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    !static !transient <fields>;
#    !private <fields>;
#    !private <methods>;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
## Facebook
##fresco
#-keep class com.facebook.** {*;}
#-dontwarn okio.**
#-dontwarn com.squareup.okhttp.**
#-dontwarn okhttp3.**
#-dontwarn javax.annotation.**
#-dontwarn com.android.volley.toolbox.**
#-dontwarn com.facebook.**
#-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
#
## Do not strip any method/class that is annotated with @DoNotStrip
#-keep @com.facebook.common.internal.DoNotStrip class *
#-keepclassmembers class * {
#    @com.facebook.common.internal.DoNotStrip *;
#}
## Works around a bug in the animated GIF module which will be fixed in 0.12.0
#-keep class com.facebook.imagepipeline.animated.factory.AnimatedFactoryImpl {
#    public AnimatedFactoryImpl(com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory,com.facebook.imagepipeline.core.ExecutorSupplier);
#}
#
#
#-dontwarn com.amap.**
#-keep class com.amap.api.**{*;}
#-keep class com.autonavi.**{*;}
#-keep class com.loc.**{*;}
#
##okhttp
#-keep class com.hbss.http.okhttp.**{*;}
#-keep class okio.**{*;}
#-keep class com.squareup.okhttp.**{*;}
#-keep class okhttp3.**{*;}
#-keep class javax.annotation.**{*;}
##-keep class com.android.volley.toolbox.**{*;}
#
##gson
#-keep class com.google.**{*;}
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
#
###---------------Begin: proguard configuration for Gson  ----------
## Gson uses generic type information stored in a class file when working with fields. Proguard
## removes such information by default, so configure it to keep all of it.
#-keepattributes Signature
## Gson specific classes
#-keep class sun.misc.Unsafe { *; }
##-keep class com.google.gson.stream.** { *; }
## Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }  ##这里需要改成解析到哪个  javabean
#-keep class com.example.bookadmin.bean.** {
#    <fields>;
#    <methods>;
#}
#-keep class com.example.bookadmin.tools.deserializer.** {
#    <fields>;
#    <methods>;
#}
#-keep class com.example.bookadmin.fragment.** {
#    <fields>;
#    <methods>;
#}
#-keep class com.tencent.**{*;}
#-dontwarn com.tencent.**
#
#-keep class tencent.**{*;}
#-dontwarn tencent.**
#
#-keep class qalsdk.**{*;}
#-dontwarn qalsdk.**