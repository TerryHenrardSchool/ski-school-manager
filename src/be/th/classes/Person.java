package be.th.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Person implements Serializable {
	
	// Static attributes
	private static final long serialVersionUID = -6721217695491429434L;
	
	// Attributes
    private int id;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private String city;
    private String postcode;
    private String streetName;
    private String streetNumber;
    private String phoneNumber;
    private String email;

    // Constructor
    public Person(
		int id, 
		String lastName, 
		String firstName, 
		LocalDate dateOfBirth, 
		String city, 
		String postcode, 
		String streetName, 
		String streetNumber,
		String phoneNumber, 
		String email
	) {
        setId(id);
        setLastName(lastName);
        setFirstName(firstName);
        setDateOfBirth(dateOfBirth);
        setCity(city);
        setPostcode(postcode);
        setStreetName(streetName);
        setStreetNumber(streetNumber);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setId(int id) {
        if (!Utils.isPositiveInteger(id)) {
            throw new IllegalArgumentException("ID must be positive.");
        }
        this.id = id;
    }

    public void setLastName(String lastName) {
        if (!Utils.isNonEmptyString(lastName)) {
            throw new IllegalArgumentException("Last name cannot be null or empty.");
        }
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        if (!Utils.isNonEmptyString(firstName)) {
            throw new IllegalArgumentException("First name cannot be null or empty.");
        }
        this.firstName = firstName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (!Utils.isValidDate(dateOfBirth)) {
            throw new IllegalArgumentException("Date of birth cannot be null or in the future or before the year 1900.");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public void setCity(String city) {
        if (!Utils.isNonEmptyString(city)) {
            throw new IllegalArgumentException("City cannot be null or empty.");
        }
        this.city = city;
    }

    public void setPostcode(String postcode) {
        if (!Utils.isNonEmptyString(postcode)) {
            throw new IllegalArgumentException("Postcode cannot be null or empty.");
        }
        this.postcode = postcode;
    }

    public void setStreetName(String streetName) {
        if (!Utils.isNonEmptyString(streetName)) {
            throw new IllegalArgumentException("Street name cannot be null or empty.");
        }
        this.streetName = streetName;
    }

    public void setStreetNumber(String streetNumber) {
        if (!Utils.isNonEmptyString(streetNumber)) {
            throw new IllegalArgumentException("Street number cannot be null or empty.");
        }
        this.streetNumber = streetNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!Utils.isNonEmptyString(phoneNumber)) {
            throw new IllegalArgumentException("Phone number cannot be null or empty.");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if (!Utils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        this.email = email;
    }
    
    // Override methods
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Person person = (Person) object;
        return id == person.id &&
            Objects.equals(lastName, person.lastName) &&
            Objects.equals(firstName, person.firstName) &&
            Objects.equals(dateOfBirth, person.dateOfBirth) &&
            Objects.equals(city, person.city) &&
            Objects.equals(postcode, person.postcode) &&
            Objects.equals(streetName, person.streetName) &&
            Objects.equals(streetNumber, person.streetNumber) &&
            Objects.equals(phoneNumber, person.phoneNumber) &&
            Objects.equals(email, person.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
    		id, 
    		lastName, 
    		firstName, 
    		dateOfBirth, 
    		city, 
    		postcode, 
    		streetName, 
    		streetNumber, 
    		phoneNumber, 
    		email
		);
    }
    
    @Override
    public String toString() {
        return "Person: " +
	        "id=" + id +
	        ", lastName='" + lastName + '\'' +
	        ", firstName='" + firstName + '\'' +
	        ", dateOfBirth=" + dateOfBirth +
	        ", city='" + city + '\'' +
	        ", postcode='" + postcode + '\'' +
	        ", streetName='" + streetName + '\'' +
	        ", streetNumber='" + streetNumber + '\'' +
	        ", phoneNumber='" + phoneNumber + '\'' +
	        ", email='" + email + '\'';
    }
}
