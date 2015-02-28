package com.rails.ecommerce.admin.goods.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.goodsinfo.domain.GoodsInfo;
import com.rails.ecommerce.core.goodsinfo.service.GoodsInfoService;

@RequestMapping(value="/goods")
@Controller
public class GoodsInfoController {
	
	@Resource(name = "GoodsInfoServiceImpl")
	private GoodsInfoService goodsInfoService;

	/**
	 *  页面跳转
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-12 下午2:05:45
	 *  @return
	 */
	@RequestMapping(value = "/goodsinfo")
	public ModelAndView goodsinfo() {
		ModelAndView model = new ModelAndView();
		model.setViewName("goods/goods_info");
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
	@RequestMapping(value = "/addgoodsinfo")
	public String addgoodsinfo(HttpServletRequest request, HttpServletResponse response,Model model) throws NumberFormatException, Exception {
		
		if(request.getParameter("goodsInfoId")!=null){ //编辑状态
			GoodsInfo goodsInfo = goodsInfoService.findById(request.getParameter("goodsInfoId"));
			goodsInfo.setTitleImgUrl(initImgUrl(goodsInfo.getTitleImgUrl()));
			goodsInfo.setSmallImgUrl(initImgUrl(goodsInfo.getSmallImgUrl()));
			goodsInfo.setImgUrl1(initImgUrl(goodsInfo.getImgUrl1()));
			goodsInfo.setImgUrl2(initImgUrl(goodsInfo.getImgUrl2()));
			model.addAttribute("goodsInfo", goodsInfo);
		}else{   //新增状态
			GoodsInfo goodsInfo = new GoodsInfo();
			goodsInfo.setTitleImgUrl(initImgUrl(goodsInfo.getTitleImgUrl()));
			goodsInfo.setSmallImgUrl(initImgUrl(goodsInfo.getSmallImgUrl()));
			goodsInfo.setImgUrl1(initImgUrl(goodsInfo.getImgUrl1()));
			goodsInfo.setImgUrl2(initImgUrl(goodsInfo.getImgUrl2()));
			goodsInfo.setState("1");
			goodsInfo.setGoodsUnit("1");
			model.addAttribute("goodsInfo",goodsInfo);
		}
		return "goods/add_goods_info";
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
	@RequestMapping(value = "/goodsinfo/list")
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
		
		PaginationList listpage = new PaginationList();
		return goodsInfoService.findAllPage(goodsTitle,state,beginDate,endDate,Integer.valueOf(pageNo), Integer.valueOf(pageSize));
	}
	
	/**
	 *  ajax表单提交
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-13 下午7:34:10
	 *  @return
	 */
	@RequestMapping(value = "/addgoodsinfo/submit")
	@ResponseBody
	public String submit(HttpServletRequest request, HttpServletResponse response) {
		String state="";
		boolean isEdit =false;
		if(request.getParameter("goodsInfoId")!=null && !"".equals(request.getParameter("goodsInfoId")) && !"0".equals(request.getParameter("goodsInfoId"))){
			isEdit = true;
		}
		try {
			GoodsInfo gi = new GoodsInfo();
			if(isEdit){
				gi.setGoodsInfoId(request.getParameter("goodsInfoId"));
			}else{
				gi.setGoodsInfoId(UUID.randomUUID().toString().replaceAll("-", ""));
			}
			
			gi.setGoodsTitle(request.getParameter("goodsTitle"));
			gi.setPrice(Long.valueOf(request.getParameter("price")));
			if(!isEdit){
				gi.setCreateDate(new Date());
			}else{
				gi.setCreateDate(StrToDate(request.getParameter("createDate")));
			}
			gi.setLastModifyDate(new Date());	
			gi.setGoodsSummary(request.getParameter("goodsSummary"));
			gi.setState(request.getParameter("state"));
			gi.setGoodsUnit(request.getParameter("goodsUnit"));
			gi.setGoodsDescript(request.getParameter("goodsDescript"));
			gi.setTitleImgUrl(getImageUrl(request.getParameter("imgUrl_title")));
			gi.setSmallImgUrl(getImageUrl(request.getParameter("imgUrl_small")));
			gi.setImgUrl1(getImageUrl(request.getParameter("imgUrl_img1")));
			gi.setImgUrl2(getImageUrl(request.getParameter("imgUrl_img2")));
			gi.setImgUrl3(getImageUrl(request.getParameter("imgUrl_img3")));
			gi.setImgUrl4(getImageUrl(request.getParameter("imgUrl_img4")));
			gi.setImgUrl5(getImageUrl(request.getParameter("imgUrl_img5")));
			
			gi.setTitleImgName(getImageName(gi.getTitleImgUrl()));
			gi.setSmallImgName(getImageName(gi.getSmallImgUrl()));
			gi.setImgName1(getImageName(gi.getImgUrl1()));
			gi.setImgName2(getImageName(gi.getImgUrl2()));
			gi.setImgName3(getImageName(gi.getImgUrl3()));
			gi.setImgName4(getImageName(gi.getImgUrl4()));
			gi.setImgName5(getImageName(gi.getImgUrl5()));
			
//			Subject subject = SecurityUtils.getSubject();
//			Object principal = subject.getPrincipal();//获取用户登录信息
//			gi.setCreateUser(principal.toString());
//			gi.setUnit(request.getParameter("unit"));
			
			
			gi.setCreateUser("xiaoming");
			gi.setUnit("66");
			if(isEdit){
				goodsInfoService.update(gi);
			}else{
				goodsInfoService.save(gi);
			}
			
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
	@RequestMapping(value = "/goodsinfo/delete")
	public String delete(HttpServletRequest request){
		String goodsInfoId=request.getParameter("goodsInfoId");
		try {
			GoodsInfo goodsInfo=goodsInfoService.findById(goodsInfoId);
			goodsInfoService.delete(goodsInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return "faile";
		}
		return "success";
	}
	
	/**
	 *  图片上传
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-13 下午7:34:10
	 *  @return
	 */
	@RequestMapping(value = "/addgoodsinfo/imgupload")
	@ResponseBody
	public String imgupload(HttpServletRequest request, HttpServletResponse response) {
		
		String retxt ="";
		try {
			retxt = goodsInfoService.imageUpload(request);
		} catch (Exception e) {
			retxt = "error";
			e.printStackTrace();
		}
        return retxt;
	}
	
	/**
	 * 初始化url
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-16 下午3:32:51
	 *  @param imgUrl
	 *  @return
	 */
	public String initImgUrl(String imgUrl){
		if(imgUrl !=null && !"".equals(imgUrl)){
			return imgUrl;
		}
		return "resources/images/yulan.bmp";
	}
	
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date StrToDate(String str) {
	 
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	
	/**
	 * 获取文件名称
	 *  Function:
	 * 
	 *  @author gxl  DateTime 2015-1-29 下午5:08:16
	 *  @param url
	 *  @return
	 */
	public String getImageName(String url){
		String imageName = "";
		//String uploadUrl = ReadProperties.uploadUrl+"/images";
		if(url!=null && !"".equals(url)){
			//imageName = url.replace(uploadUrl, "").replace("/", "");
			imageName = url.substring(url.lastIndexOf("/")+1,url.length());
		}
		return imageName;
	}
	
	public String getImageUrl(String url){
		String imageUrl = "";
		if(url!=null && !"".equals(url)){
			if(url.indexOf("resources/images/yulan.bmp")==-1){
				imageUrl =  url;
			}
		}
		return imageUrl;
	}

}
