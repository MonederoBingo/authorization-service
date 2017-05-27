package com.monederobingo.authorization.authorization.common.context;

import com.monederobingo.authorization.authorization.common.Environment;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class ThreadContextServiceImpl implements ThreadContextService {
    private static final ThreadLocal<ThreadContext> THREAD_CONTEXT = new ThreadLocal<>();

    @Override
    public void initializeContext(Environment environment, String language) {
        ThreadContext threadContext = new ThreadContext();
        threadContext.setEnvironment(environment);
        setThreadContextOnThread(threadContext);
    }

    @Override
    public ThreadContext getThreadContext() {
        return THREAD_CONTEXT.get();
    }

    @Override
    public void setThreadContextOnThread(ThreadContext threadContext) {
        THREAD_CONTEXT.set(threadContext);
    }
}
