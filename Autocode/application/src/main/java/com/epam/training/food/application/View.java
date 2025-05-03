package com.epam.training.food.application;

import com.epam.training.food.domain.Credentials;
import com.epam.training.food.domain.Customer;
import com.epam.training.food.domain.Food;
import com.epam.training.food.domain.Order;

import java.math.BigDecimal;
import java.util.List;

public interface View {
    Credentials readCredentials();

    void printWelcomeMessage(Customer customer);

    void printAllFoods(List<Food> foods);

    FoodSelection readFoodSelection(List<Food> foods);

    void printAddedToCart(Food food, int pieces);

    void printErrorMessage(String message);

    void printOrderCreatedStatement(Order order, BigDecimal balance);
}
