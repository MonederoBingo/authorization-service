package com.monederobingo.authorization.authorization.common.context;

import com.monederobingo.authorization.authorization.common.Environment;

public class ThreadContext {
    private Environment _environment;

    public Environment getEnvironment() {
        return _environment;
    }

    public void setEnvironment(Environment environment) {
        _environment = environment;
    }
}
