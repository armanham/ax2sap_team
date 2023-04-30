package com.bdg.airport_management_system.model;

import com.bdg.airport_management_system.model.common.BaseMod;

import static com.bdg.airport_management_system.validator.Validator.checkNull;
import static com.bdg.airport_management_system.validator.Validator.validateString;


public class PassengerMod extends BaseMod {

    private String name;
    private String phone;
    private AddressMod addressMod;

    public PassengerMod(
            final String name,
            final String phone,
            final AddressMod addressMod) {
        setName(name);
        setPhone(phone);
        setAddress(addressMod);
    }

    public PassengerMod() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        validateString(name);
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        validateString(phone);
        this.phone = phone;
    }

    public AddressMod getAddress() {
        return addressMod;
    }

    public void setAddress(final AddressMod addressMod) {
        checkNull(addressMod);
        this.addressMod = addressMod;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + addressMod +
                "}\n";
    }
}