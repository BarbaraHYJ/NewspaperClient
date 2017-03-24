package com.example.barbara.newspapersub.Model;

import java.util.Collection;

/**
 * Created by barbara on 2016/12/16.
 */
public class DepartmentEntity {
    private int dId;
    private String dName;
    private Collection<UsersEntity> usersesByDId;

    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepartmentEntity that = (DepartmentEntity) o;

        if (dId != that.dId) return false;
        if (dName != null ? !dName.equals(that.dName) : that.dName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dId;
        result = 31 * result + (dName != null ? dName.hashCode() : 0);
        return result;
    }

    public Collection<UsersEntity> getUsersesByDId() {
        return usersesByDId;
    }

    public void setUsersesByDId(Collection<UsersEntity> usersesByDId) {
        this.usersesByDId = usersesByDId;
    }
}

