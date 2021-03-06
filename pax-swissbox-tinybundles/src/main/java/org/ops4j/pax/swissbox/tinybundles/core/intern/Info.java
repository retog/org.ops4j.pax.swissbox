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
package org.ops4j.pax.swissbox.tinybundles.core.intern;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author Toni Menzel (tonit)
 * @since Apr 26, 2009
 */
public class Info
{

    private static String m_paxSwissboxTinyBundlesVersion;

    private static boolean m_paxSwissboxTinyBundlesSnapshotVersion;

    static
    {
        String paxSwissboxTinybundlesVersion = "";

        try
        {
            final InputStream is = Info.class.getClassLoader().getResourceAsStream(
                "META-INF/pax-swissbox-tinybundlesversion.properties"
            );
            if( is != null )
            {
                final Properties properties = new Properties();
                properties.load( is );
                paxSwissboxTinybundlesVersion = properties.getProperty( "pax.swissbox.tinybundles.version", "" ).trim();

            }
        }
        catch( Exception ignore )
        {
            // use default versions
        }
        m_paxSwissboxTinyBundlesVersion = paxSwissboxTinybundlesVersion;

        m_paxSwissboxTinyBundlesSnapshotVersion = paxSwissboxTinybundlesVersion.endsWith( "SNAPSHOT" );

    }

    public static String getPaxSwissboxTinybundlesVersion()
    {
        return m_paxSwissboxTinyBundlesVersion;
    }

    public static boolean isPaxSwissboxTinybundlesSnapshotVersion()
    {
        return m_paxSwissboxTinyBundlesSnapshotVersion;
    }
}
