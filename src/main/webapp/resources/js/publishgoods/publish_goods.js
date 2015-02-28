$(function (){

	//controller请求地址
	var url = ctx+"/publishgoods/publishgoods/list";
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
 			  				
 				  				return '<a href="javascript:void(0);" onclick="deleteLine(\''+rowIndex+'\',\''+rowData.goodsInfoId+'\')" class="tb-btn delete1">取消发布</a>';
 				  				
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
		var columnId= $('#columnId').val();
		var param = "&goodsTitle="+encodeURI(goodsTitle)+"&state="+state+"&beginDate="+beginDate+"&endDate="+endDate+"&columnId="+columnId;
		page.queryData(param);
	});

	

	//选择发布商品
	$('#add').click(function(){
		if($('#columnId').val()==""){
			alert("请选择栏目");
			return;
		}
		$('#dd').dialog('open');

		$("#selectGoodsIframe").attr("src",'../selectgoods/selectgoods');
		//document.frames('selectGoodsIframe').document.location='../selectgoods/selectgoods';
		
	});
	
	//关闭dialog窗口
	$('#closeBtn').click(function(){
		$('#dd').dialog('close');
	});
	
	//发布商品
	$('#publishBtn').click(function(){
	
		 var selectGoodsIndex = $('#selectGoodsIframe').contents().find("#selectGoodsIndex").val();  //获得选择框的选择集合

		 if(selectGoodsIndex==""){
			 alert("请选择发布商品");
			 return;
		 }
		 
		 $.ajax({
				url:'../publishgoods/publishgoods/savepublish',
				dataType: "text",
				async:false,
				type: "GET",
				data: "selectGoodsIndex="+selectGoodsIndex+"&columnId="+$('#columnId').val(),
				success: function(date) {
					if(date=="success"){
						alert("发布成功");
						$('#dd').dialog('close');
						var param = "&columnId="+$('#columnId').val(); //列表在查一下
				 		page.queryData(param);
					}
				},
				cache: false
		});

	});
   
    //栏目下拉树
	$('#cc').combotree({    
	    url: '../publishgoods/publishgoods/columntree',    
	    required: true,
	    onChange: function(newValue, oldValue) {
	    	 var t = $('#cc').combotree('tree');	// 获取树对象
	    	 var n = t.tree('getSelected');		// 获取选择的节点
	    	 var param = "&columnId="+n.id;
	 		 page.queryData(param);
	 		//$('#selectGoodsIframe').contents().find("#columnId").val(columnId);
	 		$('#columnId').val(n.id);
	    }
	}); 


})

//删除操作
function deleteLine(rowIndex,goodsInfoId){
	$.messager.confirm('确认', '	确认取消发布资源?', function(r) {
		if (r) {
			$.ajax({
				type : 'get',
				url : ctx + "/publishgoods/publishgoods/delpublish?goodsInfoId=" + goodsInfoId+"&columnId="+$('#columnId').val(),
				success : function(s) {
					if ("success" == s) {										
						$('#table').datagrid('reload'); 
						$('#table').datagrid('deleteRow',rowIndex);//根据索引删除当前行
						$.messager.show({
		        			title:'系统提示',
		        			msg:'取消发布成功!',
		        			showType:'show'
		        			});
						// $.messager.alert('系统提示', "删除成功!", 'info');				 
					} else
						$.messager.show({
		        			title:'系统提示',
		        			msg:'取消发布失败!',
		        			showType:'show'
		        			});
					 //$.messager.alert('系统提示', "删除失败!", 'info');
				},
				async : false
			});
		}
	});
}





