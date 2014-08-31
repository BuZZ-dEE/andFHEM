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

package li.klass.fhem.appwidget.annotation;

import li.klass.fhem.R;

public enum ResourceIdMapper {
    currentUsage(R.string.currentUsage),
    hourUsage(R.string.hourUsage),
    dayUsage(R.string.dayUsage),
    dayLastUsage(R.string.dayLastUsage),
    monthUsage(R.string.monthUsage),
    yearUsage(R.string.yearUsage),
    state(R.string.state),
    lastStateChange(R.string.lastStateChange),
    measured(R.string.measured),
    desiredTemperature(R.string.desiredTemperature),
    temperature(R.string.temperature),
    temperatureMinimum(R.string.temperatureMinimum),
    temperatureMaximum(R.string.temperatureMaximum),
    sunset(R.string.sunset),
    sunrise(R.string.sunrise),
    actuator(R.string.actuator),
    humidity(R.string.humidity),
    model(R.string.model),
    commandAccepted(R.string.commandAccepted),
    rawValue(R.string.rawValue),
    maximumContent(R.string.maximumContent),
    fillPercentage(R.string.fillPercentage),
    conversion(R.string.conversion),
    battery(R.string.battery),
    batteryVoltage(R.string.batteryVoltage),
    dayTemperature(R.string.dayTemperature),
    nightTemperature(R.string.nightTemperature),
    windowOpenTemp(R.string.windowOpenTemp),
    ecoTemp(R.string.ecoTemperature),
    comfortTemp(R.string.comfortTemperature),
    warnings(R.string.warnings),
    wind(R.string.wind),
    rain(R.string.rain),
    avgDay(R.string.avgDay),
    avgMonth(R.string.avgMonth),
    minDay(R.string.minDay),
    minMonth(R.string.minMonth),
    maxDay(R.string.maxDay),
    maxMonth(R.string.maxMonth),
    isRaining(R.string.isRaining),
    power(R.string.power),
    audio(R.string.audio),
    input(R.string.input),
    forecast(R.string.forecast),
    dewpoint(R.string.dewpoint),
    pressure(R.string.pressure),
    pressureNN(R.string.pressureNN),
    rainRate(R.string.rainRate),
    rainTotal(R.string.rainTotal),
    windAvgSpeed(R.string.windAvgSpeed),
    windDirection(R.string.windDirection),
    windSpeed(R.string.windSpeed),
    uvValue(R.string.uvValue),
    uvRisk(R.string.uvRisk),
    counterA(R.string.counterA),
    counterB(R.string.counterB),
    present(R.string.present),
    delta(R.string.delta),
    lastState(R.string.lastState),
    currentSwitchDevice(R.string.currentSwitchDevice),
    currentSwitchTime(R.string.currentSwitchTime),
    lastSwitchTime(R.string.lastSwitchTime),
    nextSwitchTime(R.string.nextSwitchTime),
    type(R.string.type),
    windchill(R.string.windchill),
    twilight_next_event(R.string.twilight_next_event),
    twilight_next_event_time(R.string.twilight_next_event_time),
    twilight_sunrise(R.string.twilight_sunrise),
    twilight_sunrise_astronomical(R.string.twilight_sunrise_astronomical),
    twilight_sunrise_civil(R.string.twilight_sunrise_civil),
    twilight_sunrise_indoor(R.string.twilight_sunrise_indoor),
    twilight_sunrise_nautical(R.string.twilight_sunrise_nautical),
    twilight_sunrise_weather(R.string.twilight_sunrise_weather),
    twilight_sunset(R.string.twilight_sunset),
    twilight_sunset_astronomical(R.string.twilight_sunset_astronomical),
    twilight_sunset_civil(R.string.twilight_sunset_civil),
    twilight_sunset_indoor(R.string.twilight_sunset_indoor),
    twilight_sunset_nautical(R.string.twilight_sunset_nautical),
    twilight_sunset_weather(R.string.twilight_sunset_weather),
    twilight_light(R.string.twilight_light),
    ip(R.string.ip),
    mac(R.string.mac),
    definition(R.string.definition),
    brightness(R.string.brightness),
    motion(R.string.motion),
    mode(R.string.mode),
    thermostatThresholdOn(R.string.thermostatThresholdOn),
    thermostatThresholdOff(R.string.thermostatThresholdOff),
    valveThresholdOff(R.string.valveThresholdOff),
    valveThresholdOn(R.string.valveThresholdOn),
    ecoThresholdOn(R.string.ecoThresholdOn),
    ecoThresholdOff(R.string.ecoThresholdOff),
    excludedDevices(R.string.excludedDevices),
    idleDevices(R.string.idleDevices),
    demandDevices(R.string.demandDevices),
    price(R.string.price),
    energy(R.string.energy),
    cumulativeUsage(R.string.cumulativeUsage),
    voltage(R.string.voltage),
    current(R.string.current),
    musicMute(R.string.musicMute),
    musicRepeat(R.string.musicRepeat),
    musicShuffle(R.string.musicShuffle),
    musicVolume(R.string.musicVolume),
    musicAlbum(R.string.musicAlbum),
    musicSender(R.string.musicSender),
    musicTitle(R.string.musicTitle),
    musicDuration(R.string.musicDuration),
    musicTrackNumber(R.string.musicTrackNumber),
    musicInfo(R.string.musicInfo),
    saturation(R.string.saturation),
    hue(R.string.hue),
    color(R.string.color),
    alarm(R.string.alarm),
    apiKey(R.string.apiKey),
    callMonExternalNumber(R.string.callMonExternalNumber),
    callMonInternalNumber(R.string.callMonInternalNumber),
    callMonExternalName(R.string.callMonExternalName),
    callMonDuration(R.string.callMonDuration),
    callMonEvent(R.string.callMonEvent),
    energyPower(R.string.energy_power),
    energyConsumption(R.string.energy_consumption),
    energyCurrent(R.string.energy_current),
    channel(R.string.channel),
    currentTitle(R.string.currentTitle),
    shutterPosition(R.string.shutterPosition),
    room(R.string.rooms),
    deviceName(R.string.deviceName),
    level(R.string.level),
    currentVoltage(R.string.currentVoltage),
    sunshine(R.string.sunshine),
    cost(R.string.cost),
    co2(R.string.co2),
    hiddenRooms(R.string.hiddenRooms),
    hiddenGroups(R.string.hiddenGroups),
    sortRooms(R.string.sortRooms),
    pwm0(R.string.pwm0),
    pwm1(R.string.pwm1),
    fatFreeMass(R.string.fatFreeMass),
    fatMass(R.string.fatMass),
    fatRatio(R.string.fatRatio),
    heartPulse(R.string.heartPulse),
    height(R.string.height),
    weight(R.string.weight),
    noise(R.string.noise),
    location(R.string.location),
    mood(R.string.mood),

    blank(R.string.blank),
    none(-1);

    private int id;

    private ResourceIdMapper(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
