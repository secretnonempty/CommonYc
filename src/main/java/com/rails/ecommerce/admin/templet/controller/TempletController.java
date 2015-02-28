package com.rails.ecommerce.admin.templet.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.rails.ecommerce.admin.utils.FileUtils;
import com.rails.ecommerce.admin.utils.StaticConfig;
import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.templet.domain.Templet;
import com.rails.ecommerce.core.templet.service.TempletListService;
import com.rails.ecommerce.core.templet.service.TempletService;

/**
 * 
 *  Class Name: ColumnController.java
 *  Function:模板管理类  
 *  Modifications:    
 *  @author zjf  DateTime 2015-1-9 下午8:23:18    
 *  @version 1.0
 */

@Controller
@RequestMapping(value="/templet")
public class TempletController {

	@Resource(name = "TempletServiceImpl")
	private TempletService templetService;
	
	@Resource(name = "TempletListServiceImpl")
	private TempletListService templetListService;
	/**
	 *  Function:获取模板列表
	 *  @author zjf  DateTime 2015-1-9 下午9:01:03
	 *  @return
	 */
	@RequestMapping(value = "/templetInfo")
	public ModelAndView getColumnList(){	
		ModelAndView model = new ModelAndView();
		model.setViewName("/templet/list");
		return model;
	}
	
	/**
	 *  Function:模板添加页面
	 *  @author zjf  DateTime 2015-1-9 下午9:02:18
	 *  @return
	 */
	@RequestMapping(value = "/addTemplet")
	public ModelAndView getAddColumn(){		
		ModelAndView model = new ModelAndView();
		model.setViewName("/templet/add");
		return model;
	}
	
	
	@RequestMapping(value = "/showTemplet")
	public ModelAndView showTemplet(){		
		ModelAndView model = new ModelAndView();
		model.setViewName("resources/creatTemplets/indexList/listPage");
		return model;
	}
	
	
	/**
	 *  Function:获取模板初始化页面
	 *  @author zjf  DateTime 2015-1-13 上午10:46:13
	 *  @return  返回栏目列表json数据
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PaginationList pageLis(HttpServletRequest request, HttpServletResponse response){
		//获取权限所有id集合
		HttpSession session =  request.getSession();
		String orgIds = (String) session.getAttribute("orgIds");
		String pageNo = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		String beginDate= request.getParameter("beginDate");
		String endDate= request.getParameter("endDate");
		String organization=request.getParameter("organization");
		String templetListId=request.getParameter("templetListId");
		String templetType=request.getParameter("templetType");
		System.out.print(templetType);
		
		PaginationList listpage = new PaginationList();
		try {
			listpage=templetService.findAllPage(templetType,orgIds,templetListId,organization,beginDate,endDate,Integer.valueOf(pageNo), Integer.valueOf(pageSize));
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
		 String realDir = request.getSession().getServletContext().getRealPath(""); //系统目录
		
		String name = request.getParameter("name");
		String extension=request.getParameter("extension");
		System.out.print(extension);
		String descript = request.getParameter("descript");
		String templetListId = request.getParameter("templetListId");
		String content= request.getParameter("content");
		String keyid=request.getParameter("keyid");
		String userOrganization=request.getParameter("userOrganization");
		
		Templet templet=new Templet();
		templet.setName(name);
		templet.setDescript(descript);
		templet.setTempletListId(templetListId);
		templet.setContent(content);
		templet.setCreateUser(principal.toString());
		templet.setUnit(userOrganization);
		templet.setExtension(extension);

		try {
		     String url=templetListService.findById(templetListId).getTempletListUrl();//根据套套装ID获取URL
		     String templetListName=templetListService.findById(templetListId).getTempletListName();//根据套套装ID获取文件夹名
		     templet.setUrl(url);
		 if(keyid=="add"||keyid.equals("add")){//ID为add时进行添加
				templet.setTempletId(UUID.randomUUID().toString().replaceAll("-", ""));
				templet.setCreateDate(df.parse(df.format(new Date())));
				templet.setLastModifyDate(df.parse(df.format(new Date())));
				templetService.save(templet);
		}else{
					String createDateStr=request.getParameter("createDate");
					Date date = new Date(Long.parseLong(createDateStr));
					String createDate=df.format(date);
					Date cdate=df.parse(createDate);
					templet.setCreateDate(cdate);
					templet.setTempletId(keyid);
					templet.setLastModifyDate(df.parse(df.format(new Date())));
					templetService.update(templet);
		   }
			
			if(extension.equals("html")){
				FileUtils.WriteToFile(content, url,name+"."+extension);//把模板内容写成文件
				FileUtils.WriteToFile(content, realDir+"\\"+StaticConfig.CREATTEMPLETPATH+"\\"+templetListName,name+"."+extension);//生成到系统目录下以便浏览
			}
			if(extension.equals("js")){
				FileUtils.WriteToFile(content, url+"\\"+extension,name+"."+extension);//把模板内容写成文件
				String pateh=realDir+"\\"+StaticConfig.CREATTEMPLETPATH+"\\"+templetListName+StaticConfig.JSFIlETPATH;
				FileUtils.WriteToFile(content, pateh,name+"."+extension);
			}
			if(extension.equals("css")){
				FileUtils.WriteToFile(content, url+"\\"+extension,name+"."+extension);//把模板内容写成文件
				String pateh=realDir+"\\"+StaticConfig.CREATTEMPLETPATH+"\\"+templetListName+StaticConfig.CSSFILEPATH;
				FileUtils.WriteToFile(content, pateh,name+"."+extension);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			return "fail";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	 
	 @ResponseBody
		@RequestMapping(value = "/getNameById")
	 public String getNameById(HttpServletRequest request){
		 String templetListId=request.getParameter("templetListId");
		 String templetListName = null;
		try {
			templetListName = templetListService.findById(templetListId).getTempletListName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return templetListName;
	 }
	 
	 /** 
		 *  Function:删除一个模板
		 *  @author zjf  DateTime 2015-1-13 上午10:51:20
		 *  @return
		 */
		 @ResponseBody
		@RequestMapping(value = "/delete")
		public String delete(HttpServletRequest request){
			String  templetId=request.getParameter("id");
			try {
				Templet templet=templetService.findById(templetId);			
				templetService.delete(templet);
			} catch (Exception e) {
				e.printStackTrace();
				return "faile";
			}
			 return "success";
		}
}
