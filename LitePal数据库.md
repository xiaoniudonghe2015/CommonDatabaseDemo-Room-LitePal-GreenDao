# 使用 LitePal 将数据保存到本地数据库

## 特征

- 使用对象关系映射（ORM）模式。
- 几乎零配置（只有一个配置文件，几乎没有属性）。
- 自动维护所有表（例如，创建，更改或删除表）。
- 支持多数据库。
- 避免编写SQL语句的封装API。
- 出色的流利查询API。
- 仍然可以使用SQL的替代选择，但比原始API更轻松，更好。



#### 1. 添加依赖

```
dependencies {
    implementation 'org.litepal.guolindev:core:3.2.2'
}
```



#### 2.配置litepal.xml

在项目的**assets**文件夹中创建一个文件，并将其命名为**litepal.xml**。然后将以下代码复制到其中。

```
<?xml version="1.0" encoding="utf-8"?>
<litepal>
 		<!--定义应用程序的数据库名称。默认情况下，每个数据库名称都应以.db结尾。如果您未使用.db命名数据库，LitePal将自动为您添加后缀。-->
    <dbname value="demo" />

    <!--定义数据库的版本。每次您想要升级数据库时，版本标记都会有所帮助。修改您在映射标记中定义的模型，只需
将版本值加1，即可自动处理数据库升级-->
    <version value="1" />

    <!--在具有映射标记的列表中定义模型，LitePal将为每个映射类创建表。模型中定义的受支持字段将映射到列中-->
    <list>
    		<mapping class="com.test.model.Reader" />
    		<mapping class="com.test.model.Magazine" />
    </list>
    
    <!--定义.db文件的位置。“internal”是指.db文件将存储在任何人都无法访问的内部存储器的数据库文件夹中。“external”是指.db文件将存储在主外部存储设备上目录的路径中，应用程序可以在该目录中放置自己拥有的持久文件，每个人都可以访问该文件。“internal” 将作为默认值。
        <storage value="external" />
    -->
</litepal>
```

- **dbname**配置项目的数据库名称。

- **version**配置数据库的版本。每次您要升级数据库时，请在此处加上值。

- **列表**配置映射类。

- **storage**配置应在何处**存储**数据库文件。**内部**和**外部**是唯一有效的选项。

  

#### 3.在您自己的应用程序中调用**LitePal.initialize（context）**：

```
public class MyOwnApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
    ...
}
```

#### 4.ProGuard

```
-keep class org.litepal.** {
    *;
}

-keep class * extends org.litepal.crud.DataSupport {
    *;
}

-keep class * extends org.litepal.crud.LitePalSupport {
    *;
}
```



#### 5.使用

1.assets添加litepal.xml

```
<?xml version="1.0" encoding="utf-8"?>
<litepal>
    <dbname value="litepal_user"/>
    <version value="1"/>
    <list>
        <mapping class="com.example.commondatabasedemo.litepal.LitePalUser"/>
    </list>

    <storage value="internal"/>

</litepal>
```

2.定义模型

```
public class LitePalUser extends LitePalSupport {
    public String name;
    public String number;
    public String area;

    public LitePalUser(String name, String number) {
        this.name = name;
        this.number = number;
    }
}
```

​		@Column(unique = true) 设置唯一性

​        @Column(ignore = true) 忽略某个属性

3.升级数据库，

修改数据库模型，litepal.xml修改版本号即可

但是有一些LitePal无法处理的升级条件，并且将清除升级表中的所有数据：

- 添加一个注释为的字段`unique = true`。
- 将字段的注释更改为`unique = true`。
- 将字段的注释更改为`nullable = false`。

4.插入数据

```
LitePalUse user = new LitePalUser();
user.save();//  or  saveOrUpdate
```

5.删除数据

```
LitePal.deleteAll(LitePalUser.class, "name = ?", user.name);
```

6.更新数据

```
LitePalUse user = new LitePalUser();
user.saveOrUpdate("name =?", user.name);
```

or

```
LitePalUse user = new LitePalUser();
user.updateAll("name =?", user.name);//注意LitePalUse需要有默认的构造器
```

7.查询数据

```
List<LitePalUser> litePalUsers = LitePal.where("name = ?", name).find(LitePalUser.class);
```

```
List<LitePalUser> litePalUsers = LitePal.findAll(LitePalUser.class);
```

8.事务

```
LitePal.beginTransaction();
boolean result1 = // db operation1
boolean result2 = // db operation2
boolean result3 = // db operation3
if (result1 && result2 && result3) {
    LitePal.setTransactionSuccessful();
}
LitePal.endTransaction();
```



参考链接：https://github.com/guolindev/LitePal