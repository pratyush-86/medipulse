<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html><html><head><title>MediPulse - Register</title><meta name="viewport" content="width=device-width,initial-scale=1"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head><body>
<div class="card form-box"><h3>&#10010; Patient Registration</h3>
<c:if test="${not empty error}"><div class="err"><c:out value="${error}"/></div></c:if>
<form method="post" action="${pageContext.request.contextPath}/register">
<label>FULL NAME</label><input name="name" required>
<label>EMAIL ADDRESS</label><input type="email" name="email" required>
<label>PHONE</label><input name="phone">
<label>PASSWORD (min 8 characters)</label><input type="password" name="password" minlength="8" required>
<button style="width:100%">Create account</button></form>
<p style="margin-top:12px"><a class="link" href="${pageContext.request.contextPath}/login">Back to sign in</a></p></div></body></html>
