/*
 * AndFHEM - Open Source Android application to control a FHEM home automation
 * server.
 *
 * Copyright (c) 2012, Matthias Klass or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU GENERAL PUBLICLICENSE, as published by the Free Software Foundation.
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
 */

package li.klass.fhem.service.intent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.ResultReceiver;
import li.klass.fhem.constants.Actions;
import li.klass.fhem.constants.BundleExtraKeys;
import li.klass.fhem.constants.ResultCodes;
import li.klass.fhem.service.CommandExecutionService;

public class FloorplanIntentService extends ConvenientIntentService {
    public FloorplanIntentService() {
        super(FloorplanIntentService.class.getName());
    }

    @Override
    protected STATE handleIntent(Intent intent, long updatePeriod, ResultReceiver resultReceiver) {
        String action = intent.getAction();

        if (action.equals(Actions.FLOORPLAN_IMAGE)) {
            String relativePath = intent.getStringExtra(BundleExtraKeys.FLOORPLAN_IMAGE_RELATIVE_PATH);
            Bitmap bitmap = CommandExecutionService.INSTANCE.getBitmap(relativePath);
            if (bitmap == null) {
                return STATE.ERROR;
            }
            sendSingleExtraResult(resultReceiver, ResultCodes.SUCCESS, BundleExtraKeys.FLOORPLAN_IMAGE, bitmap);
        }
        return STATE.ERROR;
    }
}
