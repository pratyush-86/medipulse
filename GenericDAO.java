package com.medipulse.dao;

import com.medipulse.exception.MediPulseException;
import java.util.List;

/** Generic DAO contract implemented by every data-access class. */
public interface GenericDAO<T> {
    List<T> findAll() throws MediPulseException;
    boolean delete(int id) throws MediPulseException;
}
