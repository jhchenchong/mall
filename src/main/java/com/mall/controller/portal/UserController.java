package com.mall.controller.portal;

import com.mall.common.Const;
import com.mall.common.ResponseCode;
import com.mall.common.ServiceResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param session  session
     * @return ServiceResponse
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session) {
        ServiceResponse<User> serviceResponse = iUserService.login(username, password);
        if (serviceResponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, serviceResponse.getData());
        }
        return serviceResponse;
    }

    /**
     * 用户退出
     *
     * @param session session
     * @return ServiceResponse
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceResponse.createBySuccess();
    }

    /**
     * 注册用户
     *
     * @param user 用户对象
     * @return ServiceResponse
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 校验用户名或者邮箱是否存在
     *
     * @param str  用户名或者邮箱
     * @param type 用户名username 邮箱email
     * @return ServiceResponse
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户信息
     *
     * @param session session
     * @return ServiceResponse
     */
    @RequestMapping(value = "check_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServiceResponse.createBySuccess(user);
        }
        return ServiceResponse.createByErrorMessage("用户未登录，无法获取用户信息");
    }

    /**
     * 获取忘记密码的问题
     *
     * @param username 用户名
     * @return ServiceResponse
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 验证答案
     *
     * @param username 用户名
     * @param question 问题
     * @param answer   答案
     * @return ServiceResponse
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 修改密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param forgetToken token
     * @return ServiceResponse
     */
    @RequestMapping(value = "forget_rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
    }

    /**
     * 修改密码
     *
     * @param session     session
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @return ServiceResponse
     */
    @RequestMapping(value = "rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> restPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return iUserService.resetPassword(passwordOld, passwordNew, user);
        }
        return ServiceResponse.createByErrorMessage("用户未登录");
    }

    /**
     * 更新用户信息
     *
     * @param session session
     * @param user    用户信息
     * @return ServiceResponse
     */
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> updateInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser != null) {
            user.setId(currentUser.getId());
            user.setUsername(currentUser.getUsername());
            ServiceResponse<User> response = iUserService.updateInformation(user);
            if (response.isSuccess()) {
                session.setAttribute(Const.CURRENT_USER, response.getData());
            }
            return response;
        }
        return ServiceResponse.createByErrorMessage("用户未登录");
    }

    /**
     * 获取用户信息
     *
     * @param session session
     * @return ServiceResponse
     */
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> getInformation(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return iUserService.getInformation(user.getId());
        }
        return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
    }

}
