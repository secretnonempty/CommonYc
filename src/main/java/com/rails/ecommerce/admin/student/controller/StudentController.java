package com.rails.ecommerce.admin.student.controller;

import java.text.ParseException;
import java.util.ArrayList;
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
import com.rails.ecommerce.admin.api.bean.OrderRecordDetailList;
import com.rails.ecommerce.admin.api.bean.OrderRecordForm;
import com.rails.ecommerce.admin.api.bean.Result;
import com.rails.ecommerce.admin.api.bean.StudentInfoForm;
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

	private void initNetwork() {
		try {
			ThreadSafeClientConnManager tcm = new ThreadSafeClientConnManager();
			tcm.setMaxTotal(10);
			this.httpClient = new DefaultHttpClient(tcm);
			this.httpClient
					.getParams()
					.setParameter(
							"User-Agent",
							"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; TEN)");
			this.client = new SentClient(this.httpClient);
		} catch (Exception ex) {
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
	@RequestMapping(value = "/studentinfo/list")
	@ResponseBody
	public PaginationList pageList(HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException,
			Exception {
		String pageNo = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");

		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");

		String cardNo = request.getParameter("cardNo");
		String jxid = request.getParameter("jxid");

		this.initNetwork();
		Gson g = new Gson();
		String res = null;
		if (cardNo != null && !"".equals(cardNo)) {
			res = this.client.getStudentInfo(cardNo, jxid);
			if (res != null && !"".equals(res)) {
				Result<StudentInfoForm> stu = g.fromJson(res,
						new TypeToken<Result<StudentInfoForm>>() {
						}.getType());
				if (stu != null && stu.getCode() == 0) {
					StudentInfo entity = new StudentInfo();
					setStudentInfoBean(entity, stu.getData());
					entity.setJxid(jxid);
					StudentInfo vo = studentService.findById(cardNo);
					if (vo != null && !"".equals(vo.getStId())) {
						// 避免更新时把之前备注的信息清除掉
						entity.setQq(vo.getQq());
						entity.setRemark(vo.getRemark());
						// 保存查询到的学员信息,有就更新
						studentService.update(entity);
					} else {
						// 保存查询到的学员信息,没有就插入.
						studentService.save(entity);
					}
				}
			}
		}
		PaginationList listpage = new PaginationList();
		listpage = studentService.findAllPage(cardNo, jxid, beginDate,
				endDate, Integer.valueOf(pageNo), Integer.valueOf(pageSize));
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
	public List<OrderRecordDetailList> detailList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");
		String jxid = request.getParameter("jxid");

		this.initNetwork();
		Gson g = new Gson();
		String res = null;
		if (cardNo != null && !"".equals(cardNo)) {
			res = this.client.getOrderRecord(cardNo, jxid);
			if (res != null && !"".equals(res)) {
				Result<OrderRecordForm> detail = g.fromJson(res,
						new TypeToken<Result<OrderRecordForm>>() {
						}.getType());
				if (detail != null && detail.getCode() == 0) {
					return detail.getData().getResult();
//					StudentInfo entity = new StudentInfo();
//					setStudentInfoBean(entity, detail.getData());
//					entity.setJxid(jxid);
//					StudentInfo vo = studentService.findById(cardNo);
//					if (vo != null && !"".equals(vo.getStId())) {
						// 保存查询到的学员信息,有就更新
//						studentService.update(entity);
//					} else {
						// 保存查询到的学员信息,没有就插入.
//						studentService.save(entity);
//					}
				}
			}
		}
		return null;
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
		String jlcbh = request.getParameter("jlcbh");
		String yyrq = request.getParameter("yyrq");
		String xnsd = request.getParameter("xnsd");
		String jxid = request.getParameter("jxid");

		this.initNetwork();
		Gson g = new Gson();
		String res = null;
		if (cardNo != null && !"".equals(cardNo)) {
			res = this.client.cancelOrder(cardNo, jlcbh, yyrq.replace("/", "-"), xnsd, jxid);
			if (res != null && !"".equals(res)) {
				Result<String> cancel = g.fromJson(res,
						new TypeToken<Result<String>>() {
						}.getType());
				return cancel;
			}
		}
		return null;
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
		String remark = request.getParameter("remark"); // 备注

		this.initNetwork();
		Gson g = new Gson();
		String res = null;
		if (stId != null && !"".equals(stId)) {
			res = this.client.getStudentInfo(stId, jxid);
			Result<StudentInfoForm> stu = g.fromJson(res,
					new TypeToken<Result<StudentInfoForm>>() {
					}.getType());
			if (stu.getCode() == 0) {
				StudentInfo entity = new StudentInfo();
				setStudentInfoBean(entity, stu.getData());
				entity.setJxid(jxid);
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
			return "faile";
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
			StudentInfoForm stuJson) {

		entity.setStNo(stuJson.getST_NO());
		entity.setStId(stuJson.getST_ID());
		entity.setStName(stuJson.getST_NAME());
		entity.setStSex(stuJson.getST_SEX());

		entity.setStAddr(stuJson.getST_ADDR());
		entity.setLxdz(stuJson.getLXDZ());
		entity.set_stIdcard(stuJson.get_ST_IDCARD());
		entity.setStIdcard(stuJson.getST_IDCARD());
		entity.setStOtcard(stuJson.getST_OTCARD());
		entity.setStPhone(stuJson.getST_PHONE());

		entity.setJgids(stuJson.getJGIDS());
		entity.setStHandset(stuJson.getST_HANDSET());
		entity.setStCtype(stuJson.getST_CTYPE());
		entity.setStCtypename(stuJson.getST_CTYPENAME());
		entity.setStLeadcarddate(DateUtils.stringToDate(
				stuJson.getST_LEADCARDDATE(),
				DateUtils.yyyyMMddHHmmssForLoseTime));

		entity.setScId(stuJson.getSC_ID());
		entity.setStSign(stuJson.getST_SIGN());
		entity.setStPwd(stuJson.getST_PWD());
		entity.setStClasssign(stuJson.getST_CLASSSIGN());
		entity.setStClasssname(stuJson.getST_CLASSSName());

		entity.setLxxzrq(DateUtils.stringToDate(stuJson.getLXXZRQ(),
				DateUtils.yyyyMMddHHmmssForLoseTime));
		entity.setBmf(stuJson.getBMF());
		entity.setBmftype(stuJson.getBMFTYPE());
		entity.setBmrq(DateUtils.stringToDate(stuJson.getBMRQ(),
				DateUtils.yyyyMMddHHmmssForLoseTime));
		entity.setBmtbman(stuJson.getBMTBMAN());

		entity.setZhlb(stuJson.getZHLB());
		entity.setCountNumber(stuJson.getCount_Number());
		entity.setSqcx(stuJson.getSQCX());
		entity.setSqcxname(stuJson.getSQCXNAME());
		entity.setJxname(stuJson.getJXNAME().replace("驾校", "").replace("北京市", ""));

		entity.setFpsj(stuJson.getFPSJ());
		entity.setFpzwh(stuJson.getFPZWH());
		entity.setFpjsname(stuJson.getFPJSNAME());
		entity.setSsfy(stuJson.getSSFY());
		entity.setStauts(stuJson.getSTAUTS());
		entity.setStautsname(stuJson.getSTAUTSNAME());
		entity.setSfzx(stuJson.getSFZX());
		entity.setIfjdwzcf(stuJson.getIFJDWZCF());

		entity.setXlxss(stuJson.getXLXSS());
		entity.setYywlxss(stuJson.getYYWLXSS());
		entity.setSyxss(stuJson.getSYXSS());
		entity.setZfxss(stuJson.getZFXSS());
		entity.setGmxss(stuJson.getGMXSS());

		int usexss = Integer.parseInt(stuJson.getXLXSS())
				+ Integer.parseInt(stuJson.getYYWLXSS())
				+ Integer.parseInt(stuJson.getZFXSS());
		entity.setUsexss(String.valueOf(usexss));
	}
}