package com.hansonwang99.service;

import com.hansonwang99.domain.Category;

/**
 * Created by Administrator on 2017/7/14.
 */
public interface CategoryService {

    public Category saveCategory(Long userId, Long count, String name);
}
