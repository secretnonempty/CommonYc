$(function (){
	
	//alert($('#columnId').val());
	//alert(window.parent.document.getElementById('columnId').value);
	//alert($("#columnId",parent.document).val());
	//controller请求地址
	var url = ctx+"/selectgoods/selectgoods/list";
	//表格列的定义
	var columns = [[
	                   {field:'ck',checkbox:true},
			           {field:'goodsTitle',title:'标题',width:180,sortable:true},
 			           {field:'state',title:'状态',width:50,formatter:function(value,rowData,rowIndex){
 			        	   if(value=="1"){return "上架"}
 			        	   if(value=="2"){return "下架"}
 			           	 }
 			           },
 			           {field:'createUser',title:'创建人',width:90},
 			           {field:'createDate',title:'创建时间',width:110,formatter:function(value,rowData,rowIndex){
 			        	   return changeDate(value);
 			           	 }
 			           }
 				  ]]
	
	
	var pam ="&columnId="+ $("#columnId",parent.document).val(); //获得父窗口的栏目id
	$('#columnId').val($("#columnId",parent.document).val());  //本页面hidden栏目id
	
	//初始化页面
	var page = new $.WifiPage({url:url, initPageNum: 1,initPageSize:10,columns:columns ,param:pam,singleSelect:false,onSelect:true,onUnselect:true,onSelectAll:true,onUnselectAll:true});
	page.init();
	
	//查询方法
	$('#queryBtn1').click(function(){
		//查询参数
		var goodsTitle= $('#goodsTitle1').val();
		var state= $("#state1").combobox('getValue');
		var beginDate= $('#beginDate1').datebox('getValue');
		var endDate= $('#endDate1').datebox('getValue');
		var columnId= $('#columnId').val();
		var param = "&goodsTitle="+encodeURI(goodsTitle)+"&state="+state+"&beginDate="+beginDate+"&endDate="+endDate+"&columnId="+columnId;
		page.queryData(param);
	});
	
	


})




function onSelect(rowIndex,rowData){
	setSelectGoodsIndex();
}
function onUnselect(rowIndex,rowData){
	setSelectGoodsIndex();
}
function onSelectAll(rows){
	setSelectGoodsIndex();
}
function onUnselectAll(rows){
	setSelectGoodsIndex();
}

//循环放值，并变成字符串
function setSelectGoodsIndex(){
	var rows = $('#table').datagrid('getSelections');
	var ids = [];
	for(var i=0; i<rows.length; i++){
		 ids.push(rows[i].goodsInfoId);
	}
	$('#selectGoodsIndex').val(ids.join(','));
}
