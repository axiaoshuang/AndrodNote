package com.example.factory.easy_factory;

public class Main {
    public static void main(String[] args) {
        Dog dog = DogFactory.newInstance(DogType.Alaska);
        System.out.println(dog);
        dog = DogFactory.newInstance(DogType.Teddy);
        System.out.println(dog);

    }
}
