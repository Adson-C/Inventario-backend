package com.company.inventory.inventario.response;

import java.util.List;

import com.company.inventory.inventario.model.Category;

import lombok.Data;


@Data
public class CategoryResponse {

    private List<Category> categorys;

}

