package com.example.springmvc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")

    ///  GeneratedValue :- It tells your program to let the database or JPA handle making the ID
    ///  value automatically. You did not set this value in your code.
    ///  strategy = GenerationType.SEQUENCE :- It tells the program to use a database sequence object to
    ///  get the next ID number
    ///  generator='user_seq' : It links this generation rule to a specific sequence generator name
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    ///  SequenceGenerator :- It sets up the rules for the sequence in your database.
    ///  name = "user_seq" :- This is the local name inside your Java Code. The @GeneratedValue annotation
    ///  uses this name to find these rules.
    ///  sequenceName = "user_sequence" :- This is the actual real name of the sequence stored in your SQL
    ///  database.
    ///  allocation = 1 : It tells the program to increase the ID number by 1 each time a
    ///  new row is saved

    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "age")
    private Integer age;
}
