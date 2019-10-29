# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 指定代码的压缩级别
-optimizationpasses 5

-ignorewarnings # 抑制警告
-verbose    # 混淆时是否记录日志（混淆后生产映射文件 map 类名 -> 转化后类名的映射
-dontpreverify # 不预校验
-dontoptimize #不优化输入的类文件
-dontshrink #该选项 表示 不启用压缩  混淆时是否做预校验（可去掉加快混淆速度）

-dontusemixedcaseclassnames # 混淆时不会产生形形色色的类名  是否使用大小写混合
-dontskipnonpubliclibraryclasses #不跳过(混淆) jars中的 非public classes   默认选项

-keepattributes Exceptions # 解决AGPBI警告
-keepattributes Exceptions,InnerClasses,...
-keepattributes EnclosingMethod
-keepattributes SourceFile,LineNumberTable #运行抛出异常时保留代码行号
#过滤泛型
-keepattributes Signature
#过滤注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }


#继承自activity,application,service,broadcastReceiver,contentprovider....不进行混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.view.View
-keep class android.support.** {*;}

# keep setters in Views so that animations can still work.
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# 所有View的子类及其子类的get、set方法都不进行混淆
-keep public class * extends android.view.View {  #保持自定义控件指定规则的方法不被混淆
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保持指定规则的方法不被混淆（Android layout 布局文件中为控件配置的onClick方法不能混淆）
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(*Event);
}

 #保持R文件不被混淆，否则，你的反射是获取不到资源id的
-keep class **.R$* { *; }
# 不混淆R类里及其所有内部static类中的所有static变量字段，$是用来分割内嵌类与其母体的标志
-keep public class **.R$*{
   public static final int *;
}
-keepclassmembers class **.R$* {
	public static <fields>;
}

#过滤js
-keepattributes *JavascriptInterface*
#保护WebView对HTML页面的API不被混淆
-keep class **.Webview2JsInterface { *; }
-keepclassmembers class * extends android.webkit.WebViewClient {  #如果你的项目中用到了webview的复杂操作 ，最好加入
     public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
     public boolean *(android.webkit.WebView,java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebChromeClient {  #如果你的项目中用到了webview的复杂操作 ，最好加入
     public void *(android.webkit.WebView,java.lang.String);
}
#对WebView的简单说明下：经过实战检验,做腾讯QQ登录，如果引用他们提供的jar，若不加防止WebChromeClient混淆的代码，oauth认证无法回调，
#反编译基代码后可看到他们有用到WebChromeClient，加入此代码即可。

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);  #保持自定义控件类不被混淆，指定格式的构造方法不去混淆
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#-keep public class * {
#    public protected *;
#}

#需要序列化和反序列化的类不能被混淆（注：Java反射用到的类也不能被混淆）
-keep public class * implements java.io.Serializable {
        public *;
}

#不混淆Serializable接口的子类中指定的某些成员变量和方法
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep class org.** {*;}
-keep class com.android.**{*;}
#-assumenosideeffects class_specification

#v7 不混淆
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-dontwarn android.support.v7.**

# support-design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# support-v7-appcompat
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

# support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-keep class android.support.v7.widget.RoundRectDrawable { *; }

-dontwarn android.net.http.**
-keep class org.apache.http.** { *;}

#被注解的类不混淆
-keepclasseswithmembers class * {
@com.zcx.fast_permission_runtime.annotation.* <methods>;
}

-keepclassmembers class * {
public android.content.Context getContext();
}
