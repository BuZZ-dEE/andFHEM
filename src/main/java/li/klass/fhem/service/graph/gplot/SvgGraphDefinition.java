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

package li.klass.fhem.service.graph.gplot;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import li.klass.fhem.domain.log.LogDevice;

public class SvgGraphDefinition implements Serializable {
    private final String name;
    private final GPlotDefinition gPlotDefinition;
    private final LogDevice logDevice;
    private final List<String> labels;
    private final String title;

    private static final Pattern LABEL_PATTERN = Pattern.compile("<L([0-9]+)>");

    public SvgGraphDefinition(String name, GPlotDefinition gPlotDefinition, LogDevice logDevice, List<String> labels, String title) {
        this.name = name;
        this.gPlotDefinition = gPlotDefinition;
        this.logDevice = logDevice;
        this.labels = labels;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public GPlotDefinition getPlotDefinition() {
        return gPlotDefinition;
    }

    public LogDevice getLogDevice() {
        return logDevice;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SvgGraphDefinition that = (SvgGraphDefinition) o;

        return !(name != null ? !name.equals(that.name) : that.name != null)
                && !(gPlotDefinition != null ? !gPlotDefinition.equals(that.gPlotDefinition) : that.gPlotDefinition != null)
                && !(logDevice != null ? !logDevice.equals(that.logDevice) : that.logDevice != null)
                && !(labels != null ? !labels.equals(that.labels) : that.labels != null)
                && !(title != null ? !title.equals(that.title) : that.title != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (gPlotDefinition != null ? gPlotDefinition.hashCode() : 0);
        result = 31 * result + (logDevice != null ? logDevice.hashCode() : 0);
        result = 31 * result + (labels != null ? labels.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SvgGraphDefinition{" +
                "name='" + name + '\'' +
                ", gPlotDefinition=" + gPlotDefinition +
                ", logDevice=" + logDevice +
                ", labels=" + labels +
                ", title='" + title + '\'' +
                '}';
    }

    public String formatText(String toFormat) {
        String result = toFormat.replaceAll("<TL>", title);
        Matcher matcher = LABEL_PATTERN.matcher(toFormat);
        while (matcher.find()) {
            int labelIndex = Integer.parseInt(matcher.group(1)) - 1;
            String replaceBy = labelIndex < labels.size() ? labels.get(labelIndex).trim() : "";
            result = result.replaceAll(matcher.group(0), replaceBy);
        }
        return result;
    }
}
