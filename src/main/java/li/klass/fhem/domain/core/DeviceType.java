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

package li.klass.fhem.domain.core;

import li.klass.fhem.adapter.devices.CULHMAdapter;
import li.klass.fhem.adapter.devices.DmxAdapter;
import li.klass.fhem.adapter.devices.DummyAdapter;
import li.klass.fhem.adapter.devices.EnOceanAdapter;
import li.klass.fhem.adapter.devices.EnigmaDeviceAdapter;
import li.klass.fhem.adapter.devices.FHTAdapter;
import li.klass.fhem.adapter.devices.FS20ZDRDeviceAdapter;
import li.klass.fhem.adapter.devices.FloorplanAdapter;
import li.klass.fhem.adapter.devices.GCMSendDeviceAdapter;
import li.klass.fhem.adapter.devices.HueDeviceAdapter;
import li.klass.fhem.adapter.devices.LightSceneAdapter;
import li.klass.fhem.adapter.devices.MaxAdapter;
import li.klass.fhem.adapter.devices.MiLightDeviceAdapter;
import li.klass.fhem.adapter.devices.OnkyoAvrDeviceAdapter;
import li.klass.fhem.adapter.devices.OwSwitchDeviceAdapter;
import li.klass.fhem.adapter.devices.PCA9532DeviceAdapter;
import li.klass.fhem.adapter.devices.PCF8574DeviceAdapter;
import li.klass.fhem.adapter.devices.PidAdapter;
import li.klass.fhem.adapter.devices.PioneerAvrDeviceAdapter;
import li.klass.fhem.adapter.devices.PioneerAvrZoneDeviceAdapter;
import li.klass.fhem.adapter.devices.ReadingsProxyDeviceAdapter;
import li.klass.fhem.adapter.devices.RemoteControlAdapter;
import li.klass.fhem.adapter.devices.STVDeviceAdapter;
import li.klass.fhem.adapter.devices.SonosPlayerAdapter;
import li.klass.fhem.adapter.devices.SwapDeviceAdapter;
import li.klass.fhem.adapter.devices.SwitchActionRowAdapter;
import li.klass.fhem.adapter.devices.ThresholdAdapter;
import li.klass.fhem.adapter.devices.ToggleableAdapterWithSwitchActionRow;
import li.klass.fhem.adapter.devices.UniRollAdapter;
import li.klass.fhem.adapter.devices.WOLAdapter;
import li.klass.fhem.adapter.devices.WeatherAdapter;
import li.klass.fhem.adapter.devices.WebLinkAdapter;
import li.klass.fhem.adapter.devices.WifiLightDeviceAdapter;
import li.klass.fhem.adapter.devices.YamahaAVRAdapter;
import li.klass.fhem.adapter.devices.core.DeviceAdapter;
import li.klass.fhem.adapter.devices.core.DimmableAdapter;
import li.klass.fhem.adapter.devices.core.GenericDeviceAdapter;
import li.klass.fhem.domain.*;

public enum DeviceType {

    KS300("KS300", KS300Device.class),
    WEATHER("Weather", WeatherDevice.class, new WeatherAdapter()),
    FLOORPLAN("FLOORPLAN", FloorplanDevice.class, new FloorplanAdapter(), DeviceVisibility.FHEMWEB_ONLY),
    FHT("FHT", FHTDevice.class, new FHTAdapter()),
    CUL_TX("CUL_TX", CULTXDevice.class),
    HMS("HMS", HMSDevice.class),
    MAX("MAX", MaxDevice.class, new MaxAdapter()),
    WOL("WOL", WOLDevice.class, new WOLAdapter()),
    IT("IT", IntertechnoDevice.class, new ToggleableAdapterWithSwitchActionRow<>(IntertechnoDevice.class)),
    OWTEMP("OWTEMP", OwtempDevice.class),
    CUL_FHTTK("CUL_FHTTK", CULFHTTKDevice.class),
    RFXX10REC("RFXX10REC", RFXX10RECDevice.class),
    OREGON("OREGON", OregonDevice.class),
    CUL_EM("CUL_EM", CULEMDevice.class),
    OWCOUNT("OWCOUNT", OwcountDevice.class),
    SIS_PMS("SIS_PMS", SISPMSDevice.class, new ToggleableAdapterWithSwitchActionRow<>(SISPMSDevice.class)),
    USBWX("USBWX", USBWXDevice.class),
    CUL_WS("CUL_WS", CULWSDevice.class),
    FS20("FS20", FS20Device.class, new DimmableAdapter<>(FS20Device.class)),
    FILE_LOG("FileLog", FileLogDevice.class),
    DB_LOG("DbLog", DbLogDevice.class),
    STATISTICS("statistics", StatisticsDevice.class),
    OWFS("OWFS", OWFSDevice.class),
    LGTV("LGTV", LGTVDevice.class),
    RFXCOM("RFXCOM", RFXCOMDevice.class),
    CUL_HM("CUL_HM", CULHMDevice.class, new CULHMAdapter()),
    WATCHDOG("watchdog", WatchdogDevice.class),
    HOLIDAY("HOL", HOLDevice.class, new ToggleableAdapterWithSwitchActionRow<>(HOLDevice.class)),
    PID("PID", PIDDevice.class, new PidAdapter(PIDDevice.class)),
    FHT8V("FHT8V", FHT8VDevice.class),
    TRX_WEATHER("TRX_WEATHER", TRXWeatherDevice.class),
    TRX_LIGHT("TRX_LIGHT", TRXLightDevice.class, new DimmableAdapter<>(TRXLightDevice.class)),
    TRX("TRX", TRXDevice.class),
    DUMMY("dummy", DummyDevice.class, new DummyAdapter()),
    STRUCTURE("structure", StructureDevice.class, new DimmableAdapter<>(StructureDevice.class)),
    TWILIGHT("Twilight", TwilightDevice.class),
    AT("at", AtDevice.class, null),
    EN_OCEAN("EnOcean", EnOceanDevice.class, new EnOceanAdapter()),
    EIB("EIB", EIBDevice.class, new DimmableAdapter<>(EIBDevice.class)),
    HCS("HCS", HCSDevice.class, new SwitchActionRowAdapter<>(HCSDevice.class)),
    OWTHERM("OWTHERM", OwthermDevice.class),
    OWDEVICE("OWDevice", OwDevice.class, new ToggleableAdapterWithSwitchActionRow<>(OwDevice.class)),
    UNIROLL("UNIRoll", UniRollDevice.class, new UniRollAdapter()),
    TRXSecurity("TRX_SECURITY", TRXSecurityDevice.class, new SwitchActionRowAdapter<>(TRXSecurityDevice.class)),
    PRESENCE("PRESENCE", PresenceDevice.class),
    EMWZ("EMWZ", EMWZDevice.class),
    FBDect("FBDECT", FBDectDevice.class, new ToggleableAdapterWithSwitchActionRow<>(FBDectDevice.class)),
    SONOS_PLAYER("SONOSPLAYER", SonosPlayerDevice.class, new SonosPlayerAdapter()),
    SONOS("SONOS", SonosDevice.class),
    GPIO4("GPIO4", GPIO4Device.class),
    FRMOUT("FRM_OUT", FRMOutDevice.class, new ToggleableAdapterWithSwitchActionRow<>(FRMOutDevice.class)),
    ESA2000("ESA2000", ESA2000Device.class),
    HUE("HUEDevice", HUEDevice.class, new HueDeviceAdapter()),
    YAMAHA_AVR("YAMAHA_AVR", YamahaAVRDevice.class, new YamahaAVRAdapter()),
    FRMIN("FRM_IN", FRMInDevice.class),
    GENSHELLSWITCH("GenShellSwitch", GenShellSwitchDevice.class, new ToggleableAdapterWithSwitchActionRow<>(GenShellSwitchDevice.class)),
    GCM_SEND("gcmsend", GCMSendDevice.class, new GCMSendDeviceAdapter()),
    ZWAVE("ZWave", ZWaveDevice.class, new DimmableAdapter<>(ZWaveDevice.class)),
    SWAP("SWAP", SWAPDevice.class, new SwapDeviceAdapter()),
    FB_CALLMONITOR("FB_CALLMONITOR", FBCallmonitorDevice.class),
    FS20_ZDR("fs20_zdr", FS20ZDRDevice.class, new FS20ZDRDeviceAdapter()),
    OPENWEATHERMAP("openweathermap", OpenWeatherMapDevice.class),
    PCA301("PCA301", PCA301Device.class, new ToggleableAdapterWithSwitchActionRow<>(PCA301Device.class)),
    REMOTECONTROL("remotecontrol", RemoteControlDevice.class, new RemoteControlAdapter(), DeviceVisibility.FHEMWEB_ONLY),
    RPI_GPIO("RPI_GPIO", RPIGPIODevice.class, new ToggleableAdapterWithSwitchActionRow<>(RPIGPIODevice.class)),
    READINGS_PROXY("readingsProxy", ReadingsProxyDevice.class, new ReadingsProxyDeviceAdapter()),
    LACROSSE("LaCrosse", LaCrosseDevice.class),
    WEB_LINK("weblink", WebLinkDevice.class, new WebLinkAdapter()),
    PILIGHT("pilight", PilightDevice.class, new ToggleableAdapterWithSwitchActionRow<>(PilightDevice.class)),
    OWSWITCH("OWSWITCH", OwSwitchDevice.class, new OwSwitchDeviceAdapter()),
    HM485("HM485", HM485Device.class, new DimmableAdapter<>(HM485Device.class)),
    LIGHT_SCENE("LightScene", LightSceneDevice.class, new LightSceneAdapter()),
    EPGM("EGPM", EGPMDevice.class, new ToggleableAdapterWithSwitchActionRow<>(EGPMDevice.class)),
    CM160("CM160", CM160Device.class),
    BMP180("I2C_BMP180", BMP180Device.class),
    SHT21("I2C_SHT21", SHT21Device.class),
    PCA9532("I2C_PCA9532", PCA9532Device.class, new PCA9532DeviceAdapter()),
    PCF8574("I2C_PCF8574", PCF8574Device.class, new PCF8574DeviceAdapter()),
    FHEMWEB("FHEMWEB", FHEMWEBDevice.class),
    THRESHOLD("THRESHOLD", ThresholdDevice.class, new ThresholdAdapter()),
    WIFILIGHT("WifiLight", WifiLightDevice.class, new WifiLightDeviceAdapter()),
    EC3000("EC3000", EC3000Device.class),
    WITHINGS("withings", WithingsDevice.class),
    DMX("DMXDevice", DMXDevice.class, new DmxAdapter()),
    X10("X10", X10Device.class, new ToggleableAdapterWithSwitchActionRow<>(X10Device.class)),
    NETATMO("netatmo", NetatmoDevice.class),
    ROOMMATE("ROOMMATE", RoommateDevice.class),
    SMLUSB("SMLUSB", SMLUSBDevice.class),
    SOMFY("SOMFY", SomfyDevice.class, new ToggleableAdapterWithSwitchActionRow<>(SomfyDevice.class)),
    ONKYO_AVR("ONKYO_AVR", OnkyoAvrDevice.class, new OnkyoAvrDeviceAdapter()),
    REVOLT("Revolt", RevoltDevice.class),
    ENIGMA2("ENIGMA2", EnigmaDevice.class, new EnigmaDeviceAdapter()),
    PIONEER("PIONEERAVR", PioneerAvrDevice.class, new PioneerAvrDeviceAdapter()),
    FHEMduino_Env("FHEMduino_Env", FHEMduinoEnvDevice.class),
    FHEMduino_PT2262("FHEMduino_PT2262", FHEMduinoPT2262Device.class, new ToggleableAdapterWithSwitchActionRow<>(FHEMduinoPT2262Device.class)),
    SOLARVIEW("SolarView", SolarViewDevice.class),
    EMCDDEVICE("ECMDDevice", EMCDDevice.class, new ToggleableAdapterWithSwitchActionRow<>(EMCDDevice.class)),
    MILIGHT("MilightDevice", MiLightDevice.class, new MiLightDeviceAdapter()),
    STV("STV", STVDevice.class, new STVDeviceAdapter()),
    CO20("CO20", CO20Device.class),
    PIONEERAVRZONE("PIONEERAVRZONE", PioneerAvrZoneDevice.class, new PioneerAvrZoneDeviceAdapter())
    ;

    private String xmllistTag;
    private Class<? extends FhemDevice> deviceClass;
    private DeviceAdapter<? extends FhemDevice<?>> adapter;
    private DeviceVisibility visibility = null;

    <T extends FhemDevice<T>> DeviceType(String xmllistTag, Class<T> deviceClass) {
        this(xmllistTag, deviceClass, new GenericDeviceAdapter<>(deviceClass));
    }

    DeviceType(String xmllistTag, Class<? extends FhemDevice> deviceClass, DeviceAdapter<? extends FhemDevice<?>> adapter) {
        this.xmllistTag = xmllistTag;
        this.deviceClass = deviceClass;
        this.adapter = adapter;
    }

    DeviceType(String xmllistTag, Class<? extends FhemDevice> deviceClass, DeviceAdapter<? extends FhemDevice<?>> adapter, DeviceVisibility visibility) {
        this(xmllistTag, deviceClass, adapter);
        this.visibility = visibility;
    }

    public static <T extends FhemDevice<T>> DeviceAdapter<T> getAdapterFor(T device) {
        DeviceType deviceType = getDeviceTypeFor(device);
        if (deviceType == null) {
            return null;
        } else {
            return deviceType.getAdapter();
        }
    }

    public static <T extends FhemDevice> DeviceType getDeviceTypeFor(T device) {
        if (device == null) return null;
        return getDeviceTypeFor(device.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T extends FhemDevice<T>> DeviceAdapter<T> getAdapter() {
        return (DeviceAdapter<T>) adapter;
    }

    public static <T extends FhemDevice> DeviceType getDeviceTypeFor(Class<T> clazz) {
        if (clazz == null) return null;

        DeviceType[] deviceTypes = DeviceType.values();
        for (DeviceType deviceType : deviceTypes) {
            if (deviceType.getDeviceClass().isAssignableFrom(clazz)) {
                return deviceType;
            }
        }
        return null;
    }

    public Class<? extends FhemDevice> getDeviceClass() {
        return deviceClass;
    }

    public String getXmllistTag() {
        return xmllistTag;
    }

    public DeviceVisibility getVisibility() {
        return visibility;
    }
}