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