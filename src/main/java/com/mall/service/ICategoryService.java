package com.mall.service;

import com.mall.common.ServiceResponse;
import com.mall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    ServiceResponse addCategory(String categoryName, Integer parentId);

    ServiceResponse updateCategoryName(Integer categoryId, String categoryName);

    ServiceResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServiceResponse selectCategoryAndChildrenById(Integer categoryId);
}
