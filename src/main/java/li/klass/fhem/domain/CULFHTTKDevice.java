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

import android.content.Context;

import java.util.List;

import li.klass.fhem.R;
import li.klass.fhem.domain.core.ChartProvider;
import li.klass.fhem.domain.core.DeviceChart;
import li.klass.fhem.domain.core.DeviceFunctionality;
import li.klass.fhem.domain.core.FhemDevice;
import li.klass.fhem.domain.core.XmllistAttribute;
import li.klass.fhem.domain.genericview.OverviewViewSettings;
import li.klass.fhem.domain.genericview.ShowField;
import li.klass.fhem.resources.ResourceIdMapper;
import li.klass.fhem.service.graph.description.ChartSeriesDescription;
import li.klass.fhem.service.room.xmllist.DeviceNode;
import li.klass.fhem.util.DateFormatUtil;

import static li.klass.fhem.service.graph.description.SeriesType.WINDOW_OPEN;

@SuppressWarnings("unused")
@OverviewViewSettings(showState = true)
public class CULFHTTKDevice extends FhemDevice<CULFHTTKDevice> {
    private String lastWindowState;
    @XmllistAttribute("window")
    private String windowState = "???";
    @ShowField(description = ResourceIdMapper.lastStateChange, showInOverview = true)
    private String lastStateChangeTime;

    @XmllistAttribute("previous")
    public void setPrevious(String value, DeviceNode deviceNode) {
        lastWindowState = value;
        lastStateChangeTime = DateFormatUtil.formatTime(deviceNode.getMeasured());
    }

    @Override
    public void afterDeviceXMLRead(Context context, ChartProvider chartProvider) {
        String stateChangeText = "";
        if (getLastWindowState() != null) {
            stateChangeText += getLastWindowState() + " => ";
        }
        stateChangeText += getWindowState();
        setState(stateChangeText);
    }

    @Override
    public DeviceFunctionality getDeviceGroup() {
        return DeviceFunctionality.WINDOW;
    }

    public String getLastStateChangeTime() {
        return lastStateChangeTime;
    }

    public String getLastWindowState() {
        return lastWindowState;
    }

    public String getWindowState() {
        return windowState;
    }

    @Override
    public boolean isSensorDevice() {
        return true;
    }

    @Override
    protected void fillDeviceCharts(List<DeviceChart> chartSeries, Context context, ChartProvider chartProvider) {
        super.fillDeviceCharts(chartSeries, context, chartProvider);

        addDeviceChartIfNotNull(new DeviceChart(R.string.stateGraph,
                new ChartSeriesDescription.Builder()
                        .withColumnName(R.string.windowOpen, context)
                        .withFileLogSpec("3:Open|Closed::$fld[2]=~/Open.*/?1:0")
                        .withDbLogSpec("data:::$val=~s/(Open|Closed).*/$1eq\"Open\"?1:0/eg")
                        .withSeriesType(WINDOW_OPEN)
                        .withShowDiscreteValues(true)
                        .withYAxisMinMaxValue(getLogDevices().get(0).getYAxisMinMaxValueFor("state", 0, 0))
                        .build()
        ), getState());
    }
}
