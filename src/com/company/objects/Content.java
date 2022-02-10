package com.company.objects;

import java.io.Serializable;

public class Content implements Serializable {
    private int queueID = 0;
    private String operation =null;
    private String script =null;

    public Content(int scriptID, String operation, String script) {
        this.queueID = scriptID;
        this.operation = operation;
        this.script = script;
    }

    public int getQueueID() {
        return queueID;
    }

    public void setQueueID(int queueID) {
        this.queueID = queueID;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
