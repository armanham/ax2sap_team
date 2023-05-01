package com.bdg.xml_json.jaxb;

public class StudentMod {

    private int rollNo;

    private String firstName;

    private String lastName;

    private String nickname;

    private int marks;

    public StudentMod() {
    }

    public StudentMod(int rollNo, String firstName, String lastName, String nickname, int marks) {
        this.rollNo = rollNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.marks = marks;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNo=" + rollNo +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", marks=" + marks +
                '}';
    }
}