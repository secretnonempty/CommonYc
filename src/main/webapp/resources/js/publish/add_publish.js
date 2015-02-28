$(function (){

	//var pDate = new Date();
	//pDate  = formatDate(pDate);
	//$('#pubDate').textbox('setValue',pDate); //赋值时间
	
	 $('#ff').form({
	     url:'../publish/addpublish/submit',
	     onSubmit:function(){
	         return $(this).form('validate');
	     },
	     success:function(data){
	    	 if(data=="sucess"){
	        		alert("保存数据成功");
	        		location.href='../publish/publish'
	         }
	    	 if(data=="error"){
	        		alert("保存数据异常");
	         }
	    	 if(data=="zipError"){
	        		alert("压缩文件失败");
	         }
	     },
	     error: function(request) {
	            alert("保存数据失败");
	     }
	 }); 
	 
	 var dialogName = "";  //全局弹出页面名称
	//选择发布商品
	$('#addGoods').click(function(){
		$('#dd').dialog({    
		    title: '商品信息',    
		    width: 800,    
		    height: 500,    
		    closed: false,    
		    cache: false,    
		    modal: true   
		});
		dialogName = "addGoods";
		$('#dd').dialog('open');
		$("#selectIframe").attr("src",'../select/selectgoods');
	});
	
	//选择栏目
	$('#addColumn').click(function(){
		$('#dd').dialog({    
		    title: '栏目信息',    
		    width: 800,    
		    height: 500,    
		    closed: false,    
		    cache: false,    
		    modal: true   
		});
		dialogName = "addColumn";
		$('#dd').dialog('open');
		$("#selectIframe").attr("src",'../select/selectcolumn');
	});
	
	//选择模版
	$('#addTemplet').click(function(){
		$('#dd').dialog({    
		    title: '模版信息',    
		    width: 800,    
		    height: 500,    
		    closed: false,    
		    cache: false,    
		    modal: true   
		});
		dialogName = "addTemplet";
		$('#dd').dialog('open');
		$("#selectIframe").attr("src",'../select/selecttemplet');
	});
	
	//关闭dialog窗口
	$('#closeBtn').click(function(){
		$('#dd').dialog('close');
	});
	
	//选择信息
	$('#publishBtn').click(function(){
		var selectIndex = $('#selectIframe').contents().find("#selectIndex").val();  //获得选择框的选择集合
		var selectValue = $('#selectIframe').contents().find("#selectValue").val();  //获得选择框的选择集合

		if(selectIndex==""){
			alert("请选择打包信息");
			return;
		}
		//判断弹出哪个dialog然后把值放入对应的文本框
		if(dialogName=="addGoods"){
			$("#goodsValues").val(selectValue);
			$("#goodsIds").val(selectIndex);
		}
		if(dialogName=="addColumn"){
			$("#columnValues").val(selectValue);
			$("#columnIds").val(selectIndex);
		}
		if(dialogName=="addTemplet"){
			$("#templetValues").val(selectValue);
			$("#templetIds").val(selectIndex);
		}
		
		$('#dd').dialog('close');
	});
	
	//下拉单
	$('#trainNo').combobox({
		valueField:'trainNumber',
		textField:'trainNumber',
		onSelect: function(record){
			
			var pDate = $('#pubDate').textbox('getValue').replace(/\-/g,"");
			var versionNo = getVersion(record.trainNumber); //获得版本号
			$('#version').textbox('setValue',versionNo); //赋值版本号
			
			$('#name').textbox('setValue',record.trainNumber+"_"+pDate+"_"+versionNo+".zip"); //赋值名称
		}
	});
	
	
	 //组织机构下拉树
	 $('#unit').combotree({    
	    url:ctx + '/organization',    
	 	required: true,
	 	onChange: function(newValue, oldValue) {
	 		if($('#pubDate').datebox('getValue')==""){
	 			alert("请选择启用时间");
	 			//$('#unit').combotree('setValue',oldValue);
	 			//alert($('#unit').combotree('getValue'));
	 		}else{
	 			$('#name').textbox('setText',"");
		 		$('#version').textbox('setText',"");
				var t = $('#unit').combotree('tree');// 获取树对象
				var n = t.tree('getSelected');// 获取选择的节点
				var url = ctx + '/publish/getTrainCode?unit=' + n.id;
				$('#trainNo').combobox('setValue', '');
	            $('#trainNo').combobox('reload', url);
	 		}
		}
	 });

	//时间框
	$('#pubDate').datebox({
		required:true,
	    onSelect: function(date){
//	    	var text = $('#name').textbox('getText');
//	    	if(text==""){
//	    		alert("请选择车次");
//	    		$('#pubDate').datebox('setValue',"");
//	    	}else{
//	    		text = text + $('#pubDate').datebox('getValue').replace(/\-/g,"");
//	    		var versionNo = getVersion($('#trainNo').combobox("getValue")); //获得版本号
//	    		$('#version').textbox('setValue',versionNo);
//	    		text = text + "_"+versionNo+".zip";
//	    		$('#name').textbox('setValue',text);
//	    	}
	    }
	});




})

//获得版本号
function getVersion(trainNo){
	var versionNo = "" ;
	$.ajax({
		url:"../publish/publish/version",  //请求地址
		dataType: "text",
		type: "GET",
		data: "&trainNo="+trainNo,
		async:false,
		success: function(jsonData) {
			if(jsonData == "faile"){
				alert("获取版本号失败");
			}else{
				versionNo = jsonData;
			}
		},
		cache: false
	});
	return versionNo;
}

//格式化时间
function formatDate(date) {  
	var y = date.getFullYear();  
	var m = date.getMonth() + 1;  
	var d = date.getDate();  
	return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);  
}; 

