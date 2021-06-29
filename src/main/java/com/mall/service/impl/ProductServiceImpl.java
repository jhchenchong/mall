package com.mall.service.impl;

import com.mall.common.ResponseCode;
import com.mall.common.ServiceResponse;
import com.mall.dao.CategoryMapper;
import com.mall.dao.ProductMapper;
import com.mall.pojo.Category;
import com.mall.pojo.Product;
import com.mall.service.IProductService;
import com.mall.until.DateTimeUtil;
import com.mall.until.PropertiesUtil;
import com.mall.vo.ProductDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public ServiceResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArry = product.getSubImages().split(",");
                if (subImageArry.length > 0) {
                    product.setMainImage(subImageArry[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServiceResponse.createBySuccessMessage("更新产品成功");
                }
                return ServiceResponse.createByErrorMessage("更新产品失败");
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServiceResponse.createBySuccessMessage("新增产品成功");
                }
                return ServiceResponse.createByErrorMessage("新增产品失败");
            }
        }
        return ServiceResponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    public ServiceResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKey(product);
        if (rowCount > 0) {
            return ServiceResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServiceResponse.createByErrorMessage("修改产品销售状态失败");
    }

    public ServiceResponse<ProductDetailVO> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServiceResponse.createByErrorMessage("产品已下架或者删除");
        }
        return ServiceResponse.createBySuccess(assembleProductDetailVo(product));
    }

    private ProductDetailVO assembleProductDetailVo(Product product){
        ProductDetailVO productDetailVo = new ProductDetailVO();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix",null));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }
}
