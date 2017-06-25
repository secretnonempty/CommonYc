package com.rails.ecommerce.admin.student.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rails.common.utils.DateUtils;
import com.rails.ecommerce.admin.api.Util;
import com.rails.ecommerce.admin.api.bean.OrderRecordDetailList;
import com.rails.ecommerce.admin.api.bean.OrderRecordForm;
import com.rails.ecommerce.admin.api.bean.Result;
import com.rails.ecommerce.admin.api.bean.StudentInfoForm;
import com.rails.ecommerce.admin.api.bean.UserInfoForm;
import com.rails.ecommerce.admin.api.client.SentClient;
import com.rails.ecommerce.admin.webservice.client.ClientImpl;
import com.rails.ecommerce.core.common.domain.PaginationList;
import com.rails.ecommerce.core.common.domain.TrainNumberView;
import com.rails.ecommerce.core.student.domain.StudentInfo;
import com.rails.ecommerce.core.student.service.StudentService;

@RequestMapping(value = "/student")
@Controller
public class StudentController {

	Logger log = Logger.getLogger(getClass());

	private SentClient client = null;

	private DefaultHttpClient httpClient = null;

	@Resource(name = "StudentServiceImpl")
	private StudentService studentService;

	public StudentController () {
		
		ThreadSafeClientConnManager tcm = new ThreadSafeClientConnManager();
		tcm.setMaxTotal(10);
		this.httpClient = new DefaultHttpClient(tcm);
		this.httpClient
		.getParams()
		.setParameter(
				"User-Agent",
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; TEN)");
		this.client = new SentClient(this.httpClient);
	}
	
	/**
	 * 页面跳转 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:05:45
	 * @return
	 */
	@RequestMapping(value = "/studentinfo")
	public ModelAndView studentInfo() {
		ModelAndView model = new ModelAndView();
		model.setViewName("student/student_info");
		return model;
	}

	/**
	 * 页面跳转 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:05:45
	 * @return
	 */
	@RequestMapping(value = "/addstudent")
	public ModelAndView addStudentInfo() {
		ModelAndView model = new ModelAndView();
		model.setViewName("student/add_student");
		return model;
	}

//	private void initNetwork() {
//		try {
//			ThreadSafeClientConnManager tcm = new ThreadSafeClientConnManager();
//			tcm.setMaxTotal(10);
//			this.httpClient = new DefaultHttpClient(tcm);
//			this.httpClient
//					.getParams()
//					.setParameter(
//							"User-Agent",
//							"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; TEN)");
//			this.client = new SentClient(this.httpClient);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

	/**
	 * 获得初始化列表 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
//	@RequestMapping(value = "/studentinfo/list")
//	@ResponseBody
//	public PaginationList pageList(HttpServletRequest request,
//			HttpServletResponse response) throws NumberFormatException,
//			Exception {
//		String pageNo = request.getParameter("pageNum");
//		String pageSize = request.getParameter("pageSize");
//
//		String class_sign = request.getParameter("class_sign");
//		String status_name = request.getParameter("status_name");
//
//		String cardNo = request.getParameter("cardNo");
//		String jxid = request.getParameter("jxid");
//
////		this.initNetwork();
//		Gson g = new Gson();
//		String res = null;
//		PaginationList listpage = new PaginationList();
//		boolean pwdFlag = false;
//		if (cardNo != null && !"".equals(cardNo)) {
//			res = this.client.getUserInfo(cardNo);
//			if (res != null && !"".equals(res)) {
//				Result<UserInfoForm> stu = g.fromJson(res,
//						new TypeToken<Result<UserInfoForm>>() {
//						}.getType());
//				if (stu != null && stu.getCode() == 0) {
//					res = this.client.getUserInfo(stu.getData().getUserName());
//					
//					
//					
//					
//					
//					
//					
//					StudentInfo entity = new StudentInfo();
////					setStudentInfoBean(entity, stu.getData());
//					entity.setJxid(jxid);
//					StudentInfo vo = studentService.findById(cardNo);
//					if (vo != null && !"".equals(vo.getStId())) {
//						// 避免更新时把之前备注的信息清除掉
//						entity.setQq(vo.getQq());
//						entity.setRemark(vo.getRemark());
//						// new info
//						entity.setStAddr(vo.getStAddr());
//						entity.setLxdz(vo.getLxdz());
//						entity.set_stIdcard(vo.get_stIdcard());
//						entity.setStIdcard(vo.getStIdcard());
//						entity.setStOtcard(vo.getStOtcard());
//						entity.setStPhone(vo.getStPhone());
//						entity.setStHandset(vo.getStHandset());
//						entity.setScId(vo.getScId());
//						entity.setStPwd(vo.getStPwd());
//						entity.setBmf(vo.getBmf());
//						entity.setBmftype(vo.getBmftype());
//						entity.setBmrq(vo.getBmrq());
//						entity.setBmtbman(vo.getBmtbman());
//						entity.setZhlb(vo.getZhlb());
//						entity.setCountNumber(vo.getCountNumber());
//						entity.setFpzwh(vo.getFpzwh());
//						entity.setFpjsname(vo.getFpjsname());
//						entity.setSsfy(vo.getSsfy());
////						if (!entity.getStPwd().equals(vo.getStPwd())) {
////							pwdFlag = true;
////						}
//						// 保存查询到的学员信息,有就更新
//						studentService.update(entity);
//					} else {
//						// 保存查询到的学员信息,没有就插入.
//						studentService.save(entity);
//					}
//				}
//			}
//		}
//		listpage = studentService.findAllPage(cardNo, jxid, class_sign,
//				status_name, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
//		if (pwdFlag) {
//			@SuppressWarnings("unchecked")
//			List<StudentInfo> st = (ArrayList<StudentInfo>) listpage.getRows();
//			st.get(0).setXybmd("change");
//		}
//		return listpage;
//	}
	
	/**
	 * 获得初始化列表 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/studentinfo/list")
	@ResponseBody
	public PaginationList pageList(HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException,
			Exception {
		String pageNo = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");

		String class_sign = request.getParameter("class_sign");
		String status_name = request.getParameter("status_name");

		String cardNo = request.getParameter("cardNo");
		String cnbh = request.getParameter("cnbh");
		String jxid = request.getParameter("jxid");

//		this.initNetwork();
		Gson g = new Gson();
		String res = null;
		PaginationList listpage = new PaginationList();
		boolean pwdFlag = false;
		boolean userpwdFlag = true;
		Result<UserInfoForm> user = null;
		if (cardNo != null && !"".equals(cardNo)) {

			res = this.client.getUserInfo(cardNo);
			if (res != null && !"".equals(res)) {
				Result<UserInfoForm> user_one = g.fromJson(res,
						new TypeToken<Result<UserInfoForm>>() {
						}.getType());
				if (user_one != null && user_one.getCode() == 0 
						&& user_one.getData().getUserName() !=null && !"null".equals(user_one.getData().getUserName())) {
					res = this.client.getUserInfo(user_one.getData().getUserName());
					if (res != null && !"".equals(res)) {
						user = g.fromJson(res,
								new TypeToken<Result<UserInfoForm>>() {
								}.getType());
						if (user != null && user.getCode() == 0 
								&& user.getData().getUserName() !=null && !"null".equals(user.getData().getUserName())) {
							userpwdFlag = true;
						} else {
							userpwdFlag = false;
						}
					} else {
						userpwdFlag = false;
					}
				} else {
					userpwdFlag = false;
				}
			}
			
			if (userpwdFlag) {
				res = this.client.getStudentInfo(user.getData().getSfzh(), jxid);
			} else {
				res = this.client.getStudentInfo(cardNo, jxid);
			}
			if (res != null && !"".equals(res)) {
				Result<StudentInfoForm> stu = g.fromJson(res,
						new TypeToken<Result<StudentInfoForm>>() {
						}.getType());
				if (stu != null && stu.getCode() == 0) {
					StudentInfo entity = new StudentInfo();
					if (userpwdFlag) {
						setStudentInfoBean(entity, stu.getData(), user.getData());
					} else {
						setStudentInfoBean(entity, stu.getData(), null);
					}
					entity.setJxid(jxid);
					StudentInfo vo = null;
					if (userpwdFlag) {
						vo = studentService.findById(entity.getStId());
					} else {
						vo = studentService.findById(cardNo);
					}
					if (vo != null && !"".equals(vo.getStId())) {
						// 避免更新时把之前备注的信息清除掉
						if (vo.getQq() != null && !"".equals(vo.getQq())) {
							entity.setQq(vo.getQq());
						}
//						if (vo.getRemark() != null && !"".equals(vo.getRemark())) {
//							entity.setRemark(vo.getRemark());
//						}
						// new info
						entity.setStAddr(vo.getStAddr());
						entity.setLxdz(vo.getLxdz());
						if (vo.get_stIdcard() != null && !"".equals(vo.get_stIdcard())) {
							entity.set_stIdcard(vo.get_stIdcard());
						}
						if (vo.getStIdcard() != null && !"".equals(vo.getStIdcard())) {
							// 手工编辑中的身份证号时，先查询修改后的结果，然后在更新
							entity.setStIdcard(vo.getStIdcard());
						}
//						entity.setStOtcard(vo.getStOtcard());
						entity.setScId(vo.getScId());
						if (user == null || user.getData() == null) {
							entity.setStPwd(vo.getStPwd());
							entity.setStPhone(vo.getStPhone());
							entity.setStHandset(vo.getStHandset());
						}
						entity.setBmf(vo.getBmf());
						entity.setBmftype(vo.getBmftype());
						entity.setBmrq(vo.getBmrq());
						entity.setBmtbman(vo.getBmtbman());
						entity.setZhlb(vo.getZhlb());
						entity.setCountNumber(vo.getCountNumber());
						entity.setFpzwh(vo.getFpzwh());
						entity.setFpjsname(vo.getFpjsname());
						entity.setSsfy(vo.getSsfy());
						if ("".equals(entity.getStClasssname())) {
							entity.setStClasssname(vo.getStClasssname());
						}
//						if (!entity.getStPwd().equals(vo.getStPwd())) {
//							pwdFlag = true;
//						}
						// 保存查询到的学员信息,有就更新
						studentService.update(entity);
					} else {
						// 保存查询到的学员信息,没有就插入.
						studentService.save(entity);
					}
					if (user != null && user.getData() != null) {
						// 用学生信息查到的卡号，更新掉L开头的临时卡号
						user.getData().setXxzh(entity.getStId());
					}
				} else {
					log.info("获取学生身份信息失败：     " + stu.getMessage());
				}
			}
		}
		
		if ("all".equals(cnbh)) {
			StudentInfo info = null;
			StringBuffer sb = new StringBuffer();
			for (int j=16001;j<16101;j++) {
				listpage = studentService.findAllPage(cardNo, String.valueOf(j), jxid, class_sign,
						status_name, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
				
				
				for (int i=0;i<listpage.getRows().size();i++) {
					info = (StudentInfo) listpage.getRows().get(i);
					if ("".equals(info.getStLinkmanp())) {
						sb.append(info.getRemark()).append("\n");
					}
				}
			}
			for (int j=17001;j<17100;j++) {
				listpage = studentService.findAllPage(cardNo, String.valueOf(j), jxid, class_sign,
						status_name, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
				for (int i=0;i<listpage.getRows().size();i++) {
					info = (StudentInfo) listpage.getRows().get(i);
					if ("".equals(info.getStLinkmanp())) {
						sb.append(info.getRemark()).append("\n");
					}
				}
			}
			for (int j=18001;j<18083;j++) {
				listpage = studentService.findAllPage(cardNo, String.valueOf(j), jxid, class_sign,
						status_name, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
				for (int i=0;i<listpage.getRows().size();i++) {
					info = (StudentInfo) listpage.getRows().get(i);
					if ("".equals(info.getStLinkmanp())) {
						sb.append(info.getRemark()).append("\n");
					}
				}
			}
			
			writeLog(sb.toString(), "all-cnbh");
		} else {
			if (user != null && user.getCode() == 0 && user.getData() != null && !"".equals(user.getData().getXxzh())) {
				listpage = studentService.findAllPage(user.getData().getXxzh(), cnbh, jxid, class_sign,
						status_name, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
			} else {
				listpage = studentService.findAllPage(cardNo, cnbh, jxid, class_sign,
						status_name, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
			}
			if (cnbh != null && !"".equals(cnbh)) {
				StudentInfo info = null;
				StringBuffer sb = new StringBuffer();
				for (int i=0;i<listpage.getRows().size();i++) {
					info = (StudentInfo) listpage.getRows().get(i);
					sb.append(info.getRemark()).append("\n");
				}
				writeLog(sb.toString(), "cnbh");
			}
		}
		if (pwdFlag) {
			@SuppressWarnings("unchecked")
			List<StudentInfo> st = (ArrayList<StudentInfo>) listpage.getRows();
			st.get(0).setXybmd("change");
		}
		return listpage;
	}
	
	/**
	 * 获得初始化列表 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/studentinfo/list/detail")
	@ResponseBody
	public Result<OrderRecordForm> detailList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");
		String jxid = request.getParameter("jxid");

		this.client.getStudentInfo(cardNo, jxid);
		
//		this.initNetwork();
		Gson g = new Gson();
		String res = null;
		if (cardNo != null && !"".equals(cardNo)) {
			res = this.client.getOrderRecord(cardNo, jxid);
			if (res != null && !"".equals(res)) {
				Result<OrderRecordForm> detail = g.fromJson(res,
						new TypeToken<Result<OrderRecordForm>>() {
						}.getType());
				return detail;
			} else {
				Result<OrderRecordForm> temp = new Result<OrderRecordForm>();
				temp.setData(null);
				temp.setCode(101);
				temp.setMessage("接口返回为空");
				return temp;
			}
		} else {
			Result<OrderRecordForm> temp = new Result<OrderRecordForm>();
			temp.setData(null);
			temp.setCode(100);
			temp.setMessage("获取卡号失败");
			return temp;
		}
	}
	
	/**
	 * 获得初始化列表 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/studentinfo/classname")
	@ResponseBody
	public Result<StudentInfoForm> classname(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");  //这里不设置编码会有乱码
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");  
		String cardNo = request.getParameter("cardNo");
		String jxid = request.getParameter("jxid");
		Gson g = new Gson();
		String res = null;
		if (cardNo != null && !"".equals(cardNo)) {
			res = this.client.getStudentInfo(cardNo, jxid);
			if (res != null && !"".equals(res)) {
				Result<StudentInfoForm> stu = g.fromJson(res,
						new TypeToken<Result<StudentInfoForm>>() {
						}.getType());
				return stu;
			} else {
				Result<StudentInfoForm> temp = new Result<StudentInfoForm>();
				temp.setData(null);
				temp.setCode(101);
				temp.setMessage("接口返回为空");
				return temp;
			}
		} else {
			Result<StudentInfoForm> temp = new Result<StudentInfoForm>();
			temp.setData(null);
			temp.setCode(100);
			temp.setMessage("获取卡号失败");
			return temp;
		}
//		try {
//		request.setCharacterEncoding("utf-8");  //这里不设置编码会有乱码
//        response.setContentType("text/html;charset=utf-8");
//        response.setHeader("Cache-Control", "no-cache");  
//		PrintWriter out = response.getWriter();  //输出中文，这一句一定要放到response.setContentType("text/html;charset=utf-8"),  response.setHeader("Cache-Control", "no-cache")后面，否则中文返回到页面是乱码  
//		out.print(stuClassName);
//		out.flush();
//		out.close();
//		} catch (Exception e) {
//			log.error("查询数据出错", e);
//		}
	}
	
	/**
	 * adver 
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return 
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/studentinfo/list/detail/adver")
	@ResponseBody
	public Result<String> adver(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");
		String pwd = request.getParameter("pwd");
		String jxid = request.getParameter("jxid");
		String neirong = request.getParameter("cnbh");
		String content = "";
		if (neirong != null && !"".equals(neirong)) {
			content = neirong;
		} else {
			content = "想了解约车技巧、学车秘密、考试秘密，就扫码加入吧！";
			//content = "真开心，已顺利拿本，给大家分享一个很好的学车号，有约车秘籍、驾考技巧！";
		}
		String imei = "462DBFF274FC9130B938DEB9760126C5";
		String classifyid = "4";
//		String imgsOss = "2017/04/c764774c-38f2-4737-b18e-1472d356657b.jpg";
		//String imgsOss = "2017/04/4040dc5d-535b-414d-91e8-322db50eede0.jpg,2017/04/7BD07ABF-1EB1-400B-AEC6-E5D72EFB79E6.jpg,2017/04/8096a2e2-5c74-4f4c-a0b9-4f3ca2a49fe9.jpg,2017/04/958af9b1-cd41-4909-b2e9-e24ad4a0ec10.jpg,2017/04/9FB23EC2-62B3-4318-AE31-1D1ACFBA4336.jpg";
		String imgsOss = null;
		int x = (int)(Math.random()*10);
		x = x % 2;
		if (x == 0) {
			imgsOss = "2017/04/c3f8fdd2-4cfb-4c27-b30f-7962e077d93d.jpg,2017/04/20d591be-2b5f-4385-ac6f-f0a4aa24c91f.jpg,2017/04/7BD07ABF-1EB1-400B-AEC6-E5D72EFB79E6.jpg,2017/04/8096a2e2-5c74-4f4c-a0b9-4f3ca2a49fe9.jpg,2017/04/958af9b1-cd41-4909-b2e9-e24ad4a0ec10.jpg";
		} else {
			imgsOss = "2017/04/20d591be-2b5f-4385-ac6f-f0a4aa24c91f.jpg,2017/04/7BD07ABF-1EB1-400B-AEC6-E5D72EFB79E6.jpg,2017/04/8096a2e2-5c74-4f4c-a0b9-4f3ca2a49fe9.jpg,2017/04/958af9b1-cd41-4909-b2e9-e24ad4a0ec10.jpg";
		}
		Gson g = new Gson();
		this.client.getXuechebuLoginInfo(cardNo, pwd);
		String res = null;
//			Thread.sleep(10 * 60 * 1000);
		if (cardNo != null && !"".equals(cardNo)) {
			res = this.client.addPosts(imei, classifyid, imgsOss, content, jxid);
			if (res != null && !"".equals(res)) {
				Result<String> detail = g.fromJson(res,
						new TypeToken<Result<String>>() {
						}.getType());
				return detail;
			} else {
				Result<String> temp = new Result<String>();
				temp.setData(null);
				temp.setCode(101);
				temp.setMessage("接口返回为空");
				return temp;
			}
		} else {
			Result<String> temp = new Result<String>();
			temp.setData(null);
			temp.setCode(100);
			temp.setMessage("获取卡号失败");
			return temp;
		}
	}
	
	/**
	 * jiance 
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/studentinfo/list/detail/check")
	@ResponseBody
	public String checkList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
//		this.initNetwork();
		Gson g = new Gson();
//		String pageNo = request.getParameter("pageNum");
//		String pageSize = request.getParameter("pageSize");
//
//		String class_sign = request.getParameter("class_sign");
//		String status_name = request.getParameter("status_name");

		String cardNoFrom = request.getParameter("cardNo");
		String cardNoTo = request.getParameter("pwd");
		String jxid = request.getParameter("jxid");
		int start = 0;
		int end = 0;
		start = Integer.parseInt(cardNoFrom);
		end = Integer.parseInt(cardNoTo);
		StringBuffer sb = new StringBuffer();
		for (int i = start;i <= end; i++) {
			String res = null;
//			PaginationList listpage = new PaginationList();
//			boolean pwdFlag = false;
			boolean userpwdFlag = true;
			Result<UserInfoForm> user = null;
			String str = String.valueOf(i);
			if (str != null && !"".equals(str)) {
				res = this.client.getUserInfo(str);
				if (res != null && !"".equals(res)) {
					Result<UserInfoForm> user_one = g.fromJson(res,
							new TypeToken<Result<UserInfoForm>>() {
							}.getType());
					if (user_one != null && user_one.getCode() == 0 
							&& user_one.getData().getUserName() !=null && !"null".equals(user_one.getData().getUserName())) {
						res = this.client.getUserInfo(user_one.getData().getUserName());
						if (res != null && !"".equals(res)) {
							user = g.fromJson(res,
									new TypeToken<Result<UserInfoForm>>() {
									}.getType());
							if (user != null && user.getCode() == 0 
									&& user.getData().getUserName() !=null && !"null".equals(user.getData().getUserName())) {
								userpwdFlag = true;
							} else {
								userpwdFlag = false;
							}
						} else {
							userpwdFlag = false;
						}
					} else {
						userpwdFlag = false;
					}
				}
				
				if (userpwdFlag) {
					res = this.client.getStudentInfo(user.getData().getSfzh(), jxid);
				} else {
					res = this.client.getStudentInfo(str, jxid);
				}
				if (res != null && !"".equals(res)) {
					Result<StudentInfoForm> stu = g.fromJson(res,
							new TypeToken<Result<StudentInfoForm>>() {
							}.getType());
					if (stu != null && stu.getCode() == 0) {
						int usexss = Integer.parseInt(stu.getData().getXLXSS())
								+ Integer.parseInt(stu.getData().getYYWLXSS())
								+ Integer.parseInt(stu.getData().getZFXSS());
						// 科目三的
//						if ("".equals(stu.getData().getKM3CNBH()) 
//								|| user == null || user.getData() == null 
//								|| "".equals(user.getData().getPhoneNum())
//								|| usexss >= 40) {
//							continue;
//						}
						
						// 科目二的
						if (user == null || user.getData() == null 
								|| "".equals(user.getData().getPhoneNum())
								|| usexss >= 40) {
							continue;
						}
						StudentInfo entity = new StudentInfo();
						if (userpwdFlag) {
							setStudentInfoBean(entity, stu.getData(), user.getData());
						} else {
							setStudentInfoBean(entity, stu.getData(), null);
						}
						entity.setJxid(jxid);
						StudentInfo vo = null;
						if (userpwdFlag) {
							vo = studentService.findById(entity.getStId());
						} else {
							vo = studentService.findById(str);
						}
						if (vo != null && !"".equals(vo.getStId())) {
							// 避免更新时把之前备注的信息清除掉
							if (vo.getQq() != null && !"".equals(vo.getQq())) {
								entity.setQq(vo.getQq());
							}
	//						if (vo.getRemark() != null && !"".equals(vo.getRemark())) {
	//							entity.setRemark(vo.getRemark());
	//						}
							// new info
							entity.setStAddr(vo.getStAddr());
							entity.setLxdz(vo.getLxdz());
							if (vo.get_stIdcard() != null && !"".equals(vo.get_stIdcard())) {
								entity.set_stIdcard(vo.get_stIdcard());
							}
							if (vo.getStIdcard() != null && !"".equals(vo.getStIdcard())) {
								// 手工编辑中的身份证号时，先查询修改后的结果，然后在更新
								entity.setStIdcard(vo.getStIdcard());
							}
	//						entity.setStOtcard(vo.getStOtcard());
							entity.setScId(vo.getScId());
							if (user == null || user.getData() == null) {
								entity.setStPwd(vo.getStPwd());
								entity.setStPhone(vo.getStPhone());
								entity.setStHandset(vo.getStHandset());
							}
							entity.setBmf(vo.getBmf());
							entity.setBmftype(vo.getBmftype());
							entity.setBmrq(vo.getBmrq());
							entity.setBmtbman(vo.getBmtbman());
							entity.setZhlb(vo.getZhlb());
							entity.setCountNumber(vo.getCountNumber());
							entity.setFpzwh(vo.getFpzwh());
							entity.setFpjsname(vo.getFpjsname());
							entity.setSsfy(vo.getSsfy());
							if ("".equals(entity.getStClasssname())) {
								entity.setStClasssname(vo.getStClasssname());
							}
	//						if (!entity.getStPwd().equals(vo.getStPwd())) {
	//							pwdFlag = true;
	//						}
							sb.append(entity.getStId()).append("  ").append(entity.getStPwd()).append("  ")
							.append(entity.getStHandset()).append("  ").append(entity.getCnbh()).append("  ")
							.append(entity.getStLinkmanp()).append("  update\n");
							// 保存查询到的学员信息,有就更新
							studentService.update(entity);
						} else {
							sb.append(entity.getStId()).append("  ").append(entity.getStPwd()).append("  ")
							.append(entity.getStHandset()).append("  ").append(entity.getCnbh()).append("  ")
							.append(entity.getStLinkmanp()).append("  insert\n");
							// 保存查询到的学员信息,没有就插入.
							studentService.save(entity);
						}
						if (user != null && user.getData() != null) {
							// 用学生信息查到的卡号，更新掉L开头的临时卡号
							user.getData().setXxzh(entity.getStId());
						}
					} else {
						log.info("获取学生身份信息失败：     " + stu.getMessage());
					}
				}
			}
			
//			if (user != null && user.getCode() == 0 && user.getData() != null && !"".equals(user.getData().getXxzh())) {
//				listpage = studentService.findAllPage(user.getData().getXxzh(), jxid, class_sign,
//						status_name, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
//			} else {
//				listpage = studentService.findAllPage(cardNo, jxid, class_sign,
//						status_name, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
//			}
//			if (pwdFlag) {
//				@SuppressWarnings("unchecked")
//				List<StudentInfo> st = (ArrayList<StudentInfo>) listpage.getRows();
//				st.get(0).setXybmd("change");
//			}
		
		}
		
		return sb.toString();
	}
	
	/**
	 * 获得初始化列表 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/studentinfo/list/detail/check_weiyue")
	@ResponseBody
	public String checkList_weiyue(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
//		this.initNetwork();
		Gson g = new Gson();
		String jxid = request.getParameter("jxid");
		StringBuffer sb = new StringBuffer();
		String fileName = "d:\\log.txt";
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 0;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
//				System.out.println("line " + line + ": " + tempString);
				line++;
				if ("".equals(tempString) || tempString.startsWith("=")) {
					continue;
				}
				String cardNo = tempString.substring(36, 44);
				String riqi = tempString.substring(54, 64);
				String shiduan = tempString.substring(71, 75).trim();
//				System.out.println("line " + line + ": " + cardNo + riqi + shiduan);
				String res = null;
				if (cardNo != null && !"".equals(cardNo)) {
					res = this.client.getOrderRecord(cardNo, jxid);
					if (res != null && !"".equals(res)) {
						Result<OrderRecordForm> detail = g.fromJson(res,
								new TypeToken<Result<OrderRecordForm>>() {
								}.getType());
						if (detail.getCode() == 0) {
							if (detail.getData() != null && detail.getData().getResult() != null) {
								for (int i = 0;i < detail.getData().getResult().size();i ++) {
									OrderRecordDetailList a = detail.getData().getResult().get(i);
//									System.out.println(a.getYYRQ().substring(0, 10).replace("/", "-"));
									if (riqi.equals(a.getYYRQ().substring(0, 10).replace("/", "-")) && shiduan.equals(a.getXNSD())) {
										sb.append("line " + line + ":   " + cardNo + "    "+ riqi + "    " + shiduan + "    " + a.getCNBH() + "\r\n");
										break;
									}
								}
							}
						} else {
							sb.append("line " + line + ":   " + cardNo + "    "+ riqi + "    " + shiduan + "    " + detail.getCode() + "    " + detail.getMessage().getBytes("gbk") + "   recheck \r\n");
						}
					} else {
						return "接口返回为空";
					}
				} else {
					return "获取卡号失败";
				}
				Thread.sleep(1000);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		Thread.sleep(1000);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true),"UTF-8"));
			out.write(sb.toString() + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(sb.toString());
//		try {
//            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
//            FileWriter writer = new FileWriter(fileName, true);
//            writer.write(sb.toString());
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//		File dest = new File(fileName);
//		try {
//		    BufferedWriter writer  = new BufferedWriter(new FileWriter(dest));
//		    writer.write(sb.toString());
//		    writer.flush();
//		    reader.close();
//		    writer.close();
//		} catch (FileNotFoundException e) {
//		    e.printStackTrace();
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
//		System.out.println(sb.toString());
		return "success";
	}
	
	/**
	 * 获得初始化列表 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/studentinfo/list/detail/cancelorder")
	@ResponseBody
	public Result<String> cancelOrder(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");
		String pwd = request.getParameter("pwd");
		String jlcbh = request.getParameter("jlcbh");
		String yyrq = request.getParameter("yyrq");
		String xnsd = request.getParameter("xnsd");
		String jxid = request.getParameter("jxid");

//		this.initNetwork();
		Gson g = new Gson();
		String res = null;
		this.client.getLoginInfo(cardNo, pwd, jxid);
		if (cardNo != null && !"".equals(cardNo)) {
			res = this.client.cancelOrder(cardNo, jlcbh, yyrq.replace("/", "-"), xnsd, jxid);
			if (res != null && !"".equals(res)) {
				Result<String> cancel = g.fromJson(res,
						new TypeToken<Result<String>>() {
						}.getType());
				return cancel;
			} else {
				Result<String> temp = new Result<String>();
				temp.setData(null);
				temp.setCode(101);
				temp.setMessage("接口返回为空");
				return temp;
			}
		} else {
			Result<String> temp = new Result<String>();
			temp.setData(null);
			temp.setCode(100);
			temp.setMessage("获取卡号失败");
			return temp;
		}
	}
	
	public static void writeLog(String str, String code)
    {
		String today = Util.getCurDate();
		String path = null;
		if ("change".equals(code)) {
			path = "E:\\yc\\change\\change" + today.substring(4, 10) + ".log";
		} else if ("cnbh".equals(code)) {
			path = "D:\\develop\\lq-getInfo\\yc-lq-pwd\\query-cnbh" + today.substring(4, 10) + ".log";
		} else if ("all-cnbh".equals(code)) {
			path = "D:\\develop\\lq-getInfo\\yc-lq-pwd\\query-all-cnbh" + today.substring(4, 10) + ".log";
		}
        try
        {
	        File file = new File(path);
	        if(!file.exists())
	            file.createNewFile();
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        FileOutputStream out=new FileOutputStream(file, true); //如果追加方式用true
	        StringBuffer sb=new StringBuffer();
	        sb.append(sdf.format(new Date()) + "    " + str + "\n");
	        out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
	        out.close();
        }
        catch(IOException ex)
        {
        	ex.printStackTrace();
        }
    }
	
	/**
	 * 获得初始化列表 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/studentinfo/list/detail/change")
	@ResponseBody
	public Result<String> change(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");
		String pwd = request.getParameter("pwd");
		String jlcbh = request.getParameter("jlcbh");
		String cnbh = request.getParameter("cnbh");
		String yyrq = request.getParameter("yyrq");
		String xnsd = request.getParameter("xnsd");
		String jxid = request.getParameter("jxid");
		String cardNoTo = request.getParameter("cardNoTo");
		String pwdTo = request.getParameter("pwdTo");
		yyrq = yyrq.replace("/", "-");
		
//		this.initNetwork();
		Gson g = new Gson();
		String res = null;
		String resTo = null;
		String resFrom = null;
		
		this.client.getLoginInfo(cardNo, pwd, jxid);
		if (cardNo != null && !"".equals(cardNo) && cardNoTo != null && !"".equals(cardNoTo)) {
			res = this.client.cancelOrder(cardNo, jlcbh, yyrq, xnsd, jxid);
			this.client.getLoginInfo(cardNoTo, pwdTo, jxid);
			resTo = this.client.submitOrder(cardNoTo, cnbh, yyrq, xnsd, jxid);
			if (resTo != null && !"".equals(resTo)) {
				
				Result<String> cancel = g.fromJson(resTo,
						new TypeToken<Result<String>>() {
						}.getType());
				if (cancel != null && cancel.getCode() == 0) {
					writeLog("车次信息：        " + yyrq + "        " + xnsd + "        " + cnbh + "        " + jlcbh + "\n" + 
							"源卡：        " + cardNo + "      目标卡：       " + cardNoTo + "        change success" + "\n", "change");
					return cancel;
				} else {
					//this.initNetwork();
					this.client.getLoginInfo(cardNo, pwd, jxid);
					resFrom = this.client.submitOrder(cardNo, cnbh, yyrq, xnsd, jxid);
					Result<String> resFromReturn = g.fromJson(resFrom,
							new TypeToken<Result<String>>() {
							}.getType());
					Result<String> src = g.fromJson(res,
							new TypeToken<Result<String>>() {
							}.getType());
					String message = src.getMessage() + cancel.getMessage() + resFromReturn.getMessage();
					if (resFromReturn.getCode() == 0) {
						message = message + "  补偿成功！";
					} else {
						message = message + "  补偿失败！";
					}
					resFromReturn.setMessage(message);
					return resFromReturn;
				}
			} else {
				Result<String> temp = new Result<String>();
				temp.setData(null);
				temp.setCode(101);
				temp.setMessage("接口返回为空");
				return temp;
			}
		} else {
			Result<String> temp = new Result<String>();
			temp.setData(null);
			temp.setCode(100);
			temp.setMessage("目标卡号为空");
			return temp;
		}
	}
	
	/**
	 * 获得车次列表 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:06:04
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/getTrainCode")
	@ResponseBody
	public List<TrainNumberView> getOrganizationForTrainNo(
			HttpServletRequest request, HttpServletResponse response) {
		String carOrgId = request.getParameter("orgId");
		List<TrainNumberView> trainList = new ArrayList<TrainNumberView>();
		if (carOrgId != null && !"".equals(carOrgId)) {
			try {
				trainList = ClientImpl.getOrganizationForTrainNo(carOrgId);
			} catch (Exception e) {
				System.out.println("链接webservice服务器失败：" + e);
			}
		}
		return trainList;
	}

	/**
	 * ajax表单提交 Function:
	 * 
	 * @author gxl DateTime 2015-1-13 下午7:34:10
	 * @return
	 */
	@RequestMapping(value = "/addorder/submit")
	@ResponseBody
	public String submit(HttpServletRequest request,
			HttpServletResponse response) {

		StudentInfo si = new StudentInfo();
		// gi.setGoodsTitle(request.getParameter("goodsTitle"));
		// gi.setPrice(Long.valueOf(request.getParameter("price")));
		// gi.setCreateDate(new Date());
		// gi.setLastModifyDate(new Date());
		//
		// gi.setGoodsSummary(request.getParameter("goodsSummary"));
		// gi.setState(request.getParameter("state"));
		// gi.setGoodsUnit(request.getParameter("goodsUnit"));
		// //gi.setGoodsDescript(request.getParameter("goodsDescript"));
		// gi.setTitleImgUrl(request.getParameter("titleImgUrl"));
		// gi.setSmallImgUrl(request.getParameter("smallImgUrl"));
		// gi.setImgUrl1(request.getParameter("imgUrl1"));
		// gi.setImgUrl2(request.getParameter("imgUrl2"));
		//
		// gi.setCreateUser("admin");
		// gi.setUnit("测试商家");

		try {
			studentService.save(si);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "sucess";
	}

	/**
	 * Function:编辑Or添加栏目
	 * 
	 * @author zjf DateTime 2015-1-9 下午9:02:18
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editOrAddStudent")
	public String editStudent(HttpServletRequest request,
			HttpServletResponse response) {
		String stId = request.getParameter("stId"); // stId
		String jxid = request.getParameter("jxid"); // jxid
		String qq = request.getParameter("qq"); // qq
		String stIdcard = request.getParameter("stIdcard");
		String remark = request.getParameter("remark"); // 备注

//		this.initNetwork();
		Gson g = new Gson();
		String res = null;
		boolean userpwdFlag = true;
		if (stId != null && !"".equals(stId)) {
			res = this.client.getStudentInfo(stId, jxid);
			Result<StudentInfoForm> stu = g.fromJson(res,
					new TypeToken<Result<StudentInfoForm>>() {
					}.getType());
			if (stu != null && stu.getCode() == 0) {
				Result<UserInfoForm> user = null;
				res = this.client.getUserInfo(stId);
				if (res != null && !"".equals(res)) {
					Result<UserInfoForm> user_one = g.fromJson(res,
							new TypeToken<Result<UserInfoForm>>() {
							}.getType());
					if (user_one != null && user_one.getCode() == 0 
							&& user_one.getData().getUserName() !=null && !"null".equals(user_one.getData().getUserName())) {
						res = this.client.getUserInfo(user_one.getData().getUserName());
						if (res != null && !"".equals(res)) {
							user = g.fromJson(res,
									new TypeToken<Result<UserInfoForm>>() {
									}.getType());
							if (user != null && user.getCode() == 0 
									&& user.getData().getUserName() !=null && !"null".equals(user.getData().getUserName())) {
								userpwdFlag = true;
							} else {
								userpwdFlag = false;
							}
						} else {
							userpwdFlag = false;
						}
					} else {
						userpwdFlag = false;
					}
				}
				
				StudentInfo entity = new StudentInfo();
				if (userpwdFlag) {
					setStudentInfoBean(entity, stu.getData(), user.getData());
				} else {
					setStudentInfoBean(entity, stu.getData(), null);
				}
				entity.setJxid(jxid);
				entity.setStIdcard(stIdcard);
				entity.setQq(qq);
				entity.setRemark(remark);
				try {
					// 保存查询到的学员信息,有就更新
					studentService.update(entity);
				} catch (ParseException e) {
					e.printStackTrace();
					return "fail";
				} catch (Exception e) {
					e.printStackTrace();
					return "fail";
				}
				return "success";
				
			} else {
				return "fail";
			}
		} else {
			return "fail";
		}
	}

	/**
	 * 页面跳转 Function:
	 * 
	 * @author gxl DateTime 2015-1-12 下午2:05:45
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = "/editstudentinfo")
	public String editstudentinfo(HttpServletRequest request,
			HttpServletResponse response, Model model)
			throws NumberFormatException, Exception {

		if (request.getParameter("stId") != null) { // 编辑状态
			StudentInfo studentInfo = studentService.findById(request
					.getParameter("stId"));
			// studentInfo.setTitleImgUrl(initImgUrl(goodsInfo.getTitleImgUrl()));
			// studentInfo.setSmallImgUrl(initImgUrl(goodsInfo.getSmallImgUrl()));
			// studentInfo.setImgUrl1(initImgUrl(goodsInfo.getImgUrl1()));
			// studentInfo.setImgUrl2(initImgUrl(goodsInfo.getImgUrl2()));
			model.addAttribute("studentInfo", studentInfo);
		} else {// 新增状态
			StudentInfo studentInfo = new StudentInfo();
			// goodsInfo.setTitleImgUrl(initImgUrl(goodsInfo.getTitleImgUrl()));
			// goodsInfo.setSmallImgUrl(initImgUrl(goodsInfo.getSmallImgUrl()));
			// goodsInfo.setImgUrl1(initImgUrl(goodsInfo.getImgUrl1()));
			// goodsInfo.setImgUrl2(initImgUrl(goodsInfo.getImgUrl2()));
			// goodsInfo.setState("1");
			// goodsInfo.setGoodsUnit("1");
			model.addAttribute("studentInfo", studentInfo);
		}
		return "student/add_student_info";
	}

	/**
	 * 删除 Function:
	 * 
	 * @author gxl DateTime 2015-1-15 下午2:18:28
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/studentinfo/delete")
	public String delete(HttpServletRequest request) {
		String stId = request.getParameter("stId");
		try {
			StudentInfo studentInfo = studentService.findById(stId);
			studentService.delete(studentInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}

	/**
	 * 删除 Function:
	 * 
	 * @author gxl DateTime 2015-1-15 下午2:18:28
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/studentinfo/deletemuti")
	public String deleteMuti(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		String[] idArray = ids.split(",");
		String msg = "";
		for (int i = 0;i < idArray.length;i ++) {
			try {
				StudentInfo studentInfo = studentService.findById(idArray[i]);
				if (studentInfo != null && !"".equals(studentInfo.getStId())) {
					studentService.delete(studentInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = msg + ",  " +idArray[i] + "fail  ";
				return msg;
			}
		}
		return "success";
	}
	
	/**
	 * ajax表单提交 Function:
	 * 
	 * @author gxl DateTime 2015-1-13 下午7:34:10
	 * @return
	 */
	private static void setStudentInfoBean(StudentInfo entity,
			StudentInfoForm stuJson, UserInfoForm user) {

		entity.setStNo(stuJson.getST_NO());
		entity.setStId(stuJson.getST_ID());
		entity.setStName(stuJson.getST_NAME());
		entity.setStSex(stuJson.getST_SEX());

//		entity.setStAddr(stuJson.getST_ADDR());
//		entity.setLxdz(stuJson.getLXDZ());
//		entity.set_stIdcard(stuJson.get_ST_IDCARD());
		
		if (user != null && !"".equals(user.getUserName()) && !"null".equals(user.getUserName())) {
			entity.setStIdcard(user.getSfzh());
//			entity.setStOtcard(stuJson.getST_OTCARD());
			entity.setStPhone(user.getPhoneNum());
			entity.setStHandset(user.getPhoneNum());
			entity.setStPwd(user.getSchoolpwd());
		}
		
		entity.setJgids(stuJson.getJGIDS());
		entity.setStCtype(stuJson.getST_CTYPE());
		entity.setStCtypename(stuJson.getST_CTYPENAME());
		entity.setStLeadcarddate(DateUtils.stringToDate(
				stuJson.getST_LEADCARDDATE(),
				DateUtils.yyyyMMddHHmmssForLoseTime));

//		entity.setScId(stuJson.getSC_ID());
		entity.setStSign(stuJson.getST_SIGN());
		entity.setStClasssign(stuJson.getST_CLASSSIGN());
		entity.setStClasssname(stuJson.getST_CLASSSName());
		entity.setLxxzrq(DateUtils.stringToDate(stuJson.getLXXZRQ(),
				DateUtils.yyyyMMddHHmmssForLoseTime));
//		entity.setBmf(stuJson.getBMF());
//		entity.setBmftype(stuJson.getBMFTYPE());
//		entity.setBmrq(DateUtils.stringToDate(stuJson.getBMRQ(),
//				DateUtils.yyyyMMddHHmmssForLoseTime));
//		entity.setBmtbman(stuJson.getBMTBMAN());

//		entity.setZhlb(stuJson.getZHLB());
//		entity.setCountNumber(stuJson.getCount_Number());
		entity.setSqcx(stuJson.getSQCX());
		entity.setSqcxname(stuJson.getSQCXNAME());
		entity.setJxname(stuJson.getJXNAME().replace("驾校", "").replace("北京市", ""));

		entity.setFpsj(stuJson.getFPSJ());
//		entity.setFpzwh(stuJson.getFPZWH());
//		entity.setFpjsname(stuJson.getFPJSNAME());
//		entity.setSsfy(stuJson.getSSFY());
		entity.setStauts(stuJson.getSTAUTS());
		entity.setStautsname(stuJson.getSTAUTSNAME());
		entity.setSfzx(stuJson.getSFZX());
		entity.setIfjdwzcf(stuJson.getIFJDWZCF());
		entity.setCnbh(stuJson.getCNBH());
		
		entity.setXlxss(stuJson.getXLXSS());
		entity.setYywlxss(stuJson.getYYWLXSS());
		entity.setSyxss(stuJson.getSYXSS());
		entity.setZfxss(stuJson.getZFXSS());
		entity.setGmxss(stuJson.getGMXSS());
		
		int usexss = Integer.parseInt(stuJson.getXLXSS())
				+ Integer.parseInt(stuJson.getYYWLXSS())
				+ Integer.parseInt(stuJson.getZFXSS());
		entity.setUsexss(String.valueOf(usexss));
//		int a = Integer.parseInt(entity.getUsexss());
//		if (a >= 34) {
		String km3cnbh = stuJson.getKM3CNBH();
		// 借用字段存储科目三车号
		entity.setStLinkmanp(km3cnbh);
		if (!"".equals(km3cnbh)) {
			entity.setRemark(entity.getStId()+"#"+entity.getStPwd()+"#"+ km3cnbh +"###"+entity.getStPhone()+"#三@");
		} else {
			entity.setRemark(entity.getStId()+"#"+entity.getStPwd()+"#"+entity.getCnbh()+"###"+entity.getStPhone()+"#二@");
		}
	}
	
//	public static Result<CarsNoForm> getCarsNo() throws Exception {
//		String cardNo = request.getParameter("cardNo");
//		String yyrq = request.getParameter("yyrq");
//		String xnsd = request.getParameter("xnsd");
//		String jxid = request.getParameter("jxid");
//		
//		// this.initNetwork();
//		Gson g = new Gson();
//		
//		String res = null;
//		if (cardNo != null && !"".equals(cardNo)) {
//			res = this.client.getCarsNo(cardNo, yyrq, "3002", jxid);
//			if (res != null && !"".equals(res)) {
//				Result<CarsNoForm> detail = g.fromJson(res,
//						new TypeToken<Result<CarsNoForm>>() {
//						}.getType());
//				return detail;
//			} else {
//				Result<CarsNoForm> temp = new Result<CarsNoForm>();
//				temp.setData(null);
//				temp.setCode(101);
//				temp.setMessage("接口返回为空");
//				return temp;
//			}
//		} else {
//			Result<CarsNoForm> temp = new Result<CarsNoForm>();
//			temp.setData(null);
//			temp.setCode(100);
//			temp.setMessage("获取卡号失败");
//			return temp;
//		}
//	}
	
}
