<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html><html><head><title>MediPulse - Profile</title><meta name="viewport" content="width=device-width,initial-scale=1"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head><body>
<%@ include file="header.jspf" %>
<div class="card" style="max-width:520px"><h3>Profile Management</h3>
<form method="post" action="${pageContext.request.contextPath}/profile">
<label>FULL NAME</label><input name="name" value="<c:out value='${sessionScope.user.name}'/>" required>
<label>EMAIL (cannot be changed)</label><input value="<c:out value='${sessionScope.user.email}'/>" disabled>
<label>PHONE</label><input name="phone" value="<c:out value='${sessionScope.user.phone}'/>">
<label>NEW PASSWORD (leave blank to keep current)</label><input type="password" name="password" minlength="8">
<button>Save changes</button></form></div></div></body></html>
