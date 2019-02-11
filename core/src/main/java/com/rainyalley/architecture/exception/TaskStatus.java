package com.rainyalley.architecture.exception;


/**
 * @author bin.zhang
 */

public enum TaskStatus {


    // 1xx Informational


    /**
     * 处理中
     */
    PROCESSING(102, "Processing"),



    // 2xx Success

    /**
     * 处理成功
     */
    OK(200, "OK"),

    /**
     * 处理成功, 新的内容已创建
     */
    CREATED(201, "Created"),

    /**
     * 已接收, 将会异步进行后续处理
     */
    ACCEPTED(202, "Accepted"),



    // --- 4xx Client Error ---
    // --- 客户端错误, 如果变更为正确的请求参数, 将可以避免此错误

    /**
     * 由于明显的客户端错误, 拒绝处理该请求
     * <p>例如，格式错误
     */
    BAD_REQUEST(400, "Bad Request"),

    /**
     * 需要认证
     */
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * 拒绝进行此操作，且不愿意表达任何明确信息
     */
    FORBIDDEN(403, "Forbidden"),

    /**
     * 访问的资源不存在
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * 操作超时
     */
    REQUEST_TIMEOUT(408, "Request Timeout"),

    /**
     * 冲突, 或资源被占用
     * <p>泛指竞态失败
     */
    CONFLICT(409, "Conflict"),

    /**
     * 资源已不可用
     */
    GONE(410, "Gone"),

    /**
     * 不满足前置条件
     */
    PRECONDITION_FAILED(412, "Precondition Failed"),

    /**
     * 数据量过大
     */
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),

    /**
     * 请求格式正确，但未通过参数校验
     */
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),

    /**
     * 请求的资源已被锁定
     */
    LOCKED(423, "Locked"),

    /**
     * 因为先前的操作失败，所以此次操作失败
     */
    FAILED_DEPENDENCY(424, "Failed Dependency"),

    /**
     * 资源已发生变更，需要重新获取
     */
    PRECONDITION_REQUIRED(428, "Precondition Required"),

    /**
     * 操作太过频繁
     */
    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    /**
     * 重复的请求
     */
    DUPLICATE_REQUEST(461, "Duplicate Request"),


    // --- 5xx Server Error ---
    // 系统错误, 无论用户输入是否正确都会发生的错误

    /**
     * 内部错误，不愿意透露原因
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    /**
     * 不支持的操作，未实现的操作
     */
    NOT_IMPLEMENTED(501, "Not Implemented"),

    /**
     * 网关错误
     * <p> 例如本系统访问底层系统，未能收到有效的响应
     */
    BAD_GATEWAY(502, "Bad Gateway"),

    /**
     * 服务过载，拒绝此操作
     * <p>例如队列已满
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    /**
     * 网关超时
     * <p> 例如本系统访问底层系统，超时
     */
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),

    /**
     * 无法存储内容
     */
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),

    /**
     * 数据库数据错误, 例如丢失，格式不正确，被篡改
     */
    DATABASE_DATA_ERROR(520,"Database Data Error"),

    /**
     * 内部调用时，传递的数据错误
     */
    INVOKING_DATA_ERROR(521, "Invoking Data Error"),

    /**
     * 下游错误， 底层系统错误
     */
    DOWNSTREAM_ERROR(522, "Downstream Error");




    // 使用已存在的值与ResourceEnum组合来表达一个错误
    // 不要在这里增加内容!!!


    private final int value;

    private final String reasonPhrase;


    TaskStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }


    /**
     * Return the integer value of this status code.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }


    public boolean is1xxInformational() {
        return Series.INFORMATIONAL.equals(series());
    }


    public boolean is2xxSuccessful() {
        return Series.SUCCESSFUL.equals(series());
    }


    public boolean is4xxClientError() {
        return Series.CLIENT_ERROR.equals(series());
    }


    public boolean is5xxServerError() {
        return Series.SERVER_ERROR.equals(series());
    }


    public Series series() {
        return Series.valueOf(this);
    }

    /**
     * Return a string representation of this status code.
     */
    @Override
    public String toString() {
        return Integer.toString(this.value);
    }


    /**
     * Return the enum constant of this type with the specified numeric value.
     * @param statusCode the numeric value of the enum to be returned
     * @return the enum constant with the specified numeric value
     * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
     */
    public static TaskStatus valueOf(int statusCode) {
        for (TaskStatus status : values()) {
            if (status.value == statusCode) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }


    /**
     * @author bin.zhang
     */
    public enum Series {
        //
        INFORMATIONAL(1),
        SUCCESSFUL(2),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        Series(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Series valueOf(int status) {
            int seriesCode = status / 100;
            for (Series series : values()) {
                if (series.value == seriesCode) {
                    return series;
                }
            }
            throw new IllegalArgumentException("No matching constant for [" + status + "]");
        }

        public static Series valueOf(TaskStatus status) {
            return valueOf(status.value);
        }
    }

}
