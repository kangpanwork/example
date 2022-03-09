package com.sanri.test.sometest;

import java.util.ArrayList;
import java.util.List;

import com.sanri.test.reflect.RandomDataService;
import com.sanri.test.reflect.beans.Course;
import com.sanri.test.reflect.beans.People;
import com.sanri.test.reflect.beans.Student;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;

public class XstreamMain {
	@Test
	public void testPopulate(){
		RandomDataService randomDataService = new RandomDataService();
		Object o = randomDataService.populateData(Student.class);
		System.out.println(o);
	}

	public static void main(String[] args) {
		RandomDataService randomDataService = new RandomDataService();
		List<People> populateBeans = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			populateBeans.add((People) randomDataService.populateData(People.class));
		}

		XStream xStream = new XStream();
		//别名
		xStream.alias("students", People.class);
		//把子元素变成父元素的属性
//		xStream.("sno", Student.class);
//		xStream.addImplicitCollection(String.class, "favorites");

		People student = populateBeans.get(0);
		List<String> favorite = student.getFavorite();favorite = new ArrayList<String>();
		String xml = xStream.toXML(student);
		System.out.println(xml);
		
//		XStream xStream =  new XStream(new DomDriver());
//		xStream.alias("Student", Student.class);
//		try {
//			String pkgPath = PathUtil.pkgPath("temp");
//			Object fromXML = xStream.fromXML(new FileReader(pkgPath+"/test.xml"));
//			System.out.println(fromXML);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}
}
