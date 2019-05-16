<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String easyuiThemeName = "default";
	Cookie cookies[] = request.getCookies();
	if(cookies != null && cookies.length > 0){
		for(int i = 0;i < cookies.length;i++){
			if(cookies[i].getName().equals("easyuiThemeName")){
				easyuiThemeName = cookies[i].getValue();
				break;
			}               
		}
	}
%>
<link id="easyuiTheme" rel="stylesheet" href="${pageContext.request.contextPath}/js/jquery-easyui-1.8.1/themes/<c:out value="${cookie.easyuiThemeName.value}" default="default"/>/easyui.css" />
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/js/jquery-easyui-1.8.1/themes/ui-sunny/easyui.css" /> --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/jquery-easyui-1.8.1/themes/icon.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/syCss.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.8.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.8.1/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-easyui-1.8.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/syUtil.js"></script>