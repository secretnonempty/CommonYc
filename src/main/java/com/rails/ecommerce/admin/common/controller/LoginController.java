package com.rails.ecommerce.admin.common.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.admin.webservice.client.ClientImpl;
import com.rails.ecommerce.core.common.domain.Organization;
import com.rails.ecommerce.core.common.domain.SystemMenu;
import com.rails.ecommerce.core.common.domain.TreeNodeList;

@Controller
public class LoginController {

	@RequestMapping(value = "/")
	public ModelAndView login() {
//		Subject subject = SecurityUtils.getSubject();
//		Object principal = subject.getPrincipal();//获取用户登录信息
//		Session session=subject.getSession();
//		session.setAttribute("userName", principal.toString());//获取用户登录放入session
		ModelAndView model = new ModelAndView();
		model.setViewName("main");
		return model;
	}

	@RequestMapping(value = "/main")
	public ModelAndView loginIn() {
//		Subject subject = SecurityUtils.getSubject();
//		Object principal = subject.getPrincipal();//获取用户登录信息
//		Session session=subject.getSession();
//		session.setAttribute("userName", principal.toString());//获取用户登录放入session
		ModelAndView model = new ModelAndView();
		model.setViewName("main");
		return model;
	}
	
//	@RequestMapping(value = "/mainmenu")
//    @ResponseBody
//    public List<SystemMenu> sysmenulist() {
//		Subject subject = SecurityUtils.getSubject();
//		Object principal = subject.getPrincipal();//获取用户登录信息
//		Session session=subject.getSession();
//		List<SystemMenu> treeList1=(List<SystemMenu>)session.getAttribute("treeList");
//		if(treeList1==null){
//			 List<SystemMenu> treeList2 = new ArrayList<SystemMenu>();
//			 try {
//				 SystemMenu sm=ClientImpl.getResoucer(principal.toString());
//				 treeList2.add(sm);
//				    session.setAttribute("treeList", treeList2);
//				    return treeList2;
//			} catch (Exception e) {
//				System.out.println("链接webservice服务器失败："+e);
//				
//			}
//		}
//	return treeList1;
//
//    }
	
	
//	@RequestMapping(value = "/organization")
//    @ResponseBody
//    public List<TreeNodeList> getOrganization() {
//		Subject subject = SecurityUtils.getSubject();
//		Object principal = subject.getPrincipal();//获取用户登录信息
//		Session session=subject.getSession();
//		String orgIds ="";
//		List<TreeNodeList> orgaTree=(List<TreeNodeList>)session.getAttribute("orgaTree");
//		if(orgaTree==null){
//			 try {
//				 orgaTree=ClientImpl.getOrganization(principal.toString());
//				 session.setAttribute("orgaTree", orgaTree);
//				 //拼所有节点字符串
//				 if(orgaTree.size()>0){
//					 orgIds = findChildred(orgaTree,orgIds);
//				 }
//				 session.setAttribute("orgIds", orgIds.substring(0,orgIds.length()-1)); //将字符串放入session
//				 return orgaTree;
//			} catch (Exception e) {
//				System.out.println("链接webservice服务器失败："+e);
//				return null;
//			}
//		}
//		//拼所有节点字符串
//		if(orgaTree.size()>0){
//			 orgIds = findChildred(orgaTree,orgIds);
//		}
//		session.setAttribute("orgIds", orgIds.substring(0,orgIds.length()-1)); //将字符串放入session
//		return orgaTree;
//
//    }
	
	
//	@RequestMapping(value = "/userOrganization")
//    @ResponseBody
//    public List<Organization> getUserOrganization() {
//		Subject subject = SecurityUtils.getSubject();
//		Object principal = subject.getPrincipal();//获取用户登录信息
//		Session session=subject.getSession();
//		List<Organization> uo1=(List<Organization>)session.getAttribute("uo");
//		if(uo1==null){
//			 try {
//				 List<Organization> uo2=ClientImpl.getUserOrganization(principal.toString());
//				    session.setAttribute("uo", uo2);
//				    return uo2;
//			} catch (Exception e) {
//				System.out.println("链接webservice服务器失败："+e);
//			}
//		}
//	return uo1;
//
//    }

	/**
	 * 
	 *  Function:检查webservice是否启动
	 * 
	 *  @author zjf  DateTime 2015-1-31 上午9:09:21
	 *  @return
	 */
//	@RequestMapping(value = "/checkWeb")
//    @ResponseBody
//	public String checkWebservicIsOk(){
//		Subject subject = SecurityUtils.getSubject();
//		Object principal = subject.getPrincipal();//获取用户登录信息
//		 try {
//		ClientImpl.getUserOrganization(principal.toString());	
//		 } catch (Exception e) {
//				System.out.println("链接webservice服务器失败："+e);
//				return "fail";
//			}
//		 return "ok";
//	}
	
	
	/**
	 *  拼权限字符串
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-2-6 上午9:38:48
	 *  @param nodeList
	 *  @param ids
	 *  @return
	 */
	public String findChildred(List<TreeNodeList> nodeList,String ids){
		for(int i=0;i<nodeList.size();i++){
    		TreeNodeList node =  nodeList.get(i);
    		ids = ids + "'"+node.getId()+"',";
    		if(node.getChildren()!=null){
    			if(node.getChildren().size()>0){
        			ids = findChildred(node.getChildren(),ids);
        		}
    		}
    	}
    	return ids;
	}

}
