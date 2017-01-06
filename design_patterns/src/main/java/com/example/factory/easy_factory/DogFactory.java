package com.example.factory.easy_factory;

/**
 * Created by litiancheng on 2017/1/5.
 */

class DogFactory {
    static Dog newInstance(DogType dogType){
        switch (dogType) {
            case Teddy:
                return new TeddyDog();
            case Alaska:
                return new AlaskaDog();

        }
        return null;
    }
}