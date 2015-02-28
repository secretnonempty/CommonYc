package com.rails.ecommerce.admin.publish.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.admin.utils.FileUtils;
import com.rails.ecommerce.admin.webservice.client.ClientImpl;
import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.common.domain.TrainNumberView;
import com.rails.ecommerce.core.publish.domain.Publish;
import com.rails.ecommerce.core.publish.service.PublishService;

@RequestMapping(value="/publish")
@Controller
public class PublishController {
	
	@Resource(name = "PublishServiceImpl")
	private PublishService publishService;


	/**
	 *  页面跳转
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:05:45
	 *  @return
	 */
	@RequestMapping(value = "/publish")
	public ModelAndView view() {
		ModelAndView model = new ModelAndView();
		model.setViewName("publish/publish");
		return model;
	}
	
	/**
	 *  页面跳转
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:05:45
	 *  @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@RequestMapping(value = "/addpublish")
	public String addgoodsinfo(HttpServletRequest request, HttpServletResponse response,Model model) throws NumberFormatException, Exception {
		
		return "publish/add_publish";

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
	@RequestMapping(value = "/publish/list")
	@ResponseBody
	public PaginationList pageList(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
		String pageNo = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");

		String name= request.getParameter("name");
		if(name!=null){
			name = new String(name.getBytes("iso8859-1"),"utf-8");
		}

		String trainNo= request.getParameter("trainNo");
		String beginDate= request.getParameter("beginDate");
		String endDate= request.getParameter("endDate");
		
		PaginationList listpage = new PaginationList();
		return publishService.findAllPage(name,trainNo,beginDate,endDate,Integer.valueOf(pageNo), Integer.valueOf(pageSize));
	}
	
	/**
	 *  ajax表单提交
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-13 下午7:34:10
	 *  @return
	 */
	@RequestMapping(value = "/addpublish/submit")
	@ResponseBody
	public String submit(HttpServletRequest request, HttpServletResponse response) {
		String state="";
		try {
			
			//从接口中获取车次信息
			List<TrainNumberView> trains =  getOrganizationForTrainNo(request,response);
			TrainNumberView trainView = new TrainNumberView();
			for(int i = 0;i<trains.size();i++){
				TrainNumberView view = trains.get(i);
				if(view.getTrainNumber().equals(request.getParameter("trainNo"))){
					trainView = view;
					break;
				}
			}

			//压缩包
			String zipUrl  = publishService.packagePublish(request,trainView);
			if("".equals(zipUrl)){
				return "zipError";
			}
			
			Publish pub = new Publish();
			pub.setPublishId(UUID.randomUUID().toString().replaceAll("-", ""));
			pub.setTrainNo(request.getParameter("trainNo"));
			pub.setName(request.getParameter("name"));
			pub.setUnit(request.getParameter("unit"));
			pub.setVersion(request.getParameter("version"));
			pub.setDescript(request.getParameter("descript"));
			pub.setJsonUrl("");
			pub.setZipUrl(zipUrl);
			HttpSession session  =  request.getSession();
			pub.setCreateUser((String) session.getAttribute("userName"));
			pub.setCreateDate(new Date());

			publishService.save(pub);
			
			state = "sucess";
		} catch (Exception e) {
			state = "error";
			e.printStackTrace();
		}
		return state;
	}
	
	/**
	 *  删除
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-15 下午2:18:28
	 *  @param request
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value = "/publish/delete")
	public String delete(HttpServletRequest request){
		String publishId=request.getParameter("publishId");
		try {
			Publish pub=publishService.findById(publishId);
			boolean deleteState = FileUtils.deleteFile(request.getSession().getServletContext().getRealPath("")+"/"+pub.getZipUrl());
			if(!deleteState){
				return "fileDelete";
			}
			publishService.delete(pub);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return "faile";
		}
		return "success";
	}
	
	/**
	 *  生成版本号
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-28 下午7:55:12
	 *  @param request
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value = "/publish/version")
	public String version(HttpServletRequest request){
		String defaultVer = "1";
		try {
			List<Publish> pubs =publishService.findTrainNo(request.getParameter("trainNo"));
			if(pubs.size()>0){
				if(pubs.get(0).getVersion()!=null && !"".equals(pubs.get(0).getVersion())){
					long l = Long.parseLong(pubs.get(0).getVersion())+1;
					defaultVer = String.valueOf(l);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "faile";
		}
		return defaultVer;
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
		String carOrgId = request.getParameter("unit");
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
	
	

}
