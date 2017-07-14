package com.hansonwang99.service.impl;

import com.hansonwang99.domain.Category;
import com.hansonwang99.repository.CategoryRepository;
import com.hansonwang99.service.CategoryService;
import com.hansonwang99.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/7/14.
 */
@Service(value = "categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Long userId, Long count, String name) {

        Category category = new Category();
        category.setName( name );
        category.setUserId( userId );
        category.setCount( count );
        category.setCreateTime( DateUtils.getCurrentTime() );
        category.setLastModifyTime( DateUtils.getCurrentTime() );
        categoryRepository.save( category );
        return category;
    }
}
