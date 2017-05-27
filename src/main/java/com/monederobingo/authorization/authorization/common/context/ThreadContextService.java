package com.monederobingo.authorization.authorization.common.context;

import com.monederobingo.authorization.authorization.common.Environment;

public interface ThreadContextService {

    void initializeContext(Environment env, String language);

    ThreadContext getThreadContext();

    void setThreadContextOnThread(ThreadContext threadContext);
}
