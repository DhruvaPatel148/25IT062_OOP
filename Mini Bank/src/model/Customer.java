package model;

import model.annotation.Id;
import java.io.Serializable;

public class Customer implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private static long customerCounter = 100;

    @Id private final String customerId;
    private String name;
    private String email;
    private String mobile;
    private Address address;

    private static synchronized String generateCustomerId() {
        return "CUST" + (++customerCounter);      // CUST101, CUST102 ...
    }

    public Customer(String name, String email, String mobile, Address address) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public Customer(String name, String email, String mobile) {
        this(name, email, mobile, new Address("-", "-", "-"));
    }

    public String getCustomerId() { return customerId; }
    public String getName()       { return name; }
    public String getEmail()      { return email; }
    public String getMobile()     { return mobile; }
    public Address getAddress()   { return address; }

    @Override
    public Customer clone() {
        try {
            Customer copy = (Customer) super.clone();
            copy.address = new Address(address.getLine(), address.getCity(), address.getPincode());
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);          // cannot happen: we implement Cloneable
        }
    }

    @Override
    public String toString() {
        return customerId + " | " + name + " | " + email + " | " + mobile;
    }

    /** Static nested class. */
    public static class Address implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String line, city, pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine()    { return line; }
        public String getCity()    { return city; }
        public String getPincode() { return pincode; }
    }
}
