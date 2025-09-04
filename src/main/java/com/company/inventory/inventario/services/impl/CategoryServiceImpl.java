package com.company.inventory.inventario.services.impl;

import java.util.List;

import com.company.inventory.inventario.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.inventario.dao.ICategoryDao;
import com.company.inventory.inventario.response.CategoryResponseRest;
import com.company.inventory.inventario.services.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService {


    @Autowired
    private ICategoryDao categoryDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CategoryResponseRest> search() {

        CategoryResponseRest response = new CategoryResponseRest();

        try {
            List<Category> categories = (List<Category>) categoryDao.findAll();
            response.getCategoryResponse().setCategorys(categories);
            response.setMetadata("Resposta OK", "00", "Resposta bem-sucedida");
        } catch (Exception e) {
            response.setMetadata("Erro na reposta", "-1", "Erro ao buscar categorias");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }

}
