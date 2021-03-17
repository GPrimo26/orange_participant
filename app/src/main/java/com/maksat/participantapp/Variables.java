package com.maksat.participantapp;

import com.maksat.participantapp.models.AcrStatus;
import com.maksat.participantapp.models.BadgeStatus;
import com.maksat.participantapp.models.Category;
import com.maksat.participantapp.models.EventProgramByDays;
import com.maksat.participantapp.models.Profile;
import com.maksat.participantapp.models.Sport;
import com.maksat.participantapp.models.Title;
import com.maksat.participantapp.models.TokenBody;

import java.util.List;

public class Variables {
    public static TokenBody token;
    public static String ip="https://cab.rusportevents.ru";
    public static Profile profile;
    public static List<Sport> sports;
    public static List<AcrStatus> acrStatuses;
    public static List<BadgeStatus> badgeStatuses;
    public static List<Category> allCategories;
    public static List<String> datesList;
    public static List<Title> titles;
    public static List<Profile.Company.Sphere> spheres;
}
