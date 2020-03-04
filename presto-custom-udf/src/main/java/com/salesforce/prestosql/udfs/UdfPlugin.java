package com.salesforce.prestosql.udfs;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.salesforce.prestosql.udfs.scalar.CustomDateTimeFunctions;
import com.salesforce.prestosql.udfs.scalar.CustomStringFunctions;

import io.prestosql.spi.Plugin;

public class UdfPlugin implements Plugin {
	@Override
	public Set<Class<?>> getFunctions()
	{
		return ImmutableSet.<Class<?>>builder()
				.add(CustomDateTimeFunctions.class)
				.add(CustomStringFunctions.class)
				.build();
	}
}
