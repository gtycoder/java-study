package com.gty.job;

import com.gty.server.TestServer;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SimpleJobTwo extends QuartzJobBean {
    @Autowired
    private TestServer testServer;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("-----------------SimpleJobTwo的任务执行了------"+new Date());
        System.out.println("==="+testServer.test());
    }
}

