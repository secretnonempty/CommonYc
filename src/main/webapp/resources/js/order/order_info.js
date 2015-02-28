$(function (){
	
	var strDate = getSystemTime();
//	$("#beginDate").datebox("setValue", strDate);
//	$("#endDate").datebox("setValue", strDate);
	//controller请求地址
	var url = ctx+"/order/orderinfo/list";
	//表格列的定义
	var columns = [[
			           {field:'orderInfoId',title:'编号',width:80,sortable:true,
			        	   formatter: function(value,row,index){
			        				return index + 1;
			        		}
			           },
			           {field:'orderCode',title:'订单号',width:160,sortable:true},
			           {field:'goodsTitle',title:'商品名称',width:200,sortable:true},
			           {field:'trainCode',title:'车次',width:100},
			           {field:'boxNo',title:'车厢号',width:100,sortable:true},
			           {field:'seatNo',title:'席位号',width:100,sortable:true},
			           {field:'userPhone',title:'手机号码',width:200},
			           {field:'createDate',title:'下单时间',width:260,sortable:true,
			        	   formatter:function(value,row,index){
 			        	   return changeDate(value);
			           	 }},
			           {field:'onLine',title:'订单类型',width:120,sortable:true,
			        	   formatter: function(value,row,index){
			        			if (row.onLine == "1") {
			        				return row.onLine = "线上";
			        			} else {
			        				return row.onLine = "线下";
			        			}
			        		}
			           },
			           {field:'operateUserName',title:'操作员',width:120,sortable:true},
 			           {field:'goodsCount',title:'数量',width:100,sortable:true},
 			           {field:'goodsPrice',title:'总价',width:120,sortable:true},
 			           {field:'orderState',title:'订单状态',width:180,sortable:true,
 			        	  formatter: function(value, row, index) {
			        			if (row.orderState == "1") {
			        				return row.orderState = "已提交";
			        			} else if (row.orderState == "3") {
			        				return row.orderState = "已取消";
			        			} else if (row.orderState == "5") {
			        				return row.orderState = "已完成";
			        			}
			        		}
 			           }
 				  ]]
	
	//初始化页面
	var page = new $.WifiPage({url:url, initPageNum: 1, initPageSize:5, columns:columns ,param:''});
	page.init();
	
	//查询方法
	$('#queryBtn').click(function(){
		//查询参数	
		var organization= $("#organization").combotree('getValue');
		var trainNo= $("#trainNo").combobox('getValue');
		var beginDate= $('#beginDate').datebox('getValue');
		var endDate= $('#endDate').datebox('getValue');
		var param = "&organization="+organization+"&trainNo="+trainNo+"&beginDate="+beginDate+"&endDate="+endDate;
		page.queryData(param);
	});
	
	//组织机构下拉树
	$('#organization').combotree({   
	    url: ctx + '/organization',
		required: true,
		onChange: function(newValue, oldValue) {
			var t = $('#organization').combotree('tree');// 获取树对象
			var n = t.tree('getSelected');// 获取选择的节点
			var url = ctx + '/order/getTrainCode?orgId=' + n.id;
			$('#trainNo').combobox('setValue', '');
            $('#trainNo').combobox('reload', url);
			var param = "";
			page.queryData(param);
		}
	});
	
	//增加
	$('#add').click(function(){
		location.href =ctx+"/order/addorderinfo";
	});
})

//删除操作
function deleteLine(index){
	alert(index);
}





