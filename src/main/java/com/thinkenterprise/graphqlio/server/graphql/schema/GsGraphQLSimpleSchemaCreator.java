/*******************************************************************************
 * *
 * **  Design and Development by msg Applied Technology Research
 * **  Copyright (c) 2019-2020 msg systems ag (http://www.msg-systems.com/)
 * **  All Rights Reserved.
 * ** 
 * **  Permission is hereby granted, free of charge, to any person obtaining
 * **  a copy of this software and associated documentation files (the
 * **  "Software"), to deal in the Software without restriction, including
 * **  without limitation the rights to use, copy, modify, merge, publish,
 * **  distribute, sublicense, and/or sell copies of the Software, and to
 * **  permit persons to whom the Software is furnished to do so, subject to
 * **  the following conditions:
 * **
 * **  The above copyright notice and this permission notice shall be included
 * **  in all copies or substantial portions of the Software.
 * **
 * **  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * **  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * **  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * **  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * **  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * **  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * **  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * *
 ******************************************************************************/
package com.thinkenterprise.graphqlio.server.graphql.schema;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.coxautodev.graphql.tools.SchemaParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;


@Component
public class GsGraphQLSimpleSchemaCreator extends GsGraphQLAbstractSchemaCreator {

	@Autowired(required = false)
	List<GraphQLResolver<?>> resolvers;
	
	@Override
	public GraphQLSchema create() {
		
		
		initScalarTypes();
		GraphQLScalarType[] recScalars = scalarTypes.toArray(new GraphQLScalarType[scalarTypes.size()]);
		
		// @Fixme : Some other parameter like Scalars, should be configured 
		graphQLSchema = SchemaParser.newParser()
		               							.files(getFilePathes())
		               							.scalars(recScalars)
		               						    .resolvers(resolvers)
		               						    .build()
		               						    .makeExecutableSchema();
			          
		
		return graphQLSchema;
		
	}

		
	protected String[] getFilePathes() {
		
		String[] files = null;
				
		Resource[] resources = getSchemaResources();
				
		files= new String[resources.length];
		
		for (int i = 0; i < resources.length; ++i ) {
			files[i]=resources[i].getFilename();
		}
		
		return files;
		
	}
	

}
