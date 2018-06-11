package com.benben.bb.view.threeWheelView.service;


import com.benben.bb.bean.CategoryProfessModel;
import com.benben.bb.bean.CategoryProfessSubclassModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlParserCategoryHandler extends DefaultHandler {

	/**
	 * 存储所有的解析对象
	 */
	private List<CategoryProfessModel> professionList = new ArrayList<CategoryProfessModel>();

	public XmlParserCategoryHandler() {

	}

	public List<CategoryProfessModel> getDataList() {
		return professionList;
	}

	@Override
	public void startDocument() throws SAXException {
		// 当读到第一个开始标签的时候，会触发这个方法
	}

	CategoryProfessModel professionModel = new CategoryProfessModel();
	CategoryProfessSubclassModel subclassModel = new CategoryProfessSubclassModel();

	@Override
	public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
		// 当遇到开始标记的时候，调用这个方法
		if (qName.equals("profession")) {
			professionModel = new CategoryProfessModel();
			professionModel.setName(attributes.getValue(0));
			professionModel.setSubclassList(new ArrayList<CategoryProfessSubclassModel>());
		} else if (qName.equals("subcass")) {
			subclassModel = new CategoryProfessSubclassModel();
			subclassModel.setName(attributes.getValue(0));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("subcass")) {
			professionModel.getSubclassList().add(subclassModel);
		} else if (qName.equals("profession")) {
			professionList.add(professionModel);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}
