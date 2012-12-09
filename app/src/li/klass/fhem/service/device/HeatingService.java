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

package li.klass.fhem.service.device;

import android.util.Log;
import li.klass.fhem.domain.heating.*;
import li.klass.fhem.service.CommandExecutionService;
import li.klass.fhem.util.ArrayUtil;

public class HeatingService {

    public static final HeatingService INSTANCE = new HeatingService();

    private static final String SET_COMMAND = "set %s %s %s";
    public static final String TAG = HeatingService.class.getName();

    private HeatingService() {
    }

    /**
     * Sets the desired temperature. The action will only be executed if the new desired temperature is different to
     * the already set one.
     * @param device concerned device
     * @param desiredTemperatureToSet new desired temperature value
     */
    public void setDesiredTemperature(DesiredTempDevice device, double desiredTemperatureToSet) {
        String command = String.format(SET_COMMAND, device.getName(), device.getDesiredTempCommandFieldName(), desiredTemperatureToSet);
        if (desiredTemperatureToSet != device.getDesiredTemp()) {
            CommandExecutionService.INSTANCE.executeSafely(command);
            device.setDesiredTemp(desiredTemperatureToSet);
        }
    }

    /**
     * Sets the mode attribute of a given HeatingModeDevice device. The action will only be executed if the new mode is different to
     * the already set one.
     * @param device concerned device
     * @param mode new mode to set.
     */
    @SuppressWarnings("unchecked")
    public <MODE extends Enum<MODE>, D extends HeatingModeDevice<MODE>> void setMode(D device, MODE mode) {
        if (mode == device.getHeatingMode()) {
            Log.e(TAG, "won't change heating mode, as it is already set!");
            return;
        }

        if (ArrayUtil.contains(device.getIgnoredHeatingModes(), mode)) {
            Log.e(TAG, "won't send heating mode, as it is ignored: " + mode.name());
            device.setHeatingMode(mode);
            return;
        }

        Log.e(TAG, "changing mode for device " + device.getName() +
                " from " + device.getHeatingMode() + " to " + mode);

        String command = String.format(device.getName(), device.getHeatingModeCommandField(), mode.name().toLowerCase());
        CommandExecutionService.INSTANCE.executeSafely(command);
        device.setHeatingMode(mode);
    }


    /**
     * Sets the window open temperature. The action will only be executed if the new window open temperature is
     * different to the already set one.
     * @param device concerned device
     * @param windowOpenTemp new window open temperature to set
     */
    public void setWindowOpenTemp(WindowOpenTempDevice device, double windowOpenTemp) {
        if (device.getWindowOpenTemp() == windowOpenTemp) {
            return;
        }

        Log.e(TAG, "set window open temp of device " + device.getName() + " to " + windowOpenTemp);
        String command = String.format(SET_COMMAND, device.getName(), device.getWindowOpenTempCommandFieldName(), windowOpenTemp);
        CommandExecutionService.INSTANCE.executeSafely(command);
        device.setWindowOpenTemp(windowOpenTemp);
    }

    public void setComfortTemperature(ComfortTempDevice device, double temperature) {
        if (device.getComfortTemp() == temperature) {
            return;
        }

        Log.e(TAG, "set comfort temp of device " + device.getName() + " to " + temperature);
        String command = String.format(SET_COMMAND, device.getName(), device.getComfortTempCommandFieldName(), temperature);
        CommandExecutionService.INSTANCE.executeSafely(command);
        device.setComfortTemp(temperature);
    }

    public void setEcoTemperature(EcoTempDevice device, double temperature) {
        if (device.getEcoTemp() == temperature) {
            return;
        }

        Log.e(TAG, "set eco temp of device " + device.getName() + " to " + temperature);
        String command = String.format(SET_COMMAND, device.getName(), device.getEcoTempCommandFieldName(), temperature);
        CommandExecutionService.INSTANCE.executeSafely(command);
        device.setEcoTemp(temperature);
    }
}
