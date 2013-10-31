/*
 * AndFHEM - Open Source Android application to control a FHEM home automation
 * server.
 *
 * Copyright (c) 2011, Matthias Klass or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU GENERAL PUBLIC LICENSE, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU GENERAL PUBLIC LICENSE
 * for more details.
 *
 * You should have received a copy of the GNU GENERAL PUBLIC LICENSE
 * along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */

package li.klass.fhem.domain;

import li.klass.fhem.domain.core.DeviceXMLParsingBase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

public class OregonDeviceTest extends DeviceXMLParsingBase {
    @Test
    public void testForCorrectlySetAttributes() {
        OregonDevice device = getDefaultDevice();

        assertThat(device.getName(), is(DEFAULT_TEST_DEVICE_NAME));
        assertThat(device.getRoomConcatenated(), is(DEFAULT_TEST_ROOM_NAME));

        assertThat(device.getBattery(), is("90 (%)"));
        assertThat(device.getDewpoint(), is("4.3 (°C)"));
        assertThat(device.getForecast(), is("rain"));
        assertThat(device.getHumidity(), is("46 (%)"));
        assertThat(device.getPressure(), is("1000 (hPa)"));
        assertThat(device.getTemperature(), is("15.9 (°C)"));
        assertThat(device.getRainRate(), is("0 (mm/h)"));
        assertThat(device.getRainTotal(), is("976.2998 (l/m2)"));
        assertThat(device.getWindAvgSpeed(), is("0 (km/h)"));
        assertThat(device.getWindDirection(), is("245 SW"));
        assertThat(device.getWindSpeed(), is("0 (km/h)"));
        assertThat(device.getUvValue(), is("10"));
        assertThat(device.getUvRisk(), is("high"));
        assertThat(device.getState(), is("T: 15.9  H: 46"));

        assertThat(device.getAvailableTargetStates(), is(nullValue()));

        assertThat(device.getFileLog(), is(notNullValue()));
        assertThat(device.getDeviceCharts().size(), is(6));
    }

    @Override
    protected String getFileName() {
        return "oregon.xml";
    }
}
