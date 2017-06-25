package com.rails.ecommerce.admin.api.bean;

import com.google.gson.Gson;

public class JsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String a = "{'data':'1'}";
		Gson g = new Gson();
		String m = g.fromJson(a, String.class);
		
//        ObjectMapper om = new ObjectMapper();
//  
//        // 对象就在这里读取。  
//        String person = om.readValue(a, new TypeReference<String>() {});
  
//		JSONObject jsonObject = new JSONObject().getJSONObject(a);
//		System.out.println(jsonObject);
//		StudentInfoForm person = (StudentInfoForm) jsonObject.get("data");
        // 怎么用，随便。  
//        System.out.println(person.getCode());  
        System.out.println(m);  
    }  

}
