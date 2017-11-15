package com.rainyalley.architecture.service.aop;

import java.util.List;

public interface PointContextProvider {

    List<PointContext> trace();
}
