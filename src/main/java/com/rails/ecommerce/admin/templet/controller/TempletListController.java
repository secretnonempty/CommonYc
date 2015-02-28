package com.rails.ecommerce.admin.templet.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.admin.utils.FileUtils;
import com.rails.ecommerce.admin.utils.StaticConfig;
import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.templet.domain.TempletList;
import com.rails.ecommerce.core.templet.service.TempletListService;

/**
 * 
 *  Class Name: TempletListController.java
 *  Function:
 *  
 *  Modifications:   
 *  
 *  @author zjf  DateTime 2015-1-15 下午3:15:09    
 *  @version 1.0
 */
@Controller
@RequestMapping(value="/templetList")
public class TempletListController {

	@Resource(name = "TempletListServiceImpl")
	private TempletListService templetListService;
	
	
	//private String ids= "";
	
	@RequestMapping(value = "/templetListInfo")
	public ModelAndView getColumnList(){	
		ModelAndView model = new ModelAndView();
		model.setViewName("/templet/templetList");
		return model;
	}
	
	
	/**
	 *  Function:获取模板初套装始化页面
	 *  @author zjf  DateTime 2015-1-13 上午10:46:13
	 *  @return  返回栏目列表json数据
	 * @throws Exception 
	 */
	@RequestMapping(value = "/templetList")
	@ResponseBody
	public PaginationList pageLis(HttpServletRequest request, HttpServletResponse response){
		String pageNo = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		String beginDate= request.getParameter("beginDate");
		String endDate= request.getParameter("endDate");
		String organization=request.getParameter("organization");
		//获取权限所有id集合
		HttpSession session =  request.getSession();
		String orgIds = (String) session.getAttribute("orgIds");
		PaginationList listpage = new PaginationList();
		try {
			listpage=templetListService.findAllPage(orgIds,organization,beginDate,endDate,Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listpage;
	}
	
	/**
	 *  Function:添加模板或编辑
	 *  @author zjf  DateTime 2015-1-9 下午9:02:18
	 *  @return
	 * @throws Exception 
	 */
	 @ResponseBody
	@RequestMapping(value = "/saveOrEdit")
	public String  AddTemplet(HttpServletRequest request){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		//System.out.println(df.format(new Date()));// new Date()为获取当前系统时间	
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();//获取用户登录信息
		
		String templetListName = request.getParameter("templetListName");
		String descript = request.getParameter("descript");
		String templetListUrl =StaticConfig.TEMPLETlISTPATH+"/"+templetListName;
		String keyid=request.getParameter("keyid");
		String userOrganization=request.getParameter("userOrganization");
		String templetListType=request.getParameter("templetListType");
		
		TempletList templetList=new TempletList();
		templetList.setTempletListName(templetListName);
		templetList.setDescript(descript);
		templetList.setTempletListUrl(templetListUrl);
		templetList.setTempletListType(templetListType);
		
		templetList.setCreateUser(principal.toString());
		templetList.setUnit(userOrganization);
		
		 if(keyid=="add"||keyid.equals("add")){//ID为add时进行添加
			try {
				templetList.setTempletListId(UUID.randomUUID().toString().replaceAll("-", ""));
				templetList.setCreateDate(df.parse(df.format(new Date())));
				templetList.setLastModifyDate(df.parse(df.format(new Date())));
				templetListService.save(templetList);
				//FileUtils.WriteToFile("", templetListUrl,templetListName);//创建模板路径
				FileUtils.creatDir(templetListUrl);
			} catch (ParseException e) {
				e.printStackTrace();
				return "fail";
			} catch (Exception e) {
				e.printStackTrace();
				return "fail";
			}
		}else{
				try {
					String createDateStr=request.getParameter("createDate");
					Date date = new Date(Long.parseLong(createDateStr));
					String createDate=df.format(date);
					Date cdate=df.parse(createDate);
					templetList.setCreateDate(cdate);
					templetList.setTempletListId(keyid);
					templetList.setLastModifyDate(df.parse(df.format(new Date())));
					templetListService.update(templetList);
					//FileUtils.WriteToFile("", templetListUrl,templetListName);//创建模板路径
					FileUtils.creatDir(templetListUrl);
				} catch (ParseException e) {
					e.printStackTrace();
					return "fail";
				} catch (Exception e) {
			       e.printStackTrace();
					return "fail";
				}
		   }
		return "success";
	}
	 
	 @ResponseBody
	@RequestMapping(value = "/templetListName")
	 public String getTempletName(HttpServletRequest request){
		 String templetListId=request.getParameter("templetListId");
		 TempletList tl = null;
		 String templetListName = null;
		 //long id= Integer.parseInt(templetListId);
		 try {
			 if(templetListId!="0"&&!templetListId.equals("0")&&templetListId!=""){
			 tl=templetListService.findById(templetListId);
			  templetListName=tl.getTempletListName();
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		 return templetListName;
	 }
	 
	 /** 
		 *  Function:删除一个模板套装
		 *  @author zjf  DateTime 2015-1-13 上午10:51:20
		 *  @return
		 */
		 @ResponseBody
		@RequestMapping(value = "/delete")
		public String delete(HttpServletRequest request){
			String  templetListId=request.getParameter("id");
			try {
				TempletList templetList=templetListService.findById(templetListId);			
				templetListService.delete(templetList);
			} catch (Exception e) {
				e.printStackTrace();
				return "faile";
			}
			 return "success";
		}
	
	/**
	 * 
	 *  Function:获取模板套装列表类弄为空时获取全部
	 * 
	 *  @author zjf  DateTime 2015-1-19 上午11:59:01
	 *  @return
	 *  @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="/comboboxList")
	public List<TempletList> getTempletList(HttpServletRequest request){
		String  templetListType=request.getParameter("index");
		System.out.print(templetListType);
		List<TempletList> ls = null;
		try {
			ls=templetListService.findTempletListByType(templetListType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ls;
	}
   
}
