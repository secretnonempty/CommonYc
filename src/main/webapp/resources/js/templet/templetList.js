$(function (){	
	
	//加载时对话框处于关闭状态
	$('#dlgadd').dialog('close');
	
	//controller请求地址
	var url = ctx + "/templetList/templetList";
	//表格列的定义
	var columns = [[
{
	field : 'templetListName',
	title : '套装名称',
		width:4
},
{
	field : 'templetListType',
	title : '套装类型',
		width:7,formatter:function(value,rowData,rowIndex){
			
			if(rowData.templetListType=="1"){
		  		return "栏目列表类型";
		  	  }
		  	  if(rowData.templetListType=="2"){
		  		  return "商品列表类型";
		  	  }
		  	
			
	   	 }
},
{
	field : 'descript',
	title : '描述',
		width:5
},
{
	field : 'templetListUrl',
	title : '路径',
	width:5
},
//{
//	field : 'unit',
//	title : '所属单位ID',
//	width:3
//},
{
	field : 'createDate',
	title : '创建时间',
	width:7,formatter:function(value,rowData,rowIndex){
  	   return changeDate(value);
   	 }
},
{
	field : 'createUser',
	title : '创建人',
	width:3
},
{
	field : 'lastModifyDate',
	title : '更新时间',
	width:7,
	formatter:function(value,rowData,rowIndex){
  	   return changeDate(value);
     	 }
},
{
field : '',
title : '操作',
width : 40,
align : 'center',
formatter : function(value, rowData, rowIndex) {
	return '<a href="javascript:void(0);" onclick="deleteLine(\''+rowIndex+'\',\''
	+ rowData.templetListId
			+ '\')" class="tb-btn delete1">删除</a>&nbsp; <a href="javascript:void(0);" onclick="doEdit(\''
			+ rowIndex + '\');" iconCls="icon-add">编辑</a>';

}
} ] ]
	

	
	
	//初始化页面
	var page = new $.WifiPage({url:url, initPageNum: 1,initPageSize:5,columns:columns ,param:''});
	page.init();
	
	
	//查询
	$("#queryBtn").click(function(){
		//查询参数
		
		var organization= $("#organization").combotree('getValue');
		var beginDate= $('#beginDate').datebox('getValue');
		var endDate= $('#endDate').datebox('getValue');
		var param = "&organization="+organization+"&beginDate="+beginDate+"&endDate="+endDate;
		page.queryData(param);
	});
	
	
	//列表页跳转到新增页面
	$('#add').click(function(){
		doEdit("add");
		//location.href =ctx+"/templet/addTemplet";
	});
	
	

	//组织机构下拉树
	$('#organization').combotree({    
	   url:ctx + '/organization',    
		required: true,
		onChange: function(newValue, oldValue) {
		var t = $('#organization').combotree('tree');	// 获取树对象
		var n = t.tree('getSelected');		// 获取选择的节点
		organization = n.id;
		var param ="&organization="+organization;
		page.queryData(param);
			    }
			}); 
	

	//新建模板列表时取组织机构
	$('#userOrganization').combotree({   
		 url:ctx+'/organization',    
		valueField:'id', 
		 //valueField:'templetListName', 
		 textField:'organizationName'   
			}); 
	

	//提交编辑或保存
	 $("#sub").click(function(){
		 if ($("#ff").form("validate")){//easyUi自动校验
			  $('#dlgadd').dialog('close');
				 var index=$('#rowIndex').attr("value");		
				  $.ajax({
				        cache: true,
				        type: "POST",
				        url:"saveOrEdit",
				        data:$('#ff').serialize(),// 你的formid
				        async: false,
				        error: function(request) {
				        	  $('#dlgadd').dialog('close');
				        	  $.messager.show({
				        			title:'系统提示',
				        			msg:'操作失败!',
				        			showType:'show'
				        			});
				        },
				        success: function(data) {
					        if(data=="success"){	
					        	 $('#dlgadd').dialog('close');
					        	$('#table').datagrid('reload'); 
					        	page.init();//重新加载列表
				        		$.messager.show({
				        			title:'系统提示',
				        			msg:'操作成功!',
				        			showType:'show'
				        			});
					        }else{
					        	$('#dlgadd').dialog('close');
					        	$.messager.show({
				        			title:'系统提示',
				        			msg:'操作失败!',
				        			showType:'show'
				        			});
						        }
				            //$("#commonLayout_appcreshi").parent().html(data);
				        }
				    });
		 }
		
	  })

})



//编辑或添加
function doEdit(rowIndex){
	
	$('#ff').form("clear");//先请空
	if(rowIndex=="add"){
		$('#dlgadd').dialog('open').dialog('setTitle', '模板套装添加');
		$('#ff').form('load', {
			templetListName:"",
			descript :"",
			//url :"",
			//templetListUrl:""
			});
		$('#keyid').attr('value', "add");	
	}else{
	     $('#table').datagrid('selectRow',rowIndex);
	     var row = $('#table').datagrid('getSelected');
		if (row) {
			$('#dlgadd').dialog('open').dialog('setTitle', '模板套装编辑');
			$('#ff').form('load', {
				templetListName:row.templetListName,
				descript :row.descript,
				//templetListUrl :row.templetListUrl,
				//content: row.content,
				createDate:row.createDate,
				templetListType:row.templetListType,
				userOrganization:row.unit
				});	
			$('#keyid').attr('value', row.templetListId);
		}
	}
}



//删除操作
function deleteLine(rowIndex,templetListId){
	$.messager.confirm('确认', '	确认删除这个资源?', function(r) {
		if (r) {
			$.ajax({
				type : 'get',
				url : ctx + "templetList/delete?id=" + templetListId,
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