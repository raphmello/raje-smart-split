package com.raje.smartsplit.enums;

public enum ECategory {
    GENERAL("Geral"),
    MEAT("Carne"),
    BEER("Cerveja"),
    ALCOHOLIC_DRINK("Bebida alcoólica"),
    VEGAN_PRODUCT("Produto vegano"),
    FUEL("Combustível"),
    ROAD_TAX("Pedágio"),
    RENT("Aluguel");

    private final String category;

    ECategory(String category) {
        this.category = category;
    }
}
