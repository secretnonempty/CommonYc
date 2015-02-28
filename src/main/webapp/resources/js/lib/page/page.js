$(function (){
	
	 	var url;
	 	var param;
	 	var singleSelect = true;
	  	$.WifiPage = function (arg) {
	  		 //初始化页面
	         this.init = function () {     //this.init方法，加上了this，就是公有方法了，外部可以访问。
	             
	        	url = arg.url;
	        	param = arg.param;
	        	if(arg.singleSelect!=undefined){
	        		singleSelect = arg.singleSelect;
	        	}

	        	
	           //初始化页面查询数据
	     		requestData(arg.initPageNum,arg.initPageSize,arg.url,arg.param);
	             
	           //构建表格所有属性
	     	  	$('#table').datagrid({
	     			collapsible:true,
	     			fitColumns: true, 
	     			remoteSort: false,
	     			singleSelect: singleSelect, 
	     			nowrap: false,
	     	  		striped: true,
	     			method:'get',
	     			loadMsg:'数据努力加载中...',
	     			pagination: true,
	     			pageSize: arg.initPageSize,
	     			pageList: [arg.initPageSize, arg.initPageSize*2, arg.initPageSize*4],
	     			columns: arg.columns,
	     			onSelect:function (rowIndex, rowData){ //用户选择一行的时候触发
	     				if(arg.onSelect!=undefined){
	     					onSelect(rowIndex,rowData);
	     				}
	     			},
	     			onUnselect:function (rowIndex, rowData){ //用户取消选择一行的时候触发
	     				if(arg.onUnselect!=undefined){
	     					onUnselect(rowIndex,rowData);
	     				}
	     			},
	     			onSelectAll:function (rows){ //在用户选择所有行的时候触发
	     				if(arg.onSelectAll!=undefined){
	     					onSelectAll(rows);
	     				}
	     			},
	     			onUnselectAll:function (rows){//在用户取消选择所有行的时候触发
	     				if(arg.onUnselectAll!=undefined){
	     					onUnselectAll(rows);
	     				}
	     			},
	     			onLoadSuccess:function(data){ 
	     				if(arg.onLoadSuccess!=undefined){
	     					onLoadSuccess(data);
	     				}
	     			} 
	     	  	});
	         };
	         
	         this.queryData = function(queryParam){
	        	 $('#table').datagrid("load"); //点击查询后 ，初始化页面分页内容
	        	 
	        	// var pager = $('#table').datagrid('getPager');
	        	// pager.pagination('refresh',{	// 改变选项并刷新分页栏信息
	        		//	pageNumber: arg.initPageNum
	        		//});

	        	 param = queryParam;  //接收页面参数
	        	 requestData(arg.initPageNum,arg.initPageSize,arg.url,queryParam);
	         };
	    };
	    
	  	
	  	//分页方法
	  	function pagerFilter(data) {

			if (typeof data.length == 'number' && typeof data.splice == 'function') { // is array
				data = {
					total: data.length,
					rows: data
				};
			}
			var dg = $(this);
			var opts = dg.datagrid('options');
			var pager = dg.datagrid('getPager');
			pager.pagination({
				showRefresh: false,
				beforePageText: '第', //页数文本框前显示的汉字  
				afterPageText: '页    共 {pages} 页',
				displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
				onSelectPage: function(pageNum, pageSize) {
					
					requestData(pageNum,pageSize,url,param);
					opts.pageNumber = pageNum;
					opts.pageSize = pageSize;
					pager.pagination('refresh', {
						pageNumber: pageNum,
						pageSize: pageSize
					});
					//dg.datagrid('loadData', data);
				}
			});
			if (!data.originalRows) {
				data.originalRows = (data.rows);
			}
			//var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
			//var end = start + parseInt(opts.pageSize);
			//data.rows = (data.originalRows.slice(start, end));
			data.rows = data.originalRows;
			return data;
		}
	  	
	  	
	  	//获取请求数据
	  	function requestData(pageNum,pageSize,url,param) {
				var baseData; 
				
				var paramVar = "pageNum="+pageNum+"&pageSize="+pageSize;
				if(param!=""){
					paramVar = paramVar + param;
				}

				$.ajax({
					url:url,  //请求地址
					dataType: "text",
					type: "GET",
					data: paramVar,
					success: function(jsonDate) {
						//alert(jsonDate);
						eval("baseData="+jsonDate);  //将json串转换成json对象

						$('#table').datagrid({  
							loadFilter: pagerFilter
						}).datagrid("loadData",baseData);  
						
					},
					cache: false
				});
		}

	  	

	 	

})


//时间戳转换成  2015-01-14 21:28:11
function changeDate(dateStr){
	
	if(dateStr=="" || dateStr==null){
		return "";
	}
	var date = new Date(dateStr);
	Y = date.getFullYear() + '-';
	M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	D = date.getDate()<10 ? '0'+date.getDate() : date.getDate(); 
	h = date.getHours() <10 ? '0'+date.getHours()+':' : date.getHours()+ ':';
	m = date.getMinutes()<10 ? '0'+date.getMinutes()+':' : date.getMinutes() + ':';
	s = date.getSeconds()<10 ? '0'+date.getSeconds() : date.getSeconds(); 
	return Y+M+D+" "+h+m+s;

}

// 获取当前的系统时间  2015-01-14 21:28:11
function getSystemTime() {
	var curr_time = new Date();
	var strDate = curr_time.getFullYear()+"-";
	strDate += curr_time.getMonth()+1+"-";
	strDate += curr_time.getDate()+"-";
	strDate += curr_time.getHours()+":";
	strDate += curr_time.getMinutes()+":";
	strDate += curr_time.getSeconds();
	return strDate;
}

