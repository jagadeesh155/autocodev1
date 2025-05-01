package com.epam.training.food.service;

import com.epam.training.food.data.DataRepository;

public class FoodService {
    private final DataRepository dataRepository = new DataRepository();

    public String serveFood() {
        return "Serving: " + dataRepository.getData();
    }
}
