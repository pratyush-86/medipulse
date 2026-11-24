<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html><html><head><title>MediPulse - Sign in</title><meta name="viewport" content="width=device-width,initial-scale=1"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head><body>
<div class="card form-box"><h3>&#10010; MediPulse</h3><h2 style="margin-bottom:6px">Welcome back</h2>
<p style="color:#64748b;margin-bottom:14px;font-size:14px">Sign in to continue to your dashboard.</p>
<c:if test="${not empty error}"><div class="err"><c:out value="${error}"/></div></c:if>
<c:if test="${not empty sessionScope.flashOk}"><div class="ok"><c:out value="${sessionScope.flashOk}"/></div><c:remove var="flashOk" scope="session"/></c:if>
<form method="post" action="${pageContext.request.contextPath}/login">
<label>EMAIL ADDRESS</label><input type="email" name="email" placeholder="you@example.com" required>
<label>PASSWORD</label><input type="password" name="password" placeholder="Enter your password" required>
<button style="width:100%">Sign in</button></form>
<p style="margin-top:12px;font-size:14px"><a class="link" href="${pageContext.request.contextPath}/forgot">Forgot password?</a> &nbsp;|&nbsp; <a class="link" href="${pageContext.request.contextPath}/register">New patient? Register</a></p>
<div class="hint"><b>Demo sign-in</b> (password: careflow123)<br>Admin: alex@careflow.health<br>Doctor: amelia@careflow.health<br>Patient: olivia@email.com</div>
</div></body></html>
