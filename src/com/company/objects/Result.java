package com.company.objects;

import java.io.Serializable;

public class Result implements Serializable {
    private String script = null;
    private double time = 0;

    public Result(String script, double time) {
        this.script = script;
        this.time = time;
    }

    public Result() {
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
