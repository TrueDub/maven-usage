package com.castlemon.maven.domain;

import org.agileware.test.PropertiesTester;
import org.junit.Assert;
import org.junit.Test;

public class UsageTest {

	@Test
	public void test() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(Usage.class);
	}

	@Test
	public void testOther() {
		Assert.assertEquals(5, Usage.getCSVTitles().length);
		Usage usage = new Usage();
		usage.setGroupId("com.fred");
		usage.setArtifactId("bill");
		usage.setVersion("1.0");
		Assert.assertEquals(5, usage.getCSVString().length);
		String[] expectedString = { "com.fred", "bill", "1.0", null, null };
		Assert.assertArrayEquals(expectedString, usage.getCSVString());
		Assert.assertEquals("com.fred:bill:1.0", usage.getIdentifier());
	}

	@Test
	public void testCompareTo() {
		Usage usage = new Usage();
		usage.setGroupId("com.fred");
		usage.setArtifactId("bill");
		usage.setVersion("1.0");
		//
		Usage usageOther = new Usage();
		usageOther.setGroupId("com.fred");
		usageOther.setArtifactId("bill");
		usageOther.setVersion("1.0");
		// equal
		Assert.assertEquals(0, usage.compareTo(usageOther));
		// version
		usageOther.setVersion("2.0");
		Assert.assertEquals(-1, usage.compareTo(usageOther));
		// artifact
		usageOther.setArtifactId("george");
		Assert.assertEquals(-5, usage.compareTo(usageOther));
		// group
		usageOther.setGroupId("org.jim");
		Assert.assertEquals(-12, usage.compareTo(usageOther));
	}

	@Test
	public void testEquals() {
		Usage usage = new Usage();
		usage.setGroupId("com.fred");
		usage.setArtifactId("bill");
		usage.setVersion("1.0");
		//
		Usage usageOther = new Usage();
		usageOther.setGroupId("com.fred");
		usageOther.setArtifactId("bill");
		usageOther.setVersion("1.0");
		// equal
		Assert.assertTrue(usage.equals(usageOther));
		// version
		usageOther.setVersion("2.0");
		Assert.assertFalse(usage.equals(usageOther));
	}

	@Test
	public void testHashCode() {
		Usage usage = new Usage();
		usage.setGroupId("com.fred");
		usage.setArtifactId("bill");
		usage.setVersion("1.0");
		Assert.assertEquals(2038371419, usage.hashCode());
	}

}
