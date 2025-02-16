package com.nighthawk.spring_portfolio.mvc.person;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Convert;
import static jakarta.persistence.FetchType.EAGER;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/*
Person is a POJO, Plain Old Java Object.
First set of annotations add functionality to POJO
--- @Setter @Getter @ToString @NoArgsConstructor @RequiredArgsConstructor
The last annotation connect to database
--- @Entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Convert(attributeName = "person", converter = JsonType.class)
public class Person {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // email, password, roles are key attributes to login and authentication
    @NotEmpty
    @Size(min = 5)
    @Column(unique = true)
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String hobby;

    @NotEmpty
    private String isDegenerate;

    @NotEmpty
    private String smartCarColor;

    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max =
    // 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Size(min = 2, max = 30, message = "Name (2 to 30 chars)")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    // To be implemented
    @ManyToMany(fetch = EAGER)
    private Collection<PersonRole> roles = new ArrayList<>();

    /*
     * HashMap is used to store JSON for daily "stats"
     * "stats": {
     * "2022-11-13": {
     * "calories": 2200,
     * "steps": 8000
     * }
     * }
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Map<String, Object>> stats = new HashMap<>();

    // Constructor used when building object from an API
    public Person(String email, String password, String name, String hobby, String isDegenerate, String smartCarColor,
            Date dob) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.hobby = hobby;
        this.isDegenerate = isDegenerate;
        this.smartCarColor = smartCarColor;
        this.dob = dob;
    }

    // A custom getter to return age from dob attribute
    public int getAge() {
        if (this.dob != null) {
            LocalDate birthDay = this.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(birthDay, LocalDate.now()).getYears();
        }
        return -1;
    }

    // Initialize static test data
    public static Person[] init() {

        // basics of class construction
        Person Haseeb = new Person();
        Haseeb.setName("Haseeb Beg");
        Haseeb.setEmail("animeLover69@gmail.com");
        Haseeb.setPassword("password");
        Haseeb.setHobby("Anime Watching");
        Haseeb.setIsDegenerate("true");
        Haseeb.setSmartCarColor("Yellow");

        // adding Note to notes collection
        try { // All data that converts formats could fail
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1840");
            Haseeb.setDob(d);
        } catch (Exception e) {
            // no actions as dob default is good enough
        }

        Person Tirth = new Person();
        Tirth.setName("Tirth Thakkar");
        Tirth.setEmail("tirth@gmail.com");
        Tirth.setPassword("password");
        Tirth.setHobby("Sleeping");
        Tirth.setIsDegenerate("false");
        Tirth.setSmartCarColor("Pink");

        Person Test = new Person();
        Test.setName("Test");
        Test.setEmail("testing@email.com");
        Test.setPassword("password");
        Test.setHobby("Testing");
        Test.setIsDegenerate("maybe");
        Test.setSmartCarColor("Blue");

        try {
            Date d = new SimpleDateFormat("MM-dd-yyyy").parse("01-01-1845");
            Tirth.setDob(d);
        } catch (Exception e) {
        }

        // Array definition and data initialization
        Person persons[] = { Haseeb, Tirth, Test };
        return (persons);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Person persons[] = init();

        // iterate using "enhanced for loop"
        for (Person person : persons) {
            System.out.println(person); // print object
        }
    }

}