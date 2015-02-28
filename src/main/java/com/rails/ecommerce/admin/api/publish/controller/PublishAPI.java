package com.rails.ecommerce.admin.api.publish.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.rails.ecommerce.admin.api.form.ApiFrom;
import com.rails.ecommerce.core.filemanage.domain.FileTransfer;
import com.rails.ecommerce.core.filemanage.service.FileTransferService;
import com.rails.ecommerce.core.publish.domain.Publish;
import com.rails.ecommerce.core.publish.domain.PublishApi;
import com.rails.ecommerce.core.publish.service.PublishService;

/**
 * 
 *  Class Name: PublishAPI.java
 *  Function:打包文件文件接口
 *  
 *  Modifications:   
 *  
 *  @author allen  DateTime 2015-2-2 下午8:05:58    
 *  @version 1.0
 */
@Component
@Path("publish")
public class PublishAPI {
	
	@Resource(name = "PublishServiceImpl")
	private PublishService publishService;
	
	@Resource(name = "FileTransferServiceImpl")
	private FileTransferService fileTransferService;
	
	@Path("publishList")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	public ApiFrom publishNowList(@Context HttpServletRequest request){
		String railsName = request.getParameter("railsName");
		
		String updataDate = request.getParameter("updataDate");
		
		ApiFrom apiFrom = new ApiFrom();
		
		List<Publish> publishList = publishService.findDownload(railsName,updataDate);
		List<PublishApi> publishListApi = new ArrayList<PublishApi>();
		for(int i=0;i<publishList.size();i++){
			List<FileTransfer> fileList = fileTransferService.findFileTransfer(publishList.get(i).getName());
			
			PublishApi publishApi = new PublishApi();
			publishApi.setFileSize(fileList.get(0).getFileSize());
			publishApi.setMD5String(fileList.get(0).getMD5String());
			publishApi.setCreateDate(publishList.get(i).getCreateDate());
			publishApi.setCreateUser(publishList.get(i).getCreateUser());
			publishApi.setDescript(publishList.get(i).getDescript());
			publishApi.setJsonUrl(publishList.get(i).getJsonUrl());
			publishApi.setLastModifyDate(publishList.get(i).getLastModifyDate());
			publishApi.setName(publishList.get(i).getName());
			publishApi.setPublishId(publishList.get(i).getPublishId());
			publishApi.setTrainNo(publishList.get(i).getTrainNo());
			publishApi.setUnit(publishList.get(i).getUnit());
			publishApi.setVersion(publishList.get(i).getVersion());
			publishApi.setZipUrl(publishList.get(i).getZipUrl());
			publishListApi.add(publishApi);
		}
		if(publishListApi.size()!=0){
			apiFrom.setMsg("成功");
			apiFrom.setStatus("0");
			apiFrom.setReturnList(publishListApi);
		}else{
			apiFrom.setMsg("无数据");
			apiFrom.setStatus("1");
			apiFrom.setReturnList(publishListApi);
		}
		
		return apiFrom;
	}
	@Path("download")
	@GET  
	@Produces(MediaType.TEXT_PLAIN)
	public String  downLoad(@Context HttpServletRequest request,@Context HttpServletResponse response){
		String zipId = request.getParameter("zipId");
		
		List<Publish> publishList = publishService.downloadInfo(zipId);
		//获取Web项目的全路径
		String strDirPath = request.getSession().getServletContext().getRealPath("/resources/downloadfiles/zip/");
		String path = strDirPath+"\\"+publishList.get(0).getName();
		///////////////////////////////////////
		 response.setContentType("text/html; charset=UTF-8");
		 response.setCharacterEncoding("UTF-8");
          //解决中文乱码问题
          //  String filename=new String(request.getParameter("filename").getBytes("iso-8859-1"),"gbk");
          
          File file = new File(path);
          FileInputStream fis = null;
          BufferedInputStream buff = null;
          OutputStream myout = null;
          try {
              if (!file.exists()) {
                  response.sendError(404, "File not found!");
                  return "";
              }
              response.reset(); 
             //纯下载方式
	          //设置response的编码方式
	          response.setContentType("application/x-msdownload");
	          //写明要下载的文件的大小
	          response.setContentLength((int) file.length());
	          //设置附加文件名(解决中文乱码)
	          response.setHeader("Content-Disposition",
	               "attachment;filename=" + new String(file.getName().getBytes("gbk"), "iso-8859-1"));
  
              fis = new FileInputStream(file);
              buff = new BufferedInputStream(fis);
              byte[] b = new byte[1024];//相当于我们的缓存
              long k = 0;//该值用于计算当前实际下载了多少字节
              //从response对象中得到输出流,准备下载
              myout = response.getOutputStream();
              while (k < file.length()) {
                  int j = buff.read(b, 0, 1024);
                  k += j;
                  //将b中的数据写到客户端的内存
                  myout.write(b, 0, j);
              }
              //将写入到客户端的内存的数据,刷新到磁盘
              myout.flush();
          } catch (MalformedURLException e) {
              e.printStackTrace();
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
              try {
                  if (fis != null) {
                      fis.close();
                  }
                  if (buff != null)
                      buff.close();
                  if (myout != null)
                      myout.close();
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
		///////////////////////////////////////
		return "redirect:"+publishList.get(0).getZipUrl()+"?index=Y";
	}
	
}
