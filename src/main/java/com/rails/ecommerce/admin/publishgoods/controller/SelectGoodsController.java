package com.rails.ecommerce.admin.publishgoods.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.publishgoods.service.SelectGoodsService;

@RequestMapping(value="/selectgoods")
@Controller
public class SelectGoodsController {
	
	@Resource(name = "SelectGoodsServiceImpl")
	private SelectGoodsService selectGoodsService;

	/**
	 *  页面跳转
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:05:45
	 *  @return
	 */
	@RequestMapping(value = "/selectgoods")
	public ModelAndView goodsinfo() {
		ModelAndView model = new ModelAndView();
		model.setViewName("publishgoods/select_goods");
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
	public PaginationList pageList(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
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
	

}
