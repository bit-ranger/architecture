package com.rainyalley.architecture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author bin.zhang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page implements Serializable {

    private static final long serialVersionUID = 3571665247740418320L;

    /**
     * 页码
     */
    private int pageNum = 1;

    /**
     * 每页条数
     */
    private int pageSize = 10;
}
