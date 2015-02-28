$(function (){
	
	//controller请求地址
	var url = ctx+"/publish/publish/list";
	//表格列的定义
	var columns = [[
	                   {field:'trainNo',title:'车次',width:50},
			           {field:'name',title:'包名',width:180},
 			           {field:'version',title:'版本号',width:50},
 			           {field:'createUser',title:'创建人',width:60},
 			           {field:'createDate',title:'创建时间',width:110,formatter:function(value,rowData,rowIndex){
 			        	   return changeDate(value);
 			           	 }
 			           },
 			           {field:'opt',title:'操作',width:70,align:'center',formatter:function(value,rowData,rowIndex){
 			  				
 				  				return '<a href="javascript:void(0);" onclick="deleteLine(\''+rowIndex+'\',\''+rowData.publishId+'\')" class="tb-btn delete1">删除</a>';
 				  				
 				  			}
 						}
 				  ]]
	
	
	
	//初始化页面
	var page = new $.WifiPage({url:url, initPageNum: 1,initPageSize:10,columns:columns ,param:''});
	page.init();
	
	//查询方法
	$('#queryBtn').click(function(){
		//查询参数
		var name= $('#name').val();
		var trainNo= $("#trainNo").combobox('getValue');
		var beginDate= $('#beginDate').datebox('getValue');
		var endDate= $('#endDate').datebox('getValue');
		var param = "&name="+encodeURI(name)+"&trainNo="+trainNo+"&beginDate="+beginDate+"&endDate="+endDate;
		page.queryData(param);
	});
	

	//增加
	$('#add').click(function(){
		location.href =ctx+"/publish/addpublish";
	});


})

//删除操作
function deleteLine(rowIndex,publishId){
	$.messager.confirm('确认', '	确认删除这个资源?', function(r) {
		if (r) {
			$.ajax({
				type : 'get',
				url : ctx + "/publish/publish/delete?publishId=" + publishId,
				success : function(s) {
					if ("success" == s) {										
						$('#table').datagrid('reload'); 
						$('#table').datagrid('deleteRow',rowIndex);//根据索引删除当前行
						$.messager.show({
		        			title:'系统提示',
		        			msg:'删除成功!',
		        			showType:'show'
		        		});				 
					} 
					if("faile"==s){
						$.messager.show({
		        			title:'系统提示',
		        			msg:'删除失败!',
		        			showType:'show'
		        		});
					}
					if("fileDelete"==s){
						$.messager.show({
		        			title:'系统提示',
		        			msg:'删除文件失败!',
		        			showType:'show'
		        		});
					}
						
				},
				async : false
			});
		}
	});
}




