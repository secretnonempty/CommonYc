package com.rails.ecommerce.admin.publish.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.core.column.service.ColumeService;
import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.publishgoods.service.SelectGoodsService;
import com.rails.ecommerce.core.templet.service.TempletService;

@RequestMapping(value="/select")
@Controller
public class SelectController {
	
	@Resource(name = "SelectGoodsServiceImpl")
	private SelectGoodsService selectGoodsService;
	
	@Resource(name = "ColumeServiceImpl")
	private ColumeService columeService;
	
	@Resource(name = "TempletServiceImpl")
	private TempletService templetService;
	

	/**
	 *  页面跳转
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:05:45
	 *  @return
	 */
	@RequestMapping(value = "/selectgoods")
	public ModelAndView selectgoods() {
		ModelAndView model = new ModelAndView();
		model.setViewName("publish/select_goods");
		return model;
	}
	
	@RequestMapping(value = "/selectcolumn")
	public ModelAndView selectcolumn() {
		ModelAndView model = new ModelAndView();
		model.setViewName("publish/select_column");
		return model;
	}
	
	@RequestMapping(value = "/selecttemplet")
	public ModelAndView selecttemplet() {
		ModelAndView model = new ModelAndView();
		model.setViewName("publish/select_templet");
		return model;
	}
	
	
	/**
	 *  获得初始化列表
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:06:04
	 *  @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@RequestMapping(value = "/selectgoods/list")
	@ResponseBody
	public PaginationList selectgoods(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
		String pageNo = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");

		String goodsTitle= request.getParameter("goodsTitle");
		if(goodsTitle!=null){
			goodsTitle = new String(goodsTitle.getBytes("iso8859-1"),"utf-8");
		}

		String state= request.getParameter("state");
		String beginDate= request.getParameter("beginDate");
		String endDate= request.getParameter("endDate");
		
		String columnId= request.getParameter("columnId");
		
		PaginationList listpage = new PaginationList();
		return selectGoodsService.findAllPage(columnId,goodsTitle,state,beginDate,endDate,Integer.valueOf(pageNo), Integer.valueOf(pageSize));
	}
	

	/**
	 * 获得初始化列表
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-31 上午10:36:02
	 *  @param request
	 *  @param response
	 *  @return
	 */
	@RequestMapping(value = "/selectcolumn/list")
	@ResponseBody
	public PaginationList selectcolumn(HttpServletRequest request, HttpServletResponse response){
		String pageNo = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		String beginDate= request.getParameter("beginDate");
		String endDate= request.getParameter("endDate");
		String organization=request.getParameter("organization");
		PaginationList listpage = new PaginationList();
		try {
			listpage=columeService.findAllPage("",organization,beginDate,endDate,Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  listpage;
	}
	
	/**
	 *  Function:获取模板初始化页面
	 *  @author zjf  DateTime 2015-1-13 上午10:46:13
	 *  @return  返回栏目列表json数据
	 * @throws Exception 
	 */
	@RequestMapping(value = "/selecttemplet/list")
	@ResponseBody
	public PaginationList selecttemplet(HttpServletRequest request, HttpServletResponse response){
		String pageNo = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		String beginDate= request.getParameter("beginDate");
		String endDate= request.getParameter("endDate");
		String organization=request.getParameter("organization");
		PaginationList listpage = new PaginationList();
		try {
			listpage=templetService.findAllPage("","","",organization,beginDate,endDate,Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listpage;
	}
	

}
