# 使用 SQLite将数据保存到本地数据库

## 1.特征

1.轻量级
使用 SQLite 只需要带一个动态库，就可以享受它的全部功能，而且那个动态库的尺寸想当小。
2.独立性
SQLite 数据库的核心引擎不需要依赖第三方软件，也不需要所谓的“安装”。
3.隔离性
SQLite 数据库中所有的信息（比如表、视图、触发器等）都包含在一个文件夹内，方便管理和维护。
4.跨平台
SQLite 目前支持大部分操作系统，不至电脑操作系统更在众多的手机系统也是能够运行，比如：Android。
5.多语言接口
SQLite 数据库支持多语言编程接口。
6.安全性
SQLite 数据库通过数据库级上的独占性和共享锁来实现独立事务处理。这意味着多个进程可以在同一时间从同一数据库读取数据，但只能有一个可以写入数据。

## 2.简单介绍

#### 关于SQLite的数据类型：

-SQLite支持NULL、INTEGER、REAL、TEXT和BLOB数据类型

-依次代表：空值、整型值、浮点值、字符串值、二进制对象

#### **SQLiteDatabase类**

提供创建、删除、执行的SQL命令，并执行其他常见的数据库管理任务的方法

每个程序的数据库名字是唯一的

#### **SQLiteOpenHelper**

SQLiteOpenHelper是SQLiteDatabase的一个帮助类，用来管理数据库的创建和版本的更新。一般是建立一个类继承它，　并实现它的onCreate和onUpgrade方法。
　　- onCreate(SQLiteDatabase db) 
　　创建数据库时调用

　- onUpgrade(SQLiteDatabase db,int oldVersion , int newVersion) 
　　版本更新时调用

　　- getReadableDatabase()
　　创建或打开一个只读数据库

　　- getWritableDatabase()
　　创建或打开一个读写数据库

1.创建表

首先创建SQLiteOpenHelper类

```
@Override
public void onCreate(SQLiteDatabase db) {
    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + UserTable.USER_TABLE_NAME + " ("
            + UserTable.COLUMNS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + UserTable.COLUMNS_NAME + " TEXT, "
            + UserTable.COLUMNS_NUMBER + " TEXT"
            + ")";
    Log.e("->MySQLiteOpenHelper", "onCreate@41 --> " + CREATE_TABLE);
    db.execSQL(CREATE_TABLE);
}
```

2.数据库升级

```
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if (oldVersion < 2) {
        db.execSQL("ALTER TABLE user ADD COLUMN AREA TEXT");
    }
}
```

3.获取SQLiteDatabase对象

```
MySQLiteOpenHelper mySQLiteOpeMnHelper = MySQLiteOpenHelper.getInstance(context);
SQLiteDatabase sqLiteDatabase = mySQLiteOpeMnHelper.getWritableDatabase();
```

4.插入数据

```
ContentValues values = new ContentValues();
values.put("name", "ming");
long raw = sqLiteDatabase.insert(tableName, nullColumnHack, values);
```

5.删除数据

```
int raw = sqLiteDatabase.delete(table, whereClause, whereArgs);
whereClause 条件
whereArgs 条件参数
```

6.查询数据

```
Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
```

7.更新数据

```
ContentValues values = new ContentValues();
values.put("name", "ming");
int raw = sqLiteDatabase.update(table, values, whereClause, whereArgs);
```



8.游标（Cursor）
getCount()总记录条数
isFirst()判断是否第一条记录
isLast()判断是否最后一条记录
moveToFirst()移动到第一条记录
moveToLast()移动到最后一条记录
move(int offset)移动[是指偏移量而不是指移到指定位置]
moveToNext()移动到下一条记录
moveToPrevious()移动到上一条记录
getColumnIndex(String columnName)获得指定列索引的int类型值





