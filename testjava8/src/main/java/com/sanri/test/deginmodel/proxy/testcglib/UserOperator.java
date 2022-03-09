package com.sanri.test.deginmodel.proxy.testcglib;

public interface UserOperator {
    User queryByName(String name);
    void insertUser(User user);
}
