package br.ufpe.cin.routesmq.distribution.message;

import br.ufpe.cin.routesmq.distribution.Announcement.Announcement;

/**
 * Created by tjamir on 7/1/17.
 */
public class AnnouncementMessage extends Message{

    private Announcement announcement;


    public AnnouncementMessage(Announcement announcement) {
        this.announcement = announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }
}
