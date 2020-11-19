package com.example.commondatabasedemo.litepal;

import android.text.TextUtils;
import android.util.Log;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class LitePalUserManager {
    static LitePalUserManager litePalUserManager;

    public static synchronized LitePalUserManager getInstance() {
        if (litePalUserManager == null) {
            litePalUserManager = new LitePalUserManager();
        }
        return litePalUserManager;
    }

    public void insertUsers(List<LitePalUser> list) {
        try {
            if (list != null) {
                for (LitePalUser user : list) {
                    user.save();
                }
            }
        } catch (Exception e) {

        }
    }

    public void deleteUsers(List<LitePalUser> list) {
        try {
            if (list != null) {
                for (LitePalUser user : list) {
                    LitePal.deleteAll(LitePalUser.class, "name = ?", user.name);
                }
            }
        } catch (Exception e) {

        }
    }

    public void deleteAllUsers() {
        try {
            LitePal.deleteAll(LitePalUser.class);
        } catch (Exception e) {

        }
    }

    public void updateUsersByName(List<LitePalUser> list) {
        try {
            if (list != null) {
                for (LitePalUser user : list) {
                    user.updateAll("name =?", user.name);
                }
            }
        } catch (Exception e) {

        }
    }

    public List<LitePalUser> findUsersByName(String name) {
        try {
            List<LitePalUser> litePalUsers = LitePal.where("name = ?", name).find(LitePalUser.class);
            return litePalUsers;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<LitePalUser> findAllUsers() {
        try {
            List<LitePalUser> litePalUsers = LitePal.findAll(LitePalUser.class);
            return litePalUsers;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
