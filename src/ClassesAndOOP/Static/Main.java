package ClassesAndOOP.Static;

public class Main {
    public static void main(String[] args) {
        Car audi = new Car("Audi");
        Car volvo = new Car("Volvo");

        //Works because totalCars is a static field belonging to the Car/object/blueprint
        int totalCars = Car.totalCars;

        //Will not work because model is a non-static parameter which belongs to the car itself, not the car/class/blueprint
        //String model = Car.model;

        //This works, but is not recommended since totalCars is static, and it does not belong to the instance. It might confuse devs to think this actully does only belong to this car.
        int totalCarsNoGood = audi.totalCars;

        System.out.println(audi.model);
        System.out.println(volvo.model);
        System.out.println(Car.totalCars);
        System.out.println(volvo.getModel() );
    }
}
