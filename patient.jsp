<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html><html><head><title>MediPulse - Patient</title><meta name="viewport" content="width=device-width,initial-scale=1"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head><body>
<%@ include file="header.jspf" %>
<div class="grid2">
<div class="card"><h3>Book Appointment</h3>
<form method="post" action="patient"><input type="hidden" name="action" value="book">
<select name="doctorId" required><c:forEach var="d" items="${doctors}"><option value="${d.id}">Dr. <c:out value="${d.name}"/> (<c:out value="${d.specialization}"/>)</option></c:forEach></select>
<div class="row"><input type="date" name="date" required><input type="time" name="time" required></div>
<input name="notes" placeholder="Reason for visit"><button>Book appointment</button></form></div>
<div class="card"><h3>Rate your doctor</h3>
<form method="post" action="patient"><input type="hidden" name="action" value="feedback">
<select name="doctorId" required><c:forEach var="d" items="${doctors}"><option value="${d.id}">Dr. <c:out value="${d.name}"/></option></c:forEach></select>
<select name="rating"><option value="5">5 - Excellent</option><option value="4">4 - Good</option><option value="3">3 - Average</option><option value="2">2 - Poor</option><option value="1">1 - Bad</option></select>
<input name="comments" placeholder="Comments"><button>Submit feedback</button></form></div>
</div>

<div class="card"><h3>Appointment History</h3>
<table><tr><th>Doctor</th><th>Date</th><th>Time</th><th>Status</th><th>Action</th></tr>
<c:forEach var="a" items="${appointments}"><tr>
<td><c:out value="${a.doctorName}"/></td><td>${a.date}</td><td>${a.time}</td><td><span class="badge ${a.status}">${a.status}</span></td>
<td><c:if test="${a.status == 'PENDING' || a.status == 'CONFIRMED'}"><form class="inline" method="post" action="patient"><input type="hidden" name="action" value="cancel"><input type="hidden" name="id" value="${a.id}"><button class="red">Cancel</button></form></c:if></td></tr></c:forEach></table></div>

<div class="card"><h3>Medical History</h3>
<table><tr><th>Date</th><th>Doctor</th><th>Diagnosis</th><th>Treatment</th></tr>
<c:forEach var="r" items="${records}"><tr><td>${r.date}</td><td><c:out value="${r.doctorName}"/></td><td><c:out value="${r.diagnosis}"/></td><td><c:out value="${r.treatment}"/></td></tr></c:forEach></table></div>

<div class="card"><h3>Find Nearby Hospitals</h3>
<form method="get" action="patient" class="row" style="align-items:flex-start"><input name="city" value="<c:out value='${q}'/>" placeholder="Search by city, name or type (e.g. Noida)"><button style="flex:0 0 100px">Search</button></form>
<table><tr><th>Hospital</th><th>City</th><th>Type</th><th>Address</th><th>Phone</th></tr>
<c:forEach var="h" items="${hospitals}"><tr><td><c:out value="${h.name}"/></td><td><c:out value="${h.city}"/></td><td><c:out value="${h.type}"/></td><td><c:out value="${h.address}"/></td><td><c:out value="${h.phone}"/></td></tr></c:forEach></table></div>
</div></body></html>
