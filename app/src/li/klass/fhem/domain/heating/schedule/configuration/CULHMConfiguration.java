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

package li.klass.fhem.domain.heating.schedule.configuration;

import li.klass.fhem.domain.CULHMDevice;
import li.klass.fhem.domain.heating.schedule.DayProfile;
import li.klass.fhem.domain.heating.schedule.WeekProfile;
import li.klass.fhem.domain.heating.schedule.interval.FilledTemperatureInterval;
import li.klass.fhem.util.DayUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CULHMConfiguration extends HeatingConfiguration<FilledTemperatureInterval, CULHMDevice, CULHMConfiguration> {

    public static final int MAXIMUM_NUMBER_OF_HEATING_INTERVALS = 24;

    public CULHMConfiguration() {
        super("", MAXIMUM_NUMBER_OF_HEATING_INTERVALS, NumberOfIntervalsType.DYNAMIC);
    }

    @Override
    public void readNode(WeekProfile<FilledTemperatureInterval, CULHMConfiguration, CULHMDevice> weekProfile, String key, String value) {
        if (!key.startsWith("TEMPLIST")) return;

        String shortName = key.substring("TEMPLIST".length());
        DayUtil.Day day = DayUtil.getDayForShortName(shortName);

        String[] parts = value.split(" ");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            int index = i / 2;

            FilledTemperatureInterval interval = getOrCreateInterval(weekProfile, day, index);
            if (i % 2 == 0) { // time desc
                interval.setSwitchTime(part);
                if (part.equals("24:00")) {
                    interval.setTimeFixed(true);
                }
            } else { //temperature desc
                interval.setTemperature(Double.valueOf(part));
            }
        }
    }

    @Override
    public FilledTemperatureInterval createHeatingInterval() {
        return new FilledTemperatureInterval();
    }

    @Override
    public DayProfile<FilledTemperatureInterval, CULHMDevice, CULHMConfiguration> createDayProfileFor(DayUtil.Day day, CULHMConfiguration configuration) {
        return new DayProfile<FilledTemperatureInterval, CULHMDevice, CULHMConfiguration>(day, configuration);
    }

    @Override
    public List<String> generateScheduleCommands(CULHMDevice device, WeekProfile<FilledTemperatureInterval, CULHMConfiguration, CULHMDevice> weekProfile) {
        List<String> result = new ArrayList<String>();
        for (DayProfile<FilledTemperatureInterval, CULHMDevice, CULHMConfiguration> dayProfile : weekProfile.getChangedDayProfiles()) {
            result.add(generateCommandFor(device, dayProfile));
        }

        return result;
    }

    public String generateCommandFor(CULHMDevice device, DayProfile<FilledTemperatureInterval, CULHMDevice, CULHMConfiguration> dayProfile) {
        StringBuilder command = new StringBuilder();

        List<FilledTemperatureInterval> heatingIntervals = new ArrayList<FilledTemperatureInterval>(dayProfile.getHeatingIntervals());
        Collections.sort(heatingIntervals);

        for (int i = 0; i < dayProfile.getNumberOfHeatingIntervals(); i++) {
            FilledTemperatureInterval interval = heatingIntervals.get(i);
            if (i != 0) {
                command.append(" ");
            }
            command.append(interval.getChangedSwitchTime()).append(" ").append(interval.getChangedTemperature());
        }

        String shortName = DayUtil.getShortNameFor(dayProfile.getDay());
        char firstChar = shortName.charAt(0);
        shortName = ((char) (firstChar - 'a' + 'A')) + shortName.substring(1);

        return "set " + device.getName() + " tempList" + shortName + " " + command.toString();
    }
}
