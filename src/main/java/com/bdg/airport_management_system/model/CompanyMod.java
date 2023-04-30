package com.bdg.airport_management_system.model;

import com.bdg.airport_management_system.model.common.BaseMod;

import java.sql.Date;
import java.util.Objects;

import static com.bdg.airport_management_system.validator.Validator.checkNull;
import static com.bdg.airport_management_system.validator.Validator.validateString;


public class CompanyMod extends BaseMod {

    private String name;
    private Date foundDate;

    public CompanyMod(final String name, final Date foundDate) {
        setName(name);
        setFoundDate(foundDate);
    }

    public CompanyMod() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        validateString(name);
        this.name = name;
    }

    public Date getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(final Date foundDate) {
        checkNull(foundDate);
        this.foundDate = foundDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyMod that = (CompanyMod) o;
        return Objects.equals(name, that.name) && Objects.equals(foundDate, that.foundDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, foundDate);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", foundDate=" + foundDate +
                "}\n";
    }
}