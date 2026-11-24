<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html><html><head><title>MediPulse - Reset password</title><meta name="viewport" content="width=device-width,initial-scale=1"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head><body>
<div class="card form-box"><h3>&#10010; Reset password</h3>
<c:if test="${not empty error}"><div class="err"><c:out value="${error}"/></div></c:if>
<form method="post" action="${pageContext.request.contextPath}/forgot">
<label>REGISTERED EMAIL</label><input type="email" name="email" required>
<label>NEW PASSWORD</label><input type="password" name="password" minlength="8" required>
<label>CONFIRM NEW PASSWORD</label><input type="password" name="confirm" minlength="8" required>
<button style="width:100%">Save new password</button></form>
<p style="margin-top:12px"><a class="link" href="${pageContext.request.contextPath}/login">Back to sign in</a></p></div></body></html>
