package com.rainyalley.architecture.filter.limit;

import java.util.List;

public interface LimitInfoStorage {

    GlobalLimitInfo getGlobalLimitInfo();

    List<TargetCallerLimitInfo> getTargetCallerLimitInfo();

    List<TargetLimitInfo> getTargetLimitInfo();

    List<CallerLimitInfo> getCallerLimitInfo();

}
