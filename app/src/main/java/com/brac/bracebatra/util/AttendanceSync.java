package com.brac.bracebatra.util;

import com.brac.bracebatra.model.Student;

/**
 * Created by hhson on 9/27/2017.
 */
public interface AttendanceSync {
    public void onAttCheck(String id, Boolean isAttend, Boolean hasUniform ,Student student);


}
