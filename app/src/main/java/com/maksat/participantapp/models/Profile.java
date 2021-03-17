package com.maksat.participantapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;

public class Profile {


    public static class ParticipantArrivalDeparture {
        public Integer arrivalTransportId;
        public String arrivalStation;
        public String arrivalFlightInfo;
        public String arrivalDateTime;
        public Integer departureTransportId;
        public String departureStation;
        public String departureFlightInfo;
        public String departureDateTime;
        public Boolean isBaggageLost;

        public ParticipantArrivalDeparture(Integer arrivalTransportId, String arrivalStation, String arrivalFlightInfo, String arrivalDateTime, Integer departureTransportId, String departureStation, String departureFlightInfo, String departureDateTime, Boolean isBaggageLost) {
            this.arrivalTransportId = arrivalTransportId;
            this.arrivalStation = arrivalStation;
            this.arrivalFlightInfo = arrivalFlightInfo;
            this.arrivalDateTime = arrivalDateTime;
            this.departureTransportId = departureTransportId;
            this.departureStation = departureStation;
            this.departureFlightInfo = departureFlightInfo;
            this.departureDateTime = departureDateTime;
            this.isBaggageLost = isBaggageLost;
        }
    }

    public static class Company {
        public static class Sphere {
            public Integer id;
            public String nameRus;
            public String nameEng;
            public String nameAra;
            public Boolean isMassMedia;

            public Sphere(Integer id, String nameRus, String nameEng, String nameAra, Boolean isMassMedia) {
                this.id = id;
                this.nameRus = nameRus;
                this.nameEng = nameEng;
                this.nameAra = nameAra;
                this.isMassMedia = isMassMedia;
            }
        }

        public Integer id;
        public String nameRus;
        public String nameEng;
        public String positionRus;
        public String positionEng;
        public Integer countryCompanyHeadId;
        public String postAddress;
        public Sphere sphere;
        public Integer sphereId;

        public Company(Integer id, String nameRus, String nameEng, String positionRus, String positionEng, Integer countryCompanyHeadId, String postAddress, Sphere sphere, Integer sphereId) {
            this.id = id;
            this.nameRus = nameRus;
            this.nameEng = nameEng;
            this.positionRus = positionRus;
            this.positionEng = positionEng;
            this.countryCompanyHeadId = countryCompanyHeadId;
            this.postAddress = postAddress;
            this.sphere = sphere;
            this.sphereId = sphereId;
        }
    }

    public static class Category {
        public Integer id;
        public Integer eventCategoryId;
        public Integer eventId;
        public String nameRus;
        public String nameEng;
        public String nameAra;
        public String descriptionRus;
        public String descriptionEng;
        public String descriptionAra;
        public String code;
        public String color;
        public Integer priceRouble;
        public Integer priceDollar;

        public Category(Integer id, Integer eventCategoryId, Integer eventId, String nameRus, String nameEng, String nameAra, String descriptionRus, String descriptionEng, String descriptionAra, String code, String color, Integer priceRouble, Integer priceDollar) {
            this.id = id;
            this.eventCategoryId = eventCategoryId;
            this.eventId = eventId;
            this.nameRus = nameRus;
            this.nameEng = nameEng;
            this.nameAra = nameAra;
            this.descriptionRus = descriptionRus;
            this.descriptionEng = descriptionEng;
            this.descriptionAra = descriptionAra;
            this.code = code;
            this.color = color;
            this.priceRouble = priceRouble;
            this.priceDollar = priceDollar;
        }
    }

    public Integer id;
    public Integer eventId;
    public String firstNameRus;
    public String lastNameRus;
    public String patronymic;
    public String firstNameEng;
    public String lastNameEng;
    public Integer genderId;
    public Integer titleId;
    public String comment;
    public Integer languageId;
    public Integer residenceId;
    public String photo;
    public Integer sportId;
    public Integer regionId;
    public String sportNameRus;
    public String sportNameEng;
    public String regionNameRus;
    public String regionNameEng;
    public String webSite;
    public String email;
    public String phoneNumber;
    public String passportNumber;
    public Integer citizenshipId;
    public String birthday;
    public String issuedBy;
    public String issuedDate;
    public String validUntilDate;
    public String birthPlace;
    public Boolean visa;
    public Boolean needHotel;
    public ParticipantArrivalDeparture participantArrivalDeparture;
    public String hotel;
    public String room;
    public String contractOfPayment;
    public Company company;
    public Category category;
    public String checkInDateTime;
    public String checkOutDateTime;

    public Profile(@NotNull Profile profile){
        this.id = profile.id;
        this.eventId = profile.eventId;
        this.firstNameRus = profile.firstNameRus;
        this.lastNameRus = profile.lastNameRus;
        this.patronymic = profile.patronymic;
        this.firstNameEng = profile.firstNameEng;
        this.lastNameEng = profile.lastNameEng;
        this.genderId = profile.genderId;
        this.titleId = profile.titleId;
        this.comment = profile.comment;
        this.languageId = profile.languageId;
        this.residenceId = profile.residenceId;
        this.photo = profile.photo;
        this.sportId = profile.sportId;
        this.regionId = profile.regionId;
        this.sportNameRus = profile.sportNameRus;
        this.sportNameEng = profile.sportNameEng;
        this.regionNameRus = profile.regionNameRus;
        this.regionNameEng = profile.regionNameEng;
        this.webSite = profile.webSite;
        this.email = profile.email;
        this.phoneNumber = profile.phoneNumber;
        this.passportNumber = profile.passportNumber;
        this.citizenshipId = profile.citizenshipId;
        this.birthday = profile.birthday;
        this.issuedBy = profile.issuedBy;
        this.issuedDate = profile.issuedDate;
        this.validUntilDate = profile.validUntilDate;
        this.birthPlace = profile.birthPlace;
        this.visa = profile.visa;
        this.needHotel = profile.needHotel;
        this.participantArrivalDeparture = profile.participantArrivalDeparture;
        this.hotel = profile.hotel;
        this.room = profile.room;
        this.contractOfPayment = profile.contractOfPayment;
        this.company = profile.company;
        this.category = profile.category;
        this.checkInDateTime = profile.checkInDateTime;
        this.checkOutDateTime = profile.checkOutDateTime;
    }
    public Profile(Integer id, Integer eventId, String firstNameRus, String lastNameRus, String patronymic, String firstNameEng, String lastNameEng, Integer genderId, Integer titleId, String comment, Integer languageId, Integer residenceId, String photo, Integer sportId, Integer regionId, String sportNameRus, String sportNameEng, String regionNameRus, String regionNameEng, String webSite, String email, String phoneNumber, String passportNumber, Integer citizenshipId, String birthday, String issuedBy, String issuedDate, String validUntilDate, String birthPlace, Boolean visa, Boolean needHotel, ParticipantArrivalDeparture participantArrivalDeparture, String hotel, String room, String contractOfPayment, Company company, Category category, String checkInDateTime, String checkOutDateTime) {
        this.id = id;
        this.eventId = eventId;
        this.firstNameRus = firstNameRus;
        this.lastNameRus = lastNameRus;
        this.patronymic = patronymic;
        this.firstNameEng = firstNameEng;
        this.lastNameEng = lastNameEng;
        this.genderId = genderId;
        this.titleId = titleId;
        this.comment = comment;
        this.languageId = languageId;
        this.residenceId = residenceId;
        this.photo = photo;
        this.sportId = sportId;
        this.regionId = regionId;
        this.sportNameRus = sportNameRus;
        this.sportNameEng = sportNameEng;
        this.regionNameRus = regionNameRus;
        this.regionNameEng = regionNameEng;
        this.webSite = webSite;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
        this.citizenshipId = citizenshipId;
        this.birthday = birthday;
        this.issuedBy = issuedBy;
        this.issuedDate = issuedDate;
        this.validUntilDate = validUntilDate;
        this.birthPlace = birthPlace;
        this.visa = visa;
        this.needHotel = needHotel;
        this.participantArrivalDeparture = participantArrivalDeparture;
        this.hotel = hotel;
        this.room = room;
        this.contractOfPayment = contractOfPayment;
        this.company = company;
        this.category = category;
        this.checkInDateTime = checkInDateTime;
        this.checkOutDateTime = checkOutDateTime;
    }

    public void setPersonalData(@NotNull EditableProfile.PersonalData personalData){
        this.titleId=personalData.titleId;
        this.firstNameRus=personalData.firstNameRus;
        this.firstNameEng=personalData.firstNameEng;
        this.lastNameRus=personalData.lastNameRus;
        this.lastNameEng=personalData.lastNameEng;
        this.patronymic=personalData.patronymic;
        this.genderId=personalData.genderId;
        this.residenceId=personalData.residenceId;
        this.comment=personalData.comment;
        this.sportId=personalData.sportId;
    }

    public void setContact(@NotNull EditableProfile.Contact contact){
        this.phoneNumber=contact.phoneNumber;
        this.email=contact.email;
        this.webSite=contact.webSite;
    }

    public void setCompany(@NotNull EditableProfile.Company company) {
        this.company.nameRus=company.organizationRus;
        this.company.nameEng=company.organizationEng;
        this.company.positionRus=company.positionRus;
        this.company.positionEng=company.positionEng;
        this.company.sphereId=company.sphereId;
        this.company.countryCompanyHeadId=company.countryCompanyHeadId;
        this.company.postAddress=company.postAddress;
    }

    public void setVisa(@NotNull EditableProfile.Visa visa){
        this.visa=visa.visa;
    }
}

