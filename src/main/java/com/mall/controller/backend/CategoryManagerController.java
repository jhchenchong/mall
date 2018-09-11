package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServiceResponse;
import com.mall.pojo.User;
import com.mall.service.ICategoryService;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/category")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加品类
     *
     * @param session      session
     * @param categoryName 品类名称
     * @param parentId     父节点ID
     * @return ServiceResponse
     */
    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            if (iUserService.checkAdminRole(user).isSuccess()) {
                return iCategoryService.addCategory(categoryName, parentId);
            } else {
                return ServiceResponse.createByErrorMessage("无权限操作,需要管理员权限");
            }
        }
        return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
    }

    /**
     * 更新品类名称
     * @param session      session
     * @param categoryId   品类Id
     * @param categoryName 品类名称
     * @return ServiceResponse
     */
    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            if (iUserService.checkAdminRole(user).isSuccess()) {
                return iCategoryService.updateCategoryName(categoryId, categoryName);
            } else {
                return ServiceResponse.createByErrorMessage("无权限操作,需要管理员权限");
            }
        }
        return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
    }


    /**
     * 查找子类节点保持平级
     * @param session    session
     * @param categoryId 分类Id
     * @return ServiceResponse
     */
    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            if (iUserService.checkAdminRole(user).isSuccess()) {
                return iCategoryService.getChildrenParallelCategory(categoryId);
            } else {
                return ServiceResponse.createByErrorMessage("无权限操作,需要管理员权限");
            }
        }
        return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
    }

    /**
     * 递归查找子节点
     * @param session    session
     * @param categoryId 分类Id
     * @return ServiceResponse
     */
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            if (iUserService.checkAdminRole(user).isSuccess()) {
                return iCategoryService.selectCategoryAndChildrenById(categoryId);
            } else {
                return ServiceResponse.createByErrorMessage("无权限操作,需要管理员权限");
            }
        }
        return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
    }
}
