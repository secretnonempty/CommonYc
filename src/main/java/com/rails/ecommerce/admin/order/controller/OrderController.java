package com.rails.ecommerce.admin.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.admin.webservice.client.ClientImpl;
import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.common.domain.TrainNumberView;
import com.rails.ecommerce.core.order.domain.OrderInfo;
import com.rails.ecommerce.core.order.service.OrderService;

@RequestMapping(value="/order")
@Controller
public class OrderController {
	
	@Resource(name = "OrderServiceImpl")
	private OrderService orderService;

	/**
	 *  页面跳转
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:05:45
	 *  @return
	 */
	@RequestMapping(value = "/orderinfo")
	public ModelAndView orderInfo() {
		ModelAndView model = new ModelAndView();
		model.setViewName("order/order_info");
		return model;
	}
	
	/**
	 *  页面跳转
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:05:45
	 *  @return
	 */
	@RequestMapping(value = "/addorder")
	public ModelAndView addOrderInfo() {
		ModelAndView model = new ModelAndView();
		model.setViewName("order/add_order");
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
	@RequestMapping(value = "/orderinfo/list")
	@ResponseBody
	public PaginationList pageList(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
		String pageNo = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");

		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		
		String organization=request.getParameter("organization");
		String trainNo= request.getParameter("trainNo");
		
		PaginationList listpage = new PaginationList();
		listpage = orderService.findAllPage(organization, trainNo, beginDate, endDate, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		return listpage;
	}
	
	/**
	 *  获得车次列表
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:06:04
	 *  @return
	 * @throws Exception 
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/getTrainCode")
    @ResponseBody
    public List<TrainNumberView> getOrganizationForTrainNo(HttpServletRequest request, HttpServletResponse response) {
		String carOrgId = request.getParameter("orgId");
		List<TrainNumberView> trainList = new ArrayList<TrainNumberView>();
		if(carOrgId != null && !"".equals(carOrgId)) {
			 try {
				 trainList = ClientImpl.getOrganizationForTrainNo(carOrgId);
			} catch (Exception e) {
				System.out.println("链接webservice服务器失败："+e);
			}
		}
		return trainList;
    }
	
	
	
	/**
	 *  ajax表单提交
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-13 下午7:34:10
	 *  @return
	 */
	@RequestMapping(value = "/addorder/submit")
	@ResponseBody
	public String submit(HttpServletRequest request, HttpServletResponse response) {

		OrderInfo oi = new OrderInfo();
//		gi.setGoodsTitle(request.getParameter("goodsTitle"));
//		gi.setPrice(Long.valueOf(request.getParameter("price")));
//		gi.setCreateDate(new Date());
//		gi.setLastModifyDate(new Date());
//		
//		gi.setGoodsSummary(request.getParameter("goodsSummary"));
//		gi.setState(request.getParameter("state"));
//		gi.setGoodsUnit(request.getParameter("goodsUnit"));
//		//gi.setGoodsDescript(request.getParameter("goodsDescript"));
//		gi.setTitleImgUrl(request.getParameter("titleImgUrl"));
//		gi.setSmallImgUrl(request.getParameter("smallImgUrl"));
//		gi.setImgUrl1(request.getParameter("imgUrl1"));
//		gi.setImgUrl2(request.getParameter("imgUrl2"));
//		
//		gi.setCreateUser("admin");
//		gi.setUnit("测试商家");
		
		try {
			orderService.save(oi);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "sucess";
	}

}
