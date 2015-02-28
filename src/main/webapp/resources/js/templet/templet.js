$(function (){	
	
	//清空查询条件
	$('#f1').form("clear");
	//$("#templetType").combobox('select',"") 
	
	
	//初始化模板套装列表
	$('#templetListId').combobox({   
	    url:ctx+'/templetList/comboboxList',    
	    valueField:'templetListId', 
	    //valueField:'templetListUrl', 
	    textField:'templetListName'   
	});
	
	//加载时对话框处于关闭状态
	$('#dlgadd').dialog('close');
	
	//controller请求地址
	var url = ctx + "/templet/list";
	//表格列的定义
	var columns = [[
{
	field : 'name',
	title : '名称',
	width:4
},
{
	field : 'extension',
	title : '扩展名',
	width:4
},
{
	field : 'descript',
	title : '描述',
		width:5
},
{field : 'templetListId',title : '所属模板套装',width:5,
	   formatter: function(value,row,index){
		var cai;
		   $.ajax({
				url: ctx +"/templetList/templetListName",  //请求地址
				dataType: "text",
				type: "GET",
				cache: false,
				async: false,
				data: "&templetListId="+value,
				success: function(jsonDate) {
					cai = jsonDate;
				},
			});
		   return cai;
	   }
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
	width:4
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
	+ rowData.templetId
			+ '\')" class="tb-btn delete1">删除</a>&nbsp; <a href="javascript:void(0);" onclick="preview(\''
			+ rowIndex 
			+ '\')" class="tb-btn delete1">预览</a>&nbsp; <a href="javascript:void(0);" onclick="doEdit(\''
			+ rowIndex + '\');" iconCls="icon-add">编辑</a>' ;
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
	
	

	//组织机构下拉树选择后实时查询
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
	
	
	//根据套装类型检索套装列表
	 $("#templetListType").combobox({
		  onSelect: function (n,o) {
		  var index=$('#templetListType').combobox('getValue');		
		//初始化模板套装下拉列表
		 $('#quTempletListId').combobox({   
		 	url:ctx+'/templetList/comboboxList?index='+index,    
		 	valueField:'templetListId', 
		    textField:'templetListName' ,
		 	onChange: function(newValue, oldValue) {
		 	var t = $('#quTempletListId').combobox('getValue');	// 获取树对象
			var param ="&templetListId="+t;
			page.queryData(param);
					    } 
		 		});
		     }
		 });
	
	 //模板套装下拉列表（支持选择时查询）
	 $('#quTempletListId').combobox({   
	 	 url:ctx+'/templetList/comboboxList?',    
	 	 valueField:'templetListId', 
	 	 //valueField:'templetListName', 
	     textField:'templetListName' ,
	 	 onChange: function(newValue, oldValue) {
	 	 var t = $('#quTempletListId').combobox('getValue');	// 获取树对象
	 	 var tt = $('#templetType').combobox('getValue');
		 var param ="&templetListId="+t+"&templetType="+tt;
		 page.queryData(param);
				    } 
	 		});
	 
	 
	 //模板套装下拉列表（支持选择时查询）
	 $('#templetType').combobox({   
	 	 onChange: function(newValue, oldValue) {
	 	 var t = $('#quTempletListId').combobox('getValue');	// 获取树对象
	 	 var tt = $('#templetType').combobox('getValue');
		 var param ="&templetListId="+t+"&templetType="+tt;
		 page.queryData(param);
				    } 
	 		});
	
	
	//新建模板时取组织机构
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

//模板预览
function preview(rowIndex){
	$('#table').datagrid('selectRow',rowIndex);
    var row = $('#table').datagrid('getSelected');
	var name=row.name+"."+row.extension;
	var templetListId=row.templetListId;
	var templetListName=getTemLiName(templetListId);
	if(row.extension=="js"||row.extension=="css"){
		$.messager.show({
			title:'系统提示',
			msg:'非html页面无法预览!',
			showType:'show'
			});
		return;
	}
	$.layer({
	    type: 2,
	    shadeClose: true,
	    title: "模板预览页面",
	    closeBtn: [0, true],
	    shade: [0.8, '#000'],
	    border: [0],
	    offset: ['10px',''],
	    area: ['400px', ($(window).height() - 10) +'px'],
	    iframe: {src: '../resources/creatTemplets/'+templetListName+"/"+name}
	}); 
}

//根据templetListID获取templetListName
function getTemLiName(templetListId){
	var name;
	 $.ajax({
	        cache: true,
	        type: "get",
	        url:"getNameById?templetListId="+templetListId,
	        //data:templetListId,// 你的formid
	        async: false,
	        error: function(request) {
	        	  $('#dlgadd').dialog('close');
	        	  $.messager.show({
	        			title:'系统提示',
	        			msg:'操作失败!',
	        			showType:'show'
	        			});
	        	  return;
	        },
	        success: function(data) {
		       name= data;
	        }
	    });
	 return name;
}



//编辑或添加
function doEdit(rowIndex){
	$('#ff').form("clear");//先请空
	if(rowIndex=="add"){
		$('#dlgadd').dialog('open').dialog('setTitle', '模板信息添加'); 
		$('#keyid').attr('value', "add");	
	}else{
	     $('#table').datagrid('selectRow',rowIndex);
	     var row = $('#table').datagrid('getSelected');
	     if (row) {
			$('#dlgadd').dialog('open').dialog('setTitle', '模板信息编辑');
			$('#ff').form('load', {
				name:row.name,
				extension:row.extension,
				descript :row.descript,
				//templetListId :row.templetListId,
				content: row.content,
				createDate:row.createDate,
				userOrganization:row.unit
				});
			$('#templetListId').combobox('select',row.templetListId) 
			$('#keyid').attr('value', row.templetId);
		}
	}
}



//删除操作
function deleteLine(rowIndex,columnId){
	$.messager.confirm('确认', '	确认删除这个资源?', function(r) {
		if (r) {
			$.ajax({
				type : 'get',
				url : ctx + "templet/delete?id=" + columnId,
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