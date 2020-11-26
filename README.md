# CommonDatabaseDemo-Room-LitePal-GreenDao

#SQLite,GreenDao,Litepal,Room数据库性能对比测试

### 1.SQLite,GreenDao,Litepal,Room插入10000条数据速度对比如下：

<img src="插入10000条数据速度对比.jpg"  width=200px height=400px  alt="插入10000条数据速度对比" style="zoom:20%;" />

Room默认开启事务模式。如果一个一个插入的话，大概三四十秒

### 2.SQLite,GreenDao,Litepal,Room查询10000条数据速度对比如下：

<img src="查询10000条数据速度对比.jpg" width=200px height=400px  alt="查询10000条数据速度对比" style="zoom:20%;" />

### 3.SQLite,GreenDao,Litepal,Room开始事务插入10000条数据速度对比如下：



<img src="开启事务后插入10000条数据速度对比.jpg"  width=200px height=400px  alt="开启事务后插入10000条数据速度对比" style="zoom:20%;" />

### 4.开启事务插入速度变快的原因：

sqlite是比较轻量级的数据库，sqlite默认在插入数据的时候默认一条语句就是一个事务，有多少条数据就有多少次磁盘操作。一次插入10000条记录也就是要10000次读写磁盘操作，导致速度慢。



# 使用 Room 将数据保存到本地数据库

Room 在 SQLite 上提供了一个抽象层，以便在充分利用 SQLite 的强大功能的同时，能够流畅地访问数据库。

处理大量结构化数据的应用可极大地受益于在本地保留这些数据。最常见的用例是缓存相关数据。这样，当设备无法访问网络时，用户仍可在离线状态下浏览相应内容。设备重新连接到网络后，用户发起的所有内容更改都会同步到服务器。

由于 Room 负责为您处理这些问题，因此我们**强烈建议**您使用 Room（而不是 SQLite）。

如需在应用中使用 Room，请将以下依赖项添加到应用的 `build.gradle` 文件。

```groovy
dependencies {
  def room_version = "2.2.5"

  implementation "androidx.room:room-runtime:$room_version"
  annotationProcessor "androidx.room:room-compiler:$room_version"

  // optional - RxJava support for Room
  implementation "androidx.room:room-rxjava2:$room_version"

  // optional - Guava support for Room, including Optional and ListenableFuture
  implementation "androidx.room:room-guava:$room_version"

  // optional - Test helpers
  testImplementation "androidx.room:room-testing:$room_version"
}
```

Room 包含 3 个主要组件：

- [**数据库**](https://developer.android.google.cn/reference/androidx/room/Database)：包含数据库持有者，并作为应用已保留的持久关系型数据的底层连接的主要接入点。

  使用 [`@Database`](https://developer.android.google.cn/reference/androidx/room/Database) 注释的类应满足以下条件：

  - 是扩展 [`RoomDatabase`](https://developer.android.google.cn/reference/androidx/room/RoomDatabase) 的抽象类。
  - 在注释中添加与数据库关联的实体列表。
  - 包含具有 0 个参数且返回使用 [`@Dao`](https://developer.android.google.cn/reference/androidx/room/Dao) 注释的类的抽象方法。

  在运行时，您可以通过调用 [`Room.databaseBuilder()`](https://developer.android.google.cn/reference/androidx/room/Room#databaseBuilder(android.content.Context, java.lang.Class, java.lang.String)) 或 [`Room.inMemoryDatabaseBuilder()`](https://developer.android.google.cn/reference/androidx/room/Room#inMemoryDatabaseBuilder(android.content.Context, java.lang.Class)) 获取 [`Database`](https://developer.android.google.cn/reference/androidx/room/Database) 的实例。

- [**Entity**](https://developer.android.google.cn/training/data-storage/room/defining-data)：表示数据库中的表。

- [**DAO**](https://developer.android.google.cn/training/data-storage/room/accessing-data)：包含用于访问数据库的方法。

应用使用 Room 数据库来获取与该数据库关联的数据访问对象 (DAO)。然后，应用使用每个 DAO 从数据库中获取实体，然后再将对这些实体的所有更改保存回数据库中。 最后，应用使用实体来获取和设置与数据库中的表列相对应的值。

Room 不同组件之间的关系如图 1 所示：

![img](https://developer.android.google.cn/images/training/data-storage/room_architecture.png)

**图 1.**Room 架构图

RoomUser

```java
//@Entity(primaryKeys = {"firstName", "lastName"})  //复合主键
//@Entity(tableName = "users") //表名, SQLite中的表名称不区分大小写。
@Entity
public class RoomUser {
    @PrimaryKey(autoGenerate = true) //主键 自增
    public int id;
    //@ColumnInfo(name = "name")  //列名称
    public String name;
}
```

RoomUserDao

```java
@Dao
public interface RoomUserDao {
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    void insertUsers(RoomUser... users);

    @Update
    void updateUsers(RoomUser... users);

    @Delete
    void deleteUsers(RoomUser... users);

    @Query("DELETE FROM ROOMUSER")
    void deleteAllUsers();

    @Query("SELECT * FROM ROOMUSER ORDER BY ID DESC")
    LiveData<List<RoomUser>> findAllUSerInfo();


    @Query("SELECT * FROM ROOMUSER WHERE id IN (:userIds)")
    LiveData<List<RoomUser>> findAllUSerInfo(int[] userIds);

    @Query("SELECT * FROM ROOMUSER WHERE name LIKE :name")
    RoomUser findUserByName(String name);
}

```

RoomUserDatabase

```java
    @Database(entities = {RoomUser.class}, version = 2)
public abstract class RoomUserDatabase extends RoomDatabase {
    public abstract RoomUserDao getRoomUserDao();

    private static RoomUserDatabase INSTANCE;

    static RoomUserDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (RoomUserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RoomUserDatabase.class, "room_user")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE roomuser ADD COLUMN area TEXT");
        }
    };
}

```

**注意**：如果您的应用在单个进程中运行，在实例化 `AppDatabase` 对象时应遵循单例设计模式。每个 [`RoomDatabase`](https://developer.android.google.cn/reference/androidx/room/RoomDatabase) 实例的成本相当高，而您几乎不需要在单个进程中访问多个实例。

如果您的应用在多个进程中运行，请在数据库构建器调用中包含 `enableMultiInstanceInvalidation()`。这样，如果您在每个进程中都有一个 `AppDatabase` 实例，可以在一个进程中使共享数据库文件失效，并且这种失效会自动传播到其他进程中 `AppDatabase` 的实例。



Room数据库操作不能放在主线程中，否则会报以下错误：

```
  java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.
        at androidx.room.RoomDatabase.assertNotMainThread(RoomDatabase.java:267)
        at androidx.room.RoomDatabase.query(RoomDatabase.java:323)
        at androidx.room.util.DBUtil.query(DBUtil.java:83)
```

可以这样，创建AppExecutors类

```
public class AppExecutors {

    private final Executor mDiskIO;

    private final Executor mNetworkIO;

    private final Executor mMainThread;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.mDiskIO = diskIO;
        this.mNetworkIO = networkIO;
        this.mMainThread = mainThread;
    }

    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
```

然后在Application初始化：

```
public class DemoApplication extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();
    }
    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }
}
```

这样就可以执行Room数据库操作了

```
AppExecutors executors = DemoApplication.getApplication().getAppExecutors();
executors.diskIO().execute(() -> {
    执行增删改查操作
});
```



Room官网链接https://developer.android.google.cn/training/data-storage/room





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








