package com.maksat.participantapp.models;

public class EditableProfile {

    public static class PersonalData {
        public static class Photo{
            public String filename;
            public String path;
            public Boolean isNew;
            public String content;

            public Photo(String filename, String path, Boolean isNew, String content) {
                this.filename = filename;
                this.path = path;
                this.isNew = isNew;
                this.content = content;
            }
        }
        public Integer titleId;
        public String firstNameRus;
        public String lastNameRus;
        public String patronymic;
        public String firstNameEng;
        public String lastNameEng;
        public Integer genderId;
        public Integer residenceId;
        public EditableProfile.PersonalData.Photo photo;
        public String comment;
        public Integer sportId;

        public PersonalData(Integer titleId, String firstNameRus, String lastNameRus, String patronymic, String firstNameEng, String lastNameEng, Integer genderId, Integer residenceId, EditableProfile.PersonalData.Photo photo, String comment, Integer sportId) {
            this.titleId = titleId;
            this.firstNameRus = firstNameRus;
            this.lastNameRus = lastNameRus;
            this.patronymic = patronymic;
            this.firstNameEng = firstNameEng;
            this.lastNameEng = lastNameEng;
            this.genderId = genderId;
            this.residenceId = residenceId;
            this.photo=photo;
            this.comment = comment;
            this.sportId = sportId;
        }
        public PersonalData(Profile profile){
            this.titleId=profile.titleId;
            this.firstNameRus=profile.firstNameRus;
            this.lastNameRus=profile.lastNameRus;
            this.patronymic=profile.patronymic;
            this.firstNameEng=profile.firstNameEng;
            this.lastNameEng=profile.lastNameEng;
            this.genderId=profile.genderId;
            this.residenceId=profile.residenceId;
            this.photo=new Photo("", null, false, profile.photo);
            this.comment=profile.comment;
            this.sportId=profile.sportId;
        }
    }

    public static class Company{
        public String organizationRus;
        public String organizationEng;
        public String positionRus;
        public String positionEng;
        public Integer sphereId;
        public Integer countryCompanyHeadId;
        public String postAddress;

        public Company(String organizationRus, String organizationEng, String positionRus, String positionEng, Integer sphereId, Integer countryCompanyHeadId, String postAddress) {
            this.organizationRus = organizationRus;
            this.organizationEng = organizationEng;
            this.positionRus = positionRus;
            this.positionEng = positionEng;
            this.sphereId = sphereId;
            this.countryCompanyHeadId = countryCompanyHeadId;
            this.postAddress = postAddress;
        }
        public Company(Profile profile){
            this.organizationRus=profile.company.nameRus;
            this.organizationEng=profile.company.nameEng;
            this.positionRus=profile.company.positionRus;
            this.positionEng=profile.company.positionEng;
            this.sphereId=profile.company.sphereId;
            this.countryCompanyHeadId=profile.company.countryCompanyHeadId;
            this.postAddress=profile.company.postAddress;
        }
    }

    public static class Contact{
        public String phoneNumber;
        public String email;
        public String webSite;

        public Contact(String phoneNumber, String email, String webSite) {
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.webSite = webSite;
        }
        public Contact(Profile profile){
            this.phoneNumber=profile.phoneNumber;
            this.email=profile.email;
            this.webSite=profile.webSite;
        }
    }

    public static class Visa{
        public Boolean visa;

        public Visa(Boolean visa) {
            this.visa = visa;
        }
    }

    public static class ArrivalDeparture{
        public String arrivalStation;
        public String arrivalDateTime;
        public String arrivalFlightInfo;
        public String departureStation;
        public String departureDateTime;
        public String departureFlightInfo;

        public ArrivalDeparture(String arrivalStation, String arrivalDateTime, String arrivalFlightInfo, String departureStation, String departureDateTime, String departureFlightInfo) {
            this.arrivalStation = arrivalStation;
            this.arrivalDateTime = arrivalDateTime;
            this.arrivalFlightInfo = arrivalFlightInfo;
            this.departureStation = departureStation;
            this.departureDateTime = departureDateTime;
            this.departureFlightInfo = departureFlightInfo;
        }
        public ArrivalDeparture(Profile profile){
            this.arrivalStation = profile.participantArrivalDeparture.arrivalStation;
            this.arrivalDateTime = profile.participantArrivalDeparture.arrivalDateTime;
            this.arrivalFlightInfo = profile.participantArrivalDeparture.arrivalFlightInfo;
            this.departureStation = profile.participantArrivalDeparture.departureStation;
            this.departureDateTime = profile.participantArrivalDeparture.departureDateTime;
            this.departureFlightInfo = profile.participantArrivalDeparture.departureFlightInfo;
        }
    }
}
