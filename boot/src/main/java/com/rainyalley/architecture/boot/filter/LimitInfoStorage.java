package com.rainyalley.architecture.boot.filter;

import java.util.List;

public interface LimitInfoStorage {

    GlobalLimitInfo getGlobalLimitInfo();

    List<TargetCallerLimitInfo> getTargetCallerLimitInfo();

    List<TargetLimitInfo> getTargetLimitInfo();

    List<CallerLimitInfo> getCallerLimitInfo();

}
