package com.fitmannation.model;

public enum MembershipPlan {
    BASIC("Basic Plan", 999.0),
    PRO("Pro Transformation", 2999.0),
    PREMIUM("Premium 1-on-1 Coaching", 4999.0),
    SPECIAL("Special Case Coaching", 0.0), // Custom pricing
    GROUP("Group Fitness Classes", 1999.0),
    FAMILY("Family & Corporate Plans", 0.0); // Custom pricing
    
    private final String name;
    private final Double price;
    
    MembershipPlan(String name, Double price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() {
        return name;
    }
    
    public Double getPrice() {
        return price;
    }
}


