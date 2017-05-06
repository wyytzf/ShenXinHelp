package com.xd.shenxinhelp.model;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class Contact implements Comparable<Contact> {
    private String name;
    private String tel;
    private String isAdded; //是否已经添加,0未添加，1已添加

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(String isAdded) {
        this.isAdded = isAdded;
    }

    @Override
    public int compareTo(@NonNull Contact o) {
        if(this.getName().compareToIgnoreCase(o.getName())==1){
            return 1;
        }else  if(this.getName().compareToIgnoreCase(o.getName())==-1){
            return -1;
        }else
           return 0;
    }
}
