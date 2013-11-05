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

import org.w3c.dom.NamedNodeMap;

import java.util.List;

import li.klass.fhem.R;
import li.klass.fhem.appwidget.annotation.ResourceIdMapper;
import li.klass.fhem.appwidget.annotation.SupportsWidget;
import li.klass.fhem.appwidget.annotation.WidgetMediumLine1;
import li.klass.fhem.appwidget.annotation.WidgetMediumLine2;
import li.klass.fhem.appwidget.annotation.WidgetMediumLine3;
import li.klass.fhem.appwidget.annotation.WidgetTemperatureAdditionalField;
import li.klass.fhem.appwidget.annotation.WidgetTemperatureField;
import li.klass.fhem.appwidget.view.widget.medium.MediumInformationWidgetView;
import li.klass.fhem.appwidget.view.widget.medium.TemperatureWidgetView;
import li.klass.fhem.domain.core.Device;
import li.klass.fhem.domain.core.DeviceChart;
import li.klass.fhem.domain.fht.FHTMode;
import li.klass.fhem.domain.genericview.ShowField;
import li.klass.fhem.domain.heating.DesiredTempDevice;
import li.klass.fhem.domain.heating.HeatingDevice;
import li.klass.fhem.domain.heating.TemperatureDevice;
import li.klass.fhem.domain.heating.WindowOpenTempDevice;
import li.klass.fhem.domain.heating.schedule.WeekProfile;
import li.klass.fhem.domain.heating.schedule.configuration.FHTConfiguration;
import li.klass.fhem.domain.heating.schedule.interval.FromToHeatingInterval;
import li.klass.fhem.service.graph.description.ChartSeriesDescription;
import li.klass.fhem.util.ValueDescriptionUtil;
import li.klass.fhem.util.ValueExtractUtil;
import li.klass.fhem.util.ValueUtil;

import static li.klass.fhem.service.graph.description.SeriesType.ACTUATOR;
import static li.klass.fhem.service.graph.description.SeriesType.DESIRED_TEMPERATURE;
import static li.klass.fhem.service.graph.description.SeriesType.TEMPERATURE;

@SupportsWidget({TemperatureWidgetView.class, MediumInformationWidgetView.class})
@SuppressWarnings("unused")
public class FHTDevice extends Device<FHTDevice> implements DesiredTempDevice,
        WindowOpenTempDevice, HeatingDevice<FHTMode, FHTConfiguration, FromToHeatingInterval, FHTDevice>, TemperatureDevice {
    public static double MAXIMUM_TEMPERATURE = 30.5;
    public static double MINIMUM_TEMPERATURE = 5.5;

    @ShowField(description = ResourceIdMapper.actuator, showInOverview = true)
    @WidgetTemperatureAdditionalField(description = ResourceIdMapper.actuator)
    @WidgetMediumLine3(description = ResourceIdMapper.actuator)
    private String actuator;

    private FHTMode heatingMode;

    @ShowField(description = ResourceIdMapper.desiredTemperature, showAfter = "temperature")
    @WidgetMediumLine2(description = ResourceIdMapper.desiredTemperature)
    private double desiredTemp = MINIMUM_TEMPERATURE;

    @ShowField(description = ResourceIdMapper.dayTemperature, showAfter = "desiredTemp")
    private double dayTemperature = MINIMUM_TEMPERATURE;

    @ShowField(description = ResourceIdMapper.nightTemperature, showAfter = "dayTemperature")
    private double nightTemperature = MINIMUM_TEMPERATURE;

    @ShowField(description = ResourceIdMapper.windowOpenTemp, showAfter = "nightTemperature")
    private double windowOpenTemp = MINIMUM_TEMPERATURE;

    @ShowField(description = ResourceIdMapper.warnings)
    private String warnings;

    @ShowField(description = ResourceIdMapper.temperature, showInOverview = true, showInFloorplan = true)
    @WidgetTemperatureField
    @WidgetMediumLine1
    private String temperature;

    @ShowField(description = ResourceIdMapper.battery)
    private String battery;

    private static final FHTConfiguration heatingConfiguration = new FHTConfiguration();
    private WeekProfile<FromToHeatingInterval, FHTConfiguration, FHTDevice> weekProfile = new WeekProfile<FromToHeatingInterval, FHTConfiguration, FHTDevice>(heatingConfiguration);

    @Override
    public void onChildItemRead(String tagName, String key, String value, NamedNodeMap nodeAttributes) {
        super.onChildItemRead(tagName, key, value, nodeAttributes);
        weekProfile.readNode(key, value);

        if (key.startsWith("ACTUATOR") && value != null && value.matches("[0-9]*[%]?")) {
            double percentage = ValueExtractUtil.extractLeadingDouble(value);
            actuator = ValueDescriptionUtil.appendPercent(percentage);
        }
    }

    @Override
    public void afterXMLRead() {
        super.afterXMLRead();
        weekProfile.afterXMLRead();
    }

    public void readBATTERY(String value) {
        battery = value;
    }

    public void readMEASURED_TEMP(String value) {
        temperature = ValueUtil.formatTemperature(value);
    }

    public void readWARNINGS(String value) {
        warnings = value;
    }

    public void readMODE(String value) {
        try {
            this.heatingMode = FHTMode.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.heatingMode = FHTMode.UNKNOWN;
        }
    }

    public void readDESIRED_TEMP(String value) {
        if (value.equalsIgnoreCase("off")) value = "5.5";
        if (value.equalsIgnoreCase("on")) value = "30.5";

        desiredTemp = ValueExtractUtil.extractLeadingDouble(value);
    }

    public void readDAY_TEMP(String value) {
        dayTemperature = ValueExtractUtil.extractLeadingDouble(value);
    }

    public void readNIGHT_TEMP(String value) {
        nightTemperature = ValueExtractUtil.extractLeadingDouble(value);
    }

    public void readWINDOWOPEN_TEMP(String value) {
        windowOpenTemp = ValueExtractUtil.extractLeadingDouble(value);
    }

    public String getActuator() {
        return actuator;
    }

    @Override
    public String toString() {
        return "FHTDevice{" +
                "actuator='" + actuator + '\'' +
                "} " + super.toString();
    }

    public String getTemperature() {
        return temperature;
    }

    public void setDesiredTemp(double desiredTemp) {
        this.desiredTemp = desiredTemp;
    }

    public String getDesiredTempDesc() {
        return ValueDescriptionUtil.desiredTemperatureToString(desiredTemp, MINIMUM_TEMPERATURE, MAXIMUM_TEMPERATURE);
    }

    @Override
    public String getDesiredTempCommandFieldName() {
        return "desired-temp";
    }

    public double getDesiredTemp() {
        return desiredTemp;
    }

    public String getDayTemperatureDesc() {
        return ValueDescriptionUtil.desiredTemperatureToString(dayTemperature, MINIMUM_TEMPERATURE, MAXIMUM_TEMPERATURE);
    }

    public double getDayTemperature() {
        return dayTemperature;
    }

    public void setDayTemperature(double dayTemperature) {
        this.dayTemperature = dayTemperature;
    }

    public String getNightTemperatureDesc() {
        return ValueDescriptionUtil.desiredTemperatureToString(nightTemperature, MINIMUM_TEMPERATURE, MAXIMUM_TEMPERATURE);
    }

    public double getNightTemperature() {
        return nightTemperature;
    }

    public void setNightTemperature(double nightTemperature) {
        this.nightTemperature = nightTemperature;
    }

    public String getWindowOpenTempDesc() {
        return ValueDescriptionUtil.desiredTemperatureToString(windowOpenTemp, MINIMUM_TEMPERATURE, MAXIMUM_TEMPERATURE);
    }

    @Override
    public String getWindowOpenTempCommandFieldName() {
        return "windowopen-temp";
    }

    public Double getWindowOpenTemp() {
        return windowOpenTemp;
    }

    public void setWindowOpenTemp(double windowOpenTemp) {
        this.windowOpenTemp = windowOpenTemp;
    }

    public String getWarnings() {
        return warnings;
    }

    public FHTMode getHeatingMode() {
        if (heatingMode == null) return FHTMode.UNKNOWN;
        return heatingMode;
    }

    @Override
    public FHTMode[] getIgnoredHeatingModes() {
        return new FHTMode[0];
    }

    @Override
    public FHTMode[] getHeatingModes() {
        return FHTMode.values();
    }

    @Override
    public String getHeatingModeCommandField() {
        return "mode";
    }

    @Override
    public WeekProfile<FromToHeatingInterval, FHTConfiguration, FHTDevice> getWeekProfile() {
        return weekProfile;
    }

    public void setHeatingMode(FHTMode heatingMode) {
        this.heatingMode = heatingMode;
    }

    public String getBattery() {
        return battery;
    }

    @Override
    protected void fillDeviceCharts(List<DeviceChart> chartSeries) {
        super.fillDeviceCharts(chartSeries);

        if (temperature != null && actuator != null) {
            addDeviceChartIfNotNull(new DeviceChart(R.string.temperatureActuatorGraph,
                    ChartSeriesDescription.getRegressionValuesInstance(R.string.temperature, "4:measured", TEMPERATURE),
                    ChartSeriesDescription.getDiscreteValuesInstance(R.string.desiredTemperature, "4:desired-temp", DESIRED_TEMPERATURE),
                    ChartSeriesDescription.getDiscreteValuesInstance(R.string.actuator, "4:actuator.*[0-9]+%:0:int", ACTUATOR)),
                    temperature, actuator);
        } else if (temperature == null && actuator != null) {
            addDeviceChartIfNotNull(new DeviceChart(R.string.actuatorGraph,
                    ChartSeriesDescription.getDiscreteValuesInstance(R.string.desiredTemperature, "4:desired-temp", DESIRED_TEMPERATURE),
                    ChartSeriesDescription.getDiscreteValuesInstance(R.string.actuator, "4:actuator.*[0-9]+%:0:int", ACTUATOR)),
                    actuator);
        }
    }
}
