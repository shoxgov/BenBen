package com.benben.bb.bean;

public class CategoryProfessSubclassModel {
    private String name;

    public CategoryProfessSubclassModel() {
        super();
    }

    public CategoryProfessSubclassModel(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "ProvinceModel [name=" + name + "]";
    }

}
