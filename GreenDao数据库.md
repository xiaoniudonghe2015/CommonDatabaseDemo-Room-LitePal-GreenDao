

# 使用 GreenDao 将数据保存到本地数据库

## 特征

greenDAO的独特功能集：

- 坚如磐石：greenDAO自2011年以来一直存在，并被无数著名应用程序使用
- 超级简单：简洁明了的API，在V3中带有注释
- 小型：该库小于150K，它只是普通的Java jar（没有依赖于CPU的本机部分）
- 快速：可能是智能代码生成的驱动，是最快的Android ORM
- 安全且富有表现力的查询API：QueryBuilder使用属性常量来避免输入错误
- 灵活的属性类型：使用自定义类或枚举来表示实体中的数据
- 加密：支持SQLCipher加密数据库

#### 1. 添加依赖

 root `build.gradle`

```
buildscript {
    repositories {
        jcenter()
        mavenCentral() // add repository
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.3'
        classpath 'org.greenrobot:greendao-gradle-plugin:3.3.0' // add plugin
    }
}
```



app/build.gradle

```
apply plugin: 'com.android.application'
apply plugin: 'org.greenrobot.greendao' // apply plugin
 
dependencies {
    implementation 'org.greenrobot:greendao:3.3.0' // add library
}
```



#### 2.R8, ProGuard

```
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties { *; }

# If you DO use SQLCipher:
-keep class org.greenrobot.greendao.database.SqlCipherEncryptedHelper { *; }

# If you do NOT use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do NOT use RxJava:
-dontwarn rx.**
```



#### 3.使用

##### 1.配置

```
android {
    ···
    //greendao配置
    greendao {
        //数据库版本号，升级时修改
        schemaVersion 2
    }
}
```

**schemaVersion：**数据库架构的当前版本。

**daoPackage**：生成的DAO，DaoMaster和DaoSession的软件包名称。 *默认为源实体的程序包名称。*

**targetGenDir：**应将生成的源存储在的位置。 *默认为生成目录内生成的源文件夹*（ build/generated/source/greendao）。

**generateTests：** 设置为true以自动生成单元测试

**targetGenDirTests：** 应将生成的单元测试存储在的基本目录。默认为 *src/androidTest/java*.。

##### 2.建模型

###### 只用手写@Entity及字段属性，其余会自动生成。还会生成***Dao类，DaoSession，DaoMaster类

```
@Entity
public class GreenDaoUser {
    private Long id;

    private String name;

    private String number;

    private String area;

    public GreenDaoUser(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Generated(hash = 1771973115)
    public GreenDaoUser(Long id, String name, String number, String area) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.area = area;
    }

    @Generated(hash = 83249558)
    public GreenDaoUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
```

@Id(autoincrement = true)  设置主键自增长

*@Index*(unique = **true**)  设置唯一性

@Property(nameInDb = "custom_name")    设置数据库中的字段名



##### 3.获取Dao类

```
MyGreenDaoDbHelper myGreenDaoDbHelper = new MyGreenDaoDbHelper(DemoApplication.getApplication(), "greendao_user.db");
DaoMaster daoMaster = new DaoMaster(myGreenDaoDbHelper.getWritableDatabase());
DaoSession daoSession = daoMaster.newSession();
GreenDaoUserDao greenDaoUserDao = daoSession.getGreenDaoUserDao();
```

但是，**DevOpenHelper将在架构更改时删除所有表**（在onUpgrade（）中）。因此，我们建议您创建并**使用DaoMaster.OpenHelper的子类**。

##### 4.插入数据

```
GreenDaoUser user = new GreenDaoUser();
getGreenDaoUserDao().insert(user);
```

##### 5.删除数据

删除所有

```
getGreenDaoUserDao().deleteAll();
```

根据条件删除

```
getGreenDaoUserDao().queryBuilder().where(GreenDaoUserDao.Properties.Id.eq(whereClause)).buildDelete().executeDeleteWithoutDetachingEntities();
```

##### 6.更新数据

```
GreenDaoUser user = new GreenDaoUser();
getGreenDaoUserDao().update(user);
```

##### 7.查询数据

查询所有数据

```
getGreenDaoUserDao().queryBuilder().list();
```

根据条件查询

```
getGreenDaoUserDao().queryBuilder().where(GreenDaoUserDao.Properties.Name.eq(whereClause)).list();
```



#### 4.数据库升级

依赖

```
implementation 'io.github.yuweiguocn:GreenDaoUpgradeHelper:v2.2.1'
```

混淆

```
#greendao upgrade
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static void dropTable(org.greenrobot.greendao.database.Database, boolean);
    public static void createTable(org.greenrobot.greendao.database.Database, boolean);
}
```

代码实现

```
public class MyGreenDaoDbHelper extends DaoMaster.OpenHelper {
    public MyGreenDaoDbHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, GreenDaoUserDao.class);
    }
}
```



参考链接：https://github.com/greenrobot/greenDAO