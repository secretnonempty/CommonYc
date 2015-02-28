$(function (){
	//初始化文本编辑器
	 $('#ff').form({
	     url:'../goods/addgoodsinfo/submit',
	     onSubmit:function(){
	         return $(this).form('validate');
	     },
	     success:function(data){
	    	 if(data=="sucess"){
	        		alert("保存数据成功");
	        		location.href='../goods/goodsinfo'
	         }
	     },
	     error: function(request) {
	            alert("保存数据失败");
	     }
	 }); 
	 
	 $("#state").combobox('setValue',$('#stateValue').val());//根据ID设定默认值
	 $("#goodsUnit").combobox('setValue',$('#goodsUnitValue').val());//根据ID设定默认值

})

