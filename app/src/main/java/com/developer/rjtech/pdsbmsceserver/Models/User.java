package com.developer.rjtech.pdsbmsceserver.Models;

public class User {

    private String Name;
    private String Password;
    private String phone;
    private String IsStaff;
    private String companyYearId;

    public String getPhone() {
        return phone;
    }

    public User() {
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User(String name, String password, String phone) {
        Name = name;
        Password = password;
        this.phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }


    public User(String name, String password, String phone, String isStaff, String companyYearId) {
        Name = name;
        Password = password;
        this.phone = phone;
        IsStaff = isStaff;
        this.companyYearId = companyYearId;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getCompanyYearId() {
        return companyYearId;
    }

    public void setCompanyYearId(String companyYearId) {
        this.companyYearId = companyYearId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }


}


