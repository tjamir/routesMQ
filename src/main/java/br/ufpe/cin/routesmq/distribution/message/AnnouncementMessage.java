package br.ufpe.cin.routesmq.distribution.message;

import br.ufpe.cin.routesmq.distribution.Announcement.Announcement;

import java.util.List;

/**
 * Created by tjamir on 7/1/17.
 */
public class AnnouncementMessage extends Message{

    private List<Announcement> announcement;


    public AnnouncementMessage(List<Announcement> announcement) {
        this.announcement = announcement;
    }


    public List<Announcement> getAnnouncement() {
        return announcement;
    }
}
