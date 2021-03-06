package com.haxademic.demo.hardware.webcam;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.hardware.webcam.IWebCamCallback;

import processing.core.PImage;

public class Demo_WebCamWrapper 
extends PAppletHax
implements IWebCamCallback {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.WEBCAM_INDEX, 12 );
		p.appConfig.setProperty(AppSettings.WEBCAM_THREADED, false );
		p.appConfig.setProperty(AppSettings.SHOW_DEBUG, true );
		p.appConfig.setProperty(AppSettings.FILLS_SCREEN, true );
	}
		
	public void setupFirstFrame () {
		p.webCamWrapper.setDelegate(this);
	}

	public void drawApp() {
		p.background( 0 );
		DrawUtil.setDrawCenter(p);
		DrawUtil.setCenterScreen(p);
		p.image(p.webCamWrapper.getImage(), 0, 0);
	}

	@Override
	public void newFrame(PImage frame) {
		p.debugView.setValue("Last WebCam frame", p.frameCount);
	}

}
