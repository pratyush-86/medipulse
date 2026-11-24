package com.medipulse.model;

/** Implemented by every user type; each role supplies its own dashboard. */
public interface Dashboardable {
    String getDashboardPath();
    String getRoleLabel();
}
