package com.mall.service;

import com.mall.common.ServiceResponse;
import com.mall.pojo.Product;
import com.mall.vo.ProductDetailVO;

public interface IProductService {

    ServiceResponse saveOrUpdateProduct(Product product);

    ServiceResponse<String> setSaleStatus(Integer productId, Integer status);

    ServiceResponse<ProductDetailVO> manageProductDetail(Integer productId);
}
