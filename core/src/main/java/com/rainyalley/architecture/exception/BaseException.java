package com.rainyalley.architecture.exception;

/**
 * @author bin.zhang
 */
public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 8578382796497717923L;
    private TaskStatus taskStatus;

    private Resource resource;

    private String message;

    public BaseException(TaskStatus taskStatus, Resource resource, String message, Throwable cause) {
        super(message, cause);
        this.taskStatus = taskStatus;
        this.resource = resource;
        this.message = message;
    }

    public BaseException(TaskStatus taskStatus, Resource resource, String message) {
        super(message);
        this.taskStatus = taskStatus;
        this.resource = resource;
        this.message = message;
    }

    public BaseException(TaskStatus taskStatus, Resource resource) {
        super(taskStatus.getReasonPhrase() + (resource.getName().length() == 0 ? "" : " : " + resource.getName()));
        this.taskStatus = taskStatus;
        this.resource = resource;
        this.message = taskStatus.getReasonPhrase() + (resource.getName().length() == 0 ? "" : " : " + resource.getName());
    }

    @Override
    public String getMessage() {
        return message;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"taskStatus\":\"")
                .append(taskStatus)
                .append("\"");
        sb.append(",\"resource\":\"")
                .append(resource)
                .append("\"");
        sb.append(",\"message\":\"")
                .append(message)
                .append("\"");
        sb.append("}");
        return sb.toString();
    }
}
