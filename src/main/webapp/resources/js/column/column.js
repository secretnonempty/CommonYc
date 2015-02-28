$(function (){	
	
	//初始化模板套装列表
	$('#templetListId').combobox({   
    url:ctx+'/templetList/comboboxList',    
    valueField:'templetListId', 
    //valueField:'templetListUrl', 
    textField:'templetListName'   
});
	
	$('#dlgadd').dialog('close');

	//栏目为默认栏目时上级栏目不可选
	 $("#isDefault").combobox({
		 onSelect: function (n,o) {
		 var index=$('#isDefault').combobox('getValue');
		 if(index=="1"){//根栏目
			 $('#pid').combotree({readonly :true}); 
			 //$('#templetListId').combobox({readonly :false});
			 $('#pid').combotree({required :false}); 
			// $('#templetListId').combobox({required :false});
		 }else{
			 $('#pid').combotree({readonly :false});
//			 $('#templetListId').combobox({readonly :true});
//			 $('#templetListId').combobox({required :false});
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
	//{field : 'unit',title : '排序',width:5},
	{field : 'isDefault',title : '默认栏目',width:7,
	formatter: function(value,row,index){
				if (row.isDefault=="1"){
					return "是";
				}
				else {
					return "否";
					}
				}	
	},
	{field : 'pid',title : '上级栏目',width:7,
		 formatter: function(value,row,index){
			
					var cai;
					   $.ajax({
							url: ctx +"/column/pidName",  //请求地址
							dataType: "text",
							type: "GET",
							cache: false,
							async: false,
							data: "&pid="+row.pid,
							success: function(jsonDate) {
								cai = jsonDate;
							},
						});
					   return cai;
				   }
	},
	{field : 'isShow',title : '是否启用',width:7,
		 formatter: function(value,row,index){
				if (row.isShow=="1"){
					return "是";
				}
				else {
					return "否";
					}
				}		
	},
	{field : 'imageUrl',title : '图标',width:7,
		formatter:function(value,rowData,rowIndex){
		//var url="<img onMouseOver='ShowDiv("+rowData.imageUrl+")'onMouseOut='HideDiv()' style='width:50px;height:40px' src='../"+rowData.imageUrl+"'/>";
			   return  '<img  onMouseOver="ShowDiv(\''+rowData.imageUrl+'\',event)"  style="width:50px;height:40px" onMouseOut="HideDiv()" src="../'+rowData.imageUrl+'"/>';
			}
	},
	{field : 'templetListId',title : '模板套装',width:10,
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
	{field : 'descript',title : '描述',width:10},
	{field : 'createDate',title : '创建日期',width:15
		,formatter:function(value,rowData,rowIndex){
		   return changeDate(value);
		 }},
	{field : 'createUser',title : '创建人',width:15},
	{
	field : '',
	title : '操作',
	width:40,
	formatter : function(value, rowData, rowIndex) {
		return '<a href="javascript:void(0);" iconCls="icon-add" onclick="deleteLine(\''+rowIndex+'\',\''
				+ rowData.columnId
				+ '\')" class="tb-btn delete1" >删除</a>&nbsp; <a href="javascript:void(0);" onclick="doEditOrAdd(\''
				+ rowIndex + '\');" iconCls="icon-add">编辑</a>';}
	} ] ]
		
	
	//初始化页面
	var page = new $.WifiPage({url:url, initPageNum: 1,initPageSize:5,columns:columns ,param:''});
	page.init();
	

	
//查询（点击查询按钮时查询）
	$("#queryBtn").click(function(){
		//查询参数	
		var organization= $("#organization").combotree('getValue');
		var beginDate= $('#beginDate').datebox('getValue');
		var endDate= $('#endDate').datebox('getValue');
		var param = "&organization="+organization+"&beginDate="+beginDate+"&endDate="+endDate;
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
				        url:"editOrAdd",
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
				        		return;
					        }else{
					        	$.messager.show({
				        			title:'系统提示',
				        			msg:'编辑失败!',
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
	 
 
//根据套装类型检索套装列表
 $("#templetListType").combobox({
	 onSelect: function (n,o) {
	 var index=$('#templetListType').combobox('getValue');		
	//初始化模板套装下拉列表
	 $('#templetListId').combobox({   
	 	 url:ctx+'/templetList/comboboxList?index='+index,    
	 	 valueField:'templetListId', 
	 	 //valueField:'templetListName', 
	 	 textField:'templetListName'   
	 		}); 
	     }
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


//添加加获取组织机构
$('#userOrganization').combotree({   
	 url:ctx+'/organization',    
	valueField:'id', 
	 //valueField:'templetListName', 
	 textField:'organizationName'   
		}); 



//组织机构下拉树
$('#organization').combotree({    
   url:ctx + '/organization',    
	required: true,
	onChange: function(newValue, oldValue) {
	var t = $('#organization').combotree('tree');	// 获取树对象
	var n = t.tree('getSelected');		// 获取选择的节点
	organization = n.id;
	var param = "&organization="+organization;
	page.queryData(param);
		    }
		}); 



//进入增加页面从列表页面跳入
$('#add').click(function(){
	doEditOrAdd("add");
});
	
})


/**js 方法*************************/
//删除操作
function deleteLine(rowIndex,columnId){
	$.messager.confirm('确认', '	确认删除这个资源?', function(r) {
		if (r) {
			$.ajax({
				type : 'get',
				url : ctx + "column/delete?id=" + columnId,
				success : function(s) {
					if ("success"==s) {	
						$('#table').datagrid('reload'); 
						$('#table').datagrid('deleteRow',rowIndex);//根据索引删除当前行
						$.messager.show({
		        			title:'系统提示',
		        			msg:'删除成功!',
		        			showType:'show'
		        			});
						return;
					}
					if("cantDelete"==s){
						$.messager.show({
		        			title:'系统提示',
		        			msg:'该栏目下已经有商品发布，无法删除!',
		        			showType:'show'
		        			});
						return;
					}else{
						$.messager.show({
		        			title:'系统提示',
		        			msg:'删除失败!',
		        			showType:'show'
		        			});
						return;
					}
				},
				async : false
			});
		}
	});
}

//弹出编辑或添加对话框
function doEditOrAdd(rowIndex){
		
//		$('#templetListId').combobox({required :true});	
		$("#loading_img3").attr("src","");
	    $("#imgUrl_img3").val("");
	    $("#imageUrl").val("");
	if(rowIndex=="add"){
		 $('#dlgadd').dialog('open').dialog('setTitle','添加栏目');
		 $('#ff').form('clear');
		 $('#keyid').attr('value', "add");
		 $("#loading_img3").attr("src","../resources/images/yulan.bmp");
		 $("#imgUrl_img3").val("");
		 $("#imageUrl").val("");
	}else{
		  clickEditGetSelectAtrr(rowIndex);
		  $('#table').datagrid('selectRow',rowIndex);
	      var row = $('#table').datagrid('getSelected');
		if (row){
			$('#dlgadd').dialog('open').dialog('setTitle','栏目编辑');
			$('#ff').form('load', {
			name:row.name,
			code :row.code,
			order :row.order,
			isDefault: row.isDefault,
			isShow:row.isShow,	
			templetListType:row.templetListType,
//			templetListId :row.templetListId,
			pid:row.pid,
			descript:row.descript,
			createDate:row.createDate,
		    userOrganization:row.unit,
		  // uploadFile_img3:row.imgUrl
			imageUrl:row.imageUrl
		});	
			$('#templetListId').combobox('select',row.templetListId) 
			$('#keyid').attr('value', row.columnId);
			$("#loading_img3").attr("src","../"+row.imageUrl);
		    $("#imgUrl_img3").val("");
			$('#keyid').attr('value', row.columnId);
		}
	}
}

//点击编辑时更新下拉框应有的属性
function clickEditGetSelectAtrr(rowIndex){
		//var index=$('#isDefault').combobox('getValue');	
		//var row = $('#table').datagrid('getSelected');
		 $('#table').datagrid('selectRow',rowIndex);
	     var row = $('#table').datagrid('getSelected');
	 if(row.isDefault=="1"){
		 $('#pid').combotree({readonly :true}); 
		 $('#pid').combotree({required :false}); 
//		 $('#templetListId').combobox({readonly :false});
//		 $('#templetListId').combobox({required :false});
	 }else{
		 $('#pid').combotree({readonly :false});
		// $('#templetListId').combobox({readonly :true});
		// $('#templetListId').combobox({required :false});
	 }
}



function ajaxFileUpload(inputFileType) {
	if($("#uploadFile"+inputFileType).val()==""){
		 $.messager.alert('系统提示', "请选择图片!", 'info');
		return false;
	}

    $.ajaxFileUpload({
            url: 'imgupload', //用于文件上传的服务器端请求地址
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: 'uploadFile'+inputFileType, //文件上传域的ID
            dataType: 'text', //返回值类型 一般设置为json
            success: function (data){  //服务器成功响应处理函数
            	 if(data=="type_error"){
            		 $.messager.alert("图片类型错误");
                 }else  if(data=="error"){
                	 $.messager.alert("上传图片错误");
                 }else {
                 	data = data.replace("<PRE>","").replace("</PRE>","");
                 	$("#loading"+inputFileType).attr("src","../"+data);
                 	$("#imgUrl"+inputFileType).val(data);
                 	$("#imageUrl").val(data);
                 	$.messager.show({
	        			title:'系统提示',
	        			msg:'图片上传成功!',
	        			showType:'show'
	        			});
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

function ShowDiv(pic,e)
{
//	  xOffset = 100;
//	  yOffset = 20; 
	   $("body").append("<div id='tooltip'>" +"</div>");
	   $("#tooltip")
	   .css("top",(e.pageY-150 ) + "px")
	   .css("left",(e.pageX-230 ) + "px")
	   //.css("background-image","url(../"+pic+")")
	   .css("height","200px")
	   //.css("padding-left","0px")
	   .css("border","1px solid #696")
	   .css("width","200px")
	   .css("position","absolute")
	   .fadeIn("fast");  
	    tooltip.innerHTML="<img src='../"+pic+"'/ style='width:200px;height:200px'>";
	    tooltip.style.display="block";
}

function HideDiv()
{
	tooltip.style.display="none";
}
//function showe(e){
//	
//	 $("body").append("<div id='tooltip' style='width: 50px;height: 50px;float: left;clear: both;'>" +"</div>");
//	 $("#tooltip")
//	   .css("top",(e.pageY - xOffset) + "px")
//	   .css("left",(e.pageX + yOffset) + "px")
//	   //.css("background-image","url(../"+pic+")")
//	   .css("height","310px")
//	   .css("padding-left","50px")
//	   .css("border","2px solid #696")
//	   .css("width","260px")
//	   .css("position","absolute")
//	   .fadeIn("fast"); 
//}

//function tooltip(){ 
//	 /* CONFIG */  
//	  xOffset = 100;
//	  yOffset = 20;  
//	  // these 2 variable determine popup's distance from the cursor
//	  // you might want to adjust to get the right result  
//	 /* END CONFIG */  
//	 $(".img1").hover(function(e){     //鼠标要移动的图片        
//	  this.t = this.src;         
//	  $("body").append("<div id='tooltip'>" +"</div>");
//	  $("#tooltip")
//	   .css("top",(e.pageY - xOffset) + "px")
//	   .css("left",(e.pageX + yOffset) + "px")
//	   .css("background-image","url("+this.t+")")
//	   .css("height","310px")
//	   .css("padding-left","50px")
//	   .css("border","2px solid #696")
//	   .css("width","260px")
//	   .css("position","absolute")
//	   .fadeIn("fast");  
//	    },
//	 function(){
//	  this.title = this.t;  
//	  $("#tooltip").remove();
//	    }); 
//	 $(".img1").mousemove(function(e){
//	  $("#tooltip")
//	   .css("top",(e.pageY - xOffset) + "px")
//	   .css("left",(e.pageX + yOffset) + "px")
//	   .fadeIn("slow");
//	 });   
//	};
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



//$('#organization').combotree({
//onSelect:function(node) {
//	//查询参数	
//	var organization= $("#organization").combotree('getValue');
//	var beginDate= $('#beginDate').datebox('getValue');
//	var endDate= $('#endDate').datebox('getValue');
//	var param = "&organization="+organization+"&beginDate="+beginDate+"&endDate="+endDate;
//	page.queryData(param);
//}
//});


//location.href =ctx+"/column/addColumn";
