$(function (){
	
	//controller请求地址
	var url = ctx+"/goods/goodsinfo/list";
	//表格列的定义
	var columns = [[
			           {field:'goodsTitle',title:'标题',width:180,sortable:true},
 			           {field:'state',title:'状态',width:50,formatter:function(value,rowData,rowIndex){
 			        	   if(value=="1"){return "上架"}
 			        	   if(value=="2"){return "下架"}
 			           	 }
 			           },
 			           {field:'createUser',title:'创建人',width:100},
 			           {field:'createDate',title:'创建时间',width:100,formatter:function(value,rowData,rowIndex){
 			        	   return changeDate(value);
 			           	 }
 			           },
 			           {field:'opt',title:'操作',width:80,align:'center',formatter:function(value,rowData,rowIndex){
 			  				
 				  				return '<a href="javascript:void(0);" onclick="doEdit(\''+ rowData.goodsInfoId + '\')">编辑</a>&nbsp;<a href="javascript:void(0);" onclick="deleteLine(\''+rowIndex+'\',\''+rowData.goodsInfoId+'\')" class="tb-btn delete1">删除</a>';
 				  				
 				  			}
 						}
 				  ]]
	
	
	
	//初始化页面
	var page = new $.WifiPage({url:url, initPageNum: 1,initPageSize:10,columns:columns ,param:''});
	page.init();
	
	//查询方法
	$('#queryBtn').click(function(){
		//查询参数
		var goodsTitle= $('#goodsTitle').val();
		var state= $("#state").combobox('getValue');
		var beginDate= $('#beginDate').datebox('getValue');
		var endDate= $('#endDate').datebox('getValue');
		var param = "&goodsTitle="+encodeURI(goodsTitle)+"&state="+state+"&beginDate="+beginDate+"&endDate="+endDate;
		page.queryData(param);
	});
	

	//增加
	$('#add').click(function(){
		location.href =ctx+"/goods/addgoodsinfo";
	});


})

//删除操作
function deleteLine(rowIndex,goodsInfoId){
	$.messager.confirm('确认', '	确认删除这个资源?', function(r) {
		if (r) {
			$.ajax({
				type : 'get',
				url : ctx + "/goods/goodsinfo/delete?goodsInfoId=" + goodsInfoId,
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
function doEdit(goodsInfoId){
	location.href =ctx+"/goods/addgoodsinfo?goodsInfoId="+goodsInfoId;
}




