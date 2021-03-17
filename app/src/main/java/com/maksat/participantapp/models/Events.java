package com.maksat.participantapp.models;

import java.util.List;

public class Events {
    private List<events> events;
    private Integer currEventId;

    public static class events{
        private Integer id;
        private String nameFullRus;
        private String nameShortRus;
        private String nameFullEng;
        private String nameShortEng;
        private String nameFullAra;
        private String nameShortAra;
        private String dateStart;
        private String dateFinish;
        private String dateRegistrationStart;
        private String dateRegistrationFinish;
        private String descriptionRus;
        private String descriptionEng;
        private String descriptionAra;

        public events(Integer id, String nameFullRus, String nameShortRus, String nameFullEng, String nameShortEng, String nameFullAra, String nameShortAra, String dateStart, String dateFinish, String dateRegistrationStart, String dateRegistrationFinish, String descriptionRus, String descriptionEng, String descriptionAra) {
            this.id = id;
            this.nameFullRus = nameFullRus;
            this.nameShortRus = nameShortRus;
            this.nameFullEng = nameFullEng;
            this.nameShortEng = nameShortEng;
            this.nameFullAra = nameFullAra;
            this.nameShortAra = nameShortAra;
            this.dateStart = dateStart;
            this.dateFinish = dateFinish;
            this.dateRegistrationStart = dateRegistrationStart;
            this.dateRegistrationFinish = dateRegistrationFinish;
            this.descriptionRus = descriptionRus;
            this.descriptionEng = descriptionEng;
            this.descriptionAra = descriptionAra;
        }

        public Integer getId() {
            return id;
        }

        public String getNameFullRus() {
            return nameFullRus;
        }

        public String getNameShortRus() {
            return nameShortRus;
        }

        public String getNameFullEng() {
            return nameFullEng;
        }

        public String getNameShortEng() {
            return nameShortEng;
        }

        public String getNameFullAra() {
            return nameFullAra;
        }

        public String getNameShortAra() {
            return nameShortAra;
        }

        public String getDateStart() {
            return dateStart;
        }

        public String getDateFinish() {
            return dateFinish;
        }

        public String getDateRegistrationStart() {
            return dateRegistrationStart;
        }

        public String getDateRegistrationFinish() {
            return dateRegistrationFinish;
        }

        public String getDescriptionRus() {
            return descriptionRus;
        }

        public String getDescriptionEng() {
            return descriptionEng;
        }

        public String getDescriptionAra() {
            return descriptionAra;
        }
    }

    public Events(List<Events.events> events, Integer currEventId) {
        this.events = events;
        this.currEventId = currEventId;
    }

    public List<Events.events> getEvents() {
        return events;
    }

    public Integer getCurrEventId() {
        return currEventId;
    }
}
