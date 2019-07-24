package com.mf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description:(2.转码和截图功能)
 * @see:接收Contants实体的路径
 * @author:zzy
 * @date:2018-7-15
 * @version:V1.0
 */
@Component
public class ConverVideo {

	private static final Logger log = LoggerFactory.getLogger(ConverVideo.class);

	private String filename; // 包括后缀名
	private String filerealname; // 文件名不包括后缀名
	private String targetExtensionMp4 = Contants.TARGET_EXTENSION_MP4;//".mp4" 设置转换的格式
	
	private String videoFolderTemp = Contants.VIDEO_FOLDER_TEMP; // 临时文件目录
	private String targetFolder = Contants.TARGET_FOLDER; // 转码后保存的目录
	
	private String ffmpegPath = Contants.FFMPEG_PATH; // ffmpeg.exe的目录
	private String mencoderPath = Contants.MENCODER_PATH; // mencoder的目录
	private String imageRealPath = Contants.IMAGE_REAL_PATH; // 截图的存放目录

	/**
	 * @Description:(1.转码和截图功能调用)
	 * @param:@param yuanPATH
	 * @return:void
	 * @author:Zoutao
	 * @date:2018-6-23
	 * @version:V1.0
	 */

	/* 本地测试专用--zoutao */
	public static void main(String[] args) {
		String yuanPATH = "D:/test.mp4"; // 本地源视频
		ConverVideo zout = new ConverVideo();
		boolean beginConver = zout.beginConver(yuanPATH);
		System.out.println(beginConver);
	}

	/**
	 * 转换视频格式
	 * 
	 * @param String
	 *            targetExtension 目标视频后缀名 .xxx
	 * @param boolean isDelSourseFile 转换完成后是否删除源文件
	 * @return
	 */
	public boolean beginConver(String sourceVideoPath) {
		File fi = new File(sourceVideoPath);
		filename = fi.getName();// 获取文件名+后缀名
		filerealname = filename.substring(0, filename.lastIndexOf(".")); // 获取不带后缀的文件名-后面加.toLowerCase()小写

		log.info("----接收到文件(" + sourceVideoPath + ")需要转换-------");
		// 检测本地是否存在
		if (checkfile(sourceVideoPath)) {
			log.info(sourceVideoPath + "========该文件存在哟 ");
			return false;
		}
		log.info("----开始转文件(" + sourceVideoPath + " 文件名称：" + filename + ")-------------------------- ");

		// 执行转码机制
		if (process(sourceVideoPath, targetExtensionMp4)) {
			log.info("视频转码结束，开始截图================= ");
			// 视频转码完成，调用截图功能--zoutao
			if (processImg(sourceVideoPath)) {
				log.info("截图成功！ ");
			} else {
				log.info("截图失败！ ");
			}

			String temppath = videoFolderTemp + filerealname + ".avi";
			File file2 = new File(temppath);
			if (file2.exists()) {
				log.info("删除临时文件：" + temppath);
				file2.delete();
			}

			sourceVideoPath = null;
			return true;
		} else {
			sourceVideoPath = null;
			return false;
		}
	}

	/**
	 * 检查文件是否存在-多处都有判断
	 * 
	 * @param path
	 * @return
	 */
	private boolean checkfile(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				log.error("checkfile 视频文件不存在=============" + path);
				return true;
			}
		} catch (Exception e) {
			log.error("checkfile 拒绝对文件进行读访问： ", e);
		}
		return false;
	}

	/**
	 * 视频截图功能
	 * 
	 * @param sourceVideoPath
	 *            需要被截图的视频路径（包含文件名和后缀名）
	 * @return
	 */
	public boolean processImg(String sourceVideoPath) {
		try {	
			// 先确保保存截图的文件夹存在
			File fileImage = new File(imageRealPath);
			if (!fileImage.exists()) {
				log.info("文件夹不存在，创建该文件夹 imageRealPath: " + imageRealPath);
				if(!fileImage.mkdirs()) {
					throw new Exception("创建文件失败：" + targetFolder);
				}
			}
			File fi = new File(sourceVideoPath);
			filename = fi.getName(); // 获取视频文件的名称。
			filerealname = filename.substring(0, filename.lastIndexOf(".")); // 获取视频名+不加后缀名
																				// 后面加.toLowerCase()转为小写
			List<String> commend = new ArrayList<String>();
			// 第一帧： 00:00:01
			// 截图命令：time ffmpeg -ss 00:00:01 -i test1.flv -f image2 -y test1.jpg
			commend.add(ffmpegPath); // 指定ffmpeg工具的路径
			commend.add("-ss");
			commend.add("00:00:01"); // 1是代表第1秒的时候截图
			commend.add("-i");
			commend.add(sourceVideoPath); // 截图的视频路径
			commend.add("-f");
			commend.add("image2");
			commend.add("-y");
			commend.add(imageRealPath + filerealname + ".jpg"); // 生成截图xxx.jpg
			// 打印截图命令--zoutao
			StringBuffer test = new StringBuffer();
			for (int i = 0; i < commend.size(); i++) {
				test.append(commend.get(i) + " ");
			}
			log.info("截图命令:" + test);

			// 转码后完成截图功能-还是得用线程来解决--zoutao
			// 调用线程处理命令
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process p = builder.start();

			// 获取进程的标准输入流
			final InputStream is1 = p.getInputStream();
			// 获取进程的错误流
			final InputStream is2 = p.getErrorStream();
			// 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
			new Thread() {
				public void run() {
					BufferedReader br = new BufferedReader(new InputStreamReader(is1));
					try {
						String lineB = null;
						while ((lineB = br.readLine()) != null) {
							if (lineB != null) {
								log.info(" processImg: " + lineB); //必须取走线程信息避免堵塞
							}
						}
					} catch (IOException e) {
						log.error("processAVI IOException: " + e.getMessage(), e);
						return;
					}
					// 关闭流
					finally {
						try {
							is1.close();
						} catch (IOException e) {
							log.error("processAVI IOException: " + e.getMessage(),e);
							return;
						}
					}
				}
			}.start();
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
					try {
						String lineC = null;
						while ((lineC = br2.readLine()) != null) {
							if (lineC != null) {
								log.info("processAVI: " + lineC); //必须取走线程信息避免堵塞
							}
						}
					} catch (IOException e) {
						log.error("processAVI IOException: " + e.getMessage(),e);
						return;
					}
					// 关闭流
					finally {
						try {
							is2.close();
						} catch (IOException e) {
							log.error("processAVI IOException: " + e.getMessage(),e);
							return;
						}
					}
				}
			}.start();
			// 等Mencoder进程转换结束，再调用ffmepg进程非常重要！！！
			p.waitFor();
			log.info("截图进程结束");
			return true;
		} catch (Exception e) {
			log.error("processImg exception:" + e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 实际转换视频格式的方法
	 * 
	 * @param targetExtension
	 *            目标视频后缀名
	 * @return
	 */
	private boolean process(String sourceVideoPath, String targetExtension) {
		// 先判断视频的类型-返回状态码
		int type = checkContentType(sourceVideoPath);
		boolean status = false;
		// 根据状态码处理
		if (type == 0) {
			log.info("ffmpeg可以转换,统一转为mp4文件");
			status = processVideoFormat(sourceVideoPath, targetExtension);// 可以指定转换为什么格式的视频
		} else if (type == 1) {
			// 如果type为1，将其他文件先转换为avi，然后在用ffmpeg转换为指定格式
			log.info("ffmpeg不可以转换,先调用mencoder转码avi");
			String avifilepath = processAVI(sourceVideoPath, type);
			log.info("process avifilepath： " + avifilepath);
			if (avifilepath == null) {
				// 转码失败--avi文件没有得到
				log.error("mencoder转码失败,未生成AVI文件");
				return false;
			} else {
				log.info("生成AVI文件成功,ffmpeg开始转码:");
				status = processVideoFormat(avifilepath, targetExtension);
			}
		}
		return status; // 执行完成返回布尔类型true
	}

	/**
	 * 检查文件类型
	 * 
	 * @return
	 */
	private int checkContentType(String sourceVideoPath) {
		// 取得视频后缀-
		String type = sourceVideoPath.substring(sourceVideoPath.lastIndexOf(".") + 1, sourceVideoPath.length()).toLowerCase();

		log.info("源视频类型为:" + type);
		// 如果是ffmpeg能解析的格式:(asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等)
		if (type.equals("avi")) {
			return 0;
		} else if (type.equals("mpg")) {
			return 0;
		} else if (type.equals("wmv")) {
			return 0;
		} else if (type.equals("3gp")) {
			return 0;
		} else if (type.equals("mov")) {
			return 0;
		} else if (type.equals("mp4")) {
			return 0;
		} else if (type.equals("asf")) {
			return 0;
		} else if (type.equals("asx")) {
			return 0;
		} else if (type.equals("flv")) {
			return 0;
		} else if (type.equals("mkv")) {
			return 0;
		}
		// 如果是ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
		// 就先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		else if (type.equals("wmv9")) {
			return 1;
		} else if (type.equals("rm")) {
			return 1;
		} else if (type.equals("rmvb")) {
			return 1;
		}
		log.info("上传视频格式异常");
		return 9;
	}

	/**
	 * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
	 * 可以先用（mencoder）转换为avi(ffmpeg能解析的)格式.再用ffmpeg解析为指定格式
	 * 
	 * @param type
	 * @return
	 */
	private String processAVI(String sourceVideoPath, int type) {
		try {
			File tempFile = new File(videoFolderTemp);
			if (!tempFile.exists()) {
				log.info("文件夹不存在，创建该文件夹: videoFolderTemp " + videoFolderTemp);
				if(!tempFile.mkdirs()) {
					throw new Exception("创建文件失败：" + videoFolderTemp);
				}
			}
			log.info("调用了mencoder.exe工具");
			List<String> commend = new ArrayList<String>();
			commend.add(mencoderPath); // 指定mencoder.exe工具的位置
			commend.add(sourceVideoPath); // 指定源视频的位置
			commend.add("-oac");
			commend.add("mp3lame"); // lavc 原mp3lame
			commend.add("-lameopts");
			commend.add("preset=64");
			commend.add("-ovc");
			commend.add("xvid"); // mpg4(xvid),AVC(h.264/x264),只有h264才是公认的MP4标准编码，如果ck播放不了，就来调整这里
			commend.add("-xvidencopts"); // xvidencopts或x264encopts
			commend.add("bitrate=600"); // 600或440
			commend.add("-of");
			commend.add("avi");
			commend.add("-o");
			commend.add(videoFolderTemp + filerealname + ".avi"); // 存放路径+名称，生成.avi视频
			// 打印出转换命令-zoutao
			StringBuffer test = new StringBuffer();
			for (int i = 0; i < commend.size(); i++) {
				test.append(commend.get(i) + " ");
			}
			log.info("mencoder输入的命令:" + test);
			
			// 调用线程命令启动转码
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process p = builder.start(); // 多线程处理加快速度-解决数据丢失
			// 获取进程的标准输入流
			final InputStream is1 = p.getInputStream();
			// 获取进程的错误流
			final InputStream is2 = p.getErrorStream();
			// 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
			new Thread() {
				public void run() {
					BufferedReader br = new BufferedReader(new InputStreamReader(is1));
					try {
						String lineB = null;
						while ((lineB = br.readLine()) != null) {
							if (lineB != null) {
								log.info("processAVI lineB: " + lineB); // 打印mencoder转换过程代码-可注释
							}
						}
					} catch (IOException e) {
						log.error("processAVI IOException: " + e.getMessage(), e);
						return;
					}// 关闭流
					finally {
						try {
							is1.close();
						} catch (IOException e) {
							log.error("processAVI IOException: " + e.getMessage(),e);
							return;
						}
					}
				}
			}.start();
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
					try {
						String lineC = null;
						while ((lineC = br2.readLine()) != null) {
							if (lineC != null) {
								log.info(lineC); // 打印mencoder转换过程代码
							}
						}
					} catch (IOException e) {
						log.error("processAVI IOException: " + e.getMessage(), e);
						return;
					}// 关闭流
					finally {
						try {
							is2.close();
						} catch (IOException e) {
							log.error("processAVI IOException: " + e.getMessage(),e);
							return;
						}
					}
				}
			}.start();

			// 等Mencoder进程转换结束，再调用ffmepg进程非常重要！！！
			p.waitFor();
			log.info("processAVI Mencoder进程结束");
			return videoFolderTemp + filerealname + ".avi"; // 返回转为AVI以后的视频地址
		} catch (Exception e) {
			log.error("processAVI exception: " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 转换为指定格式--zoutao ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	 * 
	 * @param oldfilepath
	 * @param targetExtension
	 *            目标格式后缀名 .xxx
	 * @param isDelSourseFile
	 *            转换完成后是否删除源文件
	 * @return
	 */
	private boolean processVideoFormat(String oldfilepath, String targetExtension) {
		try {
			log.info("processVideoFormat 调用了ffmpeg.exe工具");
			File targetFolderFile = new File(targetFolder);
			if (!targetFolderFile.exists()) {
				log.info("文件夹不存在，创建该文件夹: targetFolder " + targetFolder);
				if(!targetFolderFile.mkdirs()) {
					throw new Exception("创建文件失败：" + targetFolder);
				}
			}
			
			List<String> commend = new ArrayList<String>();
			commend.add(ffmpegPath); // ffmpeg.exe工具地址
			commend.add("-i");
			commend.add(oldfilepath); // 源视频路径
			commend.add("-vcodec");
			commend.add("h263"); //
			commend.add("-ab"); // 新增4条
			commend.add("128"); // 高品质:128 低品质:64
			commend.add("-acodec");
			commend.add("mp3"); // 音频编码器：原libmp3lame
			commend.add("-ac");
			commend.add("2"); // 原1
			commend.add("-ar");
			commend.add("22050"); // 音频采样率22.05kHz
			commend.add("-r");
			commend.add("29.97"); // 高品质:29.97 低品质:15
			commend.add("-c:v");
			commend.add("libx264"); // 视频编码器：视频是h.264编码格式
			commend.add("-strict");
			commend.add("-2");
			commend.add(targetFolder + filerealname + targetExtension); // //转码后的路径+名称，是指定后缀的视频
	
			// 打印命令--zoutao
			StringBuffer testStr = new StringBuffer();
			for (int i = 0; i < commend.size(); i++) {
				testStr.append(commend.get(i) + " ");
			}
			log.info("processVideoFormat ffmpeg输入的命令:" + testStr);

			// 多线程处理加快速度-解决rmvb数据丢失builder名称要相同
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			Process p = builder.start(); // 多线程处理加快速度-解决数据丢失

			final InputStream is1 = p.getInputStream();
			final InputStream is2 = p.getErrorStream();
			new Thread() {
				public void run() {
					BufferedReader br = new BufferedReader(new InputStreamReader(is1));
					try {
						String lineB = null;
						while ((lineB = br.readLine()) != null) {
							if (lineB != null)
								log.info(lineB); // 打印mencoder转换过程代码
						}
					} catch (IOException e) {
						log.error("processVideoFormat IOException: " + e.getMessage(), e);
						return;
					}// 关闭流
					finally {
						try {
							is1.close();
						} catch (IOException e) {
							log.error("IOException: ", e);
							return;
						}
					}
				}
			}.start();
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
					try {
						String lineC = null;
						while ((lineC = br2.readLine()) != null) {
							if (lineC != null)
								log.info(lineC); // 打印mencoder转换过程代码
						}
					} catch (IOException e) {
						log.error("processVideoFormat IOException: " + e.getMessage(), e);
						return;
					}// 关闭流
					finally {
						try {
							is2.close();
						} catch (IOException e) {
							log.error("IOException: ", e);
							return;
						}
					}
				}
			}.start();
			
			p.waitFor(); // 进程等待机制，必须要有，否则不生成mp4！！！
			log.info("生成mp4视频为:" + targetFolder + filerealname + ".mp4");
			return true;
		} catch (Exception e) {
			log.error("processVideoFormat exception: " + e.getMessage(), e);
			return false;
		}
	}
	
}
