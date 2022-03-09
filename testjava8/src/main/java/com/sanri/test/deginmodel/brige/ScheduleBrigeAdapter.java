package com.sanri.test.deginmodel.brige;

public abstract class ScheduleBrigeAdapter {
    protected Result result;
    protected String eventId;

    protected ScheduleData scheduleData;

    public ScheduleBrigeAdapter(Result result,String eventId) {
        this.result = result;
        this.eventId = eventId;
    }

    public ScheduleData scheduleData(){
        return null;
    }

}
