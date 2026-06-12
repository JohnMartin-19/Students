package com.mburu.student_api.entity;

import java.util.*;

public enum Role {
    USER, //-----> only read + some slight writing
    ADMIN     //--> can perform all actions(CRUD)
}