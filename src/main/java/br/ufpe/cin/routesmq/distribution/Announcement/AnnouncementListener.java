package br.ufpe.cin.routesmq.distribution.Announcement;


/**
 * Created by tjamir on 7/2/17.
 */
public interface AnnouncementListener {

    public void routeDiscovered(RouteAnnouncement routeAnnouncement);


    public void serviceDiscovered(ServiceAnnouncement serviceAnnouncement);
}
