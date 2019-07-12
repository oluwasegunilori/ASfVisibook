package com.example.asfvisibook.Model;

public class Excos {
    private String name,sex,dateofbirth, phoneno , email,faculty, department, address, part, post;

    public Excos() {
    }

    public Excos(String name, String sex, String dateofbirth, String phoneno, String email, String faculty, String department, String address, String part, String post) {
        this.name = name;
        this.sex = sex;
        this.dateofbirth = dateofbirth;
        this.phoneno = phoneno;
        this.email = email;
        this.faculty = faculty;
        this.department = department;
        this.address = address;
        this.part = part;
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
