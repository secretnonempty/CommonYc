package com.rails.ecommerce.admin.webservice.client;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.rails.ecommerce.admin.utils.StaticConfig;
import com.rails.ecommerce.core.common.domain.Organization;
import com.rails.ecommerce.core.common.domain.Role;
import com.rails.ecommerce.core.common.domain.SystemMenu;
import com.rails.ecommerce.core.common.domain.SystemRoleResource;
import com.rails.ecommerce.core.common.domain.SystemTime;
import com.rails.ecommerce.core.common.domain.TrainNumberView;
import com.rails.ecommerce.core.common.domain.TreeNodeList;
import com.rails.ecommerce.core.user.domain.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ClientImpl {
	/**
	 * 
	 *  Function:获取资源树
	 *  @author zjf  DateTime 2015-1-29 上午10:03:16
	 *  @param loginName
	 *  @return
	 */
	public static SystemMenu getResoucer(String loginName){
		MultivaluedMap<String ,String> map = new MultivaluedMapImpl();
		map.add("loginName", loginName);
		map.add("rootId", "9");//电商管理根资源ID
		Client c = Client.create();
		WebResource r = c.resource(StaticConfig.RESOURCEPATH);
		GenericType<SystemMenu> sys = new GenericType<SystemMenu>(){};
		SystemMenu response = r.path("getMenu").queryParams(map).get(sys);
		return  response;
	}
	
	public static List<Role> getRole(String loginName){
		MultivaluedMap<String ,String> map = new MultivaluedMapImpl();
		map.add("loginName", loginName);
		Client c = Client.create();
		WebResource r = c.resource(StaticConfig.RESOURCEPATH);
		GenericType<SystemRoleResource> sys = new GenericType<SystemRoleResource>(){};
		SystemRoleResource response = r.path("getRoleResource").queryParams(map).get(sys);
		response.getListRole();
		return response.getListRole();
	}
	
	/**
	 * 
	 *  Function:获取登录人员可以管理的部门机构树
	 *  @author zjf  DateTime 2015-1-29 上午10:03:52
	 *  @param loginName
	 *  @return
	 */
	public static List<TreeNodeList> getOrganization(String loginName){
		MultivaluedMap<String ,String> map = new MultivaluedMapImpl();
		map.add("loginName", loginName);
		Client c = Client.create();
		WebResource r = c.resource(StaticConfig.RESOURCEPATH);
		GenericType<List<TreeNodeList>> sys = new GenericType<List<TreeNodeList>>(){};
		List<TreeNodeList> response = r.path("getOrgan").queryParams(map).get(sys);
		return response;
	}
	
	public static List<Organization> getUserOrganization(String loginName){
		
		MultivaluedMap<String ,String> map = new MultivaluedMapImpl();
		map.add("carOrgId", loginName);
		Client c = Client.create();
		WebResource r = c.resource(StaticConfig.USERPATH);
		GenericType<List<Organization>> sys = new GenericType<List<Organization>>(){};
		List<Organization> response = r.path("listOrganizationByLoginName").queryParams(map).get(sys);
//		ClientResponse response = r.path("getInfo").queryParams(map).get(ClientResponse.class);
		//System.out.println(response.getEntity(String.class));

		return response;
	}
	
	/**
	 * 
	 *  Function:获取部门机构对应的车次
	 *  @author zjf  DateTime 2015-1-29 上午10:03:52
	 *  @param loginName
	 *  @return
	 */
	public static List<TrainNumberView> getOrganizationForTrainNo(String carOrgId) {
		MultivaluedMap<String ,String> map = new MultivaluedMapImpl();
		map.add("carOrgId", carOrgId);
		Client c = Client.create();
		WebResource r = c.resource(StaticConfig.TRAINPATH);
		GenericType<List<TrainNumberView>> sys = new GenericType<List<TrainNumberView>>(){};
		List<TrainNumberView> response = r.path("getTrainList").queryParams(map).get(sys);
//		ClientResponse response = r.path("getInfo").queryParams(map).get(ClientResponse.class);
		//System.out.println(response.getEntity(String.class));

		return response;
	}
	
	/**
	 * 
	 *  Function:获取部门机构对应的车次
	 *  @author zjf  DateTime 2015-1-29 上午10:03:52
	 *  @param loginName
	 *  @return
	 */
	public static List<SystemTime> getSysTime() {
		MultivaluedMap<String ,String> map = new MultivaluedMapImpl();
//		map.add("carOrgId", carOrgId);
		Client c = Client.create();
		WebResource r = c.resource(StaticConfig.SYSTIME);
		GenericType<List<SystemTime>> sys = new GenericType<List<SystemTime>>(){};
		List<SystemTime> response = r.path("").queryParams(map).get(sys);
//		ClientResponse response = r.path("getInfo").queryParams(map).get(ClientResponse.class);
		//System.out.println(response.getEntity(String.class));

		return response;
	}

	/**
	 * 
	 *  Function:读取用户信息
	 * 
	 *  @author allen  DateTime 2015-1-30 下午10:08:30
	 *  @param loginName
	 *  @return
	 */
	public static User checkUser(String loginName,String pass){
		MultivaluedMap<String ,String> map = new MultivaluedMapImpl();
		map.add("loginName", loginName);
		Client c = Client.create();
		WebResource r = c.resource(StaticConfig.USERPATH);
		GenericType<User> sys = new GenericType<User>(){};
		User user = r.path("getByLoginName").queryParams(map).get(sys);
		//用户名是否为空；
		if(user!=null){
			return user;
		}else{
			return null;
		}
	}
	/**
	 * 
	 *  Function:读取用户信息
	 * 
	 *  @author allen  DateTime 2015-1-30 下午10:08:30
	 *  @param loginName
	 *  @return TrainNumberView
	 */
	public static List<TrainNumberView> getTrainNumber(String loginName){
		MultivaluedMap<String ,String> map = new MultivaluedMapImpl();
		map.add("loginName", loginName);
		Client c = Client.create();
		WebResource r = c.resource(StaticConfig.USERPATH);
		GenericType<List<TrainNumberView>> sys = new GenericType<List<TrainNumberView>>(){};
		List<TrainNumberView> trainNumberView = r.path("getTrainsByUser").queryParams(map).get(sys);
		//用户名是否为空；
		if(trainNumberView!=null){
			return trainNumberView;
		}else{
			return null;
		}
	}
	/**
	 * 
	 *  Function:
	 * 
	 *  @author cai  DateTime 2015-1-30 下午7:50:56
	 *  @param args
	 */
	
	
	public static void main(String[] args) {
		
		List<Organization> li=getUserOrganization("admin");
		for(int i=0;i<li.size();i++){
			System.out.print(li.get(i).getOrganizationName());
			System.out.print(li.get(i).getId());
			System.out.print(li.get(i).getOrganizationCode());
		}
		
	}
}
