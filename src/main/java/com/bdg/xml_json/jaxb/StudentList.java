package com.bdg.xml_json.jaxb;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "StudentList")
@XmlType(propOrder = {"students"})
@XmlAccessorType(XmlAccessType.FIELD)
public class StudentList {

    @XmlElement
    @XmlElementWrapper
    private List<StudentMod> students = new ArrayList<>();

    public StudentList(List<StudentMod> students) {
        this.students = students;
    }

    public StudentList() {
    }

    public List<StudentMod> getStudents() {
        return students;
    }

    public void setStudents(List<StudentMod> students) {
        this.students = students;
    }
}