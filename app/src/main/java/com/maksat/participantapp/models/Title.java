package com.maksat.participantapp.models;

public class Title {
    public Integer id;
    public String nameRus;
    public String nameEng;
    public String nameAra;

    public Title(Integer id, String nameRus, String nameEng, String nameAra) {
        this.id = id;
        this.nameRus = nameRus;
        this.nameEng = nameEng;
        this.nameAra = nameAra;
    }
}
