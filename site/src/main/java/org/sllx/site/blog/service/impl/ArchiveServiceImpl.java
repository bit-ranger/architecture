package org.sllx.site.blog.service.impl;

import org.sllx.mvc.ServiceBasicSupport;
import org.sllx.site.blog.dao.ArchiveDao;
import org.sllx.site.blog.entity.Archive;
import org.sllx.site.blog.service.ArchiveService;

public class ArchiveServiceImpl extends ServiceBasicSupport<Archive> implements ArchiveService{
    private ArchiveDao archiveDao;

    public void setArchiveDao(ArchiveDao archiveDao) {
        setDao(archiveDao);
        this.archiveDao = archiveDao;
    }
}
