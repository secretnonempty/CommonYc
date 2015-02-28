package com.rails.ecommerce.admin.upload.controller;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.rails.ecommerce.admin.utils.MD5CheckUtil;
import com.rails.ecommerce.core.filemanage.service.FileTransferService;
import com.rails.ecommerce.core.order.service.OrderService;

@RequestMapping(value="/transfer")
@Controller
public class UploadController {
	
	@Resource(name = "OrderServiceImpl")
	private OrderService orderService;
	
	@Resource(name = "FileTransferServiceImpl")
	private FileTransferService fileTransferService;

	/**
	 * 
	 *  Function:上传入口
	 * 
	 *  @author WengShengyuan  DateTime 2015-1-31 上午10:12:20
	 *  @param name 要保存的文件名
	 *  @param MD5String 文件的MD5验证串(文件完整性验证用)
	 *  @param file 文件流
	 *  @param request 
	 *  @return
	 * @throws Exception 
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public ModelAndView upload(String name,
	// 上传多个文件
			@RequestParam("FILE_MD5")  String MD5String,
			@RequestParam("file") MultipartFile file, HttpServletRequest request)
			throws Exception {

		System.out.println("MD5:" + MD5String);
		// 获取文件 存储位置
		String realPath = "E:\\testupload";
		File pathFile = new File(realPath);

		if (!pathFile.exists()) {
			// 文件夹不存 创建文件
			pathFile.mkdirs();
		}
		// for (MultipartFile f : file) {

		System.out.println("文件类型：" + file.getContentType());
		System.out.println("文件名称：" + file.getOriginalFilename());
		System.out.println("文件大小: " + file.getSize());
		// 将文件copy上传到服务器
		File newFile = new File(realPath + File.separator + file.getOriginalFilename());
		file.transferTo(newFile);
		// 匹配MD5证明传输已完成
		if (MD5String.equals(MD5CheckUtil.getMD5Checksum(newFile))) {
			fileTransferService.restore(realPath, realPath + File.separator + file.getOriginalFilename());
		}
		// FileUtils.copy
		// }
		// 获取modelandview对象
//		ModelAndView view = new ModelAndView();
//		view.setViewName("redirect:index.jsp");
		return new ModelAndView();
	}
	
}
