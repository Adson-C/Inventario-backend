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
            List<Category> categories = new ArrayList<>();
            categoryDao.findAll().forEach(categories::add);
            System.out.println("Categorias encontradas: " + categories.size());
            response.getCategoryResponse().setCategorys(categories != null ? categories : new ArrayList<>());
            response.setMetadata("Resposta OK", "00", "Resposta bem-sucedida");
        } catch (Exception e) {
            response.setMetadata("Erro na reposta", "-1", "Erro ao buscar categorias");
            e.printStackTrace();
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
            e.printStackTrace(); // Para ver o erro completo no console
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CategoryResponseRest> save(Category category) {
                    CategoryResponseRest response = new CategoryResponseRest();
                    List<Category> list = new ArrayList<>();
                    try {
                        Category categorySaved = categoryDao.save(category);
                        if(categorySaved != null){
                            list.add(categorySaved);
                            response.getCategoryResponse().setCategorys(list);
                            response.setMetadata("Resposta OK", "00", "Categoria salva com sucesso");
                        } else {
                            response.setMetadata("Resposta NOK", "01", "Categoria não salva");
                            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
                        }
                    } catch (Exception e) {
                        response.setMetadata("Erro na reposta", "-1", "Erro ao salvar categoria");
                        e.printStackTrace(); // Para ver o erro completo no console
                        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {
        CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();

        try {
            Optional<Category> categoryOptional = categoryDao.findById(id);
            if (categoryOptional.isPresent()) {
                // Atualiza os dados da categoria
                Category categoryToUpdate = categoryOptional.get();
                categoryToUpdate.setName(category.getName());
                categoryToUpdate.setDescription(category.getDescription());
                Category categoryUpdated = categoryDao.save(categoryToUpdate);
                if (categoryUpdated != null) {
                    list.add(categoryUpdated);
                    response.getCategoryResponse().setCategorys(list);
                    response.setMetadata("Resposta OK", "00", "Categoria atualizada com sucesso");
                } else {
                    response.setMetadata("Resposta NOK", "01", "Categoria não atualizada");
                    return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                response.setMetadata("Resposta NOK", "01", "Categoria não encontrada");
                return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Erro na reposta", "-1", "Erro ao atualizar categoria");
            e.printStackTrace(); // Para ver o erro completo no console
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CategoryResponseRest> delete(Long id) {
        CategoryResponseRest response = new CategoryResponseRest();
        try {
            Optional<Category> categoryOptional = categoryDao.findById(id);
            if (categoryOptional.isPresent()) {
                categoryDao.delete(categoryOptional.get());
                response.setMetadata("Resposta OK", "00", "Categoria excluída com sucesso");
            } else {
                response.setMetadata("Resposta NOK", "01", "Categoria não encontrada");
                return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Erro na reposta", "-1", "Erro ao excluir categoria");
            e.printStackTrace(); // Para ver o erro completo no console
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }
}