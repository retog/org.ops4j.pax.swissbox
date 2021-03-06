/*
 * Copyright 2009 Toni Menzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.swissbox.samples.tinybundles.dp;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import org.ops4j.pax.swissbox.samples.tinybundles.DPTestingHelper;
import static org.ops4j.pax.swissbox.tinybundles.dp.DP.*;
import org.ops4j.store.Handle;
import org.ops4j.store.Store;
import org.ops4j.store.StoreFactory;

/**
 * @author Toni Menzel (tonit)
 * @since Apr 9, 2009
 */
public class DeploymentPackageTest
{

    @Before
    public void prepare()
    {
        System.setProperty( "java.protocol.handler.pkgs", "org.ops4j.pax.url" );

    }

    @Test
    public void testMinimalCreate()
        throws IOException
    {
        Store<InputStream> cache = StoreFactory.defaultStore();

        Handle original = cache.store( newDeploymentPackage()
            .setSymbolicName( "MyFirstDeploymentPackage" )
            .setVersion( "1.0.0" )
            .build()
        );

        DPTestingHelper.verifyStandardHeaders( cache.load( original ), false );

        DPTestingHelper.verifyNonMissing( cache.load( original ) );
        DPTestingHelper.verifyBundleContents( cache.load( original ) );
    }

    @Test
    public void testOneCreate()
        throws IOException
    {
        Store<InputStream> cache = StoreFactory.defaultStore();

        Handle original = cache.store( newDeploymentPackage()
            .setSymbolicName( "MyFirstDeploymentPackage" )
            .setVersion( "1.0.0" )
            .setBundle( "t1", "mvn:org.ops4j.pax.url/pax-url-mvn/1.1.0" )
            .build()
        );

        DPTestingHelper.verifyStandardHeaders( cache.load( original ), false );

        DPTestingHelper.verifyNonMissing( cache.load( original ), "t1" );
        DPTestingHelper.verifyBundleContents( cache.load( original ), "t1" );
    }

    @Test
    public void createDPAndDeleteItemInFixPack()
        throws IOException
    {
        Store<InputStream> cache = StoreFactory.defaultStore();

        Handle original = cache.store( newDeploymentPackage()
            .setSymbolicName( "MyFirstDeploymentPackage" )
            .setVersion( "1.0.0" )
            .setResource( "log4j.properties", getClass().getResourceAsStream( "/log4j.properties" ) )
            .setBundle( "t1", "mvn:org.ops4j.pax.url/pax-url-mvn/1.1.0" )
            .setBundle( "t2", "mvn:org.ops4j.pax.url/pax-url-wrap/1.1.0" )
            .build()
        );

        DPTestingHelper.verifyStandardHeaders( cache.load( original ), false );

        DPTestingHelper.verifyNonMissing( cache.load( original ), "log4j.properties", "t1", "t2" );
        DPTestingHelper.verifyBundleContents( cache.load( original ), "t1", "t2" );

        Handle fix = cache.store( newFixPackage( cache.load( original ) ).remove( "t1" ).build() );

        DPTestingHelper.verifyStandardHeaders( cache.load( fix ), true );

        DPTestingHelper.verifyNonMissing( cache.load( fix ) );
        DPTestingHelper.verifyMissing( cache.load( fix ), "log4j.properties", "t2" );
        DPTestingHelper.verifyBundleContents( cache.load( fix ), "t2" );
    }

    @Test
    public void createDPAndChangeItemInFixPack()
        throws IOException
    {
        Store<InputStream> cache = StoreFactory.defaultStore();

        Handle target = cache.store( newDeploymentPackage()
            .setSymbolicName( "MyFirstDeploymentPackage" )
            .setVersion( "1.0.0" )
            .setResource( "log4j.properties", getClass().getResourceAsStream( "/log4j.properties" ) )
            .setBundle( "t1", "mvn:org.ops4j.pax.url/pax-url-mvn/1.1.0" )
            .setBundle( "t2", "mvn:org.ops4j.pax.url/pax-url-wrap/1.1.0" )
            .setBundle( "t3", "mvn:org.ops4j.pax.url/pax-url-link/1.1.0" )

            .build()
        );
        DPTestingHelper.verifyStandardHeaders( cache.load( target ), false );
        DPTestingHelper.verifyNonMissing( cache.load( target ), "log4j.properties", "t1", "t2", "t3" );
        DPTestingHelper.verifyBundleContents( cache.load( target ), "t1", "t2", "t3" );

        Handle fix = cache.store(
            newFixPackage( cache.load( target ) )
                .setBundle( "t2", "mvn:org.ops4j.pax.url/pax-url-war/1.1.0" ) // replace wrap by war ! Fix!
                .remove( "t1" )
                .build()
        );

        DPTestingHelper.verifyStandardHeaders( cache.load( fix ), true );
        DPTestingHelper.verifyMissing( cache.load( fix ), "t3", "log4j.properties" );
        DPTestingHelper.verifyNonMissing( cache.load( fix ), "t2" );
        DPTestingHelper.verifyBundleContents( cache.load( fix ), "t2" );
    }


}
