<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Insert title here</title>
<jsp:include page="../inc.jsp"></jsp:include>
</head>
<body style="font-size:30px;">
<ul id="tt" class="easyui-tree">
    <li>
        <span>Folder</span>
        <ul>
            <li>
                <span>Sub Folder 1</span>
                <ul>
                    <li>
                        <span><a href="#">File 11</a></span>
                    </li>
                    <li>
                        <span>File 12</span>
                    </li>
                    <li>
                        <span>File 13</span>
                    </li>
                </ul>
            </li>
            <li>
                <span>File 2</span>
            </li>
            <li>
                <span>File 3</span>
            </li>
        </ul>
    </li>
    <li>
        <span>File21</span>
    </li>
</ul>

<ul id="tt2">
	
</ul>
</body>
<script type="text/javascript">
$(function(){
	$('#tt').tree({
	    //url:'tree_data.json'
	});
	
	$('#tt2').tree({
	    url:'tree_data.json'
	});
})

</script>
</html>