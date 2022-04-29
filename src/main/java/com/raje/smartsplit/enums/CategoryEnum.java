package com.raje.smartsplit.enums;

public enum CategoryEnum {
    MEAT("Carne"),
    BEER("Cerveja"),
    ALCOHOLIC_DRINK("Bebida alcoólica"),
    VEGAN_PRODUCT("Produto vegano"),
    FUEL("Combustível"),
    ROAD_TAX("Pedágio"),
    RENT("Aluguel");

    private final String category;

    CategoryEnum(String category) {
        this.category = category;
    }
}
