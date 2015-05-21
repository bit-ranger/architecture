package top.rainynight.site.blog.service.impl;

import top.rainynight.foundation.ServiceBasicSupport;
import top.rainynight.site.blog.dao.ArchiveDao;
import top.rainynight.site.blog.entity.Archive;
import top.rainynight.site.blog.service.ArchiveService;

public class ArchiveServiceImpl extends ServiceBasicSupport<Archive> implements ArchiveService {
    private ArchiveDao archiveDao;

    public void setArchiveDao(ArchiveDao archiveDao) {
        setDao(archiveDao);
        this.archiveDao = archiveDao;
    }
}
