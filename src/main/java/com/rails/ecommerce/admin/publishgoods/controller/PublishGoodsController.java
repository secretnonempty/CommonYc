package com.rails.ecommerce.admin.publishgoods.controller;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.core.column.service.ColumeService;
import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.common.domain.TreeList;
import com.rails.ecommerce.core.publishgoods.domain.PublishGoods;
import com.rails.ecommerce.core.publishgoods.service.PublishGoodsService;

@RequestMapping(value="/publishgoods")
@Controller
public class PublishGoodsController {
	
	@Resource(name = "ColumeServiceImpl")
	private ColumeService columeService;
	
	@Resource(name = "PublishGoodsServiceImpl")
	private PublishGoodsService publishGoodsService;
	

	/**
	 *  页面跳转
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:05:45
	 *  @return
	 */
	@RequestMapping(value = "/publishgoods")
	public ModelAndView goodsinfo() {
		ModelAndView model = new ModelAndView();
		model.setViewName("publishgoods/publish_goods");
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
	@RequestMapping(value = "/publishgoods/list")
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
		return publishGoodsService.findAllPage(columnId,goodsTitle,state,beginDate,endDate,Integer.valueOf(pageNo), Integer.valueOf(pageSize));
	}



	/**
	 *  获取栏目树
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-15 下午2:18:28
	 *  @param request
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value = "/publishgoods/columntree")
	public List<TreeList> columntree(HttpServletRequest request){
		List<TreeList> treeList = null;
		try {
			treeList = columeService.findAllPid("");
		}  catch (Exception e) {
			e.printStackTrace();
		}
        return treeList;
	}
	
	/**
	 *  发布商品
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-15 下午2:18:28
	 *  @param request
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value = "/publishgoods/savepublish")
	public String savepublish(HttpServletRequest request){
		String selectGoodsIndex = request.getParameter("selectGoodsIndex");
		String columnId = request.getParameter("columnId");
		String message = "";
		try {
			String [] stringArr= selectGoodsIndex.split(",");
			for(int i=0;i<stringArr.length;i++){
				PublishGoods pg = new PublishGoods();
				pg.setPublishGoodsId(UUID.randomUUID().toString().replaceAll("-", ""));
				pg.setColumnId(columnId);
				pg.setGoodsInfoId(stringArr[i]);
				publishGoodsService.save(pg);
			}
			message = "success";
		}  catch (Exception e) {
			e.printStackTrace();
		}
        return message;
	}
	
	/**
	 *  取消发布产品
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-15 下午2:18:28
	 *  @param request
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value = "/publishgoods/delpublish")
	public String delpublish(HttpServletRequest request){
		String goodsInfoId=request.getParameter("goodsInfoId");
		String columnId=request.getParameter("columnId");
		try {
			List<PublishGoods> pubs=publishGoodsService.findByColumnGoodsInfoId(goodsInfoId,columnId);
			if(pubs.size()>0){
				for(int i=0;i<pubs.size();i++){
					publishGoodsService.delete(pubs.get(i));
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return "faile";
		}
		return "success";
	}

}
