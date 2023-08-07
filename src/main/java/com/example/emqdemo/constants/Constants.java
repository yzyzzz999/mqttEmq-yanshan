package com.example.emqdemo.constants;

public class Constants {

    //气体字典（类型-单位）
    public static final String GAS_DICT = "GasDict";

    //气体告警（气体-告警类型）
    public static final String ALARM_DICT = "AlarmDict";

    //一氧化碳
    public static final String GAS_CO = "CO";

    //硫化氢
    public static final String GAS_H2S = "H2S";

    //氧气
    public static final String GAS_O2 = "O2";

    //甲烷
    public static final String GAS_CH4 = "CH4";

    //一氧化碳
    public static final String UNIT_CO = "ppm";

    //硫化氢
    public static final String UNIT_H2S = "ppm";

    //氧气
    public static final String UNIT_O2 = "%VOL";

    //甲烷
    public static final String UNIT_CH4 = "%LEL";

    //一氧化碳告警
    public static final String ALARM_CO = "CO超标";

    //硫化氢告警
    public static final String ALARM_H2S = "H2S超标";

    //氧气告警
    public static final String ALARM_O2 = "02超低限";

    //甲烷告警
    public static final String ALARM_CH4 = "CH4超标";

    //mqttMessage 后缀
    public static final String MQTT_SUFFIX = " concentration";

    //告警状态 (正常)
    public static final String ALARM_NORMAL = "00";

    //告警状态 (超过低段报警值报警)
    public static final String ALARM_LOW = "01";

    //告警状态 (超过高段报警值报警)
    public static final String ALARM_HIGH = "02";
}
