package com.medipulse.service;

import com.medipulse.model.Appointment;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** COLLECTIONS and GENERICS: builds report data with Maps, Lists and streams. */
public class AnalyticsService {

    /** Appointments per status, sorted alphabetically (TreeMap). */
    public Map<String, Long> statusSummary(List<Appointment> list) {
        return list.stream().collect(
            Collectors.groupingBy(Appointment::getStatus, TreeMap::new, Collectors.counting()));
    }

    /** Doctors with the most appointments, highest first (LinkedHashMap keeps order). */
    public Map<String, Long> topDoctors(List<Appointment> list, int limit) {
        Map<String, Long> counts = list.stream().collect(
            Collectors.groupingBy(Appointment::getDoctorName, Collectors.counting()));
        Map<String, Long> top = new LinkedHashMap<>();
        counts.entrySet().stream()
              .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
              .limit(limit)
              .forEach(e -> top.put(e.getKey(), e.getValue()));
        return top;
    }
}
