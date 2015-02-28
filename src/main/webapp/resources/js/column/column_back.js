$(function (){	
	
	$('#dlgadd').dialog('close');

	//栏目为默认栏目时上级栏目不可选
	 $("#isDefault").combobox({
		 onSelect: function (n,o) {
		 var index=$('#isDefault').combobox('getValue');		
		 if(index=="1"){
			 $('#pid').combotree({readonly :true}); 
			 $('#templetListId').combobox({readonly :false});
			 $('#pid').combotree({required :false}); 
			 $('#templetListId').combobox({required :false});
		 }else{
			 $('#pid').combotree({readonly :false});
			 $('#templetListId').combobox({readonly :true});
			 $('#templetListId').combobox({required :false});
		 }
		   }
		 });
	
	//controller请求地址
	var url = ctx + "/column/list";
	//表格列的定义
	var columns = [[
	{field : 'name',title : '名称',width:7},
	{field : 'code',title : '编号',width:5},
	{field : 'order',title : '排序',width:5},
	{field : 'isDefault',title : '默认栏目',width:7,
	formatter: function(value,row,index){
				if (row.isDefault=="1"){
					return "YES";
				}
				else {
					return "NO";
					}
				}	
	},
	{field : 'pid',title : '上级栏目ID',width:7,
		 formatter: function(value,row,index){
				if (row.pid=="0"){
					return "";
				}
				else {
					return value;
					}
				}		
	},
	{field : 'isShow',title : '是否启用',width:7,
		 formatter: function(value,row,index){
				if (row.isShow=="1"){
					return "YES";
				}
				else {
					return "NO";
					}
				}		
	},
	{field : 'imageUrl',title : '图标',width:7,
		formatter:function(value,rowData,rowIndex){
		var url="<img style='width:50px;height:40px' src='../"+rowData.imageUrl+"'/>";
			   return url;
			}
	},
	{field : 'templetListId',title : '模板套装',width:10,
		   formatter: function(value,row,index){
			if (row.templetListId=="1"){
				return "temp1";
			}if (row.templetListId=="2"){
				return "temp2";
			}
			else {
				return "";
				}
			}
	},
	{field : 'descript',title : '描述',width:10},
	{field : 'createDate',title : '创建日期',width:15
		,formatter:function(value,rowData,rowIndex){
		   return changeDate(value);
		 }},
	{field : 'createUser',title : '创建人',width:15}
	 ] ]
		
	
	//初始化页面
	var page = new $.WifiPage({url:url, initPageNum: 1,initPageSize:5,columns:columns ,param:''});
	page.init();
	
	
	//查询方法
	$('#queryBtn').click(function(){
		//查询参数
		var goodsTitle= $('#goodsTitle').val();
		var state= $("#state").combobox('getValue');
		var beginDate= $('#beginDate').val();
		var endDate= $('#endDate').val();
		var param = "&goodsTitle="+goodsTitle+"&state="+state+"&beginDate="+beginDate+"&endDate="+endDate;
		page.queryData(param);
	});
	
		
	//提交编辑或保存
	 $("#sub").click(function(){
		    if ($("#ff").form("validate")){//easyUi自动校验
				 $('#dlgadd').dialog('close');//校验通过关闭对话框
				 var index=$('#rowIndex').attr("value");
				  $.ajax({
				        cache: true,
				        type: "POST",
				        url:"edit",
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
					        if(data=="success"){
					        	 $('#dlgadd').dialog('close');
					        	 page.init();
				        		$.messager.show({
				        			title:'系统提示',
				        			msg:'操作成功!',
				        			showType:'show'
				        			});
					        }else{
					        	$.messager.show({
				        			title:'系统提示',
				        			msg:'您要删除的栏目已经有商品发布无法删除!',
				        			showType:'show'
				        			});
					        	 $('#dlgadd').dialog('close');
						        }
				        }
				    });
				  
			 }else{
				 $.messager.show({
		 			title:'系统提示',
		 			msg:'请检查您提交的数据',
		 			showType:'show'
		 			});
			 }
	  });
	 
	 
 //初始化模板下拉列表
$('#templetListId').combobox({   
    url:ctx+'/templetList/comboboxList',    
   valueField:'templetListId', 
    //valueField:'templetListName', 
   textField:'templetListName'   
		}); 
	 
	 
//栏目下拉树
$('#pid').combotree({    
	url:ctx + '/column/columntree',    
	required: true,
    onChange: function(newValue, oldValue) {
     var t = $('#pid').combotree('tree');	// 获取树对象
	 var n = t.tree('getSelected');		// 获取选择的节点
     columnId = n.id;
	 var param = "&columnId="+columnId;
	 page.queryData(param);
		   }
	}); 
})


//添加
function newColumn(){
    $('#dlgadd').dialog('open').dialog('setTitle','添加栏目');
    $('#ff').form('clear');
    $('#keyid').attr('value', "add");	
    url = "edit";
    }

//编辑
function editColumn(){
	clickEditGetSelectAtrr();
	var row = $('#table').datagrid('getSelected');
	if (row){
	$('#dlgadd').dialog('open').dialog('setTitle','Edit User');
	$('#ff').form('load',row);
	$('#keyid').attr('value', row.columnId);
	url = 'edit';
	}
}

//删除
function destroyColumn(){
	var row = $('#table').datagrid('getSelected');
	if (row){
	$.messager.confirm('Confirm','Are you sure you want to destroy this user?',function(r){
	if (r){
	$.post("delete",{id:row.columnId},function(result){
	if (result.success){	
	$('#table').datagrid('deleteRow',row);//根据索引删除当前行
	$('#table').datagrid('reload');	// reload the user data
	$.messager.show({	// show error message
		title: 'success',
		msg: result.errorMsg
		});
	} else {
	$.messager.show({	// show error message
	title: 'Error',
	msg: result.errorMsg
	});
	}
	},'json');
	}
	});
	}
}


//点击编辑时更新下拉框应有的属性
function clickEditGetSelectAtrr(){

	//var index=$('#isDefault').combobox('getValue');	
	var row = $('#table').datagrid('getSelected');
	 if(row.isDefault=="1"){
		 $('#pid').combotree({readonly :true}); 
		 $('#templetListId').combobox({readonly :false});
		 $('#pid').combotree({required :false}); 
		 $('#templetListId').combobox({required :false});
	 }else{
		 $('#pid').combotree({readonly :false});
		 $('#templetListId').combobox({readonly :true});
		 $('#templetListId').combobox({required :false});
	 }
}



//图片上传
function ajaxFileUpload(inputFileType) {
	if($("#uploadFile"+inputFileType).val()==""){
		 $.messager.alert('系统提示', "请选择图片!", 'info');
		return false;
	}

    $.ajaxFileUpload({
            url: 'imgupload', //用于文件上传的服务器端请求地址
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: 'uploadFile'+inputFileType, //文件上传域的ID
            dataType: 'json', //返回值类型 一般设置为json
            success: function (data, status){  //服务器成功响应处理函数
                if(data.status=="SUCCESS"){
                	$.messager.show({
	        			title:'系统提示',
	        			msg:'上传图片成功!',
	        			showType:'show'
	        			});              	
                	$("#loading"+inputFileType).attr("src","../"+data.src);
                	$("#imgUrl"+inputFileType).val(data.src);
                }
                if(data.status=="type_error"){
                	 $.messager.alert('系统提示', "图片类型错误!", 'info');
                }
                if(data.status=="error"){
                	 $.messager.alert('系统提示', "上传图片错误!", 'info');
                }
            },
            error: function (data, status, e) {//服务器响应失败处理函数
            	 $.messager.alert('系统提示', "上传图片失败!", 'info');
            }
    })
    return false;
}
function addImg(inputFileType){
	ajaxFileUpload(inputFileType);
}
function browse(inputFileType){
	$('#uploadFile'+inputFileType).click();
}

//$.messager.alert('系统提示', "删除成功!", 'info');	

//初始化上级栏目下拉列表
//$('#pid').combobox({   
//    url:ctx+'/column/pidList',    
//    valueField:'columnId',    
//    textField:'name'   
//});


// $('#pid').css('background-color','red');

//{field : 'templetList',title : '模板',width:10,
//formatter: function(value,row,index){
//	if (row.templetList){
//		return row.templetList.templetListName;
//	} else {
//		return value;
//	}
//}
//},