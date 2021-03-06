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

package li.klass.fhem.behavior.dim;

import android.content.Context;

import com.google.common.base.Optional;

import li.klass.fhem.adapter.uiservice.StateUiService;
import li.klass.fhem.domain.core.FhemDevice;
import li.klass.fhem.domain.setlist.SetList;
import li.klass.fhem.domain.setlist.SetListSliderValue;
import li.klass.fhem.service.room.xmllist.DeviceNode;

public class DimmableBehavior {

    private final FhemDevice fhemDevice;
    private DimmableTypeBehavior behavior;

    public DimmableBehavior(FhemDevice fhemDevice, DimmableTypeBehavior dimmableTypeBehavior) {
        this.behavior = dimmableTypeBehavior;
        this.fhemDevice = fhemDevice;
    }

    public float getCurrentDimPosition() {
        return behavior.getCurrentDimPosition(fhemDevice);
    }

    public float getDimUpPosition() {
        float currentPosition = getCurrentDimPosition();
        if (currentPosition + getDimStep() > behavior.getDimUpperBound()) {
            return behavior.getDimUpperBound();
        }
        return currentPosition + getDimStep();
    }

    public float getDimDownPosition() {
        float currentPosition = getCurrentDimPosition();
        if (currentPosition - getDimStep() < behavior.getDimLowerBound()) {
            return behavior.getDimLowerBound();
        }
        return currentPosition - getDimStep();
    }


    public String getDimStateForPosition(float position) {
        return behavior.getDimStateForPosition(fhemDevice, position);
    }

    public float getPositionForDimState(String dimState) {
        return behavior.getPositionForDimState(dimState);
    }

    public float getDimLowerBound() {
        return behavior.getDimLowerBound();
    }

    public float getDimUpperBound() {
        return behavior.getDimUpperBound();
    }

    public float getDimStep() {
        return behavior.getDimStep();
    }

    public void switchTo(StateUiService stateUiService, Context context, float state) {
        behavior.switchTo(stateUiService, context, fhemDevice, state);
    }

    public FhemDevice getFhemDevice() {
        return fhemDevice;
    }

    DimmableTypeBehavior getBehavior() {
        return behavior;
    }

    public static Optional<DimmableBehavior> behaviorFor(FhemDevice fhemDevice) {
        SetList setList = fhemDevice.getSetList();

        Optional<DiscreteDimmableBehavior> discrete = DiscreteDimmableBehavior.behaviorFor(setList);
        if (discrete.isPresent()) {
            return Optional.of(new DimmableBehavior(fhemDevice, discrete.get()));
        }

        Optional<ContinuousDimmableBehavior> continuous = ContinuousDimmableBehavior.behaviorFor(setList);
        if (continuous.isPresent()) {
            DimmableTypeBehavior behavior = continuous.get();
            return Optional.of(new DimmableBehavior(fhemDevice, behavior));
        }

        return Optional.absent();
    }

    public static Optional<DimmableBehavior> continuousBehaviorFor(FhemDevice device, String attribute) {
        if (!device.getSetList().contains(attribute)) {
            return Optional.absent();
        }
        SetListSliderValue setListSliderValue = (SetListSliderValue) device.getSetList().get(attribute);
        return Optional.of(new DimmableBehavior(device, new ContinuousDimmableBehavior(setListSliderValue, attribute)));
    }


    public static boolean isDimDisabled(FhemDevice device) {
        DeviceNode disableDim = device.getXmlListDevice().getAttributes().get("disableDim");
        return disableDim != null && "true".equalsIgnoreCase(disableDim.getValue());
    }
}
