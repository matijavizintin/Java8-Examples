package com.test;

/**
 * Created by Matija Vižintin
 * Date: 05. 05. 2015
 * Time: 14.46
 */
public class Person {
    public String name;
    public Gender gender;
    public Integer age;

    public Person() {
    }

    public Person(Integer age) {
        this.age = age;
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Person(Person p) {
        name = p.getName();
        gender = p.getGender();
        age = p.getAge();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override public String toString() {
        return "Person{" +
               "name='" + name + '\'' +
               ", gender=" + gender +
               ", age=" + age +
               '}';
    }
}
