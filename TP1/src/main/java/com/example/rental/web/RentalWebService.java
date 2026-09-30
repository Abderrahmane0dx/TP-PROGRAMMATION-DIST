package com.example.rental.web;
import java.util.ArrayList;
import java.util.List;

import com.example.rental.entity.Car;
import com.example.rental.entity.Dates;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RentalWebService {
    
    private List<Car> cars = new ArrayList<>();

    public RentalWebService() {
        cars.add(new Car("11AA22", "Ferrari", 100));
        cars.add(new Car("33BB44", "Porsche", 80));
        cars.add(new Car("55CC66", "Renault", 30));
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    // get a car by its plate number :
    @GetMapping("/cars/{plateNumber}")
    public Car getCarFeatures(@PathVariable String plateNumber) {
        for (Car car : cars) {
            if (car.getPlateNumber().equals(plateNumber)) {
                return car;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No car with plate number " + plateNumber);
    }

    // rent a car by its plate number :
    @PutMapping(value = "/cars/{plateNumber}")
    public void rent(@PathVariable("plateNumber") String plateNumber,
                     @RequestParam(value="rent", required = true)boolean rent,
                     @RequestBody(required = false) Dates dates) {
        for (Car car : cars) {
            if (car.getPlateNumber().equals(plateNumber)) {
                if (rent) {
                    if (dates == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rental dates are required");
                    }
                    car.setRented(true);
                    car.setStartDate(dates.getBegin());
                    car.setEndDate(dates.getEnd());
                } else {
                    car.setRented(false);
                    car.setStartDate(null);
                    car.setEndDate(null);
                }
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No car with plate number " + plateNumber);
    }
}
