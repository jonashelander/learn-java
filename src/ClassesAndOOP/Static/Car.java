package ClassesAndOOP.Static;

public class Car {

    //Non-static variable, each instance has its own copy
    String model;

    //Static variable, belongs to the class and all instances share the same value
    static int totalCars;

    public Car(String model) {
        this.model = model;
        //For every new car created, we increment the totalCars count. And since totalCars is static, it will be shared across all instances of Car.
        totalCars++;
    }

    public String getModel() {
        return model;
    }

    //Can't call a non-static variable from a static method, because static methods belong to the class and don't have access to instance variables. So we can't use 'this' keyword in a static method.
/*    public static String getModel() {
        System.out.println(this.model);
        return model;*/
}

