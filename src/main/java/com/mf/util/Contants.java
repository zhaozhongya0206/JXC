package com.mf.util;

/**
 * @ClassName:Contants  
 * @Description:常量使用定义
 * @author zzy
 * @date 2018年12月10日
 * @version V1.0
 */
public class Contants {
	
	public static final String FFMPEG_PATH = "D:\\ffmpeg\\bin\\ffmpeg.exe"; // ffmpeg工具安装位置
    public static final String MENCODER_PATH = "D:\\ffmpeg\\bin\\mencoder";     // mencoder工具安装的位置

    public static final String VIDEO_FOLDER_TEMP = "D:\\test\\projectpicture\\websiteimages\\temp\\";  //临时路径  需要被转换格式的视频目录
    public static final String IMAGE_REAL_PATH = "D:\\test\\projectpicture\\websiteimages\\finshimg\\"; // 截图的存放目录
    public static final String TARGET_FOLDER = "src/main/webapp/camera/finshvideo/"; // 转码后视频保存的目录
    
    
    public static final String TARGET_EXTENSION_MP4 = ".mp4";//设置转换的格式
    
    public static final String EXCEPTION_OUT_VIDEO_PATH = "/data/exception/video/"; //异常视频copy存储路径
    public static final String EXCEPTION_OUT_IMG_PATH = "/data/exception/img/"; //异常图片copy存储路径
    
    public static final String PLAY_VIDEO_PATH = "/index/video/"; //首页视频播放路径存放
    
}