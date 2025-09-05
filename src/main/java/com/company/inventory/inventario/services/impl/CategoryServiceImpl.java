package com.company.inventory.inventario.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CategoryResponseRest> searchById(Long id) {
        CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();

        try {
            Optional<Category> category = categoryDao.findById(id);
            if(category.isPresent()){
                list.add(category.get());
                response.getCategoryResponse().setCategorys(list);
                response.setMetadata("Resposta OK", "00", "Resposta bem-sucedida");
            } else {
                response.setMetadata("Resposta NOK", "01", "Categoria não encontrada");
                return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            response.setMetadata("Erro na reposta", "-1", "Erro ao buscar por id");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }

}
