package Coinzy.controllers.admin;

import Coinzy.models.Announcement;
import Coinzy.providers.AnnouncementProvider;

import java.util.List;

public class AnnouncementController {

    private final AnnouncementProvider announcementProvider;

    public AnnouncementController() {
        this.announcementProvider = new AnnouncementProvider();
    }

    // Method to add a new announcement
    public boolean addAnnouncement(String title, String message) {
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setMessage(message);
        return announcementProvider.insertAnnouncement(announcement);
    }

    // Method to fetch all announcements
    public List<Announcement> getAllAnnouncements() {
        return announcementProvider.getAnnouncements();
    }
}
