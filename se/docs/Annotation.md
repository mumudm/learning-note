## 注解|Annotation

注解（Annotation） 是 JDK5 引入的概念。
注解是放在 Java 源码的类、方法、字段、参数前的一种特殊“注释”。
注释会被编译器直接忽略，注解则可以被编译器打包进入 class 文件，注解是一种用作标注的“元数据”。

## 注解的作用
从 JVM 的角度看，注解本身对代码逻辑没有影响，如何使用全有工具决定。
Java 的注解对应使用时期可以分为三类：
1. 编译器使用的注解  
    `@Override`: 重写，编译器会检查是否重写方法  
    `@Deprecated`: 废弃，编译器会检查此方法是否废弃  
    `@SuppressWarnings`：告诉编译器忽略此处代码产生的警告。
2. 由工具处理.class文件使用的注解，像 `lombok` 就是这种类型。
3. 程序运行期能够读取的注解，加载后一直存在于 JVM 中，也是最多使用的类型。大部分情况这种注解会有 Java 代码读取来实现功能。

## 定义
Java 语言使用`@interface`语法来定义注解（Annotation）

```Java
@Target({
        ElementType.METHOD,
        ElementType.FIELD
})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TestAnnotation {
    int type() default 0;
    String level() default "info";
    String value() default "";
}
```

1. 注解的参数类似无参数方法，可以用`default`设定一个默认值（强烈推荐）。最常用的参数应当命名为`value`。
2. 必须设置`@Target`和`@Retention`  
3. `@Retention`通常设置为`RUNTIME`  

### 元注解
有一些注解可以用来修饰其他注解，就叫做元注解（meta annotation）。Java 自定义了一些元注解，通常来说，只需要使用，不需要去自定义元注解。

- @Target：注解的作用目标
- @Retention：注解的生命周期
- @Documented：注解是否应当被包含在 JavaDoc 文档中
- @Inherited：是否允许子类继承该注解

`@Target` 可以定义 Annotation 能够被应用于源码的哪些位置(可以是一个或多个，一个可以可以省略数组的写法)：
- 类或接口：`ElementType.TYPE`
- 字段：`ElementType.FIELD`             
- 方法：`ElementType.METHOD`
- 构造方法：`ElementType.CONSTRUCTOR`
- 方法参数：`ElementType.PARAMETER`

`@Retention` 定义了注解生命周期，如果该注解不存在，默认值是 `class`：
- 仅编译期：`RetentionPolicy.SOURCE`
- 仅class文件：`RetentionPolicy.CLASS`
- 运行期：`RetentionPolicy.RUNTIME`

`@Repeatable`表示这个注解是否可以在一个地方使用多次。  

`@Inherited`定义子类是否可继承父类定义的 Annotation。`@Inherited`仅对`@Target(ElementType.TYPE)`类型（类或接口）的 Annotation 起作用。

`@Documented`表示此注解会生成在 Java doc 中。

## 读取注解

Java 的注解本身对代码逻辑没有任何影响。根据`@Retention`的配置：

`SOURCE`类型的注解在编译期就被丢掉了；
`CLASS`类型的注解仅保存在 class 文件中，它们不会被加载进 JVM；
`RUNTIME`类型的注解会被加载进 JVM，并且在运行期可以被程序读取。  

如何使用注解完全由工具决定。`SOURCE`类型的注解主要由编译器使用，因此我们一般只使用，不编写。`CLASS`类型的注解主要由底层工具库使用，涉及到 class 的加载，一般我们很少用到。`RUNTIME`类型的注解不但要使用，还经常需要编写。
以下读取都是针对`RUNTIME`类型。

因为注解定义后也是一种 class，所有的注解都继承自java.lang.annotation.Annotation，因此，读取注解，需要使用反射 API。

### 常用方法
判断某个注解是否存在
```Java
Class.isAnnotationPresent(Class)// 是否存在于类上
Field.isAnnotationPresent(Class)// 是否存在于字段上
Method.isAnnotationPresent(Class)// 是否存在于方法上
Constructor.isAnnotationPresent(Class)// 是否存在于构造器上
```
读取注解

```Java
Class.getAnnotation(Class)
Field.getAnnotation(Class)
Method.getAnnotation(Class)
Constructor.getAnnotation(Class)
```


```Java
*.getParameterAnnotations()// 获取 * 上的所有注解，* 代表 @Target 的所有类型,返回值是数组
*.getAnnotation(TestAnnotation.class)// 获取 * 上的 @TestAnnotation 注解


// 示例
Annotation annotation = field.getAnnotation(TestAnnotation.class);
if(annotation instanceof TestAnnotation){
 MyAnnotation myAnnotation = (MyAnnotation) annotation;
 System.out.println("type: " + myAnnotation.type());
 System.out.println("value: " + myAnnotation.value());
}
```
