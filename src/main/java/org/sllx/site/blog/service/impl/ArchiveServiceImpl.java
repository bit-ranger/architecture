package org.sllx.site.blog.service.impl;

import org.sllx.site.blog.dao.ArchiveDao;
import org.sllx.site.blog.entity.Archive;
import org.sllx.site.blog.service.ArchiveService;
import org.sllx.site.core.base.BaseServiceImpl;

public class ArchiveServiceImpl extends BaseServiceImpl<Archive> implements ArchiveService{
    private ArchiveDao archiveDao;

    public void setArchiveDao(ArchiveDao archiveDao) {
        setDao(archiveDao);
        this.archiveDao = archiveDao;
    }
}
