package com.rails.ecommerce.admin.test;

import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jettison.json.JSONObject;

import com.rails.ecommerce.core.common.domain.SystemMenu;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class WebServiceClient {

	/**
	 *  Function:
	 * 
	 *  @author zjf  DateTime 2015-1-26 下午8:34:30
	 *  @param args
	 */
	public static void main(String[] args) {
		
		
		MultivaluedMap map = new MultivaluedMapImpl();
		map.add("loginName", "admin");
		Client c = Client.create();
		WebResource r = c.resource("http://192.168.1.107:8080/admin/api/roleresource");
		ClientResponse response = r.path("getRoleResource").queryParams(map).get(ClientResponse.class);
		String jsionStr=response.getEntity(String.class);
		System.out.println(response.getEntity(String.class));
		
		//String json=你的json字符串;
//		JSONObject jsonObject=JSONObject.fromObject(jsionStr);
//		jsionStr=jsonObject.getString("category_attribute_search_response");
		//SystemMenu object=(SystemMenu)JSONObject.toBean(jsionStr,SystemMenu.class);


	}

}
