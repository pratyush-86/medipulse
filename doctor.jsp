<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html><html><head><title>MediPulse - Doctor</title><meta name="viewport" content="width=device-width,initial-scale=1"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head><body>
<%@ include file="header.jspf" %>
<div class="cards">
 <div class="card stat"><h2>${appointments.size()}</h2><span>Appointments</span></div>
 <div class="card stat"><h2>${records.size()}</h2><span>Records written</span></div>
 <div class="card stat"><h2>${avgRating} / 5</h2><span>Average rating</span></div>
</div>

<div class="card"><h3>Schedule and Appointment Overview</h3>
<table><tr><th>Patient</th><th>Date</th><th>Time</th><th>Notes</th><th>Status</th><th>Action</th></tr>
<c:forEach var="a" items="${appointments}"><tr>
<td><c:out value="${a.patientName}"/></td><td>${a.date}</td><td>${a.time}</td><td><c:out value="${a.notes}"/></td>
<td><span class="badge ${a.status}">${a.status}</span></td>
<td><form class="inline" method="post" action="doctor"><input type="hidden" name="action" value="status"><input type="hidden" name="id" value="${a.id}">
<button name="status" value="CONFIRMED">Confirm</button> <button name="status" value="COMPLETED" class="grey">Complete</button> <button name="status" value="CANCELLED" class="red">Cancel</button></form></td></tr></c:forEach></table></div>

<div class="grid2">
<div class="card"><h3>Add Patient Record</h3>
<form method="post" action="doctor"><input type="hidden" name="action" value="addRecord">
<select name="patientId" required><c:forEach var="p" items="${patients}"><option value="${p.id}"><c:out value="${p.name}"/></option></c:forEach></select>
<input name="diagnosis" placeholder="Diagnosis" required><input name="treatment" placeholder="Treatment / prescription"><button>Save record</button></form></div>
<div class="card"><h3>Patient Feedback</h3>
<table><tr><th>Patient</th><th>Rating</th><th>Comment</th></tr>
<c:forEach var="f" items="${feedback}"><tr><td><c:out value="${f.patientName}"/></td><td>${f.rating} / 5</td><td><c:out value="${f.comments}"/></td></tr></c:forEach></table></div>
</div>

<div class="card"><h3>Patient Records</h3>
<table><tr><th>Date</th><th>Patient</th><th>Diagnosis</th><th>Treatment</th></tr>
<c:forEach var="r" items="${records}"><tr><td>${r.date}</td><td><c:out value="${r.patientName}"/></td><td><c:out value="${r.diagnosis}"/></td><td><c:out value="${r.treatment}"/></td></tr></c:forEach></table></div>
</div></body></html>
