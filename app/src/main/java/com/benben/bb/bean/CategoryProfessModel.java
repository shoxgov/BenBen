package com.benben.bb.bean;

import java.util.List;

public class CategoryProfessModel {
    private String name;
    private List<CategoryProfessSubclassModel> subclassList;

    public CategoryProfessModel() {
        super();
    }

    public CategoryProfessModel(String name, List<CategoryProfessSubclassModel> subclassList) {
        super();
        this.name = name;
        this.subclassList = subclassList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryProfessSubclassModel> getSubclassList() {
        return subclassList;
    }

    public void setSubclassList(List<CategoryProfessSubclassModel> subclassList) {
        this.subclassList = subclassList;
    }

    @Override
    public String toString() {
        return "ProvinceModel [name=" + name + ", cityList=" + subclassList + "]";
    }

}
