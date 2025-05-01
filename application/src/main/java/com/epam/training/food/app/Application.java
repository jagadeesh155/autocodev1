package com.epam.training.food.app;

import com.epam.training.food.service.FoodService;

public class Application {
    public static void main(String[] args) {
        FoodService service = new FoodService();
        System.out.println(service.serveFood());
    }
}
