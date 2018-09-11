package com.mall.service;

import com.mall.common.ServiceResponse;
import com.mall.pojo.User;

public interface IUserService {

    ServiceResponse<User> login(String username, String password);

    ServiceResponse<String> register(User user);

    ServiceResponse<String> checkValid(String str, String type);

    ServiceResponse selectQuestion(String username);

    ServiceResponse<String> checkAnswer(String username, String question, String answer);

    ServiceResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken);

    ServiceResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServiceResponse<User> updateInformation(User user);

    ServiceResponse<User> getInformation(Integer userId);
}
