package com.haxademic.demo.hardware.webcam;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.app.config.AppSettings;
import com.haxademic.core.data.constants.PRenderers;
import com.haxademic.core.draw.context.DrawUtil;
import com.haxademic.core.draw.image.BufferThresholdMonitor;
import com.haxademic.core.draw.image.ImageUtil;
import com.haxademic.core.hardware.webcam.IWebCamCallback;

import processing.core.PGraphics;
import processing.core.PImage;

public class Demo_BufferThresholdMonitor 
extends PAppletHax
implements IWebCamCallback {
	public static void main(String args[]) { PAppletHax.main(Thread.currentThread().getStackTrace()[1].getClassName()); }

	protected PGraphics flippedCamera;
	protected BufferThresholdMonitor thresholdMonitor;
	
	protected void overridePropsFile() {
		p.appConfig.setProperty(AppSettings.WIDTH, 1280 );
		p.appConfig.setProperty(AppSettings.HEIGHT, 720 );
		p.appConfig.setProperty(AppSettings.WEBCAM_INDEX, 3 ); // 18
		p.appConfig.setProperty(AppSettings.SHOW_DEBUG, true );
	}
		
	public void setupFirstFrame () {
		// build black/white monitor
		thresholdMonitor = new BufferThresholdMonitor();
		
		// capture webcam frames
		p.webCamWrapper.setDelegate(this);
	}
	
	@Override
	public void newFrame(PImage frame) {
		// p.webCamWrapper.getImage()
		// lazy-init flipped camera buffer
		if(flippedCamera == null) flippedCamera = p.createGraphics(frame.width, frame.height, PRenderers.P2D);
		ImageUtil.copyImageFlipH(frame, flippedCamera);	
		
		// calculate activity monitor with new frame
		thresholdMonitor.update(flippedCamera);
		p.debugView.setTexture(flippedCamera);
	}

	public void drawApp() {
		// set up context
		p.background( 0 );
		DrawUtil.setDrawCenter(p);
		DrawUtil.setCenterScreen(p);
		
		// show activity calculation and texture in debug panel
		thresholdMonitor.setCutoff(p.mousePercentX());
		p.debugView.setValue("threshold cutoff", p.mousePercentX());
		p.debugView.setValue("threshold calculation", thresholdMonitor.thresholdCalc());
		p.debugView.setTexture(thresholdMonitor.thresholdBuffer());

		// show diff buffer
		ImageUtil.cropFillCopyImage(thresholdMonitor.thresholdBuffer(), p.g, true);
	}
	
}
