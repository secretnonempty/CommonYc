$(function (){
	
	//controller请求地址
	var url = ctx+"/select/selectcolumn/list";
	//表格列的定义
	var columns = [[
	                   {field:'ck',checkbox:true},
			           {field:'name',title:'名称',width:110,sortable:true},
			           {field : 'code',title : '编号',width:30},
			           {field : 'isShow',title : '是否启用',width:70,
			        	   formatter: function(value,row,index){
			      				if (row.isShow=="1"){return "是";}
			      				else {return "否";}
			      			}		
			           },
			           {field : 'pid',title : '上级栏目',width:50,
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
 			           {field:'createUser',title:'创建人',width:60},
 			           {field:'createDate',title:'创建时间',width:110,formatter:function(value,rowData,rowIndex){
 			        	   return changeDate(value);
 			           	 }
 			           }
 				  ]]
	
	
	//初始化页面
	var page = new $.WifiPage({url:url, initPageNum: 1,initPageSize:8,columns:columns ,param:'',singleSelect:false,onSelect:true,onUnselect:true,onSelectAll:true,onUnselectAll:true,onLoadSuccess:true});
	page.init();
	
	//查询方法
	$('#queryBtn').click(function(){
		//查询参数
		var organization= $("#organization").combotree('getValue');
		var beginDate= $('#beginDate').datebox('getValue');
		var endDate= $('#endDate').datebox('getValue');
		var param = "&organization="+organization+"&beginDate="+beginDate+"&endDate="+endDate;
		page.queryData(param);
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


})



//重载page.js 中的方法
function onSelect(rowIndex,rowData){
	setSelectIndex();
}
function onUnselect(rowIndex,rowData){
	var ids =  [];  
	var titles =[];
	if($('#selectIndex').val().length>0){   //原有已经选中的值，有可能是前一页选中的
		ids  = $('#selectIndex').val().split(",");  //split是将字符串转换成数组
		titles  = $('#selectValue').val().split(",");
		if(findValue(ids,rowData.columnId)){ //如果找到了，删除
			ids.splice(jQuery.inArray(rowData.columnId.toString(),ids),1);
			titles.splice(jQuery.inArray(rowData.name,titles),1);
		}
		
		$('#selectIndex').val(ids.join(','));
		$('#selectValue').val(titles.join(','));
	}
}
function onSelectAll(rows){
	setSelectIndex();
}
function onUnselectAll(rows){
	var ids =  [];  
	var titles =[];
	if($('#selectIndex').val().length>0){   //原有已经选中的值，有可能是前一页选中的
		ids  = $('#selectIndex').val().split(",");  //split是将字符串转换成数组
		titles  = $('#selectValue').val().split(",");
		for(var i=0;i<rows.length;i++){
			if(findValue(ids,rows[i].columnId)){ //如果找到了，删除
				ids.splice(jQuery.inArray(rows[i].columnId.toString(),ids),1);
				titles.splice(jQuery.inArray(rows[i].name,titles),1);
			}
		}
		
		
		$('#selectIndex').val(ids.join(','));
		$('#selectValue').val(titles.join(','));
	}
}

function onLoadSuccess(data){
	var ids =  [];  
	var titles =[];
	var rows = data.rows;
	if($('#selectIndex').val().length>0){   //原有已经选中的值，有可能是前一页选中的
		ids  = $('#selectIndex').val().split(",");  //split是将字符串转换成数组
		titles  = $('#selectValue').val().split(",");
		if(rows.length>0){
			for(var i=0;i<rows.length;i++){
				if(findValue(ids,rows[i].columnId)){
					$('#table').datagrid('checkRow',i); //赋值已选中
				}
			}
		}
	}
	
}
	


//循环放值，并变成字符串
function setSelectIndex(){
	var rows = $('#table').datagrid('getSelections'); //所选择到的值
	var ids =  [];  
	var titles =[];
	
	if($('#selectIndex').val().length>0){   //原有已经选中的值，有可能是前一页选中的
		ids  = $('#selectIndex').val().split(",");  //split是将字符串转换成数组
		titles  = $('#selectValue').val().split(",");
	}

	//循环判断以前是否有id存在，如果没有就加进去
	for(var i=0; i<rows.length; i++){
		if(!findValue(ids,rows[i].columnId)){ //如果没有加入ids
			ids.push(rows[i].columnId);
			titles.push(rows[i].name);
		}
	}

	$('#selectIndex').val(ids.join(','));
	$('#selectValue').val(titles.join(','));
	
}

//查询数组中的值
function findValue(ids,id){
	var value = false;
	if(ids.length>0){
		for(var i=0; i<ids.length; i++){
			if(ids[i]==id){
				value = true;
				break;
			}
		}
	}
	return value;
}
