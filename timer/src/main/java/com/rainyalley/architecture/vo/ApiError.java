package com.rainyalley.architecture.vo;


import lombok.Data;

/**
 * @author bin.zhang
 */
@Data
public class ApiError {
      private long timestamp;
      private int status;
      private String error;
      private String exception;
      private String message;
      private String path;
}
