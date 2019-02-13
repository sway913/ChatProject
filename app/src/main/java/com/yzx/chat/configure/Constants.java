package com.yzx.chat.configure;

import com.yzx.chat.tool.DirectoryHelper;

/**
 * Created by yzx on 2017年03月12日
 * 当你将信心放在自己身上时，你将永远充满力量
 */
public class Constants {


    //    public static final String URL_API_BASE = "http://192.168.16.103:3000/v1/api/";
    public static final String URL_API_BASE = "https://www.orline.cn/superim/v1/api/";

    public static final String URL_MAP_IMAGE_FORMAT = "http://restapi.amap.com/v3/staticmap?markers=-1,http://www.orline.cn/superim/ic_location_flag.png,0:%f,%f&size=400*160&zoom=15&key=ded050da042a8b77a62c6cda02cc330a";

    public static final String SERVER_PUBLIC_KEY = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEOs56iDUHeQ5tcdFlRKzHKvSdR8Y/pFJMbYlZWF90dqGXVFfCf0/3ZiKYrKAeOvR3HqXcxMvQudLn+Y99X0FMQw==";


    public static final String DATABASE_NAME = "dffdkk.db";
    public static final int DATABASE_VERSION = 1;

    public static final String RSA_KEY_ALIAS = "RSA_SecretKey";
    public static final String AES_KEY_ALIAS = "AES_SecretKey";
    public static final String TOKEN_ALIAS = "Token";
    public static final String DEVICE_ID_ALIAS = "DeviceID";


    public static final int CHAT_MESSAGE_PAGE_SIZE = 16;
    public static final int SEARCH_LOCATION_PAGE_SIZE = 16;

    public static final int MAX_ONCE_IMAGE_SEND_COUNT = 9;
    public static final int MAX_ONCE_FILE_SEND_COUNT = 3;
    public static final int MAX_FILE_SEND_SIZE = 50 * 1024 * 1024;

    public static final int MAX_VOICE_RECORDER_DURATION = 60 * 999;
    public static final int MIN_VOICE_RECORDER_DURATION = 800;

    public static final int MAX_VIDEO_RECORDER_DURATION = 10 * 1000 + 300;

    public static final String LOCATION_STYLE_FILE_PATH = DirectoryHelper.getPublicTempPath() + "map_style.data";
    public static final int LOCATION_DEFAULT_ZOOM = 15;
    public static final int LOCATION_INTERVAL = 2000;
   }
