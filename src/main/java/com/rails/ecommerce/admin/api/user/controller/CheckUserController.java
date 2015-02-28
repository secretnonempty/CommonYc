package com.rails.ecommerce.admin.api.user.controller;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.rails.common.utils.MD5Util;
import com.rails.ecommerce.admin.api.form.UserApiForm;
import com.rails.ecommerce.admin.webservice.client.ClientImpl;
import com.rails.ecommerce.core.common.domain.TrainNumberView;
import com.rails.ecommerce.core.user.domain.User;

@Component
@Path(value="user")
public class CheckUserController {
	
	@Path(value = "checkUser")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    public UserApiForm getUserLogin(@Context HttpServletRequest request) {
		UserApiForm userApiForm = new UserApiForm();
		String name = request.getParameter("userName");
		String pass = request.getParameter("passWord");
		
			try {
				//按照用户名远程调用接口查询是否存在此用户
				User userInfo = ClientImpl.checkUser(name,pass);
				if(userInfo.getPassword()!=null){
					String dataPassword = userInfo.getPassword();
					try {
						boolean tmpLogin = MD5Util.validPassword(pass,dataPassword);
						if(tmpLogin){
							userApiForm.setMsg("登录成功");
							userApiForm.setStatus("0");
							userInfo.setPassword("");
							List<User> listUser = new ArrayList<User>();
							listUser.add(userInfo);
							List<TrainNumberView> trainNumberList = ClientImpl.getTrainNumber(name);
							userApiForm.setReturnList1(trainNumberList);
							userApiForm.setReturnList(listUser);
						}else{
							userApiForm.setMsg("登录密码不正确");
							userApiForm.setStatus("1");
						}
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}else{
					userApiForm.setMsg("无登录名");
					userApiForm.setStatus("2");
				}
				return userApiForm;
			} catch (Exception e) {
				userApiForm.setMsg("链接webservice服务器失败："+e);
				System.out.println("链接webservice服务器失败："+e);
			}
			return null;
    }


}
