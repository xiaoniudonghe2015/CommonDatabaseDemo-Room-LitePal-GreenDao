package com.example.commondatabasedemo.greendao;

import com.example.commondatabasedemo.DemoApplication;


import java.util.List;

public class GreenDaoManager {

    private MyGreenDaoDbHelper myGreenDaoDbHelper;
    private DaoMaster daoMaster; //用于创建数据库以及获取DaoSession
    private DaoSession daoSession;//用于获取各个表对应的Dao类

    private static class GreenDaoManagerHolder {
        private static final GreenDaoManager greenDaoManager = new GreenDaoManager();
    }

    public static GreenDaoManager getInstance() {
        return GreenDaoManagerHolder.greenDaoManager;
    }

    public GreenDaoManager() {
        initDb();
    }

    private void initDb() {
        myGreenDaoDbHelper = new MyGreenDaoDbHelper(DemoApplication.getApplication(), "greendao_user.db");
        daoMaster = new DaoMaster(myGreenDaoDbHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public GreenDaoUserDao getGreenDaoUserDao() {
        return daoSession.getGreenDaoUserDao();
    }

    public void insertUsers(List<GreenDaoUser> list) {
        if (list != null) {
            for (GreenDaoUser user : list) {
                getGreenDaoUserDao().insert(user);
            }
        }
    }

    public void deleteAllUsers() {
        getGreenDaoUserDao().deleteAll();
    }

    public void deleteUsersById(int whereClause) {
        getGreenDaoUserDao().queryBuilder().where(GreenDaoUserDao.Properties.Id.eq(whereClause)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void updateUsers(List<GreenDaoUser> list) {
        if (list != null) {
            for (GreenDaoUser user : list) {
                getGreenDaoUserDao().update(user);
            }
        }
    }

    public List<GreenDaoUser> findAllUsers() {
        return getGreenDaoUserDao().queryBuilder().list();
    }

    public List<GreenDaoUser> findUserByName(String whereClause) {
        return getGreenDaoUserDao().queryBuilder().where(GreenDaoUserDao.Properties.Name.eq(whereClause)).list();
    }
}
