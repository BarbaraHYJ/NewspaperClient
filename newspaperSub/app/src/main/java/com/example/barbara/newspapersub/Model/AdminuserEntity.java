package com.example.barbara.newspapersub.Model;

/**
 * Created by barbara on 2016/12/26.
 */
public class AdminuserEntity {

    private String aName;
    private String aPassword;

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaPassword() {
        return aPassword;
    }

    public void setaPassword(String aPassword) {
        this.aPassword = aPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminuserEntity that = (AdminuserEntity) o;

        if (aName != null ? !aName.equals(that.aName) : that.aName != null) return false;
        if (aPassword != null ? !aPassword.equals(that.aPassword) : that.aPassword != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aName != null ? aName.hashCode() : 0;
        result = 31 * result + (aPassword != null ? aPassword.hashCode() : 0);
        return result;
    }
}
