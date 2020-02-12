/**
 * *****************************************************************************
 *
 * <p>Design and Development by msg Applied Technology Research Copyright (c) 2019-2020 msg systems
 * ag (http://www.msg-systems.com/) All Rights Reserved.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * <p>****************************************************************************
 */
package com.graphqlio.server.server;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.graphqlio.gts.resolver.GtsResolverRegistry;
import com.graphqlio.server.graphql.GsGraphQLService;

/**
 * Main Server (Service) responsible to start GraphQL service Application needs inject service and
 * run start command
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */
public class GsServer {

  private GsGraphQLService gsGraphQLService;

  public GsServer(GsGraphQLService gsGraphQLService) {
    this.gsGraphQLService = gsGraphQLService;
  }

  public boolean start() {
    /// keys associated to a client connection are deleted if connection closes
    /// however there may be keys left from last session if application terminated unexpectedly
    /// therefore we clean up key value store when starting the server
    return gsGraphQLService.start();
  }

  public void stop() {
    gsGraphQLService.stop();
  }

  public void registerGraphQLResolver(GraphQLResolver resolver) {
    GtsResolverRegistry.registerGraphQLResolver(resolver);
  }

  public void deregisterGraphQLResolver(GraphQLResolver resolver) {
    GtsResolverRegistry.deregisterGraphQLResolver(resolver);
  }
}
