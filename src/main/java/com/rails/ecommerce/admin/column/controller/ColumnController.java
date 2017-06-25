package com.rails.ecommerce.admin.column.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.admin.utils.FileUtils;
import com.rails.ecommerce.admin.utils.StaticConfig;
import com.rails.ecommerce.core.column.domain.Colume;
import com.rails.ecommerce.core.column.service.ColumeService;
import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.common.domain.TreeList;
import com.rails.ecommerce.core.publishgoods.service.PublishGoodsService;

/**
 * 
 *  Class Name: ColumnController.java
 *  Function:栏目列表类  
 *  Modifications:    
 *  @author zjf  DateTime 2015-1-9 下午8:23:18    
 *  @version 1.0
 */
@RequestMapping(value="/column")
@Controller
public class ColumnController {

	@Resource(name = "ColumeServiceImpl")
	private ColumeService columeService;
	
	@Resource(name = "PublishGoodsServiceImpl")
	private PublishGoodsService publishGoodsService;
	
	
	
	/**
	 *  Function:进入栏目列表页面
	 *  @author zjf  DateTime 2015-1-9 下午9:02:18
	 *  @return
	 */
	@RequestMapping(value = "/columnInfo")
	public ModelAndView getColumn(HttpServletRequest request,
		ModelAndView model){
		model.setViewName("/column/list");	
		return model;
	}
	
	/**
	 *  Function:进入添加栏目页面
	 *  @author zjf  DateTime 2015-1-12 下午8:19:40
	 *  @return
	 */
	@RequestMapping(value="/addColumn")
	public ModelAndView add(){
		ModelAndView model = new ModelAndView();
		model.setViewName("/column/add");
		return model;
	}
	
	/**
	 *  Function:获取栏初始化页面
	 *  @author zjf  DateTime 2015-1-13 上午10:46:13
	 *  @return  返回栏目列表json数据
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
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
			listpage=columeService.findAllPage(orgIds,organization,beginDate,endDate,Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  listpage;
	}
	

	 	 
	 /**
		 *  Function:编辑Or添加栏目
		 *  @author zjf  DateTime 2015-1-9 下午9:02:18
		 *  @return
		 * @throws Exception 
		 */
		 @ResponseBody
		@RequestMapping(value = "/editOrAdd")
		public String  editColumn(HttpServletRequest request,HttpServletResponse response){
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			    System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
			    Subject subject = SecurityUtils.getSubject();
				Object principal = subject.getPrincipal();//获取用户登录信息
			 
			    String name = request.getParameter("name");//模板名称
			    String type=request.getParameter("templetListType");
				String code = request.getParameter("code");
				String isShow = request.getParameter("isShow");
				String order= request.getParameter("order");
				String isDefault= request.getParameter("isDefault");
				String userOrganization=request.getParameter("userOrganization");
				String pidStr=request.getParameter("pid");
				String templetListId= request.getParameter("templetListId");
				String imageUrl=request.getParameter("imageUrl");
				String descript= request.getParameter("descript");
				
				Colume colume=new Colume();
				colume.setTempletListId(templetListId);
				colume.setName(name);
				colume.setPid(pidStr);
				colume.setCode(code);
				colume.setOrder(order);
				colume.setIsShow(isShow);
				colume.setImageUrl(imageUrl);
				colume.setIsDefault(isDefault);
				colume.setDescript(descript);
				colume.setCreateUser(principal.toString());//从登录用户中取得
				colume.setUnit(userOrganization);
				colume.setTempletListType(type);
			     String keyid=request.getParameter("keyid");
			 if(keyid=="add"||keyid.equals("add")){//ID为add时进行添加
				 try {
					 	colume.setColumnId(UUID.randomUUID().toString().replaceAll("-", ""));
					 	colume.setCreateDate(df.parse(df.format(new Date())));
					 	colume.setLastModifyDate(df.parse(df.format(new Date())));
						columeService.save(colume);
					} catch (ParseException e) {		
						e.printStackTrace();
						return "fail";
					} catch (Exception e) {
						e.printStackTrace();
						return "fail";
					}	
			 }else{
				 colume.setColumnId(request.getParameter("keyid"));
				 try {
					 String createDateStr=request.getParameter("createDate");
						Date date = new Date(Long.parseLong(createDateStr));
						String createDate=df.format(date);
						Date cdate=df.parse(createDate);
						
						colume.setCreateDate(cdate);
						colume.setLastModifyDate(df.parse(df.format(new Date())));
						columeService.update(colume);
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
	/** 
	 *  Function:删除一个栏目
	 *  @author zjf  DateTime 2015-1-13 上午10:51:20
	 *  @return
	 */
	 @ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request){
	
		try {
			if(publishGoodsService.findByColumnId(request.getParameter("id")).size()!=0){//判断该栏目下有没有发布商品
				return "cantDelete";
			}
			Colume colume=columeService.findById(request.getParameter("id"));			
			columeService.delete(colume);
		} catch (Exception e) {
			e.printStackTrace();
			return "faile";
		}
		 return "success";
	}
	
	 
	 /**
	  * 
	  *  Function:根据上级ID获取上级
	  * 
	  *  @author zjf  DateTime 2015-1-30 下午7:22:32
	  *  @param request
	  *  @return
	  */
	 @ResponseBody
		@RequestMapping(value = "/pidName",produces="text/html;charset=UTF-8")
		 public String getPidName(HttpServletRequest request){
			 String pid=request.getParameter("pid");
			 Colume tl = null;
			 String pidName = "";
			 try {
				 if(pid!=""&&!pid.equals("null")){
				 tl=columeService.findById(pid);
				 pidName=tl.getName();
				 }
			} catch (Exception e) {
				e.printStackTrace();
				return "fail";
			}
			 return pidName;
		 }
	 
	 
	 /**
		 *  图片上传
		 *  Function:
		 *  @author zjf  DateTime 2015-1-13 下午7:34:10
		 *  @return
		 */
		@RequestMapping(value = "/imgupload")
		@ResponseBody
		public String imgupload(HttpServletRequest request, HttpServletResponse response) {
			
			String retxt ="";
	        String realDir = request.getSession().getServletContext().getRealPath("");  
	        String contextpath = request.getContextPath();  
	        String basePath = request.getScheme() + "://"  
	        + request.getServerName() + ":" + request.getServerPort()  
	        + contextpath + "/";  
	        try {  
	        String filePath = StaticConfig.CREATTEMPLETPATH+"/"+"colunmImages";  
	        String realPath = realDir+"\\"+filePath;  
	        //判断路径是否存在，不存在则创建  
	        File dir = new File(realPath);  
	        if(!dir.isDirectory())  
	            dir.mkdir();  
	        if(ServletFileUpload.isMultipartContent(request)){  
	            DiskFileItemFactory dff = new DiskFileItemFactory();  
	            dff.setRepository(dir);  
	            dff.setSizeThreshold(1024000);  
	            ServletFileUpload sfu = new ServletFileUpload(dff);  
	            FileItemIterator fii = null;  
	            fii = sfu.getItemIterator(request);  
	            String title = "";   //图片标题  
	            String url = "";    //图片地址  
	            String fileName = "";  
	            String state="SUCCESS";  
	            String realFileName=""; 
	           
	            while(fii.hasNext()){  
	                FileItemStream fis = fii.next();  
	                try{  
	                    if(!fis.isFormField() && fis.getName().length()>0){  
	                        fileName = fis.getName();  
	                        Pattern reg=Pattern.compile("[.]jpg|png|jpeg|gif$");  
	                        Matcher matcher=reg.matcher(fileName);  
	                        if(!matcher.find()) {  
	                            state = "type_error";  
	                            break;  
	                        }  
	                        realFileName = new Date().getTime()+fileName.substring(fileName.lastIndexOf("."),fileName.length());  
	                        url = realPath+"\\"+realFileName;  
	                        String imageUploadUrl=filePath+"/"+realFileName;
	                        BufferedInputStream in = new BufferedInputStream(fis.openStream());//获得文件输入流  
	                        FileOutputStream a = new FileOutputStream(new File(url));  
	                        BufferedOutputStream output = new BufferedOutputStream(a);  
	                        Streams.copy(in, output, true);//开始把文件写到你指定的上传文件夹  
	                        a.flush();
	                        output.flush();
	                        a.close();
	                        in.close();
	                        output.close();
	                        FileUtils.copyFile(url, StaticConfig.NEWPATH+"/"+"colunmImages"+"/"+realFileName);//拷贝文件到指定目
	                    }else{  
	                        String fname = fis.getFieldName();  
	  
	                        if(fname.indexOf("pictitle")!=-1){  
	                            BufferedInputStream in = new BufferedInputStream(fis.openStream());  
	                            byte c [] = new byte[10];  
	                            int n = 0;  
	                            while((n=in.read(c))!=-1){  
	                                title = new String(c,0,n);  
	                                break;  
	                            }
	                            in.close();
	                        }
	                    } 
	                }catch(Exception e){  
	                    e.printStackTrace();  
	                }  
	            }  
	            //retxt ="{src:'"+basePath+filePath+"/"+realFileName+"',status:'"+state+"'}";  
	            //retxt ="{src:'"+filePath+"/"+realFileName+"',status:'"+state+"'}";  
	            retxt =filePath+"/"+realFileName;  
	        }  
	        }catch(Exception ee) {  
	        	retxt ="error";
	            ee.printStackTrace();  
	        } 
	        return retxt;
		}


	 /**
	  * 
	  *  Function:获取栏目上级目录下拉列表
	  * 
	  *  @author zjf  DateTime 2015-1-16 上午10:54:12
	  *  @return
	  *  @throws Exception
	  */
	 @ResponseBody
	 @RequestMapping(value="/pidList")
	 public List<Colume> getPidList() throws Exception{
		 return columeService.findAll();
	 }
	 
	 
	 
	 /**
		 *  获取栏目树
		 *  Function:
		 * 
		 *  @author zjf  DateTime 2015-1-15 下午2:18:28
		 *  @param request
		 *  @return
		 */
		@ResponseBody
		@RequestMapping(value = "/columntree")
		public List<TreeList> columntree(HttpServletRequest request){
			List<TreeList> treeList = null;
			try {
				treeList = columeService.findAllPid("");
			}  catch (Exception e) {
				e.printStackTrace();
			}
	        return treeList;
		}
	 
	public ColumeService getColumeService() {
		return columeService;
	}

	public void setColumeService(ColumeService columeService) {
		this.columeService = columeService;
	}

}
