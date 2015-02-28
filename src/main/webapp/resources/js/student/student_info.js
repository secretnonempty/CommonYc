$(function (){
	
	$('#dlgadd').dialog('close');
	$('#dlgadd_detail').dialog('close');
//	var strDate = getSystemTime();
//	$("#beginDate").datebox("setValue", strDate);
//	$("#endDate").datebox("setValue", strDate);
	//controller请求地址
	var url = ctx+"/student/studentinfo/list";
	//表格列的定义
	var columns = [[
			           {field:'stNo',title:'编号',width:50,align:'center',sortable:true,
			        	   formatter: function(value,row,index){
			        				return index + 1;
			        		}
			           },
			           {field:'stId',title:'卡号',width:120,align:'center',sortable:true},
			           {field:'stPwd',title:'密码',width:160,align:'center',sortable:true},
			           {field:'stName',title:'姓名',width:100,align:'center',
			        	   formatter:function(value,rowData,rowIndex) {
			        		   return '<a href="javascript:void(0);" onclick="showDetail(\''+ rowData.stId + '\',\'' + rowData.jxid + '\')">'+ rowData.stName +'</a>';
			        	   }
			           },
			           {field:'jxname',title:'驾校名称',width:110,align:'center',sortable:true},
			           {field:'sqcxname',title:'车型',width:90,align:'center',sortable:true},
			           {field:'stClasssname',title:'班种',width:120,align:'center',sortable:true},
			           {field:'qq',title:'QQ',width:140,align:'center'},
			           {field:'stautsname',title:'所处阶段',width:140,align:'center',sortable:true},
			           {field:'usexss',title:'已用',width:60,align:'center',sortable:true},
			           {field:'xlxss',title:'训练',width:60,align:'center',sortable:true},
			           {field:'yywlxss',title:'预约',width:60,align:'center',sortable:true},
			           {field:'syxss',title:'剩余',width:60,align:'center',sortable:true},
 			           {field:'zfxss',title:'作废',width:60,align:'center',sortable:true},
 			           {field:'gmxss',title:'共计',width:60,align:'center',sortable:true},
 			           {field:'remark',title:'备注',width:280,align:'center',sortable:true},
 			           {field:'opt',title:'操作',width:140,align:'center',formatter:function(value,rowData,rowIndex) {
 			        	   return '<a href="javascript:void(0);" onclick="doEdit(\''+ rowIndex + '\')">编辑</a>&nbsp;<a href="javascript:void(0);" onclick="deleteLine(\''+rowIndex+'\',\''+rowData.stId+'\')" class="tb-btn delete1">删除</a>';
 			           }
					}
 				  ]]
	
	//初始化页面
	var page = new $.WifiPage({url:url, initPageNum: 1, initPageSize:12, columns:columns ,param:'&jxid=1'});
	page.init();
	
	//查询方法
	$('#queryBtn').click(function(){
		//查询参数	
		var cardNo = $("#cardNo").val();
		var jxid = $("#jxid").combobox('getValue');
		var beginDate = $('#beginDate').datebox('getValue');
		var endDate = $('#endDate').datebox('getValue');
		var param = "&cardNo="+cardNo+"&jxid="+jxid+"&beginDate="+beginDate+"&endDate="+endDate;
		page.queryData(param);
	});
	
	//组织机构下拉树
//	$('#organization').combotree({   
//	    url: ctx + '/organization',
//		required: true,
//		onChange: function(newValue, oldValue) {
//			var t = $('#organization').combotree('tree');// 获取树对象
//			var n = t.tree('getSelected');// 获取选择的节点
//			var url = ctx + '/order/getTrainCode?orgId=' + n.id;
//			$('#trainNo').combobox('setValue', '');
//            $('#trainNo').combobox('reload', url);
//			var param = "";
//			page.queryData(param);
//		}
//	});
	
	//增加
	$('#add').click(function(){
		location.href = ctx+"/student/addstudentinfo";
	});
	
	
	//提交编辑或保存
	$("#sub").click(function(){
//		if ($("#ff").form("validate")) { //easyUi自动校验
			$('#dlgadd').dialog('close');//校验通过关闭对话框
			var index = $('#rowIndex').attr("value");
			$.ajax({
				cache: true,
				type: "POST",
				url:"editOrAddStudent",
				data:$('#ff').serialize(),// 你的formid
				async: false,
				error: function(request) {
					$.messager.show({
						title:'系统提示',
						msg:'系统异常稍候重试!',
						showType:'show'
					});
					$('#dlgadd').dialog('close');
				},
				success: function(data) {
					if(data=="success") {
						$('#dlgadd').dialog('close');
						page.init();
						$.messager.show({
							title:'系统提示',
							msg:'操作成功!',
							showType:'show'
						});
						return;
					} else {
						$.messager.show({
							title:'系统提示',
							msg:'编辑失败!',
							showType:'show'
						});
						$('#dlgadd').dialog('close');
					}
				}
			});
//		} else {
//			$.messager.show({
//				title:'系统提示',
//				msg:'请检查您提交的数据',
//				showType:'show'
//			});
//		}
		});
})
// 取消预约
function cancelOrder(cardNo,yyrq,xnsd,jlcbh,jxid) {
	var xnsd_show;
	if (xnsd == '812') {
		xnsd_show = '上午';
	} else if (xnsd == '15') {
		xnsd_show = "下午";
	} else if (xnsd == '58'||xnsd == '59') {
		xnsd_show = "晚上";
	} else if (xnsd == '13'||xnsd == '35') {
		xnsd_show= xnsd;
	}
	$.messager.confirm('确认', '您确认取消&nbsp;&nbsp;'+yyrq+"&nbsp;&nbsp;&nbsp;&nbsp;"+xnsd_show+'&nbsp;&nbsp;时段的这条预约记录吗?', function(r) {
		if (r) {
			$.ajax({
				url: ctx + "student/studentinfo/list/detail/cancelorder",  //请求地址
				dataType: "text",
				type: "GET",
				cache: false,
				data: "&cardNo=" + cardNo + "&yyrq=" + yyrq + "&xnsd=" + xnsd + "&jlcbh=" + jlcbh + "&jxid=" + jxid,
				success : function(s) {
					showDetail(cardNo,jxid);
					var obj = jQuery.parseJSON(s);
					if (obj != null && obj.code == 0) {
						alert("取消成功");
					} else if (obj != null && obj.code == 1) {
						alert(obj.message);
					}
				},
				async : false
			});
		}
	});
}

// 删除操作
function deleteLine(rowIndex, stId) {
	$.messager.confirm('确认', '您确认删除这条记录吗?', function(r) {
		if (r) {
			$.ajax({
				type : 'get',
				url : ctx + "/student/studentinfo/delete?stId=" + stId,
				success : function(s) {
					if ("success" == s) {						
						$('#table').datagrid('reload'); 
						$('#table').datagrid('deleteRow',rowIndex);//根据索引删除当前行
						$.messager.show({
		        			title:'系统提示',
		        			msg:'删除成功!',
		        			showType:'show'
		        			});
						// $.messager.alert('系统提示', "删除成功!", 'info');				 
					} else
						$.messager.show({
		        			title:'系统提示',
		        			msg:'删除失败!',
		        			showType:'show'
		        			});
					 //$.messager.alert('系统提示', "删除失败!", 'info');
				},
				async : false
			});
		}
	});
}

//弹出编辑或添加对话框
function doEdit(rowIndex) {
	$('#table').datagrid('selectRow', rowIndex);
    var row = $('#table').datagrid('getSelected');
	if (row) {
		$('#dlgadd').dialog('open').dialog('setTitle','学生信息编辑');
		$('#ff').form('load', {
			stName:row.stName,
			stId :row.stId,
			jxname :row.jxname,
			jxid :row.jxid,
			stClasssname: row.stClasssname,
			stautsname:row.stautsname,
			qq:row.qq,
			remark:row.remark
		});
	}
//	location.href = ctx + "/student/editstudentinfo?stId=" + stId;
}

// 弹出具体的预约日期
function showDetail(stId,jxid) {
//	$('#table').datagrid('selectRow', stId);
    $.ajax({
		url: ctx + "student/studentinfo/list/detail",  //请求地址
		dataType: "text",
		type: "GET",
		cache: false,
		async: false,
		data: "&cardNo=" + stId + "&jxid=" + jxid,
		success: function(jsonDate) {
			if (jsonDate == '') {
				return alert("无预约记录");
			}
			if (jsonDate != null) {
				$("#table_detail tr").empty();
				var jxname;
				if (jxid == '1') {
					jxname = '龙泉';
				} else if (jxid == '2') {
					jxname = "新月";
				} else if (jxid == '3') {
					jxname = "京都府";
				} else if (jxid == '4') {
					jxname = "海驾";
				} else if (jxid == '5') {
					jxname = "新丰";
				}
//				$("#table_detail").append('<tr><td>' + stId + '</td><td>' + jxname + '</td></tr>');
				var obj = jQuery.parseJSON(jsonDate);
				var yyrq_temp;
				var xnsd_temp;
				//下面使用each进行遍历
				$.each(obj, function (i, value) {
					var yyrq = value.yyrq.substring(0,10);
					if (yyrq_temp != yyrq || xnsd_temp != value.xnsd) {
						var xnsd;
						if (value.xnsd == '812') {
							xnsd = '上午';
						} else if (value.xnsd == '15') {
							xnsd = "下午";
						} else if (value.xnsd == '58'||value.xnsd == '59') {
							xnsd = "晚上";
						} else if (value.xnsd == '13'||value.xnsd == '35') {
							xnsd= value.xnsd;
						}
						var sfxl;
						if (value.sfxl == '8') {
							sfxl = '未训';
						} else if (value.sfxl == '1') {
							sfxl = "已训";
						} else if (value.sfxl == '3') {
							sfxl = "作废";
						} else {
							sfxl= value.sfxl;
						}
						yyrq_temp = yyrq;
						xnsd_temp = value.xnsd;
						$("#table_detail").append('<tr><td>' + yyrq + '</td><td>' + xnsd + '</td><td>' + value.cnbh + '</td><td>' + sfxl + '</td><td><a href="javascript:void(0);" onclick="cancelOrder(\''+ stId + '\',\'' + yyrq + '\',\'' + value.xnsd + '\',\'' + value.jlcbh + '\',\'' + jxid + '\')">取消</a></td></tr>');
					}
				});
				$('#dlgadd_detail').dialog('open').dialog('setTitle','预约记录&nbsp;&nbsp;&nbsp;&nbsp;' + jxname + '&nbsp;&nbsp;卡号：' + stId);
			}
		},
	});
}
