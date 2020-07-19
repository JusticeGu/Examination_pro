package com.q7w.examination.Service;

import com.q7w.examination.entity.Announcement;

import java.util.List;

/**
 * @author JunXxxxi
 * @date 2020/7/19 19:29
 **/
public interface AnnouncementService {
    public List<Announcement> announcementList();
    public int addAnnouncement(Announcement announcement);
    public int delAnnouncement(int aid);
    public int modifyAnnouncement(Announcement announcement);

}
