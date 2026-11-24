<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html><html><head><title>MediPulse - Admin</title><meta name="viewport" content="width=device-width,initial-scale=1"><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head><body>
<%@ include file="header.jspf" %>
<div class="cards">
 <div class="card stat"><h2>${counts['DOCTOR']}</h2><span>Doctors</span></div>
 <div class="card stat"><h2>${counts['PATIENT']}</h2><span>Patients</span></div>
 <div class="card stat"><h2>${appointments.size()}</h2><span>Appointments</span></div>
 <div class="card stat"><h2>${statusSummary['PENDING'] == null ? 0 : statusSummary['PENDING']}</h2><span>Pending</span></div>
</div>

<div class="grid2">
<div class="card"><h3>Performance Analytics</h3>
<b style="font-size:13px">Appointments by status</b><br><br>
<c:forEach var="e" items="${statusSummary}"><div style="margin-bottom:6px"><span style="display:inline-block;width:100px;font-size:13px">${e.key}</span><span class="bar" style="width:${e.value * 25}px"></span>${e.value}</div></c:forEach>
<br><b style="font-size:13px">Busiest doctors</b><br><br>
<c:forEach var="e" items="${topDoctors}"><div style="margin-bottom:6px"><span style="display:inline-block;width:140px;font-size:13px"><c:out value="${e.key}"/></span><span class="bar" style="width:${e.value * 25}px"></span>${e.value}</div></c:forEach>
</div>
<div class="card"><h3>System Settings</h3>
<form method="post" action="admin"><input type="hidden" name="action" value="saveSettings">
<label>CLINIC NAME</label><input name="clinic_name" value="<c:out value='${settings.clinic_name}'/>">
<label>SLOT LENGTH (MINUTES)</label><input name="slot_minutes" value="<c:out value='${settings.slot_minutes}'/>">
<label>WORKING HOURS</label><input name="working_hours" value="<c:out value='${settings.working_hours}'/>">
<button>Save settings</button></form></div>
</div>

<div class="card"><h3>Add User</h3>
<form method="post" action="admin"><input type="hidden" name="action" value="addUser">
<div class="row"><input name="name" placeholder="Full name" required><input type="email" name="email" placeholder="Email" required>
<input type="password" name="password" placeholder="Password (min 8)" minlength="8" required><input name="phone" placeholder="Phone">
<select name="role"><option>DOCTOR</option><option>PATIENT</option><option>ADMIN</option></select>
<input name="specialization" placeholder="Specialization (doctors)"></div>
<button>Add user</button></form></div>

<div class="card"><h3>User Management</h3>
<table><tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Action</th></tr>
<c:forEach var="u" items="${users}"><tr>
<td>${u.id}</td><td><c:out value="${u.name}"/></td><td><c:out value="${u.email}"/></td><td>${u.roleLabel}</td>
<td><c:if test="${u.role != 'ADMIN'}"><form class="inline" method="post" action="admin" onsubmit="return confirm('Delete this user?')">
<input type="hidden" name="action" value="deleteUser"><input type="hidden" name="id" value="${u.id}"><button class="red">Delete</button></form></c:if></td></tr></c:forEach></table></div>

<div class="card"><h3>Appointment Management</h3>
<table><tr><th>ID</th><th>Patient</th><th>Doctor</th><th>Date</th><th>Time</th><th>Status</th><th>Change status</th></tr>
<c:forEach var="a" items="${appointments}"><tr>
<td>${a.id}</td><td><c:out value="${a.patientName}"/></td><td><c:out value="${a.doctorName}"/></td><td>${a.date}</td><td>${a.time}</td>
<td><span class="badge ${a.status}">${a.status}</span></td>
<td><form class="inline" method="post" action="admin"><input type="hidden" name="action" value="status"><input type="hidden" name="id" value="${a.id}">
<select name="status" style="width:auto;margin:0"><option>PENDING</option><option>CONFIRMED</option><option>CANCELLED</option><option>COMPLETED</option></select> <button>Set</button></form></td></tr></c:forEach></table></div>

<div class="grid2">
<div class="card"><h3>Hospitals Directory</h3>
<form method="post" action="admin"><input type="hidden" name="action" value="addHospital">
<div class="row"><input name="name" placeholder="Hospital name" required><input name="city" placeholder="City" required></div>
<div class="row"><input name="address" placeholder="Address"><input name="phone" placeholder="Phone"><input name="type" placeholder="Type"></div><button>Add hospital</button></form><br>
<table><tr><th>Name</th><th>City</th><th></th></tr>
<c:forEach var="h" items="${hospitals}"><tr><td><c:out value="${h.name}"/></td><td><c:out value="${h.city}"/></td>
<td><form class="inline" method="post" action="admin"><input type="hidden" name="action" value="deleteHospital"><input type="hidden" name="id" value="${h.id}"><button class="red">Remove</button></form></td></tr></c:forEach></table></div>
<div class="card"><h3>Activity Log (background threads)</h3><div class="log">
<c:forEach var="line" items="${activity}"><c:out value="${line}"/><br></c:forEach></div></div>
</div>
</div></body></html>
