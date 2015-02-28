$(function (){


	//初始化文本编辑器
	//CKEDITOR.instances.goodsDescript.setData("dddd");

	CKEDITOR.replace('goodsDescript');	
	
	$('#goodsDescript').val(CKEDITOR.instances.goodsDescript.getData()); //将组件得到的值，赋值给textarea
	
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
	 
	 
	 //组织机构下拉树
//	 $('#unit').combotree({    
//	    url:ctx + '/organization',    
//	 	required: true
//	 });

})


//iputFileType都以下划线加名称开头
//上传图片3元素  1.uploadFile是file标签  2. loading默认图像   3.imgUrl生成图片链接地址

function ajaxFileUpload(inputFileType) {

	if($("#uploadFile"+inputFileType).val()==""){
		alert("请选择图片");
		return false;
	}


    $.ajaxFileUpload({
            url: '../goods/addgoodsinfo/imgupload', //用于文件上传的服务器端请求地址
            secureuri: false, //是否需要安全协议，一般设置为false
            fileElementId: 'uploadFile'+inputFileType, //文件上传域的ID
            dataType: 'text', //返回值类型 一般设置为json
            success: function (data){  //服务器成功响应处理函数
                if(data=="type_error"){
                	alert("图片类型错误");
                }else  if(data=="error"){
                	alert("上传图片错误");
                }else {
                	alert("上传图片成功");
                	data = data.replace("<PRE>","").replace("</PRE>","");
                	$("#loading"+inputFileType).attr("src","../"+data);
                	$("#imgUrl"+inputFileType).val(data);
                	//alert($("#imgUrl"+inputFileType).val());
                }
            },
            error: function (data, status, e) {//服务器响应失败处理函数
            	alert("上传图片失败");
            }
    })

    return false;
}

var divNum = 1;
function add(){
	$("#buttonUpload1").show(); 
	
	if(divNum>5){
		divNum = 5;
		alert("最大只能显示5张图片");
		return ;
	}
	$("#img"+divNum).show(); 
	$("#uploadFile_img"+divNum).show(); 
	$("#buttonUpload"+divNum).show(); 
	
	divNum++;

}

function minus(){
	if(divNum<1){
		divNum = 1;
		alert("已经没有增加的图片");
		return ;
	}
	$("#img"+divNum).hide(); 
	$("#uploadFile_img"+divNum).hide(); 
	$("#buttonUpload"+divNum).hide(); 
	divNum--;
}

function addImg(inputFileType){
	ajaxFileUpload(inputFileType);
}

function onChangeImg(inputFileType){
	var docObj=document.getElementById('uploadFile'+inputFileType);
	var src = window.URL.createObjectURL(docObj.files[0]);
	$("#loading"+inputFileType).attr("src",src);
}



