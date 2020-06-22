/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import java.util.Date;
import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
/**
 *
 * @author Duong
 */
public class NewClass {
     public static class SimpleJob implements Job {

        @Override
        public void execute(JobExecutionContext arg0) throws JobExecutionException {
            System.out.println("This is a quartz job!"+arg0.getFireTime());
            System.out.println(new Date());
        }
    }
    public static void main(String[] args) {
        try {
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();

            sched.start();
            JobDetail job = newJob(SimpleJob.class)
                    .withIdentity("job1", "group1") // name "myJob", group "group1"
                    .build();
            // Trigger the job to run now, and then every 40 seconds
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(5)
                            .repeatForever())
                    .build();

            // Tell quartz to schedule the job using our trigger
            sched.scheduleJob(job, trigger);
        } catch (Exception e) {
        }
    }
}
