$(function (){
//	$('#dlgadd').dialog('close');
	$('#dlgadd_detail').dialog('close');
//	var strDate = getSystemTime();
//	$("#beginDate").datebox("setValue", strDate);
//	$("#endDate").datebox("setValue", strDate);
	//controller请求地址
	var url = ctx+"/book/booklist/list";
	
	//查询方法
	$('#queryBtn').click(function(){
		$('#dataLoad').show();
		//查询参数	
		var cardNo = $("#cardNo").val();
		var jxid = $("#jxid").combobox('getValue');
//		var beginDate = $('#beginDate').datebox('getValue');
//		var endDate = $('#endDate').datebox('getValue');
		var param = "&cardNo="+cardNo+"&jxid="+jxid;
		$.ajax({
			url: ctx + "book/booklist/list",  //请求地址
			dataType: "text",
			type: "GET",
			cache: false,
			data: param,
			success : function(s) {
				$("#table").empty();
				$('#dataLoad').hide();
				var res = jQuery.parseJSON(s);
				if (res != null && res.code == 0) {
					var xnsdList = res.data.xnsdList;
					var yyrqList = res.data.yyrqList;
					var uidatas = res.data.uidatas;
					if (xnsdList == null || xnsdList == '' || yyrqList == null || yyrqList == '' || uidatas == null || uidatas == '') {
						return alert("查询失败");
					}
					$("#table").append('<tr><td id="datetd" style="width:16px;text-align:center;"></td><tr id="datetr"></tr></tr>');
					//下面使用each进行遍历
					$.each(yyrqList, function (index, valueDate) {
						var yyrqDate = valueDate.yyrq.substring(0,10);
						$("#datetd").after('<td style="width:30px;text-align:center;">' + yyrqDate.substring(5,10).replace("/","-").replace("/","-") + valueDate.displayWeek.substring(1,2) + '</td>');
					});
					//下面使用each进行遍历
					$.each(xnsdList, function (indexXnsd, valueXnsd) {
						var xnsd_outer = valueXnsd.xnsd;
						var xnsd_show;
						if (xnsd_outer == '812') {
							xnsd_show = '上午';
						} else if (xnsd_outer == '15') {
							xnsd_show = "下午";
						} else if (xnsd_outer == '58'||xnsd_outer == '59') {
							xnsd_show = "晚上";
						} else if (xnsd_outer == '13'||xnsd_outer == '35') {
							xnsd_show = xnsd_outer;
						}
						$("#datetr").before('<tr><td style="width:16px;text-align:center;" id="datetd'+xnsd_outer+'">'+ xnsd_show +'</td></tr>');
						//下面使用each进行遍历
						$.each(uidatas, function (i, value) {
							var xnsd = value.xnsd;
							if (xnsd_outer == xnsd) {
								if (value.isBpked) {
									$("#datetd"+xnsd).after('<td style="width:30px;text-align:center;color:red;">已约</td>');
								} else if (value.sl == 0) {
									$("#datetd"+xnsd).after('<td style="width:30px;text-align:center;color:black;">无</td>');
								} else {
									$("#datetd"+xnsd).after('<td style="width:30px;text-align:center;"><a href="javascript:void(0);" onclick="queryCarsNo(\''+ cardNo + '\',\'' 
											+ value.yyrq.substring(0,10).replace("/","-").replace("/","-") + '\',\'' + value.xnsd + '\',\'' + jxid + '\')">' + value.sl+ "#"+ value.yyrq.substring(8,10) + '</a></td>');
								}
							}
						});
					});
				} else if (res != null && res.code != 0) {
					alert(res.message);
				}
			},
			async : false
		});
	});
	
	//增加
	$('#add').click(function(){
		location.href = ctx+"/student/addstudentinfo";
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
					} else if (obj != null && obj.code != 0) {
						alert(obj.message);
					}
				},
				async : false
			});
		}
	});
}

function daysBetween(DateOne, DateTwo) {   
    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));  
    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);  
    var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));  
  
    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));  
    var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);  
    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));  
  
    var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);   
    return Math.abs(cha);  
}  

// 查询车号
function queryCarsNo(cardNo,yyrq,xnsd,jxid) {
	var now = new Date();
	var sysDate = now.getFullYear()+"-"+((now.getMonth()+1)<10?"0":"")+(now.getMonth()+1)+"-"+(now.getDate()<10?"0":"")+now.getDate();
	if (daysBetween(yyrq, sysDate) < 2) {
		$.messager.confirm('确认', '注意：预约&nbsp;&nbsp;'+yyrq+'&nbsp;&nbsp;的车将不能取消，继续吗?', function(r) {
			if (r) {
				queryCarsNoDetail(cardNo,yyrq,xnsd,jxid);
			}
		});
	} else {
		queryCarsNoDetail(cardNo,yyrq,xnsd,jxid);
	}
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
}

function queryCarsNoDetail(cardNo,yyrq,xnsd,jxid) {
	$('#dataLoad').show();
	$.ajax({
		url: ctx + "/book/booklist/list/carsno",  //请求地址
		dataType: "text",
		type: "GET",
		cache: false,
		async: false,
		data: "&cardNo=" + cardNo + "&yyrq=" + yyrq + "&xnsd=" + xnsd + "&jxid=" + jxid,
		success: function(jsonDate) {
			var res = jQuery.parseJSON(jsonDate);
			$('#dataLoad').hide();
			if (res != null && res.code == 0) {
				$("#table_detail").empty();
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
				var obj = res.data.result;
				if (obj == null || obj == '') {
					return alert("无剩余车辆");
				}
				//下面使用each进行遍历
				$.each(obj, function (i, value) {
					if (i%10 == 0) {
						$("#table_detail").append('<tr><td id="td'+ i +'"><a href="javascript:void(0);" onclick="submitOrder(\''+ cardNo + '\',\'' 
								+ yyrq + '\',\'' + xnsd + '\',\'' + value.cnbh + '\',\'' + jxid + '\')">'+ value.cnbh +'</a></td></tr>');
					} else {
						$("#td" + (i-1)).after('<td id="td'+ i +'"><a href="javascript:void(0);" onclick="submitOrder(\''+ cardNo + '\',\'' 
								+ yyrq + '\',\'' + xnsd + '\',\'' + value.cnbh + '\',\'' + jxid + '\')">'+ value.cnbh +'</a></td>');
					}
				});
				$('#dlgadd_detail').dialog('open').dialog('setTitle','预约记录&nbsp;&nbsp;&nbsp;&nbsp;' + jxname + '&nbsp;&nbsp;卡号：' + cardNo);
			} else if (res != null && res.code != 0) {
				return alert(res.message);
			}
		},
	});
}

// 预订
function submitOrder(cardNo,yyrq,xnsd,cnbh,jxid) {
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
	$.messager.confirm('确认', '您确认预约&nbsp;&nbsp;'+yyrq+"&nbsp;&nbsp;"+xnsd_show+'&nbsp;&nbsp;时段的&nbsp;&nbsp;'+ cnbh +'&nbsp;&nbsp;号车吗?', function(r) {
		if (r) {
			$.ajax({
				url: ctx + "/book/booklist/list/submitorder",  //请求地址
				dataType: "text",
				type: "GET",
				cache: false,
				data: "&cardNo=" + cardNo + "&yyrq=" + yyrq + "&xnsd=" + xnsd + "&cnbh=" + cnbh + "&jxid=" + jxid,
				success : function(s) {
					queryCarsNoDetail(cardNo,yyrq,xnsd,jxid);
					var obj = jQuery.parseJSON(s);
					if (obj != null) {
						if (obj.message == null || obj.message == '') {
							alert("预约成功");
						} else {
							alert(obj.message);
						}
					}
				},
				async : false
			});
		}
	});
}

// 转
function change(cardNo,yyrq,xnsd,jlcbh,cnbh,jxid) {
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
	$.messager.confirm('确认', '您确认将源卡：'+cardNo+'&nbsp;&nbsp;'+yyrq+"&nbsp;&nbsp;&nbsp;&nbsp;"+xnsd_show+'&nbsp;&nbsp;时段转入到目标卡：'+ $("#cardNoTo").val() +'&nbsp;吗?', function(r) {
		if (r) {
			$.ajax({
				url: ctx + "student/studentinfo/list/detail/change",  //请求地址
				dataType: "text",
				type: "GET",
				cache: false,
				data: "&cardNo=" + cardNo + "&yyrq=" + yyrq + "&xnsd=" + xnsd + "&jlcbh=" + jlcbh + "&cnbh=" + cnbh + "&jxid=" + jxid + "&cardNoTo=" + $("#cardNoTo").val(),
				success : function(s) {
					showDetail(cardNo,jxid);
					var obj = jQuery.parseJSON(s);
					if (obj != null) {
						if (obj.message == null || obj.message == '') {
							alert("转换成功");
						} else {
							alert(obj.message);
						}
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
			var res = jQuery.parseJSON(jsonDate);
			if (res != null && res.code == 0) {
				$("#table_detail tr").empty();
				$("#table_detail").append('<tr><td colspan="2">源卡：' + stId + '</td><td style="text-align:right">目标卡：</td><td width="54px" nowrap><input style="width:96%" class="easyui-textbox" id="cardNoTo" name="cardNoTo"></input></td></tr>');
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
				var obj = res.data.result;
				if (obj == null || obj == '') {
					return alert("无预约记录");
				}
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
						$("#table_detail").append('<tr><td>' + yyrq + '</td><td>' + xnsd + '</td><td>' + value.cnbh + '</td><td>' + sfxl 
								+ '</td><td><a href="javascript:void(0);" onclick="cancelOrder(\''+ stId + '\',\'' + yyrq + '\',\'' + value.xnsd + '\',\'' + value.jlcbh + '\',\'' 
								+ jxid + '\')">cancel</a></td><td><a href="javascript:void(0);" onclick="change(\''+ stId + '\',\'' 
								+ yyrq + '\',\'' + value.xnsd + '\',\'' + value.jlcbh + '\',\'' + value.cnbh + '\',\'' + jxid + '\')">change</a></td></tr>');
					}
				});
				$('#dlgadd_detail').dialog('open').dialog('setTitle','预约记录&nbsp;&nbsp;&nbsp;&nbsp;' + jxname + '&nbsp;&nbsp;卡号：' + stId);
			} else if (res != null && res.code != 0) {
				return alert(res.message);
			}
		},
	});
}

// 输入验证码
function checkRadomCode(stId,jxid) {
//	$('#table').datagrid('selectRow', stId);
    $.ajax({
		url: ctx + "student/studentinfo/list/checkcode",  //请求地址
		dataType: "text",
		type: "GET",
		cache: false,
		async: false,
		data: "&cardNo=" + stId + "&jxid=" + jxid,
		success: function(jsonDate) {
			if (jsonDate == '') {
				return alert("无预约记录");
			}
			if (jsonDate != null && jsonDate.code == 0) {
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
				var obj = jQuery.parseJSON(jsonDate.data.Result);
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
						$("#table_detail").append('<tr><td>' + yyrq + '</td><td>' + xnsd + '</td><td>' + value.cnbh + '</td><td>' + sfxl + '</td><td><a href="javascript:void(0);" onclick="cancelOrder(\''+ stId + '\',\'' + yyrq + '\',\'' + value.xnsd + '\',\'' + value.jlcbh + '\',\'' + value.cnbh + '\',\'' + jxid + '\')">取消</a></td></tr>'); 
					}
				});
				$('#dlgadd_detail').dialog('open').dialog('setTitle','预约记录&nbsp;&nbsp;&nbsp;&nbsp;' + jxname + '&nbsp;&nbsp;卡号：' + stId);
			} else if (jsonDate != null && jsonDate.code != 0) {
				return alert(jsonDate.message);
			}
		},
	});
}
